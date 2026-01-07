package augmy.interactive.com.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.SocialLogo
import augmy.interactive.com.base.theme.isDarkTheme
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.contacts_instagram
import website_brand.composeapp.generated.resources.contacts_instagram_tag
import website_brand.composeapp.generated.resources.contacts_linkedin
import website_brand.composeapp.generated.resources.contacts_linkedin_tag
import website_brand.composeapp.generated.resources.contacts_twitter
import website_brand.composeapp.generated.resources.contacts_twitter_tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaBottomSheet(
    modifier: Modifier = Modifier,
    text: String? = null,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit
) {
    SimpleModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        text?.let {
            Text(
                text = it,
                style = LocalTheme.current.styles.subheading
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally)
        ) {
            SocialLogo(
                size = 28.dp,
                tag = Res.string.contacts_twitter_tag,
                link = Res.string.contacts_twitter,
                asset = Asset.Logo.Twitter,
                tint = if(isDarkTheme) Color.White else Color.Black
            )
            SocialLogo(
                tag = Res.string.contacts_instagram_tag,
                link = Res.string.contacts_instagram,
                asset = Asset.Logo.Instagram
            )
            SocialLogo(
                tag = Res.string.contacts_linkedin_tag,
                link = Res.string.contacts_linkedin,
                asset = Asset.Logo.LinkedIn
            )
        }
    }
}