package augmy.interactive.com.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors
import augmy.interactive.com.ui.components.BrandHeaderButton
import kotlinx.browser.window
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.login_success
import website_brand.composeapp.generated.resources.login_success_action

@Composable
fun LoginScreen(
    nonce: String?,
    loginToken: String?,
) {
    ModalScreenContent(
        scrollState = rememberScrollState(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                text = stringResource(Res.string.login_success),
                style = LocalTheme.current.styles.subheading
            )
            Icon(
                modifier = Modifier.size(42.dp),
                imageVector = Icons.Outlined.CheckCircle,
                tint = SharedColors.GREEN_CORRECT,
                contentDescription = null
            )
        }
        BrandHeaderButton(
            text = stringResource(Res.string.login_success_action),
            onClick = {
                window.location.href = "https://augmy.org/login?nonce=${nonce}&loginToken=${loginToken}"
            }
        )
    }
}
