package ui.account.affect.components.flower

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import augmy.interactive.com.ui.landing.components.GenericStableState
import augmy.interactive.com.ui.landing.components.orZero
import augmy.interactive.com.ui.landing.components.toRadians
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

object FlowerUtils {
    const val MAX_BEND_DEGREES = 10f

    data class FlowerShadow(
        val color: Color,
        val offset: Dp = 1.5.dp
    )

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

    operator fun Color.times(factor: Float) = Color(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f),
        alpha = alpha
    )

    fun bodyWidthAt(progress: Float): Float {
        val p = progress.coerceIn(0f, 1f)
        return when {
            p < 0.15f -> androidx.compose.ui.util.lerp(0.55f, 0.85f, p / 0.15f)
            p < 0.75f -> androidx.compose.ui.util.lerp(0.85f, 1f, (p - 0.15f) / 0.6f)
            else -> 1f
        }
    }

    data class StemPose(
        val position: Offset,
        val tangentUpDeg: Float,
        val outwardNormal: Offset
    )

    fun computeWindBend(
        witherAmount: Float,
        phase: Float,
        swayOffset: Float,
        breezeStrength: Float,
        animationStrengthFactor: Float
    ): Float {
        val windBend =
            sin(phase * 1.0f + swayOffset * 0.4f) *
                    1.2f * breezeStrength * animationStrengthFactor

        val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
        val witherBendScale = 1f + hangCurve * 0.8f

        return (windBend * witherBendScale)
            .coerceIn(-MAX_BEND_DEGREES, MAX_BEND_DEGREES)
    }

    fun computeDisplayRotationTarget(
        bentDegrees: Float,
        witherAmount: Float,
        phase: Float,
        swayOffset: Float,
        breezeStrength: Float,
        animationStrengthFactor: Float
    ): Float {
        val windBend =
            sin(phase * 1.0f + swayOffset * 0.4f) *
                    1.2f * breezeStrength * animationStrengthFactor

        val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
        val witherBendScale = 1f + hangCurve * 0.8f
        val clampedWindBend =
            (windBend * witherBendScale).coerceIn(-MAX_BEND_DEGREES, MAX_BEND_DEGREES)

        return (bentDegrees + clampedWindBend).coerceIn(-85f, 85f)
    }

    fun buildStemPoints(
        points: MutableList<Offset>,
        centerX: Float,
        stemBottomY: Float,
        displayRotation: Float,
        displayTranslationX: Float,
        witherAmount: Float,
        wobbleOffsets: List<Float>
    ) {
        val bendPx = tan(displayRotation * (PI.toFloat() / 180f)) * stemBottomY / 3f
        val tipOffsetX = bendPx + displayTranslationX
        val res = points.lastIndex

        for (i in 0..res) {
            val t = i.toFloat() / res
            val y = stemBottomY * (1f - t)
            val cantilever = t * t * (3f - 2f * t)
            val wobble = wobbleOffsets[i] * t * (1f - witherAmount)
            points[i] = Offset(centerX + tipOffsetX * cantilever + wobble, y)
        }
    }

    fun sampleStemPose(
        points: List<Offset>,
        tRaw: Float
    ): StemPose {
        val t = tRaw.coerceIn(0f, 1f)
        val stemPos = evalStemAt(points, t)

        val epsilon = 0.01f
        val pA = evalStemAt(points, (t - epsilon).coerceAtLeast(0f))
        val pB = evalStemAt(points, (t + epsilon).coerceAtMost(1f))
        val tangent = pB - pA
        val len = hypot(tangent.x, tangent.y).takeIf { it > 0f } ?: 1f

        val tanX = tangent.x / len
        val tanY = tangent.y / len

        val rawPerpX = -tanY

        val groundBlend = (t / 0.06f).coerceIn(0f, 1f)
        val blendedPerpX = androidx.compose.ui.util.lerp(1f, rawPerpX, groundBlend)
        val blendedPerpY = androidx.compose.ui.util.lerp(0f, tanX, groundBlend)
        val bLen = hypot(blendedPerpX, blendedPerpY).takeIf { it > 0f } ?: 1f

        val finalPerpX = blendedPerpX / bLen
        val finalPerpY = blendedPerpY / bLen

        val tangentUpDeg = atan2(tanX, -tanY) * (180f / PI.toFloat())

        return StemPose(
            position = stemPos,
            tangentUpDeg = tangentUpDeg,
            outwardNormal = Offset(finalPerpX, finalPerpY)
        )
    }

    fun makeFissureWiggle(depth: Int, roughness: Float, rng: Random): List<Float> {
        val pts = mutableListOf(0f, 0f)
        var r = roughness
        repeat(depth) {
            val expanded = mutableListOf<Float>()
            for (i in 0 until pts.size - 1) {
                expanded.add(pts[i])
                val mid = (pts[i] + pts[i + 1]) / 2f + (rng.nextFloat() - 0.5f) * r
                expanded.add(mid)
            }
            expanded.add(pts.last())
            pts.clear()
            pts.addAll(expanded)
            r *= 0.5f
        }
        return pts
    }

    fun halfWidthAt(t: Float, stemWidthPx: Float): Float {
        val flareMultiplier = 1.5f
        val flareZone = 0.13f
        val norm = (t / flareZone).coerceIn(0f, 1f)
        val smooth = norm * norm * (3f - 2f * norm)
        return stemWidthPx / 2f * androidx.compose.ui.util.lerp(flareMultiplier, 1f, smooth)
    }

    fun evalStemAt(points: List<Offset>, t: Float): Offset {
        val scaled = t * (points.size - 1)
        val i = scaled.toInt().coerceIn(0, points.size - 2)
        return androidx.compose.ui.geometry.lerp(points[i], points[i + 1], scaled - i)
    }

    fun GenericStableState.updatePoints(
        stemCanvasSize: Size,
        displayTranslationX: Float,
        bentAngle: Float,
        permanentBendPx: Float,
        witherAmount: Float
    ) {
        val centerX = stemCanvasSize.width / 2f
        val stemBottomY = stemCanvasSize.height - 2f
        val segHeight = stemBottomY / stemSegments
        val bendDir = sign(permanentBendPx)
        val bendPx = (tan(toRadians(bentAngle)) * stemCanvasSize.height / 3f)

        for (i in 0..stemSegments) {
            val progress = i.toFloat() / stemSegments
            val y = stemBottomY - segHeight * i
            val wobble = wobbleOffsets.getOrNull(i.coerceAtMost(wobbleOffsets.lastIndex)).orZero()
            val baseX = centerX + (bendPx * progress + wobble * (1f - witherAmount)) * bendDir
            val topX = centerX + displayTranslationX
            val smoothedX = if (i == stemSegments) topX
            else androidx.compose.ui.util.lerp(baseX, topX, progress * 0.15f)
            points[i] = Offset(smoothedX, y)
        }
    }

    fun DrawScope.drawStem(
        points: List<Offset>,
        stemWidthPx: Float,
        color: Color
    ) {
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 2) {
                val p1 = points[i + 1]; val p2 = points[i + 2]
                quadraticTo(p1.x, p1.y, (p1.x + p2.x) / 2f, (p1.y + p2.y) / 2f)
            }
            lineTo(points.last().x, points.last().y)
        }
        drawPath(path, color, style = Stroke(width = stemWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }

    // Copy from material3 internal in order to make it non-composable
    fun RoundedPolygon.toShapeKotlin(startAngle: Int = 0): Shape {
        return object : Shape {
            private val shapePath: Path = this@toShapeKotlin.toPathKotlin(startAngle)
            private var workPath: Path? = null
            private var lastSize = Size.Unspecified

            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Outline {
                if (size != lastSize || workPath == null) {
                    lastSize = size
                    workPath = Path()
                } else {
                    workPath!!.rewind()
                }

                val path = workPath!!
                path.addPath(shapePath)

                val scaleMatrix = Matrix().apply {
                    scale(x = size.width, y = size.height)
                }
                path.transform(scaleMatrix)
                path.translate(size.center - path.getBounds().center)

                return Outline.Generic(path)
            }
        }
    }

    fun RoundedPolygon.toPathKotlin(
        startAngle: Int = 0,
        path: Path = Path()
    ): Path {
        path.rewind()

        pathFromCubics(
            path = path,
            startAngle = startAngle,
            repeatPath = false,
            closePath = true,
            cubics = cubics,
            rotationPivotX = centerX,
            rotationPivotY = centerY,
        )

        return path
    }

    fun pathFromCubics(
        path: Path,
        startAngle: Int,
        repeatPath: Boolean,
        closePath: Boolean,
        cubics: List<Cubic>,
        rotationPivotX: Float,
        rotationPivotY: Float,
    ) {
        var first = true
        var firstCubic: Cubic? = null
        path.rewind()
        cubics.fastForEach {
            if (first) {
                path.moveTo(it.anchor0X, it.anchor0Y)
                if (startAngle != 0) {
                    firstCubic = it
                }
                first = false
            }
            path.cubicTo(
                it.control0X,
                it.control0Y,
                it.control1X,
                it.control1Y,
                it.anchor1X,
                it.anchor1Y,
            )
        }
        if (repeatPath) {
            var firstInRepeat = true
            cubics.fastForEach {
                if (firstInRepeat) {
                    path.lineTo(it.anchor0X, it.anchor0Y)
                    firstInRepeat = false
                }
                path.cubicTo(
                    it.control0X,
                    it.control0Y,
                    it.control1X,
                    it.control1Y,
                    it.anchor1X,
                    it.anchor1Y,
                )
            }
        }

        if (closePath) path.close()

        if (startAngle != 0 && firstCubic != null) {
            val angleToFirstCubic = toRadians(
                atan2(
                    y = cubics[0].anchor0Y - rotationPivotY,
                    x = cubics[0].anchor0X - rotationPivotX,
                )
            )
            path.transform(Matrix().apply { rotateZ(-angleToFirstCubic + startAngle) })
        }
    }
}
