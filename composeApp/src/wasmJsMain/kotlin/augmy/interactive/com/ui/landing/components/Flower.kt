package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Flower(
    modifier: Modifier = Modifier,
    breezeStrength: Float = .2f,
    flower: FlowerModel,
    random: Random = Random,
    shadow: FlowerShadow? = FlowerShadow(
        color = LocalTheme.current.colors.backgroundDark
    )
) {
    val density = LocalDensity.current

    val safeBreezeStrength = breezeStrength
        .takeUnless { it.isNaN() || it <= 0f }
        ?.coerceAtLeast(1f)
        ?: 1f

    val swayOffset = remember { random.nextFloat() * 2f * PI.toFloat() }
    val bendSign = remember { if (random.nextBoolean()) 1f else -1f }
    val animationStrengthFactor = 1f - flower.witherAmount * 0.9f

    val transition = rememberInfiniteTransition(label = "phase")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (4500f / safeBreezeStrength).toInt(),
                easing = LinearEasing
            )
        ),
        label = "phase"
    )
    val scaleFactor = (with(density) { flower.height.toPx() / 120.dp.toPx() }).coerceAtLeast(0.3f)
    val permanentBendPx = remember(flower.witherAmount) {
        bendSign * 6f * (0.25f + flower.witherAmount) * scaleFactor
    }
    val swayAmplitudePx = 5f * breezeStrength * animationStrengthFactor * scaleFactor
    val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx

    val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
    val p3xPx = permanentBendPx + totalSwayPx * 0.75f
    val dx = p3xPx - p2xPx

    val displayTranslationX by animateFloatAsState(
        targetValue = p3xPx,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier
            .width(flower.width)
            .height(flower.height + 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        with(flower) {
            Compose(
                displayTranslationX,
                dx,
                phase,
                swayOffset,
                permanentBendPx,
                scaleFactor,
                breezeStrength,
                animationStrengthFactor,
                shadow,
                random
            )
        }
    }
}

fun autoWitherColor(baseColor: Color, wither: Float): Color {
    if (wither <= 0f) return baseColor
    val brownTint = Color(0xFF8B6F47)
    val grayTint = Color(0xFF666666)
    val target = when {
        wither < 0.6f -> lerp(baseColor, brownTint, wither / 0.6f)
        else -> lerp(brownTint, grayTint, (wither - 0.6f) / 0.4f)
    }
    return lerp(baseColor, target, wither.coerceIn(0f, .6f))
}

fun toRadians(degrees: Float): Float = degrees * (PI.toFloat() / 180f)

fun Float.isNanOrInfinite() = this.isNaN() || this.isInfinite()

