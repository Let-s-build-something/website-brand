package augmy.interactive.com.ui.landing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.landing.components.avatar.AvatarConfiguration
import augmy.interactive.com.ui.landing.components.avatar.AvatarHeadConfiguration
import augmy.interactive.com.ui.landing.demo.RandomFlowerField
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedGarden(
    modifier: Modifier = Modifier,
    configuration: AvatarConfiguration,
    valence: Float = 0f,
    arousal: Float = .2f,
    seed: String
) {
    val density = LocalDensity.current
    val potHeightDp = remember { mutableStateOf(0f) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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
                valence = valence,
                arousal = arousal,
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
}