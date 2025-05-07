package augmy.interactive.com.ui.landing

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.BrandActionButton
import augmy.interactive.com.ui.components.SocialMediaBottomSheet
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_download_button
import website_brand.composeapp.generated.resources.landing_download_content
import website_brand.composeapp.generated.resources.landing_download_heading
import website_brand.composeapp.generated.resources.landing_download_not_distributed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColumnScope.DownloadBlock() {
    val showSocialModal = rememberSaveable {
        mutableStateOf(false)
    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.landing_download_heading),
        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.landing_download_content),
        style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
    )

    if(showSocialModal.value) {
        SocialMediaBottomSheet(
            text = stringResource(Res.string.landing_download_not_distributed),
            onDismissRequest = {
                showSocialModal.value = false
            }
        )
    }

    BrandActionButton(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 24.dp),
        onPress = {
            showSocialModal.value = true
        },
        text = stringResource(Res.string.landing_download_button),
        imageVector = Icons.Outlined.Download
    )
}