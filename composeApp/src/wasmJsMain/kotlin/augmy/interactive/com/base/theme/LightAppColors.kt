package augmy.interactive.com.base.theme

import androidx.compose.ui.graphics.Color
import augmy.interactive.com.theme.BaseColors

object LightAppColors: BaseColors {

    override val primary: Color = Colors.Onyx
    override val secondary: Color = Colors.Coffee
    override val disabled: Color = secondary.copy(alpha = .6f)

    override val brandMain: Color = Colors.Asparagus

    override val brandMainDark: Color = Colors.HunterGreen

    override val tetrial: Color = Colors.Flax

    override val quartial: Color = Colors.AtomicTangerine

    override val backgroundLight: Color = Color.White

    override val backgroundDark: Color = Colors.AzureWeb

    override val shimmer: Color = Colors.HunterGreen16
    override val overShimmer: Color = Colors.HunterGreen42

    override val toolbarColor: Color = backgroundLight
}