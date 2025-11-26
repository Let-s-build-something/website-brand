package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_STEM_RATIO
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

data class FlowerShadow(
    val color: Color,
    val offset: Dp = 2.5.dp
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Flower(
    modifier: Modifier = Modifier,
    random: Random = Random,
    growth: Float = 1f,
    witherProgress: Float = 0f,
    stemWidth: Dp = 2.5.dp,
    stemHeight: Dp = 72.dp,
    flowerBaseSize: Dp = (stemHeight.value * FLOWER_STEM_RATIO).dp,
    flowerColor: Color = LocalTheme.current.colors.tetrial,
    stemColor: Color = LocalTheme.current.colors.brandMainDark,
    breezeStrength: Float = .2f,
    shadow: FlowerShadow? = FlowerShadow(
        color = LocalTheme.current.colors.backgroundDark
    )
) {
    val growthScale = ((growth.coerceIn(-1f, 1f) + 1f) / 2f).coerceAtLeast(0f)
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val safeBreezeStrength = breezeStrength
        .takeUnless { it.isNaN() || it <= 0f }
        ?.coerceAtLeast(1f)
        ?: 1f

    val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)
    val actualStemColor = autoWitherColor(stemColor, witherAmount)

    val localFlowerSize = flowerBaseSize * growthScale
    val localStemHeight = stemHeight * growthScale * (1f - 0.3f * witherAmount)
    val localStemWidth = stemWidth * growthScale

    val stemSegments = remember { random.nextInt(3, 6) }
    val points = remember { MutableList(stemSegments + 1) { Offset.Zero } }
    val bendSign = remember { if (random.nextBoolean()) 1f else -1f }
    val swayOffset = remember { random.nextFloat() * 2f * PI.toFloat() }

    // Animation slowdown factor
    val animationStrengthFactor = (1f - witherAmount.times(.75f))

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

    val density = LocalDensity.current
    val flowerSizePx = with(density) { localFlowerSize.toPx() }
    val stemHeightPx = with(density) { localStemHeight.toPx() }
    val stemWidthPx = with(density) { localStemWidth.toPx() }

    val referenceStemHeightPx = with(density) { 72.dp.toPx() }
    val scaleFactor = (stemHeightPx / referenceStemHeightPx).coerceAtLeast(0.3f)

    val wobbleOffsets = remember {
        List(stemSegments) { (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor }
    }
    val permanentBendPx = remember(witherAmount) {
        bendSign * 6f * (0.25f + witherAmount) * scaleFactor
    }
    val swayAmplitudePx = 5f * breezeStrength * animationStrengthFactor * scaleFactor
    val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx

    val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
    val p3xPx = permanentBendPx + totalSwayPx * 0.75f


    val dx = p3xPx - p2xPx
    val dy = stemHeightPx * 0.33f
    val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

    val maxBendDeg = 100f
    val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
    val witherBendScale = 1f + hangCurve * 0.8f
    val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

    val tangentAngle = bentAngle
    val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
            1.5f * breezeStrength * animationStrengthFactor

    val containerWidthDp = with(density) {
        (flowerSizePx * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.toDp() ?: 0.dp
    }
    val containerHeightDp = animateDpAsState(
        with(density) {
            (stemHeightPx + flowerSizePx).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.toDp() ?: 0.dp
        }
    )

    val displayTranslationX by animateFloatAsState(
        targetValue = p3xPx,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val displayRotation by animateFloatAsState(
        targetValue = tangentAngle + flowerExtraSway,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = modifier
            .width(containerWidthDp)
            .height(containerHeightDp.value),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(
            modifier = Modifier
                .height(localStemHeight)
                .width(localStemWidth * 3)
                .align(Alignment.BottomCenter)
        ) {
            val centerX = size.width / 2f
            val stemBottomY = size.height - 2f

            val segmentHeight = stemBottomY / stemSegments
            val bendDir = sign(permanentBendPx)
            val bendPx = (tan(toRadians(tangentAngle)) * size.height / 3f)

            for (i in 0..stemSegments) {
                val progress = i.toFloat() / stemSegments
                val y = stemBottomY - segmentHeight * i
                val wobble = wobbleOffsets.getOrNull(i.coerceAtMost(wobbleOffsets.lastIndex)) ?: 0f

                val baseX = centerX + (bendPx * progress + wobble * (1f - witherAmount)) * bendDir
                val animatedTopX = centerX + displayTranslationX
                val smoothedX = if (i == stemSegments) animatedTopX
                else lerp(baseX, animatedTopX, progress * 0.15f)

                points[i] = Offset(smoothedX, y)
            }

            // Draw stem
            val stemPath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 2) {
                    val p1 = points[i + 1]
                    val p2 = points[i + 2]
                    val ctrlX = (p1.x + p2.x) / 2f
                    val ctrlY = (p1.y + p2.y) / 2f
                    quadraticBezierTo(p1.x, p1.y, ctrlX, ctrlY)
                }
                lineTo(points.last().x, points.last().y)
            }

            drawPath(
                path = stemPath,
                color = actualStemColor,
                style = Stroke(width = stemWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        val flowerShape = MaterialShapes.Flower.toShape()
        val petalDroop = witherAmount * 40f // Max droop

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(localFlowerSize + (shadow?.offset?.times(2) ?: 0.dp))
                .graphicsLayer {
                    translationX = displayTranslationX
                    translationY = -stemHeightPx
                    rotationZ = displayRotation + petalDroop // Droop entire head
                    transformOrigin = TransformOrigin(0.5f, 1f)
                }
                .then(if (shadow != null) Modifier
                    .background(color = shadow.color, shape = flowerShape)
                    .padding(shadow.offset)
                else Modifier)
                .background(color = actualFlowerColor, shape = flowerShape)
        )
    }
}

fun toRadians(degrees: Float): Float = degrees * (PI.toFloat() / 180f)

fun Float.isNanOrInfinite() = this.isNaN() || this.isInfinite()

fun autoWitherColor(baseColor: Color, wither: Float): Color {
    if (wither <= 0f) return baseColor
    val brownTint = Color(0xFF8B6F47) // Wilted brown
    val grayTint = Color(0xFF666666)  // Dead gray
    val target = when {
        wither < 0.6f -> lerp(baseColor, brownTint, wither / 0.6f)
        else -> lerp(brownTint, grayTint, (wither - 0.6f) / 0.4f)
    }
    return lerp(baseColor, target, wither.coerceIn(0f, 1f))
}
