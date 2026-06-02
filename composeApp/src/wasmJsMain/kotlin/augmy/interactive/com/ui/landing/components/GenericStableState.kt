package augmy.interactive.com.ui.landing.components

import androidx.compose.ui.geometry.Offset

class GenericStableState(
    val stemSegments: Int,
    val wobbleOffsets: List<Float>,
) {
    val points: MutableList<Offset> = MutableList(stemSegments + 1) { Offset.Zero }
}
