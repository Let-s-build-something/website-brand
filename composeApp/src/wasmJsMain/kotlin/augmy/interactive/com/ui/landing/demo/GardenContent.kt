package augmy.interactive.com.ui.landing.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AutoResizeText
import augmy.interactive.com.ui.components.FontSizeRange
import augmy.interactive.com.ui.landing.components.Flower
import augmy.interactive.com.ui.landing.components.GrassFloor
import augmy.interactive.com.ui.landing.components.NetworkProximityCategory
import augmy.interactive.com.ui.landing.components.SocialCircleSample
import augmy.interactive.com.ui.landing.components.rememberGrassFloorState
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_POT_RATIO
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_STEM_RATIO
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.head
import website_brand.composeapp.generated.resources.wellbeing_garden_sharing_count
import kotlin.random.Random

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GardenContent(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val potHeightDp = remember { mutableStateOf(0f) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SocialCircleSample(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .animateContentSize()
                            .fillMaxWidth(.25f)
                            .widthIn(max = 400.dp),
                        categories = listOf(
                            NetworkProximityCategory.Intimate,
                            NetworkProximityCategory.Personal
                        )
                    )

                    AutoResizeText(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(
                            Res.string.wellbeing_garden_sharing_count,
                            4
                        ),
                        style = LocalTheme.current.styles.category,
                        fontSizeRange = FontSizeRange(
                            min = 2.sp,
                            max = LocalTheme.current.styles.category.fontSize * 1.5f
                        )
                    )
                }
            }
        }

        Column(
            Modifier
                .padding(top = 32.dp)
                .widthIn(max = 550.dp)
                .fillMaxWidth(.8f)
        ) {

            Box(
                Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth(.917f)
                    .align(Alignment.End),
                contentAlignment = Alignment.BottomCenter
            ) {
                RandomFlowerField(
                    baseString = "garden",
                    potHeightDp = potHeightDp,
                    growth = 1f,
                    arousal = 0.1f
                )
            }
            Image(
                modifier = Modifier
                    .zIndex(10f)
                    .onSizeChanged { coordinates ->
                        potHeightDp.value = with(density) { coordinates.height.toDp().value }
                    },
                painter = painterResource(Res.drawable.head),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.gardenHead),
                alpha = .6f,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun RandomFlowerField(
    modifier: Modifier = Modifier,
    baseString: String,
    flowerCount: Int = 5,
    potHeightDp: MutableState<Float>,
    growth: Float,
    arousal: Float
) {
    val seed = baseString.hashCode().toLong()
    val random = remember(seed) { Random(seed) }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        val parentWidthDp = maxWidth
        val stemHeightDp = (potHeightDp.value * FLOWER_POT_RATIO).dp
        val flowerBaseSizeDp = (stemHeightDp.value * FLOWER_STEM_RATIO).dp
        val halfFlower = flowerBaseSizeDp / 2f
        val sideMargin = 4.dp

        val minCenterX = sideMargin + halfFlower
        val maxCenterX = parentWidthDp - sideMargin - halfFlower

        val normalizedCenters = remember(seed, flowerCount) {
            List(flowerCount) { random.nextFloat() }.sorted()
        }

        val centers = remember(normalizedCenters, parentWidthDp) {
            val availableWidth = parentWidthDp - 2 * sideMargin

            val maxOverlapRatio = 0.2f
            val minDistanceRatio = ((flowerBaseSizeDp / parentWidthDp) * (1f - maxOverlapRatio))

            val adjustedNormalized = mutableListOf<Float>()
            for (i in normalizedCenters.indices) {
                var proposed = normalizedCenters[i]
                if (i > 0) {
                    val prev = adjustedNormalized.last()
                    val minAllowed = prev + minDistanceRatio
                    if (proposed < minAllowed) proposed = minAllowed
                }
                adjustedNormalized.add(proposed.coerceAtMost(1f))
            }

            adjustedNormalized.map { n ->
                (sideMargin + availableWidth * n).coerceIn(minCenterX, maxCenterX)
            }
        }

        val breezeStrength = arousal
            .plus(0.3f)
            .coerceAtLeast(0.05f) * 4.5f

        centers.forEachIndexed { index, centerXDp ->
            val leftOffset = centerXDp - halfFlower

            AnimatedVisibility(arousal != 0f) {
                Flower(
                    modifier = Modifier
                        .offset(x = leftOffset)
                        .zIndex(index + 1f),
                    growth = 1f + growth.coerceAtMost(0.5f),
                    random = random,
                    stemHeight = stemHeightDp,
                    flowerBaseSize = flowerBaseSizeDp,
                    witherProgress = (growth - 1) * -0.5f,
                    breezeStrength = breezeStrength
                )
            }
        }
        GrassFloor(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .zIndex((flowerCount + 1).toFloat()),
            witherProgress = growth * -1f,
            breezeStrength = breezeStrength,
            state = rememberGrassFloorState()
        )
    }
}

@Composable
fun MiniatureIndicator(
    modifier: Modifier = Modifier,
    stemHeight: Dp = 24.dp,
    seed: Long,
    arousal: Float,
    valence: Float
) {
    val random = remember(seed) { Random(seed) }
    val breezeStrength = arousal
        .plus(0.3f)
        .coerceAtLeast(0.05f) * 4.5f

    Box(
        modifier = modifier.width(IntrinsicSize.Min),
        contentAlignment = Alignment.BottomCenter
    ) {
        Flower(
            growth = 1f + valence.coerceAtMost(0.5f),
            random = random,
            stemHeight = stemHeight,
            flowerBaseSize = (stemHeight.value * FLOWER_STEM_RATIO).dp,
            witherProgress = (valence - 1) * -0.5f,
            breezeStrength = breezeStrength
        )
        GrassFloor(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            witherProgress = valence * -1f,
            breezeStrength = breezeStrength,
            state = rememberGrassFloorState(
                maxBladeHeight = stemHeight * 0.5f,
            )
        )
    }
}

object Garden {
    const val FLOWER_POT_RATIO = .125f
    const val FLOWER_STEM_RATIO = .8f
    val visualRoofRange = -0.5f..0.5f
}
