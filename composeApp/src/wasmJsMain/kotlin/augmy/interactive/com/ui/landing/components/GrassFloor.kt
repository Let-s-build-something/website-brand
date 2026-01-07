package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import augmy.interactive.com.theme.LocalTheme
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun rememberAvatarFloorState(
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
fun CurvyGrassFloor(
    modifier: Modifier = Modifier,
    state: GrassFloorState = rememberAvatarFloorState(),
    grassColor: Color = LocalTheme.current.colors.brandMainDark,
    floorColor: Color = LocalTheme.current.colors.brandMain,
    witheredColor: Color = LocalTheme.current.colors.backgroundLight,
    witherProgress: Float = 0f,
    random: Random = Random.Default,
    edgeCount: Int = 0,
    breezeStrength: Float = 1f,
) {
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val floorPercent = 0.3f
    val density = LocalDensity.current

    val totalHeight = state.maxBladeHeight + 12.dp
    val heightPx = with(density) { totalHeight.toPx() }
    val groundHeight = totalHeight * floorPercent
    val groundHeightPx = with(density) { groundHeight.toPx() }
    val hillFrequency = 0.02f
    val rippleFrequency = 0.05f
    val hillAmplitude = groundHeightPx * 0.2f
    val rippleAmplitude = groundHeightPx * 0.1f

    val randomPhaseHill = remember(random) { random.nextFloat() * 2f * PI.toFloat() }
    val randomPhaseRipple = remember(random) { random.nextFloat() * 2f * PI.toFloat() }
    val randomHillFreq = remember(random) { hillFrequency * (0.8f + random.nextFloat() * 0.4f) }
    val randomRippleFreq = remember(random) { rippleFrequency * (0.8f + random.nextFloat() * 0.4f) }
    val randomHillAmp = remember(random) { hillAmplitude * (0.8f + random.nextFloat() * 0.4f) }
    val randomRippleAmp = remember(random) { rippleAmplitude * (0.8f + random.nextFloat() * 0.4f) }
    val maxAmp = randomHillAmp + randomRippleAmp

    val baseY = heightPx - groundHeightPx + maxAmp

    val getWaveY: DrawScope.(Float) -> Float = { x ->
        val hillWave = sin(x * randomHillFreq + randomPhaseHill) * randomHillAmp
        val rippleWave = sin(x * randomRippleFreq + randomPhaseRipple) * randomRippleAmp
        val waveY = hillWave + rippleWave
        baseY + waveY
    }

    Box(modifier = modifier.height(totalHeight)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val groundColor = lerp(
                floorColor.adjustBrightness(0.65f),
                witheredColor.adjustBrightness(0.8f),
                witherAmount
            )

            val path = Path().apply {
                moveTo(0f, height)
                lineTo(0f, baseY)

                for (x in 0..width.toInt() step 4) {
                    val normalizedX = x.toFloat()
                    lineTo(normalizedX, getWaveY(normalizedX))
                }

                lineTo(width, baseY)
                lineTo(width, height)
                close()
            }

            drawPath(path = path, color = groundColor)
        }

        val adjustedState = state.copy(
            maxBladeHeight = totalHeight * (1f - floorPercent)
        )

        GrassFloor(
            modifier = Modifier.fillMaxSize(),
            state = adjustedState,
            grassColor = grassColor,
            witheredColor = witheredColor,
            witherProgress = witherProgress,
            random = random,
            edgeCount = edgeCount,
            breezeStrength = breezeStrength,
            bottomYProvider = getWaveY
        )
    }
}

@Composable
private fun GrassFloor(
    modifier: Modifier = Modifier,
    state: GrassFloorState = rememberAvatarFloorState(),
    grassColor: Color = LocalTheme.current.colors.brandMainDark,
    witheredColor: Color = LocalTheme.current.colors.backgroundLight,
    witherProgress: Float = 0f,
    random: Random = Random,
    edgeCount: Int = 0,
    breezeStrength: Float = 1f,
    bottomYProvider: DrawScope.(Float) -> Float = { _ -> size.height }
) {
    val localDensity = LocalDensity.current
    var size by remember { mutableStateOf(IntSize.Zero) }
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val safeBreezeStrength = breezeStrength
        .takeUnless { it.isNaN() || it <= 0f }
        ?.coerceAtLeast(1f)
        ?: 1f

    val blades: List<GrassBlade?> = remember(size, state.bladeSpacing, state.maxBladeHeight) {
        if (size.width == 0) emptyList() else {
            val bladeCount = with(localDensity) { (size.width / state.bladeSpacing.toPx()) }.toInt()

            List(bladeCount) { index ->
                val heightFactor = random.nextFloat().coerceAtLeast(0.3f)

                if (heightFactor < 0.5f && random.nextFloat() < 0.15f) {
                    null
                } else {
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
                    } else baseTilt

                    GrassBlade(
                        heightFactor = heightFactor,
                        tilt = tilt,
                        brightnessFactor = 0.9f + random.nextFloat() * 0.2f
                    )
                }
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
            if (blade != null) {
                val x = index * bladeSpacingPx
                val heightPx = baseHeightPx * blade.heightFactor * (1f - 0.6f * witherAmount.div(2))

                val bladeColor = lerp(
                    grassColor * blade.brightnessFactor,
                    witheredColor,
                    witherAmount / 2
                )

                val sway = sin((breezeOffset + index * 10f) * (PI / 180)).toFloat() * 5f * safeBreezeStrength
                val tilt = blade.tilt * (1f - witherAmount.div(2) * 0.5f) + witherAmount.div(2) * 20f + sway

                val bottomY = bottomYProvider(x)

                rotate(tilt, pivot = Offset(x, bottomY)) {
                    drawRoundRect(
                        color = bladeColor,
                        topLeft = Offset(x, bottomY - heightPx + 4f),
                        size = Size(bladeWidthPx, heightPx),
                        cornerRadius = CornerRadius(bladeWidthPx / 2, bladeWidthPx / 2)
                    )
                }
            }
        }
    }
}

