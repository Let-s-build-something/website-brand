package augmy.interactive.com.base.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.LocalThemeColors
import augmy.interactive.com.theme.LocalThemeIcons
import augmy.interactive.com.theme.LocalThemeShapes
import augmy.interactive.com.theme.LocalThemeStyles
import augmy.interactive.com.theme.SharedColors

/** Theme with dynamic resources */
@Composable
fun AugmyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if(isDarkTheme) DarkAppColors else LightAppColors
    val styles = if(isDarkTheme) DarkThemeStyles() else DefaultThemeStyles()
    val icons = if(isDarkTheme) AppThemeIconsDark else AppThemeIconsLight

    CompositionLocalProvider(
        LocalThemeColors provides colors,
        LocalThemeIcons provides icons,
        LocalThemeStyles provides styles,
        LocalThemeShapes provides AppThemeShapes()
    ) {
        MaterialTheme(
            colorScheme = ColorScheme(
                primary = colors.brandMain,
                onPrimary = colors.tetrial,
                primaryContainer = colors.backgroundLight,
                onPrimaryContainer = colors.backgroundDark,
                inversePrimary = colors.tetrial,
                secondary = colors.brandMainDark,
                onSecondary = colors.tetrial,
                secondaryContainer = colors.brandMainDark,
                onSecondaryContainer = colors.primary,
                tertiary = Color.Transparent,
                onTertiary = Color.Transparent,
                tertiaryContainer = Color.Transparent,
                onTertiaryContainer = Color.Transparent,
                background = colors.backgroundLight,
                onBackground = colors.backgroundDark,
                surface = colors.backgroundLight,
                onSurface = colors.primary,
                surfaceVariant = Color.Transparent,
                onSurfaceVariant = Color.Transparent,
                surfaceTint = Color.Transparent,
                inverseSurface = Color.Transparent,
                inverseOnSurface = Color.Transparent,
                error = SharedColors.RED_ERROR,
                onError = Color.White,
                errorContainer = SharedColors.RED_ERROR_50,
                onErrorContainer = SharedColors.RED_ERROR,
                outline = Color.Transparent,
                outlineVariant = Color.Transparent,
                scrim = Color.Transparent,
                surfaceBright = colors.backgroundLight,
                surfaceDim = colors.backgroundDark,
                surfaceContainer = colors.backgroundLight,
                surfaceContainerHigh = colors.backgroundLight,
                surfaceContainerHighest = colors.backgroundDark,
                surfaceContainerLow = colors.backgroundLight,
                surfaceContainerLowest = colors.backgroundLight,
            ),
            typography = Typography(
                bodyLarge = styles.regular,
                bodySmall = styles.regular,
                bodyMedium = styles.regular,
                titleLarge = styles.regular,
                titleSmall = styles.title,
                titleMedium = styles.title,
                headlineMedium = styles.heading,
                headlineSmall = styles.heading,
                headlineLarge = styles.heading,
                labelLarge = styles.category,
                labelSmall = styles.category,
                labelMedium = styles.category
            ),
            content = content
        )
    }
}

/** Whether the current theme is dark */
val isDarkTheme
    @Composable
    get() = LocalTheme.current.colors is DarkAppColors

/**
 * Clickable modifier, which enables clickable to be scaled based on the presses
 */
@Composable
fun Modifier.scalingClickable(
    enabled: Boolean = true,
    onDoubleTap: ((Offset) -> Unit)? = null,
    onLongPress: ((Offset) -> Unit)? = null,
    onPress: ((Offset, isPressed: Boolean) -> Unit)? = null,
    scaleInto: Float = 0.85f,
    onTap: ((Offset) -> Unit)? = null
): Modifier = composed {
    if(enabled) {
        val hoverInteractionSource = remember { MutableInteractionSource() }
        val isHovered = hoverInteractionSource.collectIsHoveredAsState()
        val isPressed = remember { mutableStateOf(false) }
        val scale = animateFloatAsState(
            if ((isPressed.value || isHovered.value) && enabled) scaleInto else 1f,
            label = "scalingClickableAnimation"
        )

        scale(scale.value)
            .hoverable(
                enabled = enabled,
                interactionSource = hoverInteractionSource
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if(enabled) onPress?.invoke(it, true)
                        isPressed.value = true
                        tryAwaitRelease()
                        if(enabled) onPress?.invoke(it, false)
                        isPressed.value = false
                    },
                    onTap = onTap,
                    onDoubleTap = onDoubleTap,
                    onLongPress = onLongPress
                )
            }
    }else this
}
