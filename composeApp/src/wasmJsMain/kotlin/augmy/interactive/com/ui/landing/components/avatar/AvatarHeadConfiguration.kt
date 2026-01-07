package augmy.interactive.com.ui.landing.components.avatar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.compose.resources.DrawableResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.head
import website_brand.composeapp.generated.resources.head_piercing

sealed class AvatarHeadConfiguration {

    class Generic(): AvatarHeadConfiguration()

    class Piercing(): AvatarHeadConfiguration()

    val drawableRes: DrawableResource
        get() = when (this) {
            is Generic -> Res.drawable.head
            is Piercing -> Res.drawable.head_piercing
        }

    val scalpFraction: Float
        get() = when (this) {
            is Generic -> .920f
            is Piercing -> .896f
        }

    val contentAlignment: Alignment
        get() = when (this) {
            is Generic -> Alignment.BottomEnd
            is Piercing -> Alignment.BottomCenter
        }

    val padding: PaddingValues
        @Composable get() =
            when (this) {
                is Generic -> PaddingValues(
                    end = with(LocalDensity.current) { 4f.toDp() }
                )
                is Piercing -> PaddingValues(
                    start = with(LocalDensity.current) { 4.5f.toDp() }
                )
            }

}
