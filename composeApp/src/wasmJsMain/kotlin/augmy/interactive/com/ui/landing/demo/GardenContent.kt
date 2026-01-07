package augmy.interactive.com.ui.landing.demo

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AutoResizeText
import augmy.interactive.com.ui.components.FontSizeRange
import augmy.interactive.com.ui.landing.components.CurvyGrassFloor
import augmy.interactive.com.ui.landing.components.Flower
import augmy.interactive.com.ui.landing.components.NetworkProximityCategory
import augmy.interactive.com.ui.landing.components.SandyDesertFloor
import augmy.interactive.com.ui.landing.components.SocialCircleSample
import augmy.interactive.com.ui.landing.components.avatar.AvatarConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarFloorConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarFlowerConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarHeadConfiguration
import augmy.interactive.com.ui.landing.components.rememberAvatarFloorState
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_GRASS_RATIO
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_POT_RATIO
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.ic_data_usage
import website_brand.composeapp.generated.resources.ic_relax
import website_brand.composeapp.generated.resources.landing_demo_you_automatic_analysis
import website_brand.composeapp.generated.resources.landing_demo_you_health
import website_brand.composeapp.generated.resources.landing_demo_you_music
import website_brand.composeapp.generated.resources.landing_demo_you_screen_time
import website_brand.composeapp.generated.resources.logo_spotify
import website_brand.composeapp.generated.resources.wellbeing_garden_sharing_count
import kotlin.random.Random

private enum class IntegrationType {
    Music,
    ScreenTime,
    Health
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GardenContent(
    modifier: Modifier = Modifier,
    configuration: AvatarConfiguration,
    seed: String
) {
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
            val head = configuration.head ?: AvatarHeadConfiguration.Generic()

            Box(
                Modifier
                    .widthIn(max = 550.dp)
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = head.contentAlignment
            ) {
                RandomFlowerField(
                    modifier = Modifier
                        .fillMaxWidth(head.scalpFraction)
                        .padding(head.padding),
                    random = remember(seed) { Random(seed.hashCode()) },
                    potHeightDp = potHeightDp,
                    valence = 1f,
                    arousal = .2f,
                    configuration = configuration
                )
            }
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = 550.dp)
                    .fillMaxWidth(.8f)
                    .zIndex(10f)
                    .onSizeChanged { coordinates ->
                        potHeightDp.value = with(density) { coordinates.height.toDp().value }
                    },
                painter = painterResource(head.drawableRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.gardenHead),
                alpha = .6f,
                contentScale = ContentScale.FillWidth
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            text = stringResource(Res.string.landing_demo_you_automatic_analysis),
            style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IntegrationType.entries.forEach { type ->
                IntegrationItem(
                    isSelected = false,
                    text = stringResource(
                        when (type) {
                            IntegrationType.Music -> Res.string.landing_demo_you_music
                            IntegrationType.ScreenTime -> Res.string.landing_demo_you_screen_time
                            IntegrationType.Health -> Res.string.landing_demo_you_health
                        }
                    ),
                    painter = when (type) {
                        IntegrationType.Music -> painterResource(Res.drawable.logo_spotify)
                        IntegrationType.ScreenTime -> painterResource(Res.drawable.ic_data_usage)
                        IntegrationType.Health -> painterResource(Res.drawable.ic_relax)
                    }
                )
            }
        }
    }
}

