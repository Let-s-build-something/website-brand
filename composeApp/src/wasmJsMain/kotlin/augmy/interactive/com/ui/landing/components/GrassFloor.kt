package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun rememberGrassFloorState(
    key: Any? = null,
    bladeSpacing: Dp = 1.6.dp,
    bladeWidth: Dp = 2.dp,
    maxBladeHeight: Dp = 20.dp
) = remember(key) {
    GrassFloorState(
        bladeSpacing = bladeSpacing,
        bladeWidth = bladeWidth,
        maxBladeHeight = maxBladeHeight
    )
}

data class GrassBlade(
    val heightFactor: Float,
    val tilt: Float,
    val brightnessFactor: Float
)

data class GrassFloorState(
    val bladeSpacing: Dp = 1.6.dp,
    val bladeWidth: Dp = 2.dp,
    val maxBladeHeight: Dp = 20.dp,
)

@Composable
fun GrassFloor(
    modifier: Modifier = Modifier,
    state: GrassFloorState = rememberGrassFloorState(),
    grassColor: Color = LocalTheme.current.colors.brandMainDark,
    witheredColor: Color = LocalTheme.current.colors.backgroundLight,
    witherProgress: Float = 0f,
    random: Random = Random,
    edgeCount: Int = 0,
    breezeStrength: Float = 1f // 0 = still, 1 = lively sway
) {
    val localDensity = LocalDensity.current
    var size by remember { mutableStateOf(IntSize.Zero) }
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val safeBreezeStrength = breezeStrength
        .takeUnless { it.isNaN() || it <= 0f }
        ?.coerceAtLeast(1f)
        ?: 1f

    val blades = remember(size, state.bladeSpacing, state.maxBladeHeight) {
        if (size.width == 0) emptyList() else {
            val bladeCount = with(localDensity) { (size.width / state.bladeSpacing.toPx()) }.toInt()

            List(bladeCount) { index ->

                val baseTilt = (random.nextFloat() - 0.5f) * 25f
                val tilt = if (edgeCount > 0) {
                    val leftEdgeDistance = index.toFloat()
                    val rightEdgeDistance = (bladeCount - 1 - index).toFloat()

                    val biasStrength = when {
                        leftEdgeDistance < edgeCount -> 1f - (leftEdgeDistance / edgeCount)
                        rightEdgeDistance < edgeCount -> 1f - (rightEdgeDistance / edgeCount)
                        else -> 0f
                    }

                    val biasDirection = when {
                        leftEdgeDistance < edgeCount -> -1f
                        rightEdgeDistance < edgeCount -> 1f
                        else -> 0f
                    }
                    val maxBiasTilt = 25f

                    baseTilt + biasDirection * maxBiasTilt * biasStrength * (0.7f + random.nextFloat() * 0.3f)
                }else baseTilt

                GrassBlade(
                    heightFactor = random.nextFloat().coerceAtLeast(0.3f),
                    tilt = tilt,
                    brightnessFactor = 0.9f + random.nextFloat() * 0.2f
                )
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "breeze")
    val breezeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (4000 / safeBreezeStrength).toInt(),
                easing = LinearEasing
            )
        ),
        label = "breezeAngle"
    )

    Canvas(
        modifier = modifier
            .height(state.maxBladeHeight + 6.dp)
            .onSizeChanged { size = it }
    ) {
        val bladeWidthPx = state.bladeWidth.toPx()
        val bladeSpacingPx = state.bladeSpacing.toPx()
        val baseHeightPx = state.maxBladeHeight.toPx()

        blades.forEachIndexed { index, blade ->
            val x = index * bladeSpacingPx
            val heightPx = baseHeightPx * blade.heightFactor * (1f - 0.6f * witherAmount.div(2))

            val bladeColor = lerp(
                grassColor * blade.brightnessFactor,
                witheredColor,
                witherAmount / 2
            )

            val sway = sin((breezeOffset + index * 10f) * (PI / 180)).toFloat() * 5f * safeBreezeStrength
            val tilt = blade.tilt * (1f - witherAmount.div(2) * 0.5f) + witherAmount.div(2) * 20f + sway

            rotate(tilt, pivot = Offset(x, size.height.toFloat())) {
                drawRoundRect(
                    color = bladeColor,
                    topLeft = Offset(x, size.height.toFloat() - heightPx),
                    size = Size(bladeWidthPx, heightPx),
                    cornerRadius = CornerRadius(bladeWidthPx / 2, bladeWidthPx / 2)
                )
            }
        }
    }
}

private operator fun Color.times(factor: Float) = Color(
    red = (red * factor).coerceIn(0f, 1f),
    green = (green * factor).coerceIn(0f, 1f),
    blue = (blue * factor).coerceIn(0f, 1f),
    alpha = alpha
)
