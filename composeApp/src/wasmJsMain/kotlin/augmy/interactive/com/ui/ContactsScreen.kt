package augmy.interactive.com.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.contacts_email
import website_brand.composeapp.generated.resources.contacts_email_value
import website_brand.composeapp.generated.resources.contacts_workplace
import website_brand.composeapp.generated.resources.contacts_workplace_value
import website_brand.composeapp.generated.resources.toolbar_action_contacts

/** home/landing screen which is initially shown on the application */
@Composable
fun ContactsScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val emailValue = buildAnnotatedLinkString(
        stringResource(Res.string.contacts_email_value)
    )

    ModalScreenContent(scrollState = rememberScrollState()) {
        SelectionContainer {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    CompactLayout(
                        emailValue = emailValue,
                        verticalPadding = verticalPadding
                    )
                }else {
                    LargeLayout(
                        emailValue = emailValue,
                        verticalPadding = verticalPadding,
                        horizontalPadding = horizontalPadding
                    )
                }
            }
        }
    }
}


@Composable
private fun CompactLayout(
    emailValue: AnnotatedString,
    verticalPadding: Dp
) {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.toolbar_action_contacts),
                style = LocalTheme.current.styles.heading
            )
            Box(
                Modifier
                    .background(
                        LocalTheme.current.colors.brandMainDark,
                        LocalTheme.current.shapes.roundShape
                    )
                    .padding(verticalPadding / 7)
            ) {
                AsyncImageThumbnail(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(LocalTheme.current.shapes.componentShape),
                    asset = Asset.Image.EarHelp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.LocationCity,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.secondary
                    )
                    Text(
                        text = stringResource(Res.string.contacts_workplace),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = stringResource(Res.string.contacts_workplace_value),
                    style = LocalTheme.current.styles.title
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.AlternateEmail,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.secondary
                    )
                    Text(
                        text = stringResource(Res.string.contacts_email),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = emailValue,
                    style = LocalTheme.current.styles.title
                )
            }
        }
    }
}

@Composable
private fun LargeLayout(
    emailValue: AnnotatedString,
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.toolbar_action_contacts),
                style = LocalTheme.current.styles.heading
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.LocationCity,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.secondary
                    )
                    Text(
                        text = stringResource(Res.string.contacts_workplace),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = stringResource(Res.string.contacts_workplace_value),
                    style = LocalTheme.current.styles.title
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.AlternateEmail,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.secondary
                    )
                    Text(
                        text = stringResource(Res.string.contacts_email),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = emailValue,
                    style = LocalTheme.current.styles.title
                )
            }
        }
        Box(
            Modifier
                .weight(1f)
                .padding(top = 32.dp)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                asset = Asset.Image.EarHelp
            )
        }
    }
}