private data class Speck(val x: Float, val y: Float, val r: Float, val dark: Boolean)
@Composable
fun SandyDesertFloor(
    modifier: Modifier = Modifier,
    random: Random,
    clip: Boolean = true,
    state: GrassFloorState,
    sandColor: Color = Color(0xFFE8D1A8),
    drySandColor: Color = Color(0xFFF5E6C8),
    rockColor: Color = Color(0xFF8B7D6B),
    witherProgress: Float = 0f,
    breezeStrength: Float = 1f
) {
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val dry = (witherAmount * witherAmount).coerceIn(0f, 1f) // more dramatic late-stage
    val motionFactor = (1f - witherAmount).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "sandPulse")

    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (12000 / breezeStrength.coerceAtLeast(0.1f)).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsePhase"
    )

    val rockPositions = remember(state.maxBladeHeight) {
        val list = mutableListOf<Triple<Float, Float, Float>>()
        val rockCount = 20
        repeat(rockCount) {
            val x = random.nextFloat()
            val size = 6f + random.nextFloat() * 12f
            val yOffset = random.nextFloat() * 8f
            list.add(Triple(x, size, yOffset))
        }
        list
    }
    val maxSpecks = 600
    val specks = remember(state.maxBladeHeight, random) {
        List(maxSpecks) {
            Speck(
                x = random.nextFloat(),
                y = random.nextFloat(),
                r = 0.6f + random.nextFloat() * 2.4f,
                dark = random.nextFloat() < 0.5f
            )
        }
    }

    val layers = 5
    val layerVariations = remember(state.maxBladeHeight) {
        List(layers) {
            val extraPhase = random.nextFloat() * 2f * PI.toFloat()
            val freqMult = 0.8f + random.nextFloat() * 0.4f
            val ampMult = 0.8f + random.nextFloat() * 0.4f
            Triple(extraPhase, freqMult, ampMult)
        }
    }

    Canvas(modifier = modifier.height(state.maxBladeHeight)) {
        val width = size.width
        val height = size.height
        val transitionWidth = if (clip) width * .15f else 0f

        clipPath(
            path = Path().apply {
                moveTo(0f, height)
                lineTo(width, height)
                quadraticBezierTo(width, 0f, width - transitionWidth, 0f)
                lineTo(transitionWidth, 0f)
                quadraticBezierTo(0f, 0f, 0f, height)
                close()
            }
        ) {
            val baseSand = lerp(sandColor, drySandColor, dry)

            repeat(layers) { layer ->
                val depthIndex = layers - 1 - layer
                val (extraPhase, freqMult, ampMult) = layerVariations[layer]
                val isForeground = depthIndex == 0
                val phaseOffset = depthIndex * (PI.toFloat() / layers) + extraPhase
                val speedFactor = 0.8f + depthIndex * 0.12f
                val currentPhase = if (isForeground) {
                    2f * PI.toFloat() * 7f * speedFactor + phaseOffset
                } else pulsePhase * speedFactor + phaseOffset

                val pulseOffset = sin(currentPhase) *
                        7f * breezeStrength.coerceIn(0f, 1.5f) * motionFactor

                val rippleAmplitude = (5f + depthIndex * 1.5f) * ampMult * (0.3f + 0.7f * motionFactor)
                val rippleFrequency = (0.008f + depthIndex * 0.001f) * freqMult

                val hillAmplitude = 15f * ampMult * (0.55f + 0.45f * motionFactor)
                val hillFrequency = 0.002f * freqMult

                val baseY = height - (depthIndex + 1) * (height / (layers + 1.5f))
                val layerY = baseY + pulseOffset

                val shadeBoost = lerp(0.40f, 0.75f, dry)
                val shadeFactor = (depthIndex.toFloat() / (layers - 1)) * shadeBoost

                val layerColor = if (isForeground) {
                    baseSand.copy(
                        red = (baseSand.red * (1.08f + 0.06f * dry)).coerceAtMost(1f),
                        green = (baseSand.green * (1.06f + 0.05f * dry)).coerceAtMost(1f),
                        blue = (baseSand.blue * (1.03f + 0.03f * dry)).coerceAtMost(1f)
                    )
                } else {
                    baseSand.adjustBrightness(1f - shadeFactor)
                }

                val path = Path().apply {
                    moveTo(0f, height)
                    lineTo(0f, layerY)
                    for (x in 0..width.toInt() step 5) {
                        val xf = x.toFloat()
                        val ripple = sin(xf * rippleFrequency + currentPhase * 0.6f) * rippleAmplitude
                        val hill = sin(xf * hillFrequency + currentPhase * 0.3f) * hillAmplitude
                        lineTo(xf, layerY + ripple + hill)
                    }
                    lineTo(width, layerY)
                    lineTo(width, height)
                    close()
                }
                drawPath(path = path, color = layerColor)
            }

            // Rocks: slightly darker, more contrast as wither increases
            val foregroundLayerIndex = layers - 1
            val (extraPhase0, freqMult0, ampMult0) = layerVariations[foregroundLayerIndex]

            val depthIndex0 = 0
            val phaseOffset0 = depthIndex0 * (PI.toFloat() / layers) + extraPhase0
            val speedFactor0 = 0.8f + depthIndex0 * 0.12f

            val currentPhase0 = 2f * PI.toFloat() * 7f * speedFactor0 + phaseOffset0

            val pulseOffset0 = sin(currentPhase0) *
                    7f * breezeStrength.coerceIn(0f, 1.5f) * motionFactor

            val rippleAmplitude0 =
                (5f + depthIndex0 * 1.5f) * ampMult0 * (0.3f + 0.7f * motionFactor)
            val rippleFrequency0 = (0.008f + depthIndex0 * 0.001f) * freqMult0

            val hillAmplitude0 =
                15f * ampMult0 * (0.55f + 0.45f * motionFactor)
            val hillFrequency0 = 0.002f * freqMult0

            val baseY0 = height - (depthIndex0 + 1) * (height / (layers + 1.5f))
            val layerY0 = baseY0 + pulseOffset0

            fun foregroundSurfaceY(x: Float): Float {
                val ripple = sin(x * rippleFrequency0 + currentPhase0 * 0.6f) * rippleAmplitude0
                val hill = sin(x * hillFrequency0 + currentPhase0 * 0.3f) * hillAmplitude0
                return layerY0 + ripple + hill
            }

            rockPositions.forEach { (normX, rockSize, yOffset) ->
                val rockX = normX * width
                val surfaceY = foregroundSurfaceY(rockX)

                // bury rocks slightly; bury more as it dries
                val embed = 0.35f + 0.25f * dry
                val rockY = surfaceY + rockSize * embed + yOffset * 0.15f

                // subtle shadow glued to the surface (helps sell contact)
                drawOval(
                    color = Color.Black.copy(alpha = 0.08f),
                    topLeft = Offset(rockX - rockSize * 0.55f, rockY - rockSize * 0.02f),
                    size = Size(rockSize * 1.1f, rockSize * 0.32f)
                )

                drawOval(
                    color = lerp(rockColor, Color(0xFF5E4B32), dry),
                    topLeft = Offset(rockX - rockSize / 2, rockY - rockSize / 3),
                    size = Size(rockSize, rockSize * 0.6f)
                )
            }

            val speckAlpha = lerp(0.02f, 0.10f, dry)
            val speckCount = (200 + 400 * dry).toInt().coerceAtMost(specks.size)

            repeat(speckCount) { i ->
                val s = specks[i]
                val x = s.x * width
                val y = s.y * height
                val c = if (s.dark) Color.Black else Color(0xFF6B5B45)

                drawCircle(
                    color = c.copy(alpha = speckAlpha),
                    radius = s.r,
                    center = Offset(x, y)
                )
            }

            // Dramatic: vertical "scorched" overlay to sell dryness
            drawRect(
                brush = Brush.verticalGradient(
                    0.0f to Color(0xFFFFFFFF).copy(alpha = 0.00f),
                    0.60f to Color(0xFFB08A57).copy(alpha = 0.10f * dry),
                    1.00f to Color(0xFF7A5A2E).copy(alpha = 0.18f * dry),
                ),
                size = Size(width, height)
            )
        }
    }
}

fun Color.adjustBrightness(factor: Float): Color {
    return copy(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f)
    )
}

private operator fun Color.times(factor: Float) = Color(
    red = (red * factor).coerceIn(0f, 1f),
    green = (green * factor).coerceIn(0f, 1f),
    blue = (blue * factor).coerceIn(0f, 1f),
    alpha = alpha
)