@Composable
fun RandomFlowerField(
    modifier: Modifier = Modifier,
    configuration: AvatarConfiguration,
    random: Random,
    flowerCount: Int = 5,
    potHeightDp: MutableState<Float>,
    valence: Float,
    arousal: Float
) {
    if (potHeightDp.value != 0f) {
        BoxWithConstraints(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomStart
        ) {
            val growthNew = 1f + valence.coerceAtMost(0.5f)
            val wither = ((valence - 1f) * -0.5f).coerceIn(0f, 1f)
            val growthScale = ((growthNew.coerceIn(-1f, 1f) + 1f) / 2f).coerceAtLeast(0f)

            val flower = (configuration.flower ?: AvatarFlowerConfiguration.Generic()).toFlowerModel(
                colors = LocalTheme.current.colors,
                stemHeight = (potHeightDp.value * FLOWER_POT_RATIO).dp,
                growthScale = growthScale,
                witherAmount = wither,
                configuration = configuration
            )

            val parentWidthDp = maxWidth
            val halfFlower = flower.width / 2f
            val sideMargin = 4.dp

            val minCenterX = sideMargin + halfFlower
            val maxCenterX = parentWidthDp - sideMargin - halfFlower

            val normalizedCenters = remember(random, flowerCount) {
                List(flowerCount) { random.nextFloat() }.sorted()
            }

            val centers = remember(normalizedCenters, parentWidthDp) {
                val availableWidth = parentWidthDp - 2 * sideMargin

                val maxOverlapRatio = 0.2f
                val minDistanceRatio = ((flower.width / parentWidthDp) * (1f - maxOverlapRatio))

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

                if (arousal != 0f) {
                    Flower(
                        modifier = Modifier
                            .offset(x = leftOffset)
                            .zIndex(index + 1f),
                        random = random,
                        flower = flower,
                        breezeStrength = breezeStrength
                    )
                }
            }
            val modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .zIndex((flowerCount + 1).toFloat())
            val state = rememberAvatarFloorState(
                maxBladeHeight = (potHeightDp.value * FLOWER_GRASS_RATIO).dp
            )

            when (configuration.floor) {
                is AvatarFloorConfiguration.Sand -> SandyDesertFloor(
                    modifier = modifier,
                    witherProgress = valence * -1f,
                    breezeStrength = breezeStrength,
                    random = random,
                    state = state
                )
                else -> CurvyGrassFloor(
                    modifier = modifier,
                    witherProgress = valence * -1f,
                    breezeStrength = breezeStrength,
                    random = random,
                    grassColor = configuration.color.secondary ?: LocalTheme.current.colors.brandMainDark,
                    floorColor = configuration.color.primary ?: LocalTheme.current.colors.brandMain,
                    state = state
                )
            }
        }
    }
}

@Composable
fun MiniatureIndicator(
    modifier: Modifier = Modifier,
    configuration: AvatarConfiguration,
    stemHeight: Dp = 24.dp,
    seed: String,
    arousal: Float,
    valence: Float
) {
    val random = remember(seed) { Random(seed.hashCode()) }
    val breezeStrength = arousal
        .plus(0.3f)
        .coerceAtLeast(0.05f) * 4.5f

    Box(
        modifier = modifier.width(IntrinsicSize.Min),
        contentAlignment = Alignment.BottomCenter
    ) {
        val growthNew = 1f + valence.coerceAtMost(0.5f)
        val wither = ((valence - 1f) * -0.5f).coerceIn(0f, 1f)
        val growthScale = ((growthNew.coerceIn(-1f, 1f) + 1f) / 2f).coerceAtLeast(0f)

        val flower = (configuration.flower ?: AvatarFlowerConfiguration.Generic()).toFlowerModel(
            colors = LocalTheme.current.colors,
            growthScale = growthScale,
            stemHeight = stemHeight,
            witherAmount = wither,
            configuration = configuration
        )
        val floorState = rememberAvatarFloorState(
            maxBladeHeight = stemHeight * 0.5f,
        )

        Flower(
            random = random,
            flower = flower,
            breezeStrength = breezeStrength
        )
        val floorModifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)

        when (configuration.floor) {
            is AvatarFloorConfiguration.Sand -> SandyDesertFloor(
                modifier = floorModifier,
                witherProgress = valence * -1f,
                breezeStrength = breezeStrength,
                random = random,
                state = floorState
            )
            else -> CurvyGrassFloor(
                modifier = floorModifier,
                random = random,
                witherProgress = valence * -1f,
                breezeStrength = breezeStrength,
                grassColor = configuration.color.secondary ?: LocalTheme.current.colors.brandMainDark,
                floorColor = configuration.color.primary ?: LocalTheme.current.colors.brandMain,
                state = floorState
            )
        }
    }
}

@Composable
private fun IntegrationItem(
    isSelected: Boolean,
    painter: Painter,
    text: String
) {
    Column(
        modifier = Modifier
            .background(
                color = LocalTheme.current.colors.backgroundLight,
                shape = LocalTheme.current.shapes.componentShape
            )
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = LocalTheme.current.colors.brandMainDark,
                        shape = LocalTheme.current.shapes.componentShape
                    )
                }else Modifier
            )
            .padding(12.dp)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painter,
            contentDescription = null
        )
        Text(
            text = text,
            style = LocalTheme.current.styles.regular.copy(
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        )
    }
}

object Garden {
    const val FLOWER_POT_RATIO = .15f
    const val FLOWER_GRASS_RATIO = .05f
}
