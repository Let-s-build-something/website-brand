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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import ui.account.affect.components.flower.FlowerUtils.times
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

@Composable
fun CurvyGrassFloor(
    modifier: Modifier = Modifier,
    state: GrassFloorState = rememberAvatarFloorState(),
    grassColor: Color = LocalTheme.current.colors.brandMainDark,
    floorColor: Color = LocalTheme.current.colors.brandMain,
    witheredColor: Color = LocalTheme.current.colors.backgroundLight,
    witherProgress: Float = 0f,
    seed: Int,
    breezeStrength: Float = 1f,
) {
    val density = LocalDensity.current
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val safeBreezeStrength = breezeStrength.takeUnless { it.isNaN() || it <= 0f }?.coerceAtLeast(1f) ?: 1f
    val totalHeight = state.maxBladeHeight + 12.dp
    val heightPx = with(density) { totalHeight.toPx() }

    var widthPx by remember { mutableStateOf(0f) }

    val stable = remember(seed, widthPx, heightPx, state) {
        if (widthPx == 0f) null
        else computeCurvyGrassStableState(
            seed,
            widthPx,
            heightPx,
            state,
            density
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "breeze")
    val breezeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((4000 / safeBreezeStrength).toInt(), easing = LinearEasing)
        ),
        label = "breezeAngle"
    )

    Box(modifier = modifier
        .height(totalHeight)
        .onSizeChanged { widthPx = it.width.toFloat() }
    ) {
        if (stable != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCurvyGrassFloor(
                    stable = stable,
                    breezeOffset = breezeOffset,
                    grassColor = grassColor,
                    floorColor = floorColor,
                    witheredColor = witheredColor,
                    witherAmount = witherAmount,
                    safeBreezeStrength = safeBreezeStrength,
                    density = density,
                    bladeWidth = state.bladeWidth,
                    bladeSpacing = state.bladeSpacing,
                    maxBladeHeight = totalHeight
                )
            }
        }
    }
}

@Composable
fun SandyDesertFloor(
    modifier: Modifier = Modifier,
    seed: Int,
    clip: Boolean = true,
    state: GrassFloorState,
    sandColor: Color = Color(0xFFE8D1A8),
    drySandColor: Color = Color(0xFFF5E6C8),
    rockColor: Color = Color(0xFF8B7D6B),
    witherProgress: Float = 0f,
    breezeStrength: Float = 1f
) {
    val witherAmount = witherProgress.coerceIn(0f, 1f)
    val scaledHeight = state.maxBladeHeight * (1f - 0.6f * witherAmount / 2f)

    val stable = remember(seed, state.maxBladeHeight, witherAmount) {
        computeSandyDesertStableState(seed, witherAmount)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sandPulse")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((12000 / breezeStrength.coerceAtLeast(0.1f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsePhase"
    )

    Canvas(modifier = modifier.height(scaledHeight)) {
        drawSandyDesert(
            stable = stable,
            pulsePhase = pulsePhase,
            witherAmount = witherAmount,
            breezeStrength = breezeStrength,
            sandColor = sandColor,
            drySandColor = drySandColor,
            rockColor = rockColor,
            clip = clip
        )
    }
}


class GrassStableState(
    val blades: List<GrassBlade?>
)

class CurvyGrassStableState(
    val phaseHill: Float,
    val phaseRipple: Float,
    val hillFreq: Float,
    val rippleFreq: Float,
    val hillAmp: Float,
    val rippleAmp: Float,
    val maxAmp: Float,
    val grass: GrassStableState
)

class SandyDesertStableState(
    val rockPositions: List<Triple<Float, Float, Float>>,
    val layerVariations: List<Triple<Float, Float, Float>>,
    val dustParticles: List<Triple<Float, Float, Float>>,
)

data class GrassBlade(
    val heightFactor: Float,
    val tilt: Float,
    val brightnessFactor: Float,
    val phaseNudge: Float,
    val swayAmplitude: Float,
)

data class GrassFloorState(
    val bladeSpacing: Dp = 1.6.dp,
    val bladeWidth: Dp = 2.dp,
    val maxBladeHeight: Dp = 20.dp,
)

