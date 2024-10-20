package augmy.interactive.com.base.theme

import androidx.compose.ui.graphics.Color
import augmy.interactive.com.theme.BaseColors

object DarkAppColors: BaseColors {

    override val primary: Color = Color.White
    override val secondary: Color = Colors.GrayLight
    override val disabled: Color = secondary.copy(alpha = .6f)

    override val brandMain: Color = Colors.Asparagus

    override val brandMainDark: Color = Colors.HunterGreen

    override val tetrial: Color = Colors.Flax
    override val quartial: Color = Colors.AtomicTangerine

    override val backgroundLight: Color = Colors.Night

    override val backgroundDark: Color = Colors.EerieBlack

    override val shimmer: Color = Colors.HunterGreen16
    override val overShimmer: Color = Colors.HunterGreen42

    override val toolbarColor: Color = backgroundDark
}