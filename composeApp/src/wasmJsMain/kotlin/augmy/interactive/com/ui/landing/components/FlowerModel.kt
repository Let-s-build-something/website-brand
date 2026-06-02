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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import ui.account.affect.components.flower.FlowerFrame
import ui.account.affect.components.flower.FlowerFrame.CactusFrame.Companion.drawCactusStem
import ui.account.affect.components.flower.FlowerFrame.CactusFrame.Companion.drawFlower
import ui.account.affect.components.flower.FlowerUtils.FlowerShadow
import ui.account.affect.components.flower.FlowerUtils.autoWitherColor
import ui.account.affect.components.flower.FlowerUtils.bodyWidthAt
import ui.account.affect.components.flower.FlowerUtils.drawStem
import ui.account.affect.components.flower.FlowerUtils.toShapeKotlin
import ui.account.affect.components.flower.FlowerUtils.updatePoints
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random
import kotlin.reflect.KClass

data class FlowerShadow(
    val color: Color,
    val offset: Dp = 1.5.dp
)

sealed class FlowerModel<F: FlowerFrame>(
    open val growthScale: Float,
    open val witherAmount: Float,
) {
    abstract val stemHeight: Dp
    abstract val stemWidth: Dp
    abstract val width: Dp
    abstract val height: Dp

    abstract fun computeStaticFrame(
        density: Density,
        random: Random,
        breezeStrength: Float = 0.2f,
        shadow: FlowerShadow? = null,
        phase: Float,
    ): F

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
    ): FlowerModel<FlowerFrame.GenericFrame>(growthScale, witherAmount) {
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
                return (flowerSize.value * 2f).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }

        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        override fun computeStaticFrame(
            density: Density,
            random: Random,
            breezeStrength: Float,
            shadow: FlowerShadow?,
            phase: Float,
        ): FlowerFrame.GenericFrame = FlowerSpec.GenericSpec(
            model = this,
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight,
            stemWidth = stemWidth,
            flowerColor = flowerColor,
            stemColor = stemColor,
            random = random,
            breezeStrength = breezeStrength,
            phase = phase,
            shadow = shadow,
        ).computeFrame(density)

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
            val stemWidthPx = with(density) { localStemWidth.toPx() }

            val stableState = remember {
                val stemSegments = random.nextInt(3, 6)
                val wobbleOffsets = List(stemSegments) {
                    (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
                }
                GenericStableState(stemSegments, wobbleOffsets)
            }

            val dy = with(density) { localStemHeight.toPx() } * 0.33f
            val baseAngle      = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f
            val hangCurve      = sin(witherAmount * PI / 2.0).toFloat()
            val bentAngle      = (baseAngle * (1f + hangCurve * 0.8f)).coerceIn(-100f, 100f)
            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor

            val actualFlowerColor = autoWitherColor(flowerColor, witherAmount)
            val actualStemColor   = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = bentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness    = Spring.StiffnessMedium
                )
            )
            val petalDroop = witherAmount * 40f

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(localStemHeight)
                    .width(localStemWidth * 3)
                    .align(Alignment.BottomCenter)
            ) {
                stableState.updatePoints(size, displayTranslationX, bentAngle, permanentBendPx, witherAmount)
                drawStem(stableState.points, stemWidthPx, actualStemColor)
            }

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
    ) : FlowerModel<FlowerFrame.ChamomileFrame>(growthScale, witherAmount) {

        companion object {
            private const val FLOWER_STEM_RATIO = .6f
            const val CENTER_PETAL_RATIO = .5f
            private const val STEM_HEIGH_RATIO = .0375f
        }

        val localStemHeight
            get() = stemHeight * growthScale * (1f - 0.3f * witherAmount)
        val flowerSize: Dp
            get() = (localStemHeight.value * FLOWER_STEM_RATIO).dp
        val localStemWidth
            get() = stemWidth * growthScale

        override val width: Dp
            get() = (flowerSize.value * 2f).takeIf { !it.isNanOrInfinite() }?.roundToInt().orZero().dp

        override val height: Dp
            get() = (localStemHeight.value + flowerSize.value).takeIf { !it.isNanOrInfinite() }?.roundToInt().orZero().dp

        override fun computeStaticFrame(
            density: Density,
            random: Random,
            breezeStrength: Float,
            shadow: FlowerShadow?,
            phase: Float,
        ): FlowerFrame.ChamomileFrame = FlowerSpec.ChamomileSpec(
            model = this,
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight,
            stemWidth = stemWidth,
            petalColor = petalColor,
            centerColor = centerColor,
            stemColor = stemColor,
            random = random,
            breezeStrength = breezeStrength,
            phase = phase,
            shadow = shadow
        ).computeFrame(density)

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
            val flowerShape = MaterialShapes.SoftBoom.toShape()
            val stemWidthPx = with(density) { localStemWidth.toPx() }

            val stableState = remember {
                val stemSegments = random.nextInt(3, 6)
                val wobbleOffsets = List(stemSegments) {
                    (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
                }
                GenericStableState(stemSegments, wobbleOffsets)
            }

            val dy = with(density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val bentAngle = (baseAngle * (1f + hangCurve * 0.8f)).coerceIn(-100f, 100f)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor

            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualCenterColor = autoWitherColor(centerColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = bentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(localStemHeight)
                    .width(localStemWidth * 3)
                    .align(Alignment.BottomCenter)
            ) {
                stableState.updatePoints(size, displayTranslationX, bentAngle, permanentBendPx, witherAmount)
                drawStem(stableState.points, stemWidthPx, actualStemColor)
            }

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
    ) : FlowerModel<FlowerFrame.DandelionFrame>(growthScale, witherAmount) {
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
                return (flowerSize.value * 2f).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }

        override fun computeStaticFrame(
            density: Density,
            random: Random,
            breezeStrength: Float,
            shadow: FlowerShadow?,
            phase: Float,
        ): FlowerFrame.DandelionFrame = FlowerSpec.DandelionSpec(
            model = this,
            random = random,
            breezeStrength = breezeStrength,
            phase = phase,
            shadow = shadow
        ).computeFrame(density)

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

            val dy = with(density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = bentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val stableState = remember {
                val stemSegments = random.nextInt(3, 6)
                val wobbleOffsets = List(stemSegments) {
                    (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
                }
                GenericStableState(stemSegments, wobbleOffsets)
            }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(localStemHeight)
                    .width(localStemWidth * 3)
                    .align(Alignment.BottomCenter)
            ) {
                stableState.updatePoints(size, displayTranslationX, bentAngle, permanentBendPx, witherAmount)
                drawStem(stableState.points, stemWidthPx, actualStemColor)
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
    ) : FlowerModel<FlowerFrame.SunflowerFrame>(growthScale, witherAmount) {
        companion object {
            private const val FLOWER_STEM_RATIO = .8f
            const val CENTER_PETAL_RATIO = .75f
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
                return (flowerSize.value * 2f).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }
        override val height: Dp
            get() {
                return (localStemHeight.value + flowerSize.value).takeIf {
                    !it.isNanOrInfinite()
                }?.roundToInt().orZero().dp
            }

        override fun computeStaticFrame(
            density: Density,
            random: Random,
            breezeStrength: Float,
            shadow: FlowerShadow?,
            phase: Float,
        ): FlowerFrame.SunflowerFrame = FlowerSpec.SunflowerSpec(
            model = this,
            random = random,
            breezeStrength = breezeStrength,
            phase = phase,
            shadow = shadow
        ).computeFrame(density)

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

            val dy = with(density) { localStemHeight.toPx() } * 0.33f
            val baseAngle = if (dy != 0f) atan(dx / dy) * (180f / PI.toFloat()) else 0f

            val maxBendDeg = 100f
            val hangCurve = sin((witherAmount * PI / 2f)).toFloat()
            val witherBendScale = 1f + hangCurve * 0.8f
            val bentAngle = (baseAngle * witherBendScale).coerceIn(-maxBendDeg, maxBendDeg)

            val flowerExtraSway = sin(phase * 1.2f + swayOffset * 0.4f) *
                    1.5f * breezeStrength * animationStrengthFactor
            val stemWidthPx = with(density) { localStemWidth.toPx() }
            val actualPetalColor = autoWitherColor(petalColor, witherAmount)
            val actualCenterColor = autoWitherColor(centerColor, witherAmount)
            val actualStemColor = autoWitherColor(stemColor, witherAmount)

            val displayRotation by animateFloatAsState(
                targetValue = bentAngle + flowerExtraSway,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )

            val stableState = remember {
                val stemSegments = random.nextInt(3, 6)
                val wobbleOffsets = List(stemSegments) {
                    (random.nextFloat() - 0.5f) * stemWidthPx * 3f * scaleFactor
                }
                GenericStableState(stemSegments, wobbleOffsets)
            }

            Canvas(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .height(localStemHeight)
                    .width(localStemWidth * 3)
                    .align(Alignment.BottomCenter)
            ) {
                stableState.updatePoints(size, displayTranslationX, bentAngle, permanentBendPx, witherAmount)
                drawStem(stableState.points, stemWidthPx, actualStemColor)
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
        val flower: KClass<out FlowerModel<out FlowerFrame>> = Generic::class,
        val baseColor: Color,
        val thornColor: Color,
        val flowerColor: Color,
    ) : FlowerModel<FlowerFrame.CactusFrame>(growthScale, witherAmount) {
        companion object {
            private const val STEM_HEIGH_RATIO = .35f
        }

        val hasFlower get() = growthScale > 0.85f
        val hasArmFlower get() = growthScale > 0.95f
        private val effectiveGrowth = growthScale.coerceAtLeast(0f)
        val witheredHeight = stemHeight * (1f - 0.4f * witherAmount)
        val witheredMaxWidth = stemWidth * effectiveGrowth// * (1f - 0.2f * witherAmount)
        val flowerSize: Dp = witheredMaxWidth * .65f
        private val armExtraFactor = 0.45f

        override val width: Dp
            get() = (witheredMaxWidth.value * (1f + 2 * armExtraFactor)).coerceAtLeast(flowerSize.value * 1.8f).dp

        override val height: Dp
            get() = (witheredHeight + flowerSize + 8.dp).coerceAtLeast(8.dp)

        override fun computeStaticFrame(
            density: Density,
            random: Random,
            breezeStrength: Float,
            shadow: FlowerShadow?,
            phase: Float,
        ): FlowerFrame.CactusFrame = FlowerSpec.CactusSpec(
            flowerShape = MaterialShapes.Flower.toShapeKotlin(),
            model = this,
            random = random,
            breezeStrength = breezeStrength,
            phase = phase,
            shadow = shadow
        ).computeFrame(density)

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
                if (numArms == 2) listOf(-1f, 1f) else {
                    List(numArms) { if (random.nextBoolean()) -1f else 1f }
                }
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
                    modifier = Modifier.size(width, witheredHeight)
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

                        val stemWidthAtProg = bodyWidthAt(prog) * maxWidthPx
                        val armOffsetAfterRotation =
                            armThicknessPx / 2 * cos(45f * PI.toFloat() / 180f)
                        val attachX =
                            centerX - sideSign * (stemWidthAtProg / 2f + armOffsetAfterRotation / 2) - armThicknessPx / 2
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
                                bisectorVariations = armBisectorVariations.subList(
                                    armVariationIndex,
                                    armVariationIndex + armNumSpines
                                ),
                                isOutwardsList = armIsOutwardsList.subList(
                                    armVariationIndex,
                                    armVariationIndex + armNumSpines
                                )
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
    }
}
