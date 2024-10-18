package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IndicatedAction(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onPress: () -> Unit,
    content: @Composable BoxScope.(Modifier) -> Unit
) {
    val isPressed = remember { mutableStateOf(false) }
    val isHovered = remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val scale = animateFloatAsState(
        if (isPressed.value || isSelected || isHovered.value) 1f else 0f,
        label = "indicatedActionAnimation"
    )

    LaunchedEffect(onPress) {
        interactionSource.interactions.collectLatest {
            when(it) {
                is HoverInteraction.Enter -> isHovered.value = true
                is HoverInteraction.Exit -> isHovered.value = false
            }
        }
    }

    Box(modifier.width(IntrinsicSize.Min)) {
        AnimatedVisibility(isSelected) {
            HorizontalDivider(
                color = LocalTheme.current.colors.brandMain,
                thickness = 2.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(scale.value)
                    .clip(LocalTheme.current.shapes.rectangularActionShape)
            )
        }
        content(
            Modifier
                .hoverable(interactionSource)
                .pointerInput(onPress) {
                    detectTapGestures(
                        onPress = {
                            isPressed.value = true
                            tryAwaitRelease()
                            onPress()
                            isPressed.value = false
                        }
                    )
                }
                .padding(vertical = 4.dp, horizontal = 8.dp)
        )
        HorizontalDivider(
            color = LocalTheme.current.colors.brandMain,
            thickness = 2.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(scale.value)
                .clip(LocalTheme.current.shapes.rectangularActionShape)
        )
    }
}