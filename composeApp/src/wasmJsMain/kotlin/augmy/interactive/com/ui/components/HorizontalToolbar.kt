package augmy.interactive.com.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.MaxModalWidthDp
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.ThemeChoice
import augmy.interactive.shared.ui.theme.LocalTheme
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.ExperimentalResourceApi
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
@OptIn(ExperimentalResourceApi::class)
@Composable
fun HorizontalToolbar(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel
) {
    val localSettings = viewModel.localSettings.collectAsState()

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/arrow_right.json").decodeToString())
    }
    val navController = LocalNavController.current

    Box(
        modifier = modifier
            .shadow(elevation = 4.dp)
            .fillMaxWidth()
            .background(color = LocalTheme.current.colors.toolbarColor)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = MaxModalWidthDp.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.height(24.dp),
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = 100
                    ),
                    colorFilter = ColorFilter.tint(LocalTheme.current.colors.secondary),
                    contentDescription = null
                )
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
            ) { isExpanded ->
                if(isExpanded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ToolbarAction(
                                text = stringResource(Res.string.toolbar_action_about),
                                route = NavigationNode.PublicAbout.route
                            )
                            ToolbarAction(
                                text = stringResource(Res.string.toolbar_action_about_research),
                                route = NavigationNode.ResearchAbout.route
                            )
                            ToolbarAction(
                                text = stringResource(Res.string.toolbar_action_about_business),
                                route = NavigationNode.BusinessAbout.route
                            )
                            ToolbarAction(
                                text = stringResource(Res.string.toolbar_action_contacts),
                                route = NavigationNode.Contacts.route
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.LightMode,
                            contentDescription = stringResource(Res.string.accessibility_light_mode),
                            tint = LocalTheme.current.colors.secondary
                        )
                        Switch(
                            modifier = Modifier.padding(horizontal = 6.dp),
                            checked = when(localSettings.value?.theme) {
                                ThemeChoice.DARK -> true
                                ThemeChoice.LIGHT -> false
                                else -> isSystemInDarkTheme()
                            },
                            colors = LocalTheme.current.styles.switchColorsDefault.copy(
                                checkedTrackColor = LocalTheme.current.colors.toolbarColor
                            ),
                            onCheckedChange = { isChecked ->
                                viewModel.updateTheme(isChecked)
                            }
                        )
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.DarkMode,
                            contentDescription = stringResource(Res.string.accessibility_dark_mode),
                            tint = LocalTheme.current.colors.secondary
                        )
                    }
                }else {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                //TODO open hamburger menu
                            }
                            .padding(8.dp)
                            .size(32.dp),
                        imageVector = Icons.AutoMirrored.Outlined.MenuOpen,
                        contentDescription = stringResource(Res.string.accessibility_menu),
                        tint = LocalTheme.current.colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolbarAction(
    modifier: Modifier = Modifier,
    route: String,
    text: String
) {
    val navController = LocalNavController.current
    val currentEntry = navController?.currentBackStackEntryAsState()
    val isPressed = remember { mutableStateOf(false) }
    val isHovered = remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val scale = animateFloatAsState(
        if (isPressed.value) 0.85f else 1f,
        label = "scalingClickableAnimation"
    )
    val isSelected = currentEntry?.value?.destination?.route == route

    LaunchedEffect(text) {
        interactionSource.interactions.collectLatest {
            when(it) {
                is HoverInteraction.Enter -> isHovered.value = true
                is HoverInteraction.Exit -> isHovered.value = false
            }
        }
    }

    Text(
        modifier = modifier
            .scale(scale.value)
            .hoverable(interactionSource)
            .pointerInput(text) {
                detectTapGestures(
                    onPress = {
                        isPressed.value = true
                        tryAwaitRelease()
                        isPressed.value = false
                    },
                    onTap = {
                        if(isSelected.not()) navController?.navigate(route)
                    }
                )
            }
            .then(
                if(isHovered.value || isSelected) {
                    Modifier.background(
                        color = LocalTheme.current.colors.brandMain,
                        shape = LocalTheme.current.shapes.rectangularActionShape
                    )
                }else Modifier
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = text,
        style = LocalTheme.current.styles.title.copy(
            color = if(isHovered.value || isSelected) {
                LocalTheme.current.colors.tetrial
            } else LocalTheme.current.colors.secondary
        )
    )
}