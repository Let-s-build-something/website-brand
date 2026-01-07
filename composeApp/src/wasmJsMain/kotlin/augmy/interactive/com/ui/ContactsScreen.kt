package augmy.interactive.com.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AvatarImage
import augmy.interactive.com.ui.components.buildAnnotatedLink
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.contacts_augmy
import website_brand.composeapp.generated.resources.contacts_augmy_value
import website_brand.composeapp.generated.resources.contacts_email
import website_brand.composeapp.generated.resources.contacts_email_value
import website_brand.composeapp.generated.resources.contacts_heading
import website_brand.composeapp.generated.resources.contacts_phone
import website_brand.composeapp.generated.resources.contacts_phone_value
import website_brand.composeapp.generated.resources.contacts_workplace
import website_brand.composeapp.generated.resources.contacts_workplace_value
import website_brand.composeapp.generated.resources.logo_monochrome

@Composable
fun ContactsScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val emailValue = buildAnnotatedLinkString(
        stringResource(Res.string.contacts_email_value)
    )

    ModalScreenContent(scrollState = rememberScrollState()) {
        SelectionContainer(Modifier.padding(top = verticalPadding / 2)) {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    CompactLayout(
                        emailValue = emailValue,
                        verticalPadding = verticalPadding
                    )
                }else {
                    LargeLayout(
                        emailValue = emailValue,
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
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.contacts_heading),
                style = LocalTheme.current.styles.heading
            )
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(
                        LocalTheme.current.colors.brandMainDark.copy(alpha = .4f),
                        LocalTheme.current.shapes.roundShape
                    )
                    .padding(8.dp)
            ) {
                AvatarImage(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .aspectRatio(1f),
                    shape = LocalTheme.current.shapes.componentShape,
                    media = MediaIO(
                        url = "https://augmy.org/storage/company/kostka_jakub_rectangle.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_kostka_jakub_rectangle.jpg"
                    ),
                    name = "Jakub Kostka",
                    tag = null
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
                        tint = LocalTheme.current.colors.disabled
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
                        tint = LocalTheme.current.colors.disabled
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
                        painter = painterResource(Res.drawable.logo_monochrome),
                        contentDescription = null,
                        tint = LocalTheme.current.colors.disabled
                    )
                    Text(
                        text = stringResource(Res.string.contacts_augmy),
                        style = LocalTheme.current.styles.regular
                    )
                }
                val userId = stringResource(Res.string.contacts_augmy_value)
                Text(
                    text = buildAnnotatedLink(
                        text = userId,
                        linkTexts = listOf(userId),
                        onLinkClicked = { link, _ ->
                            window.open("https://augmy.org/users/$userId")
                        }
                    ),
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
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.disabled
                    )
                    Text(
                        text = stringResource(Res.string.contacts_phone),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = buildAnnotatedLinkString(
                        stringResource(Res.string.contacts_phone_value)
                    ),
                    style = LocalTheme.current.styles.title
                )
            }
        }
    }
}

@Composable
private fun LargeLayout(
    emailValue: AnnotatedString,
    horizontalPadding: Dp
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.contacts_heading),
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
                        tint = LocalTheme.current.colors.disabled
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
                        painter = painterResource(Res.drawable.logo_monochrome),
                        contentDescription = null,
                        tint = LocalTheme.current.colors.disabled
                    )
                    Text(
                        text = stringResource(Res.string.contacts_augmy),
                        style = LocalTheme.current.styles.regular
                    )
                }
                val userId = stringResource(Res.string.contacts_augmy_value)
                Text(
                    text = buildAnnotatedLink(
                        text = userId,
                        linkTexts = listOf(userId),
                        onLinkClicked = { link, _ ->
                            window.open("https://augmy.org/users/$userId")
                        }
                    ),
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
                        tint = LocalTheme.current.colors.disabled
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
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = null,
                        tint = LocalTheme.current.colors.disabled
                    )
                    Text(
                        text = stringResource(Res.string.contacts_phone),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Text(
                    text = buildAnnotatedLinkString(
                        stringResource(Res.string.contacts_phone_value)
                    ),
                    style = LocalTheme.current.styles.title
                )
            }
        }
        Box(
            Modifier
                .weight(1f, fill = false)
                .padding(top = 32.dp)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(8.dp)
        ) {
            AvatarImage(
                modifier = Modifier
                    .fillMaxWidth(.45f)
                    .aspectRatio(1f),
                shape = LocalTheme.current.shapes.componentShape,
                media = MediaIO(
                    url = "https://augmy.org/storage/company/kostka_jakub_rectangle.jpg",
                    thumbnail = "https://augmy.org/storage/company/thumbnails/tn_kostka_jakub_rectangle.jpg"
                ),
                name = "Jakub Kostka",
                tag = null
            )
        }
    }
}
