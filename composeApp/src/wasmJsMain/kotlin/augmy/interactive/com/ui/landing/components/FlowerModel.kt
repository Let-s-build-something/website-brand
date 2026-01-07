package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random
import kotlin.reflect.KClass

data class FlowerShadow(
    val color: Color,
    val offset: Dp = 1.5.dp
)

sealed class FlowerModel(
    open val growthScale: Float,
    open val witherAmount: Float,
) {
    abstract val stemHeight: Dp
    abstract val stemWidth: Dp
    abstract val width: Dp
    abstract val height: Dp

    @Composable
    abstract fun BoxScope.Compose(
        displayTranslationX: Float,
        dx: Float,
        phase: Float,
        swayOffset: Float,
        permanentBendPx: Float,
        scaleFactor: Float,
        breezeStrength: Float,
        animationStrengthFactor: Float,
        shadow: FlowerShadow?,
        random: Random
    )

    data class Generic(
        override val stemHeight: Dp,
        override val stemWidth: Dp = stemHeight * STEM_HEIGH_RATIO,
        override val growthScale: Float,
        override val witherAmount: Float,
        val flowerColor: Color,
        val stemColor: Color
    ): FlowerModel(growthScale, witherAmount) {
        companion object {
            private const val FLOWER_STEM_RATIO = .8f
            private const val STEM_HEIGH_RATIO = .05f
        }

        val localStemHeight
            get() = stemHeight * growthScale * (1f - 0.3f * witherAmount)
        val flowerSize: Dp
            get() = (localStemHeight.value * FLOWER_STEM_RATIO).dp
        val localStemWidth
            get() = stemWidth * growthScale

        override val width: Dp
            get() {
                return (flowerSize.value * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun BoxScope.Compose(
            displayTranslationX: Float,
            dx: Float,
            phase: Float,
            swayOffset: Float,
            permanentBendPx: Float,
            scaleFactor: Float,
            breezeStrength: Float,
            animationStrengthFactor: Float,
            shadow: FlowerShadow?,
            random: Random
        ) {
            val density = LocalDensity.current

            val dy = with (density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val tangentAngle = bentAngle
            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemSegments = remember { random.nextInt(3, 6) }
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = tangentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val wobbleOffsets = remember {
                List(stemSegments) { (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor }
            }
            val points = remember { MutableList(stemSegments + 1) { Offset.Zero } }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
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
            val petalDroop = witherAmount * 40f

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(flowerSize + (shadow?.offset?.times(2) ?: 0.dp))
                    .graphicsLayer {
                        translationX = displayTranslationX
                        translationY = -localStemHeight.toPx()
                        rotationZ = displayRotation + petalDroop
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        clip = false
                    }
                    .then(
                        if (shadow != null) {
                            Modifier
                                .background(color = shadow.color, shape = flowerShape)
                                .padding(shadow.offset)
                        } else Modifier
                    )
                    .background(color = actualFlowerColor, shape = flowerShape)
            )
        }
    }

    data class Chamomile(
        override val stemHeight: Dp,
        override val stemWidth: Dp = stemHeight * STEM_HEIGH_RATIO,
        override val growthScale: Float,
        override val witherAmount: Float,
        val petalColor: Color,
        val centerColor: Color,
        val stemColor: Color
    ): FlowerModel(growthScale, witherAmount) {
        companion object {
            private const val FLOWER_STEM_RATIO = .6f
            private const val CENTER_PETAL_RATIO = .5f
            private const val STEM_HEIGH_RATIO = .0375f
        }

        val localStemHeight
            get() = stemHeight * growthScale * (1f - 0.3f * witherAmount)
        val flowerSize: Dp
            get() = (localStemHeight.value * FLOWER_STEM_RATIO).dp
        val localStemWidth
            get() = stemWidth * growthScale

        override val width: Dp
            get() {
                return (flowerSize.value * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun BoxScope.Compose(
            displayTranslationX: Float,
            dx: Float,
            phase: Float,
            swayOffset: Float,
            permanentBendPx: Float,
            scaleFactor: Float,
            breezeStrength: Float,
            animationStrengthFactor: Float,
            shadow: FlowerShadow?,
            random: Random
        ) {
            val density = LocalDensity.current

            val dy = with (density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val tangentAngle = bentAngle
            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemSegments = remember { random.nextInt(3, 6) }
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualCenterColor = autoWitherColor(centerColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = tangentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val wobbleOffsets = remember {
                List(stemSegments) { (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor }
            }
            val points = remember { MutableList(stemSegments + 1) { Offset.Zero } }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
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

            val numPetals = 15
            val flowerPolygon = remember {
                RoundedPolygon.star(
                    numVerticesPerRadius = numPetals,
                    radius = 1f,
                    innerRadius = 0.35f,
                    rounding = CornerRounding(0.055f),
                    innerRounding = CornerRounding(0.015f)
                )
            }
            val flowerShape = flowerPolygon.toShape()
            val petalDroop = witherAmount * 40f

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(flowerSize)
                    .graphicsLayer {
                        translationX = displayTranslationX
                        translationY = -localStemHeight.toPx()
                        rotationZ = displayRotation + petalDroop
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        clip = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = actualPetalColor,
                            shape = flowerShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * CENTER_PETAL_RATIO)
                        .background(
                            color = actualCenterColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }

    data class Dandelion(
        override val stemHeight: Dp,
        override val stemWidth: Dp = stemHeight * STEM_HEIGH_RATIO,
        override val growthScale: Float,
        override val witherAmount: Float,
        val petalColor: Color,
        val stemColor: Color
    ): FlowerModel(growthScale, witherAmount) {
        companion object {
            private const val FLOWER_STEM_RATIO = .55f
            private const val STEM_HEIGH_RATIO = .05f
        }

        val localStemHeight
            get() = stemHeight * growthScale * (1f - 0.3f * witherAmount)
        val flowerSize: Dp
            get() = (localStemHeight.value * FLOWER_STEM_RATIO).dp
        val localStemWidth
            get() = stemWidth * growthScale

        override val width: Dp
            get() {
                return (flowerSize.value * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun BoxScope.Compose(
            displayTranslationX: Float,
            dx: Float,
            phase: Float,
            swayOffset: Float,
            permanentBendPx: Float,
            scaleFactor: Float,
            breezeStrength: Float,
            animationStrengthFactor: Float,
            shadow: FlowerShadow?,
            random: Random
        ) {
            val density = LocalDensity.current

            val dy = with (density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val tangentAngle = bentAngle
            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemSegments = remember { random.nextInt(3, 6) }
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = tangentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val wobbleOffsets = remember {
                List(stemSegments) { (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor }
            }
            val points = remember { MutableList(stemSegments + 1) { Offset.Zero } }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
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

            val numPetals = 10
            val rotationOffset = 180f / numPetals.toFloat()
            val flowerPolygon = remember {
                RoundedPolygon.star(
                    numVerticesPerRadius = numPetals,
                    radius = 1f,
                    innerRadius = 0.35f,
                    rounding = CornerRounding(0.06f),
                    innerRounding = CornerRounding(0.06f)
                )
            }
            val flowerShape1 = flowerPolygon.toShape()
            val flowerShape2 = flowerPolygon.toShape(rotationOffset.roundToInt())
            val petalDroop = witherAmount * 40f

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(flowerSize)
                    .graphicsLayer {
                        translationX = displayTranslationX
                        translationY = -localStemHeight.toPx()
                        rotationZ = displayRotation + petalDroop
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        clip = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = actualPetalColor,
                            shape = flowerShape1
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * .95f)
                        .background(
                            color = lerp(actualPetalColor, Color.Black, .033f),
                            shape = flowerShape2
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * .75f)
                        .background(
                            color = lerp(actualPetalColor, Color.Black, .063f),
                            shape = flowerShape1
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * .65f)
                        .background(
                            color = lerp(actualPetalColor, Color.Black, .1f),
                            shape = flowerShape2
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * .4f)
                        .background(
                            color = lerp(actualPetalColor, Color.Black, .12f),
                            shape = flowerShape2
                        )
                )
            }
        }
    }

    data class Sunflower(
        override val stemHeight: Dp,
        override val stemWidth: Dp = stemHeight * STEM_HEIGH_RATIO,
        override val growthScale: Float,
        override val witherAmount: Float,
        val petalColor: Color,
        val centerColor: Color,
        val stemColor: Color
    ): FlowerModel(growthScale, witherAmount) {
        companion object {
            private const val FLOWER_STEM_RATIO = .8f
            private const val CENTER_PETAL_RATIO = .75f
            private const val STEM_HEIGH_RATIO = .08f
        }

        val localStemHeight
            get() = stemHeight * growthScale * (1f - 0.3f * witherAmount)
        val flowerSize: Dp
            get() = (localStemHeight.value * FLOWER_STEM_RATIO).dp
        val localStemWidth
            get() = stemWidth * growthScale

        override val width: Dp
            get() {
                return (flowerSize.value * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf { !it.isNanOrInfinite() }?.roundToInt()?.dp ?: 0.dp
            }

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun BoxScope.Compose(
            displayTranslationX: Float,
            dx: Float,
            phase: Float,
            swayOffset: Float,
            permanentBendPx: Float,
            scaleFactor: Float,
            breezeStrength: Float,
            animationStrengthFactor: Float,
            shadow: FlowerShadow?,
            random: Random
        ) {
            val density = LocalDensity.current

            val dy = with (density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val tangentAngle = bentAngle
            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemSegments = remember { random.nextInt(3, 6) }
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualCenterColor = autoWitherColor(centerColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = tangentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val wobbleOffsets = remember {
                List(stemSegments) { (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor }
            }
            val points = remember { MutableList(stemSegments + 1) { Offset.Zero } }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
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

            val numPetals = 10
            val rotationOffset = 180f / numPetals.toFloat()
            val flowerPolygon = remember {
                RoundedPolygon.star(
                    numVerticesPerRadius = numPetals,
                    radius = 1f,
                    innerRadius = 0.35f,
                    rounding = CornerRounding(0.06f),
                    innerRounding = CornerRounding(0.06f)
                )
            }
            val flowerShapeBottom = flowerPolygon.toShape()
            val flowerShapeTop = flowerPolygon.toShape(rotationOffset.roundToInt())
            val petalDroop = witherAmount * 40f

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(flowerSize)
                    .graphicsLayer {
                        translationX = displayTranslationX
                        translationY = -localStemHeight.toPx()
                        rotationZ = displayRotation + petalDroop
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        clip = false
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = lerp(actualPetalColor, Color.Black, .1f),
                            shape = flowerShapeBottom
                        )
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = actualPetalColor,
                            shape = flowerShapeTop
                        )
                )
                Box(
                    modifier = Modifier
                        .size(flowerSize * CENTER_PETAL_RATIO)
                        .background(
                            color = actualCenterColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }

    data class Cactus(
        override val stemHeight: Dp,
        override val stemWidth: Dp = stemHeight * STEM_HEIGH_RATIO,
        override val growthScale: Float = 1f,
        override val witherAmount: Float = 0f,
        val flower: KClass<out FlowerModel> = Generic::class,
        val baseColor: Color,
        val thornColor: Color,
        val flowerColor: Color,
    ): FlowerModel(growthScale, witherAmount) {
        companion object {
            private const val STEM_HEIGH_RATIO = .35f
        }
        private val hasFlower get() = growthScale > 0.85f
        private val hasArmFlower get() = growthScale > 0.95f
        private val effectiveGrowth = growthScale.coerceAtLeast(0f)
        private val scaledHeight = stemHeight * effectiveGrowth
        private val witheredHeight = scaledHeight * (1f - 0.4f * witherAmount)
        private val witheredMaxWidth = stemWidth * effectiveGrowth * (1f - 0.2f * witherAmount)
        private val flowerSize: Dp = witheredMaxWidth * .65f
        private val armExtraFactor = 0.45f

        override val width: Dp
            get() = (witheredMaxWidth.value * (1f + 2 * armExtraFactor)).coerceAtLeast(flowerSize.value * 1.8f).dp

        override val height: Dp
            get() = (witheredHeight + flowerSize + 8.dp).coerceAtLeast(8.dp)

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun BoxScope.Compose(
            displayTranslationX: Float,
            dx: Float,
            phase: Float,
            swayOffset: Float,
            permanentBendPx: Float,
            scaleFactor: Float,
            breezeStrength: Float,
            animationStrengthFactor: Float,
            shadow: FlowerShadow?,
            random: Random
        ) {
            val density = LocalDensity.current
            val flowerShape = MaterialShapes.Flower.toShape()
            val maxWidthPx = with(density) { witheredMaxWidth.toPx() }
            val flowerSizePx = with(density) { flowerSize.toPx() }
            val actualBaseColor = autoWitherColor(baseColor, witherAmount)
            val actualThornColor = autoWitherColor(thornColor, witherAmount)
            val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)
            val swayAngle by animateFloatAsState(
                targetValue = sin(phase + swayOffset) * 6f * breezeStrength * animationStrengthFactor,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            val mainNumLines = 4
            val mainNumSpinesPerLine = 13
            val mainSpinePositionsPerLine = remember {
                List(mainNumLines) {
                    List(mainNumSpinesPerLine) {
                        val i = it
                        ((i.toFloat() + random.nextFloat()) / mainNumSpinesPerLine.toFloat()) * 0.9f + 0.05f
                    }
                }
            }
            val mainBisectorVariations = remember {
                List(mainNumLines * mainNumSpinesPerLine) { random.nextFloat() * 40f - 20f }
            }
            val mainIsOutwardsList = remember {
                List(mainNumLines * mainNumSpinesPerLine) { index ->
                    val lineIndex = index / mainNumSpinesPerLine
                    when (lineIndex) {
                        0, 3 -> true
                        else -> random.nextBoolean()
                    }
                }
            }
            val numArms = remember { random.nextInt(2) + 1 }
            val sideSigns: List<Float> = remember {
                if (numArms == 2) listOf(-1f, 1f) else listOf(if (random.nextBoolean()) -1f else 1f)
            }
            val armProgress = remember {
                (0 until numArms).map { random.nextFloat() * 0.6f + 0.2f }.sorted()
            }
            val armNumLines = 3
            val armNumSpinesPerLine = 7
            val armSpinePositionsPerArm = remember {
                List(numArms) {
                    List(armNumLines) {
                        List(armNumSpinesPerLine) {
                            val i = it
                            ((i.toFloat() + random.nextFloat()) / armNumSpinesPerLine.toFloat()) * 0.9f + 0.05f
                        }
                    }
                }
            }
            val armBisectorVariations = remember {
                List(numArms * armNumLines * armNumSpinesPerLine) { random.nextFloat() * 40f - 20f }
            }
            val armIsOutwardsList = remember {
                List(numArms * armNumLines * armNumSpinesPerLine) { index ->
                    val relativeIndex = index % (armNumLines * armNumSpinesPerLine)
                    val lineIndex = relativeIndex / armNumSpinesPerLine
                    when (lineIndex) {
                        0, armNumLines - 1 -> true
                        else -> random.nextBoolean()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        translationY = -height.toPx() * .05f
                        rotationZ = swayAngle
                        transformOrigin = TransformOrigin(0.5f, 1f)
                        clip = false
                    }
            ) {
                Canvas(
                    modifier = Modifier
                        .size(width, witheredHeight)
                ) {
                    val centerX = size.width / 2f
                    val heightPx = size.height

                    val armLengthPx = maxWidthPx * .8f
                    val armThicknessPx = maxWidthPx * .55f
                    val armNumSpines = armNumLines * armNumSpinesPerLine
                    var armVariationIndex = 0
                    for (armIndex in 0 until numArms) {
                        val prog = armProgress[armIndex]
                        val sideSign = sideSigns[armIndex]
                        val attachY = if (sideSign == -1f) {
                            heightPx * (1f - prog).coerceIn(0f, .1f)
                        } else heightPx * (1f - prog).coerceIn(.4f, .6f)
                        val attachX = centerX.minus(armThicknessPx / 2) + sideSign * bodyWidthAt(prog).div(2).minus(armThicknessPx)
                        val rotation = sideSign * 45f
                        withTransform({
                            translate(attachX, attachY)
                            rotate(rotation)
                        }) {
                            drawCactusStem(
                                stemCenterX = 0f,
                                stemBottomY = 0f,
                                stemHeightPx = armLengthPx,
                                stemMaxWidthPx = armThicknessPx,
                                baseColor = actualBaseColor,
                                thornColor = actualThornColor,
                                spinePositionsPerLine = armSpinePositionsPerArm[armIndex],
                                bisectorVariations = armBisectorVariations.subList(armVariationIndex, armVariationIndex + armNumSpines),
                                isOutwardsList = armIsOutwardsList.subList(armVariationIndex, armVariationIndex + armNumSpines)
                            )
                            if (hasArmFlower) {
                                drawFlower(
                                    centerX = 0f,
                                    bottomY = -armLengthPx,
                                    flowerSizePx = flowerSizePx * .55f,
                                    color = actualFlowerColor,
                                    shadow = shadow?.copy(offset = shadow.offset * .55f),
                                    flowerShape = flowerShape
                                )
                            }
                        }
                        armVariationIndex += armNumSpines
                    }

                    drawCactusStem(
                        stemCenterX = centerX,
                        stemBottomY = heightPx,
                        stemHeightPx = heightPx,
                        stemMaxWidthPx = maxWidthPx,
                        baseColor = actualBaseColor,
                        thornColor = actualThornColor,
                        spinePositionsPerLine = mainSpinePositionsPerLine,
                        bisectorVariations = mainBisectorVariations,
                        isOutwardsList = mainIsOutwardsList
                    )
                    if (hasFlower && flowerSizePx > 0f) {
                        drawFlower(
                            centerX = centerX,
                            bottomY = 0f,
                            flowerSizePx = flowerSizePx,
                            color = actualFlowerColor,
                            shadow = shadow,
                            flowerShape = flowerShape
                        )
                    }
                }
            }
        }

        private fun DrawScope.drawFlower(
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
                    layoutDirection = layoutDirection,
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
                layoutDirection = layoutDirection,
                density = this
            )
            val left = centerX - flowerSizePx / 2f
            val top = centerY - flowerSizePx / 2f
            withTransform({ translate(left, top) }) {
                drawOutline(outline = outline, color = color, style = Fill)
            }
        }

        private fun DrawScope.drawCactusStem(
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
            fun bodyWidthAt(progress: Float): Float {
                val p = progress.coerceIn(0f, 1f)
                return when {
                    p < 0.15f -> lerp(0.55f, 0.85f, p / 0.15f)
                    p < 0.75f -> lerp(0.85f, 1f, (p - 0.15f) / 0.6f)
                    else -> 1f
                }
            }
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

        private fun bodyWidthAt(progress: Float): Float {
            val p = progress.coerceIn(0f, 1f)
            return when {
                p < 0.15f -> lerp(0.55f, 0.85f, p / 0.15f)
                p < 0.75f -> lerp(0.85f, 1f, (p - 0.15f) / 0.6f)
                else -> 1f
            }
        }
    }
}