fun computeGrassStableState(
    seed: Int,
    widthPx: Float,
    state: GrassFloorState,
    density: Density,
): GrassStableState {
    val edgeCount = 0
    val random = Random(seed)
    val bladeSpacingPx = with(density) { state.bladeSpacing.toPx() }
    val bladeCount = (widthPx / bladeSpacingPx).toInt()

    val blades = List(bladeCount) { index ->
        val heightFactor = random.nextFloat().coerceAtLeast(0.3f)
        if (heightFactor < 0.5f && random.nextFloat() < 0.15f) {
            null
        } else {
            val baseTilt = (random.nextFloat() - 0.5f) * 25f
            val tilt = if (edgeCount > 0) {
                val leftDist = index.toFloat()
                val rightDist = (bladeCount - 1 - index).toFloat()
                val biasStrength = when {
                    leftDist < edgeCount  -> 1f - (leftDist / edgeCount)
                    rightDist < edgeCount -> 1f - (rightDist / edgeCount)
                    else -> 0f
                }
                val biasDir = when {
                    leftDist < edgeCount  -> -1f
                    rightDist < edgeCount ->  1f
                    else -> 0f
                }
                baseTilt + biasDir * 25f * biasStrength * (0.7f + random.nextFloat() * 0.3f)
            } else baseTilt

            GrassBlade(
                heightFactor = heightFactor,
                tilt = tilt,
                brightnessFactor = 0.9f + random.nextFloat() * 0.2f,
                phaseNudge = (random.nextFloat() - 0.5f) * 40f,
                swayAmplitude = 0.6f + random.nextFloat() * 0.8f
            )
        }
    }
    return GrassStableState(blades)
}

fun computeCurvyGrassStableState(
    seed: Int,
    widthPx: Float,
    heightPx: Float,
    state: GrassFloorState,
    density: Density
): CurvyGrassStableState {
    val random = Random(seed)
    val floorPercent = 0.3f
    val groundHeightPx = heightPx * floorPercent
    val hillAmplitude = groundHeightPx * 0.2f
    val rippleAmplitude = groundHeightPx * 0.1f

    val phaseHill = random.nextFloat() * 2f * PI.toFloat()
    val phaseRipple = random.nextFloat() * 2f * PI.toFloat()
    val hillFreq = 0.02f * (0.8f + random.nextFloat() * 0.4f)
    val rippleFreq = 0.05f * (0.8f + random.nextFloat() * 0.4f)
    val hillAmp = hillAmplitude * (0.8f + random.nextFloat() * 0.4f)
    val rippleAmp = rippleAmplitude * (0.8f + random.nextFloat() * 0.4f)
    val maxAmp = hillAmp + rippleAmp

    val grassState = computeGrassStableState(
        seed = seed,
        widthPx = widthPx,
        state = state.copy(maxBladeHeight = state.maxBladeHeight * (1f - floorPercent)),
        density = density
    )

    return CurvyGrassStableState(phaseHill, phaseRipple, hillFreq, rippleFreq, hillAmp, rippleAmp, maxAmp, grassState)
}

fun computeSandyDesertStableState(
    seed: Int,
    witherAmount: Float,
): SandyDesertStableState {
    val random = Random(seed)
    val layers = 5

    val rockPositions = (0 until 20).map {
        Triple(
            random.nextFloat(),
            (6f + random.nextFloat() * 12f) * (1f - 0.3f * witherAmount),
            random.nextFloat() * 8f
        )
    }

    val layerVariations = List(layers) {
        Triple(
            random.nextFloat() * 2f * PI.toFloat(),
            0.8f + random.nextFloat() * 0.4f,
            0.8f + random.nextFloat() * 0.4f
        )
    }

    val dustParticles = (0..300).map {
        Triple(
            random.nextFloat(),
            random.nextFloat() * 0.7f + 0.15f,
            0.7f + random.nextFloat() * 0.8f
        )
    }

    return SandyDesertStableState(rockPositions, layerVariations, dustParticles)
}

fun DrawScope.drawGrassBlades(
    stable: GrassStableState,
    breezeOffset: Float,
    grassColor: Color,
    witheredColor: Color,
    witherAmount: Float,
    safeBreezeStrength: Float,
    bladeWidthPx: Float,
    bladeSpacingPx: Float,
    baseHeightPx: Float,
    bottomYProvider: (Float) -> Float
) {
    stable.blades.forEachIndexed { index, blade ->
        if (blade == null) return@forEachIndexed
        val x = index * bladeSpacingPx
        val heightPx = baseHeightPx * blade.heightFactor * (1f - 0.6f * witherAmount / 2f)

        val bladeColor = lerp(grassColor * blade.brightnessFactor, witheredColor, witherAmount / 2f)

        val travelingPhase = breezeOffset + x * 0.15f + blade.phaseNudge
        val sway = sin(travelingPhase * (PI / 180)).toFloat() *
                5f * safeBreezeStrength * blade.swayAmplitude
        val tilt = blade.tilt * (1f - witherAmount / 2f * 0.5f) +
                witherAmount / 2f * 20f + sway

        val bottomY = bottomYProvider(x)
        rotate(tilt, pivot = Offset(x, bottomY)) {
            drawRoundRect(
                color = bladeColor,
                topLeft = Offset(x, bottomY - heightPx + 4f),
                size = Size(bladeWidthPx, heightPx),
                cornerRadius = CornerRadius(bladeWidthPx / 2f, bladeWidthPx / 2f)
            )
        }
    }
}

