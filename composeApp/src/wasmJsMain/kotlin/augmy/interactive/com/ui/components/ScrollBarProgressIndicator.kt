package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import augmy.interactive.com.theme.LocalTheme
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val VISIBILITY_DELAY = 1_500L

/** ScrollBar indicator for [ScrollState] */
@Composable
fun ScrollBarProgressIndicator(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val cancellableScope = rememberCoroutineScope()

    val progress = rememberSaveable {
        mutableStateOf(0f)
    }
    val isVisible = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.value }.collectLatest { scrollValue ->
            progress.value = scrollValue.toFloat() / scrollState.maxValue
        }
    }

    LaunchedEffect(progress.value) {
        if(progress.value != 0f) isVisible.value = true         // ignore initial visibility
        cancellableScope.coroutineContext.cancelChildren()
        cancellableScope.launch {
            delay(VISIBILITY_DELAY)
            isVisible.value = false
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.value,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { progress.value },
            color = LocalTheme.current.colors.brandMain,
            trackColor = LocalTheme.current.colors.secondary.copy(alpha = .5f),
            strokeCap = StrokeCap.Round,
        )
    }
}
