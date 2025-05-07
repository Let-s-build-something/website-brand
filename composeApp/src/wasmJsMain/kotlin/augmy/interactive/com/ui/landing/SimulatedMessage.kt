package augmy.interactive.com.ui.landing

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.KeyframesSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes

data class SimulatedMessage(
    val content: String,
    val isCurrentUser: Boolean,
    val background: KeyframesSpec<Float>? = null,
    val isIndecisive: Boolean = false,
    // length of attention to valence, max 3000ms
    val attention: List<Pair<Long, Float>> = listOf(1500L to .2f, 1500L to .2f)
) {
    companion object {
        val howAreYouAttention = listOf(
            500L to .3f,
            2500L to .2f
        )
        val imSorryAttention = listOf(
            500L to 1f,
            833L to .8f,
            833L to .5f,
            834L to .2f
        )
        val beerAttention = listOf(
            500L to 0.5f,
            2500L to 0.2f
        )

        // TODO should reflect the overall "vibe", not the typing frequency as is - that can be simulated by liveTyping
        val chillQuickBackground = keyframes {
            durationMillis = 3500
            1.08f at 350 using LinearEasing
            1f at 700 using LinearEasing
            1.10f at 1050 using LinearEasing
            1f at 1400 using LinearEasing
            1f at 3500 using LinearEasing
        }

        val imSorryBackground = keyframes {
            durationMillis = 3500
            1.12f at 250 using FastOutLinearInEasing
            1f at 450 using LinearOutSlowInEasing
            1.08f at 900 using FastOutLinearInEasing
            1f at 1150 using LinearOutSlowInEasing
            1.1f at 1350 using FastOutLinearInEasing
            1.11f at 1850 using FastOutLinearInEasing
            1f at 2100 using LinearOutSlowInEasing
            1.15f at 2350 using FastOutLinearInEasing
            1.05f at 2800 using LinearOutSlowInEasing
            1f at 3500 using FastOutLinearInEasing
        }

        val indecisiveBackground = keyframes {
            durationMillis = 3500
            1.15f at 200 using FastOutSlowInEasing
            1.15f at 800 using LinearOutSlowInEasing
            1.05f at 1300 using LinearOutSlowInEasing
            1.12f at 1800 using FastOutSlowInEasing
            1.02f at 2200 using LinearOutSlowInEasing
            1.08f at 2700 using FastOutSlowInEasing
            1f at 3200 using LinearOutSlowInEasing
            1.07f at 3500 using FastOutSlowInEasing
        }

        val imOkayBackground = keyframes {
            durationMillis = 3500
            1.15f at 200 using FastOutSlowInEasing
            1.15f at 2200 using LinearOutSlowInEasing
            1.02f at 2700 using LinearOutSlowInEasing
            1.08f at 2850 using FastOutSlowInEasing
            1f at 2950 using LinearOutSlowInEasing
            1.09f at 3100 using FastOutSlowInEasing
            1f at 3250 using LinearOutSlowInEasing
            1.1f at 3350 using FastOutSlowInEasing
            1f at 3500 using LinearOutSlowInEasing
        }

        val beerBackground = keyframes {
            durationMillis = 3500
            1.15f at 200 using FastOutSlowInEasing
            1.15f at 1500 using LinearOutSlowInEasing
            1f at 1700 using LinearEasing
            1.1f at 1900 using FastOutSlowInEasing
            1f at 2100 using LinearEasing
            1.1f at 2300 using FastOutSlowInEasing
            1f at 2500 using FastOutSlowInEasing
            1f at 3500 using FastOutSlowInEasing
        }

        val decisiveFastBackground = keyframes {
            durationMillis = 3500
            1.15f at 500 using LinearEasing
            1.15f at 1000 using FastOutSlowInEasing
            1f at 1000 using LinearOutSlowInEasing
            1f at 3500 using LinearOutSlowInEasing
        }
    }
}