fun DrawScope.drawCurvyGrassFloor(
    stable: CurvyGrassStableState,
    breezeOffset: Float,
    grassColor: Color,
    floorColor: Color,
    witheredColor: Color,
    witherAmount: Float,
    safeBreezeStrength: Float,
    density: Density,
    bladeWidth: Dp,
    bladeSpacing: Dp,
    maxBladeHeight: Dp
) {
    val floorPercent = 0.3f
    val groundHeightPx = size.height * floorPercent
    val baseY = size.height - groundHeightPx + stable.maxAmp

    fun waveY(x: Float): Float {
        val hill   = sin(x * stable.hillFreq   + stable.phaseHill)   * stable.hillAmp
        val ripple = sin(x * stable.rippleFreq + stable.phaseRipple) * stable.rippleAmp
        return baseY + hill + ripple
    }

    val groundColor = lerp(floorColor.adjustBrightness(0.65f), witheredColor.adjustBrightness(0.8f), witherAmount)
    drawPath(
        path = Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, baseY)
            for (x in 0..size.width.toInt() step 4) lineTo(x.toFloat(), waveY(x.toFloat()))
            lineTo(size.width, baseY)
            lineTo(size.width, size.height)
            close()
        },
        color = groundColor
    )

    drawGrassBlades(
        stable = stable.grass,
        breezeOffset = breezeOffset,
        grassColor = grassColor,
        witheredColor = witheredColor,
        witherAmount = witherAmount,
        safeBreezeStrength = safeBreezeStrength,
        bladeWidthPx = with(density) { bladeWidth.toPx() },
        bladeSpacingPx = with(density) { bladeSpacing.toPx() },
        baseHeightPx = with(density) { (maxBladeHeight * (1f - floorPercent)).toPx() },
        bottomYProvider = ::waveY
    )
}

fun DrawScope.drawSandyDesert(
    stable: SandyDesertStableState,
    pulsePhase: Float,
    witherAmount: Float,
    breezeStrength: Float,
    sandColor: Color = Color(0xFFE8D1A8),
    drySandColor: Color = Color(0xFFF5E6C8),
    rockColor: Color = Color(0xFF8B7D6B),
    clip: Boolean
) {
    val layers = 5
    val transitionWidth = if (clip) size.width * 0.15f else 0f

    clipPath(Path().apply {
        moveTo(0f, size.height)
        lineTo(size.width, size.height)
        quadraticTo(size.width, 0f, size.width - transitionWidth, 0f)
        lineTo(transitionWidth, 0f)
        quadraticTo(0f, 0f, 0f, size.height)
        close()
    }) {
        val baseSand = lerp(sandColor, drySandColor, witherAmount * 0.7f)

        repeat(layers) { layer ->
            val depthIndex = layers - 1 - layer
            val (extraPhase, freqMult, ampMult) = stable.layerVariations[layer]
            val isForeground = depthIndex == 0
            val phaseOffset = depthIndex * (PI.toFloat() / layers) + extraPhase
            val speedFactor = 0.8f + depthIndex * 0.12f
            val currentPhase = if (isForeground)
                2f * PI.toFloat() * 7f * speedFactor + phaseOffset
            else pulsePhase * speedFactor + phaseOffset

            val pulseOffset = sin(currentPhase) * 7f * breezeStrength.coerceIn(0f, 1.5f)
            val baseY  = size.height - (depthIndex + 1) * (size.height / (layers + 1.5f))
            val layerY = baseY + pulseOffset

            val layerColor = if (isForeground) baseSand.copy(
                red   = (baseSand.red * 1.1f).coerceAtMost(1f),
                green = (baseSand.green * 1.08f).coerceAtMost(1f),
                blue  = (baseSand.blue * 1.05f).coerceAtMost(1f)
            ) else baseSand.adjustBrightness(1f - (depthIndex.toFloat() / (layers - 1)) * 0.4f)

            drawPath(Path().apply {
                moveTo(0f, size.height); lineTo(0f, layerY)
                for (x in 0..size.width.toInt() step 5) {
                    val ripple = sin(x * (0.008f + depthIndex * 0.001f) * freqMult + currentPhase * 0.6f) * (5f + depthIndex * 1.5f) * ampMult
                    val hill = sin(x * 0.002f * freqMult + currentPhase * 0.3f) * 15f * ampMult
                    lineTo(x.toFloat(), layerY + ripple + hill)
                }
                lineTo(size.width, layerY); lineTo(size.width, size.height); close()
            }, color = layerColor)
        }

        stable.rockPositions.forEach { (normX, rockSize, yOffset) ->
            drawOval(
                color = lerp(rockColor, Color(0xFF6B5B45), witherAmount),
                topLeft = Offset(normX * size.width - rockSize / 2f, size.height - rockSize / 2f - yOffset - rockSize / 3f),
                size = Size(rockSize, rockSize * 0.6f)
            )
        }

        stable.dustParticles.forEach { (normX, normY, radius) ->
            drawCircle(
                color = Color.Black.copy(alpha = 0.04f),
                radius = radius,
                center = Offset(normX * size.width, normY * size.height)
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
