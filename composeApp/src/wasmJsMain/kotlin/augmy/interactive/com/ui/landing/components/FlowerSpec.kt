package augmy.interactive.com.ui.landing.components

import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import ui.account.affect.components.flower.FlowerFrame
import ui.account.affect.components.flower.FlowerUtils.FlowerShadow
import ui.account.affect.components.flower.FlowerUtils.autoWitherColor
import ui.account.affect.components.flower.FlowerUtils.bodyWidthAt
import ui.account.affect.components.flower.FlowerUtils.toShapeKotlin
import ui.account.affect.components.flower.FlowerUtils.updatePoints
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.random.Random

sealed class FlowerSpec<F: FlowerFrame>(
    open val model: FlowerModel<F>
) {
    abstract fun computeFrame(density: Density): F

    data class GenericSpec(
        override val model: FlowerModel.Generic,
        val growthScale: Float,
        val witherAmount: Float,
        val stemHeight: Dp,
        val stemWidth: Dp,

        val flowerColor: Color,
        val stemColor: Color,

        val random: Random,
        val breezeStrength: Float,
        val phase: Float,

        val shadow: FlowerShadow?
    ): FlowerSpec<FlowerFrame.GenericFrame>(model) {
        override fun computeFrame(
            density: Density,
        ): FlowerFrame.GenericFrame {
            with(model) {
                val safeBreezeStrength = breezeStrength.takeIf { it > 0f } ?: 1f
                val swayOffset = random.nextFloat() * 2f * PI.toFloat()
                val bendSign = if (random.nextBoolean()) 1f else -1f
                val animationStrengthFactor = 1f - witherAmount * 0.9f

                val stemHeightPx  = with(density) { localStemHeight.toPx() }
                val stemWidthPx   = with(density) { localStemWidth.toPx() }
                val stemCanvasW   = with(density) { (localStemWidth * 3).toPx() }
                val flowerSizePx  = with(density) { flowerSize.toPx() }
                val shadowOffPx   = shadow?.let { with(density) { it.offset.toPx() } } ?: 0f

                val rawScale    = with(density) { height.toPx() / 120.dp.toPx() }
                val scaleFactor = sqrt(rawScale.coerceAtLeast(0.09f))

                val permanentBendPx = (bendSign * 6f * (0.25f + witherAmount) * scaleFactor)
                    .coerceIn(-20f, 20f)

                val swayAmplitudePx = (5f * safeBreezeStrength * animationStrengthFactor * scaleFactor)
                    .coerceAtMost(14f)
                val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx
                val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
                val p3xPx = permanentBendPx + totalSwayPx * 0.75f
                val dx = p3xPx - p2xPx

                val dy = stemHeightPx * 0.33f
                val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

                val maxBendDeg = 100f
                val hangCurve = sin(witherAmount * PI / 2.0).toFloat()
                val witherBendScale = 1f + hangCurve * 0.8f
                val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

                val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                        1.5f * safeBreezeStrength * animationStrengthFactor
                val stemSegments = random.nextInt(3, 6)
                val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)
                val actualStemColor = autoWitherColor(stemColor, witherAmount)

                val displayRotation = bentAngle + flowerExtraSway
                val petalDroop = witherAmount * 40f

                val wobbleOffsets = (0 until 6).map {
                    (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
                }.take(stemSegments)

                val centerX = stemCanvasW / 2f
                val stemBottomY = stemHeightPx - 2f
                val segHeight = stemBottomY / stemSegments
                val bendDir = sign(permanentBendPx)
                val bendPx = (tan(toRadians(bentAngle)) * stemHeightPx / 3f)

                val points = (0..stemSegments).map { i ->
                    val progress = i.toFloat() / stemSegments
                    val y = stemBottomY - segHeight * i
                    val wobble = wobbleOffsets.getOrNull(i.coerceAtMost(wobbleOffsets.lastIndex)).orZero()
                    val baseX = centerX + (bendPx * progress + wobble * (1f - witherAmount)) * bendDir
                    val topX = centerX + p3xPx
                    val smoothedX = if (i == stemSegments) topX
                    else lerp(baseX, topX, progress * 0.15f)
                    Offset(smoothedX, y)
                }

                fun shapePath(size: Float): Path {
                    val outline = MaterialShapes.Flower.toShapeKotlin().createOutline(
                        size = Size(size, size),
                        layoutDirection = LayoutDirection.Ltr,
                        density = density
                    )
                    return (outline as? Outline.Generic)?.path ?: Path()
                }

                val flowerSizeWithShadow = flowerSizePx + shadowOffPx * 2f

                return FlowerFrame.GenericFrame(
                    points = points,
                    stemCanvasWidth = stemCanvasW,
                    stemHeightPx = stemHeightPx,
                    stemWidthPx = stemWidthPx,
                    actualStemColor = actualStemColor,
                    actualFlowerColor = actualFlowerColor,
                    flowerPath = shapePath(flowerSizeWithShadow),
                    shadowPath = shadow?.let { shapePath(flowerSizePx) },
                    shadowColor = shadow?.color,
                    shadowOffsetPx = shadowOffPx,
                    flowerSizePx = flowerSizePx,
                    flowerSizeWithShadow = flowerSizeWithShadow,
                    displayTranslationX = p3xPx,
                    displayRotation = displayRotation,
                    petalDroop = petalDroop
                )
            }
        }
    }

    data class ChamomileSpec(
        override val model: FlowerModel.Chamomile,
        val growthScale: Float,
        val witherAmount: Float,
        val stemHeight: Dp,
        val stemWidth: Dp,
        val petalColor: Color,
        val centerColor: Color,
        val stemColor: Color,
        val random: Random,
        val breezeStrength: Float,
        val phase: Float,
        val shadow: FlowerShadow?
    ): FlowerSpec<FlowerFrame.ChamomileFrame>(model) {

        override fun computeFrame(
            density: Density,
        ): FlowerFrame.ChamomileFrame = with(model) {
            val safeBreezeStrength = breezeStrength.takeIf { it > 0f } ?: 1f
            val swayOffset = random.nextFloat() * 2f * PI.toFloat()
            val bendSign = if (random.nextBoolean()) 1f else -1f
            val animationStrengthFactor = 1f - witherAmount * 0.9f

            val stemHeightPx = with(density) { localStemHeight.toPx() }
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val stemCanvasW = with(density) { (localStemWidth * 3).toPx() }
            val flowerSizePx = with(density) { flowerSize.toPx() }
            val shadowOffPx = shadow?.let { with(density) { it.offset.toPx() } } ?: 0f

            val rawScale = with(density) { height.toPx() / 120.dp.toPx() }
            val scaleFactor = sqrt(rawScale.coerceAtLeast(0.09f))

            val permanentBendPx = (bendSign * 6f * (0.25f + witherAmount) * scaleFactor)
                .coerceIn(-20f, 20f)

            val swayAmplitudePx =
                (5f * safeBreezeStrength * animationStrengthFactor * scaleFactor)
                    .coerceAtMost(14f)

            val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx
            val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
            val p3xPx = permanentBendPx + totalSwayPx * 0.75f
            val dx = p3xPx - p2xPx

            val dy = stemHeightPx * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val hangCurve = sin(witherAmount * PI / 2.0).toFloat()
            val bentAngle = (baseAngle * (1f + hangCurve * 0.8f)).coerceIn(-100f, 100f)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * safeBreezeStrength * animationStrengthFactor

            val stemSegments = random.nextInt(3, 6)
            val wobbleOffsets = List(stemSegments) {
                (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
            }

            val stable = GenericStableState(stemSegments, wobbleOffsets)
            stable.updatePoints(
                stemCanvasSize = Size(stemCanvasW, stemHeightPx),
                displayTranslationX = p3xPx,
                bentAngle = bentAngle,
                permanentBendPx = permanentBendPx,
                witherAmount = witherAmount
            )

            fun shapePath(size: Float): Path {
                val outline = MaterialShapes.SoftBoom.toShapeKotlin().createOutline(
                    size = Size(size, size),
                    layoutDirection = LayoutDirection.Ltr,
                    density = density
                )
                return (outline as? Outline.Generic)?.path ?: Path()
            }

            val flowerSizeWithShadow = flowerSizePx + shadowOffPx * 2f

            FlowerFrame.ChamomileFrame(
                points = stable.points.toList(),
                stemCanvasWidth = stemCanvasW,
                stemHeightPx = stemHeightPx,
                stemWidthPx = stemWidthPx,
                actualStemColor = autoWitherColor(stemColor, witherAmount),
                actualPetalColor = autoWitherColor(petalColor, witherAmount),
                actualCenterColor = autoWitherColor(centerColor, witherAmount),
                petalPath = shapePath(flowerSizeWithShadow),
                shadowPath = shadow?.let { shapePath(flowerSizePx) },
                shadowColor = shadow?.color,
                shadowOffsetPx = shadowOffPx,
                flowerSizePx = flowerSizePx,
                flowerSizeWithShadow = flowerSizeWithShadow,
                centerRadiusPx = flowerSizePx * FlowerModel.Chamomile.CENTER_PETAL_RATIO / 2f,
                displayTranslationX = p3xPx,
                displayRotation = bentAngle + flowerExtraSway,
                petalDroop = witherAmount * 40f
            )
        }
    }

    data class DandelionSpec(
        override val model: FlowerModel.Dandelion,
        val random: Random,
        val breezeStrength: Float,
        val phase: Float,
        val shadow: FlowerShadow?
    ) : FlowerSpec<FlowerFrame.DandelionFrame>(model) {

        override fun computeFrame(
            density: Density,
        ): FlowerFrame.DandelionFrame = with(model) {
            val safeBreezeStrength = breezeStrength.takeIf { it > 0f } ?: 1f

            // ── Mirror parent Flower composable's random consumption ──────────
            val swayOffset = random.nextFloat() * 2f * PI.toFloat()
            val bendSign   = if (random.nextBoolean()) 1f else -1f
            random.nextFloat()   // randomOffsetMillis — consumed, discarded

            val animationStrengthFactor = 1f - witherAmount * 0.9f

            val stemHeightPx = with(density) { localStemHeight.toPx() }
            val stemWidthPx  = with(density) { localStemWidth.toPx() }
            val stemCanvasW  = with(density) { (localStemWidth * 3).toPx() }
            val flowerSizePx = with(density) { flowerSize.toPx() }

            val rawScale    = with(density) { height.toPx() / 120.dp.toPx() }
            val scaleFactor = sqrt(rawScale.coerceAtLeast(0.09f))

            val permanentBendPx = (bendSign * 6f * (0.25f + witherAmount) * scaleFactor)
                .coerceIn(-20f, 20f)

            val swayAmplitudePx = (5f * safeBreezeStrength * animationStrengthFactor * scaleFactor)
                .coerceAtMost(14f)
            val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx
            val p3xPx = permanentBendPx + totalSwayPx * 0.75f
            val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
            val dx    = p3xPx - p2xPx

            val dy        = stemHeightPx * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f
            val hangCurve = sin(witherAmount * PI / 2.0).toFloat()
            val bentAngle = (baseAngle * (1f + hangCurve * 0.8f)).coerceIn(-100f, 100f)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * safeBreezeStrength * animationStrengthFactor

            // ── Mirror Dandelion.Compose's remember blocks ───────────────────
            val stemSegments = random.nextInt(3, 6)
            val wobbleOffsets = List(stemSegments) {
                (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
            }

            val stable = GenericStableState(stemSegments, wobbleOffsets)
            stable.updatePoints(
                stemCanvasSize      = Size(stemCanvasW, stemHeightPx),
                displayTranslationX = p3xPx,
                bentAngle           = bentAngle,
                permanentBendPx     = permanentBendPx,
                witherAmount        = witherAmount
            )

            // ── Shapes — deterministic, same as remember { } in Compose ─────
            val numPetals      = 10
            val rotationOffset = (180f / numPetals).roundToInt()  // 18
            val polygon = RoundedPolygon.star(
                numVerticesPerRadius = numPetals,
                radius = 1f, innerRadius = 0.35f,
                rounding = CornerRounding(0.06f),
                innerRounding = CornerRounding(0.06f)
            )
            val shape1 = polygon.toShapeKotlin()
            val shape2 = polygon.toShapeKotlin(rotationOffset)

            fun shapePath(shape: Shape, sizePx: Float): Path {
                val outline = shape.createOutline(
                    size = Size(sizePx, sizePx),
                    layoutDirection = LayoutDirection.Ltr,
                    density = density
                )
                return (outline as? Outline.Generic)?.path ?: Path()
            }

            val actualPetalColor = autoWitherColor(petalColor, witherAmount)

            // ── Layers — exact match to nested Boxes in Compose ──────────────
            val layers = listOf(
                LayerData(shapePath(shape1, flowerSizePx),          actualPetalColor,                              flowerSizePx),
                LayerData(shapePath(shape2, flowerSizePx * 0.95f),  lerp(actualPetalColor, Color.Black, 0.033f),   flowerSizePx * 0.95f),
                LayerData(shapePath(shape1, flowerSizePx * 0.75f),  lerp(actualPetalColor, Color.Black, 0.063f),   flowerSizePx * 0.75f),
                LayerData(shapePath(shape2, flowerSizePx * 0.65f),  lerp(actualPetalColor, Color.Black, 0.1f),     flowerSizePx * 0.65f),
                LayerData(shapePath(shape2, flowerSizePx * 0.4f),   lerp(actualPetalColor, Color.Black, 0.12f),    flowerSizePx * 0.4f),
            )

            FlowerFrame.DandelionFrame(
                points              = stable.points.toList(),
                stemCanvasWidth     = stemCanvasW,
                stemHeightPx        = stemHeightPx,
                stemWidthPx         = stemWidthPx,
                actualStemColor     = autoWitherColor(stemColor, witherAmount),
                flowerSizePx        = flowerSizePx,
                layers              = layers,
                displayTranslationX = p3xPx,
                displayRotation     = bentAngle + flowerExtraSway,
                petalDroop          = witherAmount * 40f
            )
        }
    }

    data class SunflowerSpec(
        override val model: FlowerModel.Sunflower,
        val random: Random,
        val breezeStrength: Float,
        val phase: Float,
        val shadow: FlowerShadow?
    ) : FlowerSpec<FlowerFrame.SunflowerFrame>(model) {

        override fun computeFrame(
            density: Density,
        ): FlowerFrame.SunflowerFrame = with(model) {
            val safeBreezeStrength = breezeStrength.takeIf { it > 0f } ?: 1f

            val swayOffset = random.nextFloat() * 2f * PI.toFloat()
            val bendSign   = if (random.nextBoolean()) 1f else -1f

            val animationStrengthFactor = 1f - witherAmount * 0.9f

            val stemHeightPx = with(density) { localStemHeight.toPx() }
            val stemWidthPx  = with(density) { localStemWidth.toPx() }
            val stemCanvasW  = with(density) { (localStemWidth * 3).toPx() }
            val flowerSizePx = with(density) { flowerSize.toPx() }

            val rawScale    = with(density) { height.toPx() / 120.dp.toPx() }
            val scaleFactor = sqrt(rawScale.coerceAtLeast(0.09f))

            val permanentBendPx = (bendSign * 6f * (0.25f + witherAmount) * scaleFactor)
                .coerceIn(-20f, 20f)

            val swayAmplitudePx = (5f * safeBreezeStrength * animationStrengthFactor * scaleFactor)
                .coerceAtMost(14f)
            val totalSwayPx = sin(phase + swayOffset) * swayAmplitudePx
            val p3xPx = permanentBendPx + totalSwayPx * 0.75f
            val p2xPx = permanentBendPx * 0.5f - totalSwayPx * 0.25f
            val dx    = p3xPx - p2xPx

            val dy        = stemHeightPx * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f
            val hangCurve = sin(witherAmount * PI / 2.0).toFloat()
            val bentAngle = (baseAngle * (1f + hangCurve * 0.8f)).coerceIn(-100f, 100f)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * safeBreezeStrength * animationStrengthFactor

            val stemSegments  = random.nextInt(3, 6)
            val wobbleOffsets = List(stemSegments) {
                (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
            }

            val centerX     = stemCanvasW / 2f
            val stemBottomY = stemHeightPx - 2f
            val segHeight   = stemBottomY / stemSegments
            val bendPx      = tan(toRadians(bentAngle)) * stemHeightPx / 3f

            val points = (0..stemSegments).map { i ->
                val progress = i.toFloat() / stemSegments
                val y        = stemBottomY - segHeight * i
                val wobble   = wobbleOffsets.getOrNull(i.coerceAtMost(wobbleOffsets.lastIndex)).orZero()
                val baseX    = centerX + (bendPx * progress + wobble * (1f - witherAmount))
                val topX     = centerX + p3xPx
                val smoothedX = if (i == stemSegments) topX
                else lerp(baseX, topX, progress * 0.15f)
                Offset(smoothedX, y)
            }

            // ── Shapes ───────────────────────────────────────────────────────
            val numPetals      = 10
            val rotationOffset = 180f / numPetals
            val polygon = RoundedPolygon.star(
                numVerticesPerRadius = numPetals,
                radius = 1f,
                innerRadius = 0.35f,
                rounding = CornerRounding(0.06f),
                innerRounding = CornerRounding(0.06f)
            )
            val shapeBottom = polygon.toShapeKotlin()
            val shapeTop    = polygon.toShapeKotlin(rotationOffset.roundToInt())

            fun shapePath(shape: Shape): Path {
                val outline = shape.createOutline(
                    size = Size(flowerSizePx, flowerSizePx),
                    layoutDirection = LayoutDirection.Ltr,
                    density = density
                )
                return (outline as? Outline.Generic)?.path ?: Path()
            }

            val actualPetalColor  = autoWitherColor(petalColor, witherAmount)
            val actualCenterColor = autoWitherColor(centerColor, witherAmount)

            FlowerFrame.SunflowerFrame(
                points              = points,
                stemCanvasWidth     = stemCanvasW,
                stemHeightPx        = stemHeightPx,
                stemWidthPx         = stemWidthPx,
                actualStemColor     = autoWitherColor(stemColor, witherAmount),
                flowerSizePx        = flowerSizePx,
                bottomPetalPath     = shapePath(shapeBottom),
                topPetalPath        = shapePath(shapeTop),
                bottomPetalColor    = lerp(actualPetalColor, Color.Black, 0.1f),
                topPetalColor       = actualPetalColor,
                actualCenterColor   = actualCenterColor,
                centerRadiusPx      = flowerSizePx * FlowerModel.Sunflower.CENTER_PETAL_RATIO / 2f,
                displayTranslationX = p3xPx,
                displayRotation     = bentAngle + flowerExtraSway,
                petalDroop          = witherAmount * 40f
            )
        }
    }

    data class CactusSpec(
        override val model: FlowerModel.Cactus,
        val random: Random,
        val flowerShape: Shape,
        val breezeStrength: Float,
        val phase: Float,
        val shadow: FlowerShadow?
    ) : FlowerSpec<FlowerFrame.CactusFrame>(model) {

        data class CactusStemData(
            val stemCenterX: Float,
            val stemBottomY: Float,
            val stemHeightPx: Float,
            val stemMaxWidthPx: Float,
            val spinePositionsPerLine: List<List<Float>>,
            val bisectorVariations: List<Float>,
            val isOutwardsList: List<Boolean>
        )

        data class CactusArmData(
            val attachX: Float,
            val attachY: Float,
            val rotationDeg: Float,
            val stem: CactusStemData,
            val flowerSizePx: Float?,
            val flowerColor: Color?,
            val shadow: FlowerShadow?
        )

        override fun computeFrame(density: Density): FlowerFrame.CactusFrame = with(model) {
            val safeBreezeStrength = breezeStrength.takeIf { it > 0f } ?: 1f
            val animationStrengthFactor = 1f - witherAmount * 0.9f

            val swayOffset = random.nextFloat() * 2f * PI.toFloat()
            val swayAngle = sin(phase + swayOffset) * 6f * safeBreezeStrength * animationStrengthFactor

            val actualBaseColor = autoWitherColor(baseColor, witherAmount)
            val actualThornColor = autoWitherColor(thornColor, witherAmount)
            val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)

            val maxWidthPx = with(density) { witheredMaxWidth.toPx() }
            val flowerSizePx = with(density) { flowerSize.toPx() }
            val widthPx = with(density) { width.toPx() }
            val witheredHeightPx = with(density) { witheredHeight.toPx() }

            val mainNumLines = 4
            val mainNumSpinesPerLine = 13
            val mainSpinePositionsPerLine = List(mainNumLines) {
                List(mainNumSpinesPerLine) { idx ->
                    ((idx.toFloat() + random.nextFloat()) / mainNumSpinesPerLine.toFloat()) * 0.9f + 0.05f
                }
            }
            val mainBisectorVariations = List(mainNumLines * mainNumSpinesPerLine) {
                random.nextFloat() * 40f - 20f
            }
            val mainIsOutwardsList = List(mainNumLines * mainNumSpinesPerLine) { index ->
                when (index / mainNumSpinesPerLine) {
                    0, 3 -> true
                    else -> random.nextBoolean()
                }
            }

            val numArms = random.nextInt(2) + 2
            val sideSigns = if (numArms == 2) listOf(-1f, 1f) else {
                List(numArms) { if (random.nextBoolean()) -1f else 1f }
            }
            val armProgress = (0 until numArms).map { random.nextFloat() * 0.5f + 0.2f }.sorted()

            val armNumLines = 3
            val armNumSpinesPerLine = 7
            val armSpinePositionsPerArm = List(numArms) {
                List(armNumLines) {
                    List(armNumSpinesPerLine) { idx ->
                        ((idx.toFloat() + random.nextFloat()) / armNumSpinesPerLine.toFloat()) * 0.9f + 0.05f
                    }
                }
            }
            val armBisectorVariations = List(numArms * armNumLines * armNumSpinesPerLine) {
                random.nextFloat() * 40f - 20f
            }
            val armIsOutwardsList = List(numArms * armNumLines * armNumSpinesPerLine) { index ->
                val lineInArm = (index % (armNumLines * armNumSpinesPerLine)) / armNumSpinesPerLine
                when (lineInArm) {
                    0, armNumLines - 1 -> true
                    else -> random.nextBoolean()
                }
            }

            val armLengthPx = maxWidthPx * .8f
            val armThicknessPx = maxWidthPx * .55f
            val armNumSpines = armNumLines * armNumSpinesPerLine
            var armVariationIndex = 0

            val mainStem = CactusStemData(
                stemCenterX = widthPx / 2f,
                stemBottomY = witheredHeightPx,
                stemHeightPx = witheredHeightPx,
                stemMaxWidthPx = maxWidthPx,
                spinePositionsPerLine = mainSpinePositionsPerLine,
                bisectorVariations = mainBisectorVariations,
                isOutwardsList = mainIsOutwardsList
            )

            val arms = armProgress.mapIndexed { armIndex, prog ->
                val sideSign = sideSigns[armIndex]
                val attachY = if (sideSign == -1f) {
                    witheredHeightPx * (1f - prog).coerceIn(0f, .1f)
                }else witheredHeightPx * (1f - prog).coerceIn(.4f, .6f)

                val stemWidthAtProg = bodyWidthAt(prog) * maxWidthPx
                val attachX = widthPx / 2f - sideSign * ((stemWidthAtProg + armThicknessPx) / 2f) - armThicknessPx

                val stem = CactusStemData(
                    stemCenterX = 0f,
                    stemBottomY = 0f,
                    stemHeightPx = armLengthPx,
                    stemMaxWidthPx = armThicknessPx,
                    spinePositionsPerLine = armSpinePositionsPerArm[armIndex],
                    bisectorVariations = armBisectorVariations.subList(
                        armVariationIndex, armVariationIndex + armNumSpines
                    ),
                    isOutwardsList = armIsOutwardsList.subList(
                        armVariationIndex, armVariationIndex + armNumSpines
                    )
                )
                armVariationIndex += armNumSpines

                CactusArmData(
                    attachX = attachX,
                    attachY = attachY,
                    rotationDeg = sideSign * 45f,
                    stem = stem,
                    flowerSizePx = if (hasArmFlower) flowerSizePx * .55f else null,
                    flowerColor = if (hasArmFlower) actualFlowerColor else null,
                    shadow = if (hasArmFlower) shadow?.copy(offset = shadow.offset * .55f) else null
                )
            }

            FlowerFrame.CactusFrame(
                flowerShape = flowerShape,
                mainStem = mainStem,
                armStems = arms,
                actualBaseColor = actualBaseColor,
                actualThornColor = actualThornColor,
                actualFlowerColor = actualFlowerColor,
                flowerSizePx = flowerSizePx,
                shadow = shadow,
                swayAngle = swayAngle,
                witherAmount = witherAmount,
                hasFlower = hasFlower,
                hasArmFlower = hasArmFlower
            )
        }
    }
}
