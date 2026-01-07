package augmy.interactive.com.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors
import augmy.interactive.com.ui.components.BrandHeaderButton
import augmy.interactive.com.ui.components.ComponentHeaderButton
import augmy.interactive.com.ui.components.simulation.buildTempoStringHeuristic
import augmy.interactive.com.ui.landing.components.AnimatedGarden
import augmy.interactive.com.ui.landing.components.InfoBox
import augmy.interactive.com.ui.landing.components.avatar.AvatarColorConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarFloorConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarFlowerConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarHeadConfiguration
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.animated_garden_helper
import website_brand.composeapp.generated.resources.character_man_0
import website_brand.composeapp.generated.resources.character_man_1
import website_brand.composeapp.generated.resources.character_woman_0
import website_brand.composeapp.generated.resources.character_woman_1
import website_brand.composeapp.generated.resources.landing_demo_others_action_interact
import website_brand.composeapp.generated.resources.landing_story_0_content
import website_brand.composeapp.generated.resources.landing_story_0_resolution
import website_brand.composeapp.generated.resources.landing_story_0_title
import website_brand.composeapp.generated.resources.landing_story_1_content
import website_brand.composeapp.generated.resources.landing_story_1_resolution
import website_brand.composeapp.generated.resources.landing_story_1_title
import website_brand.composeapp.generated.resources.landing_story_2_content
import website_brand.composeapp.generated.resources.landing_story_2_resolution
import website_brand.composeapp.generated.resources.landing_story_2_title
import website_brand.composeapp.generated.resources.landing_story_3_content
import website_brand.composeapp.generated.resources.landing_story_3_resolution
import website_brand.composeapp.generated.resources.landing_story_3_title
import website_brand.composeapp.generated.resources.landing_story_cta
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class LandingStory @OptIn(ExperimentalUuidApi::class) constructor(
    val avatar: AvatarConfiguration,
    val valence: Float,
    val arousal: Float,
    val name: String? = null,
    val image: DrawableResource,
    val titleText: StringResource,
    val contentText: StringResource,
    val resolutionText: StringResource,
    val uuid: String = Uuid.random().toString()
)

