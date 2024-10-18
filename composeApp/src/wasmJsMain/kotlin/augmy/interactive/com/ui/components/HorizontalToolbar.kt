package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.MaxModalWidthDp
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.ThemeChoice
import augmy.interactive.com.theme.LocalTheme
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.accessibility_dark_mode
import website_brand.composeapp.generated.resources.accessibility_light_mode
import website_brand.composeapp.generated.resources.accessibility_menu
import website_brand.composeapp.generated.resources.app_name
import website_brand.composeapp.generated.resources.toolbar_action_about
import website_brand.composeapp.generated.resources.toolbar_action_about_business
import website_brand.composeapp.generated.resources.toolbar_action_about_research
import website_brand.composeapp.generated.resources.toolbar_action_contacts

/**
 * Custom app bar with options of customization
 */
@Composable
fun HorizontalToolbar(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel
) {
    val navController = LocalNavController.current

    val isMenuExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .shadow(elevation = 4.dp)
            .fillMaxWidth()
            .background(color = LocalTheme.current.colors.toolbarColor)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = MaxModalWidthDp.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //TODO logo here
                    Text(
                        modifier = Modifier
                            .clickable {
                                navController?.navigate(NavigationNode.Landing.route)
                            }
                            .padding(start = 6.dp, top = 4.dp, bottom = 4.dp),
                        text = stringResource(Res.string.app_name),
                        style = LocalTheme.current.styles.subheading
                    )
                }
                Crossfade(
                    targetState = LocalDeviceType.current == WindowWidthSizeClass.Expanded
                ) { isDesktop ->
                    if(isDesktop) {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ToolbarActions()
                            }

                            Spacer(modifier = Modifier.width(16.dp))
                            ThemeSwitch(viewModel = viewModel)
                        }
                    }else {
                        Crossfade(targetState = isMenuExpanded.value) { isExpanded ->
                            Icon(
                                modifier = Modifier
                                    .clickable {
                                        isMenuExpanded.value = isMenuExpanded.value.not()
                                    }
                                    .padding(8.dp)
                                    .size(32.dp),
                                imageVector = if(isExpanded) {
                                    Icons.Outlined.Close
                                }else Icons.AutoMirrored.Outlined.MenuOpen,
                                contentDescription = stringResource(Res.string.accessibility_menu),
                                tint = LocalTheme.current.colors.secondary
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(isMenuExpanded.value) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(IntrinsicSize.Min),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ToolbarActions(
                        onCollapse = {
                            isMenuExpanded.value = false
                        }
                    )
                    ThemeSwitch(
                        modifier = Modifier.align(Alignment.End),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSwitch(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel
) {
    val localSettings = viewModel.localSettings.collectAsState()

    val composition by rememberLottieComposition {
        LottieCompositionSpec.Url("https://lottie.host/433c02b0-6f94-48a8-b75f-4befb06c96ef/LAAQ4kUAUq.lottie")
    }

    val isDarkTheme = when(localSettings.value?.theme) {
        ThemeChoice.DARK -> true
        ThemeChoice.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    val progress = animateFloatAsState(
        targetValue = if(isDarkTheme) .5f else 0f,
        label = "progressThemeAnimation",
        animationSpec = tween(durationMillis = 750)
    )

    Image(
        modifier = modifier
            .clip(CircleShape)
            .requiredSizeIn(maxHeight = 50.dp, maxWidth = 50.dp)
            .scale(2f)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = ripple(bounded = true),
                onClick = {
                    viewModel.updateTheme(isDarkTheme.not())
                }
            ),
        painter = rememberLottiePainter(
            composition = composition,
            progress = {
                progress.value
            }
        ),
        contentDescription = stringResource(if(isDarkTheme) {
            Res.string.accessibility_dark_mode
        }else Res.string.accessibility_light_mode),
        colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.secondary),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun ToolbarActions(onCollapse: () -> Unit = {}) {
    ToolbarAction(
        onCollapse = onCollapse,
        text = stringResource(Res.string.toolbar_action_about),
        route = NavigationNode.PublicAbout.route
    )
    ToolbarAction(
        onCollapse = onCollapse,
        text = stringResource(Res.string.toolbar_action_about_research),
        route = NavigationNode.ResearchAbout.route
    )
    ToolbarAction(
        onCollapse = onCollapse,
        text = stringResource(Res.string.toolbar_action_about_business),
        route = NavigationNode.BusinessAbout.route
    )
    ToolbarAction(
        onCollapse = onCollapse,
        text = stringResource(Res.string.toolbar_action_contacts),
        route = NavigationNode.Contacts.route
    )
}

@Composable
private fun ToolbarAction(
    modifier: Modifier = Modifier,
    route: String,
    text: String,
    onCollapse: () -> Unit
) {
    val navController = LocalNavController.current
    val currentEntry = navController?.currentBackStackEntryAsState()
    val isSelected = currentEntry?.value?.destination?.route == route

    IndicatedAction(
        modifier = modifier,
        isSelected = isSelected,
        onPress = {
            onCollapse()
            if(isSelected.not()) navController?.navigate(route)
        },
        content = { m ->
            Text(
                modifier = m,
                text = text,
                style = LocalTheme.current.styles.title.copy(
                    color = LocalTheme.current.colors.secondary
                )
            )
        }
    )
}