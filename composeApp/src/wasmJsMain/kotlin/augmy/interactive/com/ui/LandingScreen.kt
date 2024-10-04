package augmy.interactive.com.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {

    }
}