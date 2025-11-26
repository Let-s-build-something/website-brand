package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

@Composable
fun SocialCircleSample(
    modifier: Modifier = Modifier,
    colors: Map<NetworkProximityCategory, Color> = mapOf(),
    categories: List<NetworkProximityCategory> = NetworkProximityCategory.entries,
    content: @Composable (NetworkProximityCategory) -> Unit = {}
) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val presentShares = categories.sumOf { it.share.toDouble() }
        val missingShares = NetworkProximityCategory.entries.minus(categories.toSet())
            .sumOf { it.share.toDouble() }

        var previousShares = 0.0
        categories.forEach { category ->
            val additionalShares = category.share / presentShares * missingShares
            val shares = category.share + additionalShares + previousShares

            val zIndex = categories.size - categories.indexOf(category) + 1f

            Box(
                modifier = Modifier
                    .background(
                        color = (colors[category] ?: category.color).copy(.8f),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .animateContentSize(
                        alignment = Alignment.Center,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .zIndex(zIndex)
                    .fillMaxWidth(shares.toFloat())
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                content(category)
            }
            previousShares = shares
        }
    }
}