package ui.account.affect.components.flower

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import augmy.interactive.com.ui.landing.components.FlowerSpec
import augmy.interactive.com.ui.landing.components.LayerData
import augmy.interactive.com.ui.landing.components.toRadians
import ui.account.affect.components.flower.FlowerUtils.FlowerShadow
import ui.account.affect.components.flower.FlowerUtils.bodyWidthAt
import ui.account.affect.components.flower.FlowerUtils.drawStem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

sealed class FlowerFrame {
    abstract fun DrawScope.draw(containerSize: Size, density: Density)

    data class GenericFrame(
        val points: List<Offset>,
        val stemCanvasWidth: Float,
        val stemHeightPx: Float,
        val stemWidthPx: Float,
        val actualStemColor: Color,
        val actualFlowerColor: Color,
        val flowerPath: Path,
        val shadowPath: Path?,
        val shadowColor: Color?,
        val shadowOffsetPx: Float,
        val flowerSizePx: Float,
        val flowerSizeWithShadow: Float,
        val displayTranslationX: Float,
        val displayRotation: Float,
        val petalDroop: Float
    ): FlowerFrame() {
        override fun DrawScope.draw(containerSize: Size, density: Density) {
            val stemLeft = containerSize.width / 2f - stemCanvasWidth / 2f
            val stemTop  = containerSize.height - stemHeightPx - with(density) { 4.dp.toPx() }

            val stemPath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 2) {
                    val p1 = points[i + 1]; val p2 = points[i + 2]
                    quadraticTo(p1.x, p1.y, (p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f)
                }
                lineTo(points.last().x, points.last().y)
            }

            withTransform({ translate(stemLeft, stemTop) }) {
                drawPath(
                    path  = stemPath,
                    color = actualStemColor,
                    style = Stroke(width = stemWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            val flowerCenterX = containerSize.width / 2f + displayTranslationX
            val flowerBaseY   = containerSize.height - stemHeightPx

            withTransform({
                rotate(displayRotation + petalDroop, pivot = Offset(flowerCenterX, flowerBaseY))
                translate(
                    left = flowerCenterX - flowerSizeWithShadow / 2f,
                    top  = flowerBaseY - flowerSizeWithShadow
                )
            }) {
                shadowPath?.let { sp ->
                    withTransform({ translate(shadowOffsetPx, shadowOffsetPx) }) {
                        drawPath(sp, shadowColor!!)
                    }
                }
                drawPath(flowerPath, actualFlowerColor)
            }
        }
    }

    data class ChamomileFrame(
        val points: List<Offset>,
        val stemCanvasWidth: Float,
        val stemHeightPx: Float,
        val stemWidthPx: Float,
        val actualStemColor: Color,
        val actualPetalColor: Color,
        val actualCenterColor: Color,
        val petalPath: Path,
        val shadowPath: Path?,
        val shadowColor: Color?,
        val shadowOffsetPx: Float,
        val flowerSizePx: Float,
        val flowerSizeWithShadow: Float,
        val centerRadiusPx: Float,
        val displayTranslationX: Float,
        val displayRotation: Float,
        val petalDroop: Float
    ) : FlowerFrame() {
        override fun DrawScope.draw(containerSize: Size, density: Density) {
            val stemLeft = containerSize.width / 2f - stemCanvasWidth / 2f
            val stemTop  = containerSize.height - stemHeightPx - with(density) { 4.dp.toPx() }

            withTransform({ translate(stemLeft, stemTop) }) {
                drawStem(points, stemWidthPx, actualStemColor)
            }

            val flowerCenterX = containerSize.width / 2f + displayTranslationX
            val flowerBaseY   = containerSize.height - stemHeightPx

            withTransform({
                rotate(displayRotation + petalDroop, pivot = Offset(flowerCenterX, flowerBaseY))
                translate(
                    left = flowerCenterX - flowerSizeWithShadow / 2f,
                    top  = flowerBaseY - flowerSizeWithShadow
                )
            }) {
                shadowPath?.let { sp ->
                    withTransform({ translate(shadowOffsetPx, shadowOffsetPx) }) {
                        drawPath(sp, shadowColor!!)
                    }
                }

                drawPath(petalPath, actualPetalColor)

                drawCircle(
                    color = actualCenterColor,
                    radius = centerRadiusPx,
                    center = Offset(
                        x = flowerSizeWithShadow / 2f,
                        y = flowerSizeWithShadow / 2f
                    )
                )
            }
        }
    }

    data class DandelionFrame(
        val points: List<Offset>,
        val stemCanvasWidth: Float,
        val stemHeightPx: Float,
        val stemWidthPx: Float,
        val actualStemColor: Color,
        val flowerSizePx: Float,
        val layers: List<LayerData>,
        val displayTranslationX: Float,
        val displayRotation: Float,
        val petalDroop: Float
    ) : FlowerFrame() {
        override fun DrawScope.draw(containerSize: Size, density: Density) {
            val stemLeft = containerSize.width / 2f - stemCanvasWidth / 2f
            val stemTop  = containerSize.height - stemHeightPx - with(density) { 4.dp.toPx() }

            withTransform({ translate(stemLeft, stemTop) }) {
                drawStem(points, stemWidthPx, actualStemColor)
            }

            val flowerCenterX = containerSize.width / 2f + displayTranslationX
            val flowerBaseY   = containerSize.height - stemHeightPx

            // Mirror: Box(size=flowerSize, graphicsLayer { translationX/Y, rotationZ, transformOrigin(0.5,1) })
            withTransform({
                rotate(displayRotation + petalDroop, pivot = Offset(flowerCenterX, flowerBaseY))
                translate(
                    left = flowerCenterX - flowerSizePx / 2f,
                    top  = flowerBaseY   - flowerSizePx
                )
            }) {
                // Mirror: contentAlignment=Center → each child is centered within flowerSizePx
                layers.forEach { layer ->
                    val offset = (flowerSizePx - layer.sizePx) / 2f
                    withTransform({ translate(offset, offset) }) {
                        drawPath(layer.path, layer.color)
                    }
                }
            }
        }
    }

    data class SunflowerFrame(
        val points: List<Offset>,
        val stemCanvasWidth: Float,
        val stemHeightPx: Float,
        val stemWidthPx: Float,
        val actualStemColor: Color,
        val flowerSizePx: Float,
        val bottomPetalPath: Path,
        val topPetalPath: Path,
        val bottomPetalColor: Color,
        val topPetalColor: Color,
        val actualCenterColor: Color,
        val centerRadiusPx: Float,
        val displayTranslationX: Float,
        val displayRotation: Float,
        val petalDroop: Float
    ) : FlowerFrame() {
        override fun DrawScope.draw(containerSize: Size, density: Density) {
            val stemLeft = containerSize.width / 2f - stemCanvasWidth / 2f
            val stemTop  = containerSize.height - stemHeightPx - with(density) { 4.dp.toPx() }

            withTransform({ translate(stemLeft, stemTop) }) {
                drawStem(points, stemWidthPx, actualStemColor)
            }

            val flowerCenterX = containerSize.width / 2f + displayTranslationX
            val flowerBaseY   = containerSize.height - stemHeightPx

            withTransform({
                rotate(displayRotation + petalDroop, pivot = Offset(flowerCenterX, flowerBaseY))
                translate(
                    left = flowerCenterX - flowerSizePx / 2f,
                    top  = flowerBaseY   - flowerSizePx
                )
            }) {
                drawPath(bottomPetalPath, bottomPetalColor)
                drawPath(topPetalPath, topPetalColor)
                drawCircle(
                    color  = actualCenterColor,
                    radius = centerRadiusPx,
                    center = Offset(flowerSizePx / 2f, flowerSizePx / 2f)
                )
            }
        }
    }

    data class CactusFrame(
        val flowerShape: Shape,
        val mainStem: FlowerSpec.CactusSpec.CactusStemData,
        val armStems: List<FlowerSpec.CactusSpec.CactusArmData>,
        val actualBaseColor: Color,
        val actualThornColor: Color,
        val actualFlowerColor: Color?,
        val flowerSizePx: Float,
        val shadow: FlowerShadow?,
        val swayAngle: Float,
        val witherAmount: Float,
        val hasFlower: Boolean,
        val hasArmFlower: Boolean
    ) : FlowerFrame() {

        companion object {
            fun DrawScope.drawFlower(
                flowerShape: Shape,
                centerX: Float,
                bottomY: Float,
                flowerSizePx: Float,
                color: Color,
                shadow: FlowerShadow?,
            ) {
                val centerY = bottomY - flowerSizePx / 1.75f

                if (shadow != null) {
                    val offsetPx = density * shadow.offset.value
                    val outerSizePx = flowerSizePx + 2 * offsetPx
                    val outerOutline = flowerShape.createOutline(
                        size = Size(outerSizePx, outerSizePx),
                        layoutDirection = LayoutDirection.Ltr,
                        density = this
                    )
                    val outerLeft = centerX - outerSizePx / 2f
                    val outerTop = centerY - outerSizePx / 2f
                    withTransform({ translate(outerLeft, outerTop) }) {
                        drawOutline(outline = outerOutline, color = shadow.color, style = Fill)
                    }
                }

                val outline = flowerShape.createOutline(
                    size = Size(flowerSizePx, flowerSizePx),
                    layoutDirection = LayoutDirection.Ltr,
                    density = this
                )
                val left = centerX - flowerSizePx / 2f
                val top = centerY - flowerSizePx / 2f
                withTransform({ translate(left, top) }) {
                    drawOutline(outline = outline, color = color, style = Fill)
                }
            }

            fun DrawScope.drawCactusStem(
                stemCenterX: Float,
                stemBottomY: Float,
                stemHeightPx: Float,
                stemMaxWidthPx: Float,
                baseColor: Color,
                thornColor: Color,
                spinePositionsPerLine: List<List<Float>>,
                bisectorVariations: List<Float>,
                isOutwardsList: List<Boolean>
            ) {
                val bodyFillPath = Path().apply {
                    moveTo(stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(0f), stemBottomY)
                    for (i in 0..100) {
                        val prog = i / 100f
                        lineTo(
                            stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(prog),
                            stemBottomY - prog * stemHeightPx
                        )
                    }
                    lineTo(stemCenterX + stemMaxWidthPx / 2f, stemBottomY - stemHeightPx)
                    for (i in 100 downTo 0) {
                        val prog = i / 100f
                        lineTo(
                            stemCenterX + stemMaxWidthPx / 2f * bodyWidthAt(prog),
                            stemBottomY - prog * stemHeightPx
                        )
                    }
                    close()
                }
                drawPath(bodyFillPath, color = baseColor)

                val domeHeight = stemMaxWidthPx * 0.35f
                val domeRect = Rect(
                    left = stemCenterX - stemMaxWidthPx / 2f,
                    top = stemBottomY - stemHeightPx - domeHeight,
                    right = stemCenterX + stemMaxWidthPx / 2f,
                    bottom = stemBottomY - stemHeightPx + domeHeight + 2f
                )

                val domeFillPath = Path().apply {
                    moveTo(stemCenterX - stemMaxWidthPx / 2f, stemBottomY - stemHeightPx)
                    arcTo(domeRect, 180f, 180f, false)
                    close()
                }
                drawPath(domeFillPath, color = baseColor)

                val fullOutlinePath = Path().apply {
                    moveTo(stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(0f), stemBottomY)
                    for (i in 1..100) {
                        val prog = i / 100f
                        lineTo(
                            stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(prog),
                            stemBottomY - prog * stemHeightPx
                        )
                    }
                    arcTo(domeRect, 180f, 180f, false)
                    for (i in 99 downTo 0) {
                        val prog = i / 100f
                        lineTo(
                            stemCenterX + stemMaxWidthPx / 2f * bodyWidthAt(prog),
                            stemBottomY - prog * stemHeightPx
                        )
                    }
                    close()
                }

                drawPath(
                    path = fullOutlinePath,
                    color = thornColor,
                    style = Stroke(
                        width = stemMaxWidthPx * 0.07f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                val ribPathLeft = Path()
                val ribPathRight = Path()
                val straightUntil = 0.82f
                val ribOffset = stemMaxWidthPx / 2f * 0.45f

                for (i in 0..(straightUntil * 100).toInt()) {
                    val prog = i / 100f
                    val y = stemBottomY - prog * stemHeightPx
                    if (i == 0) {
                        ribPathLeft.moveTo(stemCenterX - ribOffset, y)
                        ribPathRight.moveTo(stemCenterX + ribOffset, y)
                    } else {
                        ribPathLeft.lineTo(stemCenterX - ribOffset, y)
                        ribPathRight.lineTo(stemCenterX + ribOffset, y)
                    }
                }

                val curveSteps = 60
                for (step in 1..curveSteps) {
                    val t = step / curveSteps.toFloat()
                    val easeT = 1f - (1f - t).pow(3f)
                    val currentOffset = ribOffset * (1f - easeT)
                    val angleRad = PI.toFloat() * (1f + 0.5f * t)
                    val y = domeHeight * sin(angleRad)
                    ribPathLeft.lineTo(stemCenterX - currentOffset, stemBottomY - stemHeightPx + y)
                    ribPathRight.lineTo(stemCenterX + currentOffset, stemBottomY - stemHeightPx + y)
                }

                drawPath(
                    ribPathLeft,
                    color = thornColor,
                    style = Stroke(width = stemMaxWidthPx * 0.05f, cap = StrokeCap.Round)
                )
                drawPath(
                    ribPathRight,
                    color = thornColor,
                    style = Stroke(width = stemMaxWidthPx * 0.05f, cap = StrokeCap.Round)
                )

                fun drawSpine(baseX: Float, baseY: Float, bisectorDeg: Float) {
                    val prongLength = stemMaxWidthPx * 0.2f
                    val angle1Deg = bisectorDeg - 40f
                    val angle1Rad = toRadians(angle1Deg)
                    val dx1 = prongLength * cos(angle1Rad)
                    val dy1 = prongLength * sin(angle1Rad)
                    drawLine(
                        color = thornColor,
                        start = Offset(baseX, baseY),
                        end = Offset(baseX + dx1, baseY + dy1),
                        strokeWidth = stemMaxWidthPx * 0.04f,
                        cap = StrokeCap.Round
                    )
                    val angle2Deg = bisectorDeg + 40f
                    val angle2Rad = toRadians(angle2Deg)
                    val dx2 = prongLength * cos(angle2Rad)
                    val dy2 = prongLength * sin(angle2Rad)
                    drawLine(
                        color = thornColor,
                        start = Offset(baseX, baseY),
                        end = Offset(baseX + dx2, baseY + dy2),
                        strokeWidth = stemMaxWidthPx * 0.04f,
                        cap = StrokeCap.Round
                    )
                }

                val spineXFunctions = listOf(
                    { p: Float -> stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(p.coerceAtMost(1f)) },
                    { p: Float -> stemCenterX - stemMaxWidthPx / 2f * bodyWidthAt(p.coerceAtMost(1f)) * 0.45f },
                    { p: Float -> stemCenterX + stemMaxWidthPx / 2f * bodyWidthAt(p.coerceAtMost(1f)) * 0.45f },
                    { p: Float -> stemCenterX + stemMaxWidthPx / 2f * bodyWidthAt(p.coerceAtMost(1f)) }
                )
                var spineIndex = 0
                for (lineIndex in spinePositionsPerLine.indices) {
                    val positions = spinePositionsPerLine[lineIndex]
                    for (progress in positions) {
                        val y = stemBottomY - progress * stemHeightPx
                        val xFunc = spineXFunctions[lineIndex]
                        val baseX = xFunc(progress)
                        val outwards = if (baseX < stemCenterX) 180f else 0f
                        val isOutwards = isOutwardsList[spineIndex]
                        val bisectorBase = if (isOutwards) outwards else outwards + 180f
                        val bisector = bisectorBase + bisectorVariations[spineIndex]
                        drawSpine(baseX, y, bisector)
                        spineIndex++
                    }
                }
            }
        }

        override fun DrawScope.draw(containerSize: Size, density: Density) {
            val stemHeightPx = mainStem.stemHeightPx
            val stemOffsetY  = containerSize.height - stemHeightPx

            withTransform({
                rotate(swayAngle, pivot = Offset(containerSize.width / 2f, containerSize.height))
            }) {
                withTransform({ translate(containerSize.width / 4f, stemOffsetY) }) {

                    armStems.forEach { arm ->
                        withTransform({
                            translate(
                                arm.attachX,
                                arm.attachY
                            )
                            rotate(arm.rotationDeg)
                        }) {
                            drawCactusStem(
                                stemCenterX = arm.stem.stemCenterX,
                                stemBottomY = arm.stem.stemBottomY,
                                stemHeightPx = arm.stem.stemHeightPx,
                                stemMaxWidthPx = arm.stem.stemMaxWidthPx,
                                baseColor = actualBaseColor,
                                thornColor = actualThornColor,
                                spinePositionsPerLine = arm.stem.spinePositionsPerLine,
                                bisectorVariations = arm.stem.bisectorVariations,
                                isOutwardsList = arm.stem.isOutwardsList
                            )
                            if (hasArmFlower && arm.flowerSizePx != null && arm.flowerColor != null) {
                                drawFlower(
                                    flowerShape = flowerShape,
                                    centerX = 0f,
                                    bottomY = -arm.stem.stemHeightPx,
                                    flowerSizePx = arm.flowerSizePx,
                                    color = arm.flowerColor,
                                    shadow = arm.shadow
                                )
                            }
                        }
                    }
                    drawCactusStem(
                        stemCenterX = mainStem.stemCenterX,
                        stemBottomY = mainStem.stemBottomY,
                        stemHeightPx = mainStem.stemHeightPx,
                        stemMaxWidthPx = mainStem.stemMaxWidthPx,
                        baseColor = actualBaseColor,
                        thornColor = actualThornColor,
                        spinePositionsPerLine = mainStem.spinePositionsPerLine,
                        bisectorVariations = mainStem.bisectorVariations,
                        isOutwardsList = mainStem.isOutwardsList
                    )

                    if (hasFlower && flowerSizePx > 0f) {
                        drawFlower(
                            flowerShape = flowerShape,
                            centerX = mainStem.stemCenterX,
                            bottomY = 0f,
                            flowerSizePx = flowerSizePx,
                            color = actualFlowerColor!!,
                            shadow = shadow
                        )
                    }
                }
            }
        }
    }
}