val landingStories = listOf(
    LandingStory(
        valence = -.1f,
        arousal = .05f,
        contentText = Res.string.landing_story_0_content,
        resolutionText = Res.string.landing_story_0_resolution,
        titleText = Res.string.landing_story_0_title,
        name = "Emma",
        image = Res.drawable.character_woman_1,
        avatar = AvatarConfiguration(
            flower = AvatarFlowerConfiguration.Dandelion(),
            color = AvatarColorConfiguration(
                primary = Colors.ProximitySocial,
                secondary = Colors.HunterGreen,
                tertial = SharedColors.YELLOW,
            )
        )
    ),
    LandingStory(
        valence = .1f,
        arousal = .3f,
        contentText = Res.string.landing_story_1_content,
        resolutionText = Res.string.landing_story_1_resolution,
        titleText = Res.string.landing_story_1_title,
        image = Res.drawable.character_woman_0,
        avatar = AvatarConfiguration(
            flower = AvatarFlowerConfiguration.Generic(),
            head = AvatarHeadConfiguration.Piercing(),
            color = AvatarColorConfiguration(
                secondary = Colors.Asparagus,
                tertial = Colors.DutchWhite,
            )
        )
    ),
    LandingStory(
        valence = 1f,
        arousal = .1f,
        contentText = Res.string.landing_story_2_content,
        resolutionText = Res.string.landing_story_2_resolution,
        titleText = Res.string.landing_story_2_title,
        name = "John",
        image = Res.drawable.character_man_0,
        avatar = AvatarConfiguration(
            flower = AvatarFlowerConfiguration.Sunflower(),
            color = AvatarColorConfiguration(
                primary = Colors.ProximityIntimate,
                secondary = Colors.HunterGreen,
                tertial = SharedColors.YELLOW,
            )
        )
    ),
    LandingStory(
        valence = -.1f,
        arousal = .3f,
        contentText = Res.string.landing_story_3_content,
        resolutionText = Res.string.landing_story_3_resolution,
        titleText = Res.string.landing_story_3_title,
        image = Res.drawable.character_man_1,
        avatar = AvatarConfiguration(
            flower = AvatarFlowerConfiguration.Cactus(),
            head = AvatarHeadConfiguration.Piercing(),
            floor = AvatarFloorConfiguration.Sand(),
            color = AvatarColorConfiguration(
                primary = Colors.Asparagus,
                secondary = Colors.HunterGreen,
                tertial = SharedColors.YELLOW,
            )
        )
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LargeLayout(verticalPadding: Dp, showSignUp: MutableState<Boolean>) {
    val colors = LocalTheme.current.colors
    val isCompact = LocalDeviceType.current == WindowWidthSizeClass.Compact

    val carousalScope = rememberCoroutineScope()
    val storiesPagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { landingStories.size }
    )

    val currentPage = remember { mutableStateOf(0) }
    val showResolution = remember { mutableStateOf(false) }

    LaunchedEffect(storiesPagerState) {
        snapshotFlow { storiesPagerState.currentPage }.collectLatest {
            currentPage.value = it
        }
    }


    Column(Modifier.padding(horizontal = 12.dp)) {
        Spacer(Modifier.height(verticalPadding * 1.5f))
        Row {
            HorizontalPager(
                modifier = Modifier
                    .animateContentSize()
                    .weight(.8f)
                    .background(
                        color = colors.backgroundDark,
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(12.dp),
                state = storiesPagerState,
                key = { landingStories[it].uuid },
                verticalAlignment = Alignment.Top
            ) { index ->
                val story = landingStories[index]

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(showResolution.value) {
                        val content = buildTempoStringHeuristic(
                            text = AnnotatedString(stringResource(story.resolutionText)),
                            style = LocalTheme.current.styles.regular.toSpanStyle(),
                            enabled = true,
                            onFinish = {}
                        )

                        Text(
                            modifier = Modifier
                                .animateContentSize()
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(),
                            text = content,
                            style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(if (isCompact) 1f else .85f)
                            .padding(top = 24.dp)
                            .background(
                                color = colors.backgroundLight,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            )
                            .padding(
                                start = if (isCompact) 4.dp else 8.dp,
                                end = if (isCompact) 8.dp else 16.dp,
                                bottom = if (isCompact) 12.dp else 24.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BottomSheetDefaults.DragHandle(color = colors.secondary)

                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(62.dp)
                                    .background(
                                        color = (story.avatar.color.primary ?: colors.primary).copy(
                                            alpha = .7f
                                        ),
                                        shape = LocalTheme.current.shapes.rectangularActionShape
                                    )
                                    .clip(LocalTheme.current.shapes.rectangularActionShape),
                                painter = painterResource(story.image),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp, top = 8.dp)
                            ) {
                                Text(
                                    text = story.name ?: "You",
                                    style = LocalTheme.current.styles.category
                                )
                                Text(
                                    text = story.name?.let { name ->
                                        "@${name.lowercase()}:augmy.org"
                                    } ?: "@you:augmy.org",
                                    style = LocalTheme.current.styles.category.copy(
                                        color = LocalTheme.current.colors.disabled
                                    )
                                )
                            }
                            ComponentHeaderButton(
                                endImageVector = Icons.AutoMirrored.Outlined.Chat,
                                text = stringResource(Res.string.landing_demo_others_action_interact),
                                onClick = {}
                            )
                        }

                        AnimatedGarden(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .requiredWidthIn(max = 400.dp),
                            configuration = story.avatar,
                            valence = story.valence,
                            arousal = story.arousal,
                            seed = story.avatar.toString()
                        )
                        InfoBox(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                            text = stringResource(Res.string.animated_garden_helper),
                            style = LocalTheme.current.styles.regular
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(.1f))

            Column(
                modifier = Modifier.weight(.6f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                landingStories.forEachIndexed { index, story ->
                    Row(
                        modifier = Modifier
                            .align(if (index % 2 == 0) Alignment.Start else Alignment.End)
                            .padding(bottom = 16.dp)
                            .scalingClickable(scaleInto = .985f) {
                                showResolution.value = false
                                carousalScope.coroutineContext.cancelChildren()
                                carousalScope.launch {
                                    storiesPagerState.animateScrollToPage(index)
                                }
                            }
                            .then(
                                if(currentPage.value == index) {
                                    Modifier.background(
                                        color = colors.backgroundDark,
                                        shape = LocalTheme.current.shapes.rectangularActionShape
                                    )
                                }else Modifier.border(
                                    width = 2.dp,
                                    color = colors.backgroundDark,
                                    shape = LocalTheme.current.shapes.rectangularActionShape
                                )
                            )
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (index % 2 == 0) {
                            Image(
                                painter = painterResource(story.image),
                                contentDescription = null
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .animateContentSize()
                        ) {
                            Text(
                                modifier = Modifier.align(
                                    if (index % 2 == 0) Alignment.Start else Alignment.End
                                ),
                                text = stringResource(story.titleText),
                                style = if (currentPage.value == index) {
                                    LocalTheme.current.styles.title
                                }else LocalTheme.current.styles.regular
                            )
                            if (currentPage.value == index) {
                                val content = buildTempoStringHeuristic(
                                    text = AnnotatedString(stringResource(story.contentText)),
                                    style = LocalTheme.current.styles.regular.toSpanStyle(),
                                    enabled = true,
                                    onFinish = {
                                        showResolution.value = true
                                    }
                                )

                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = colors.backgroundLight,
                                            shape = LocalTheme.current.shapes.rectangularActionShape
                                        )
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    text = content,
                                    style = LocalTheme.current.styles.regular.copy(
                                        fontStyle = FontStyle.Italic
                                    )
                                )
                            }
                        }

                        if (index % 2 != 0) {
                            Image(
                                painter = painterResource(story.image),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

        BrandHeaderButton(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.landing_story_cta)
        ) {
            showSignUp.value = true
        }

        Spacer(Modifier.height(verticalPadding * 2))
    }
}
