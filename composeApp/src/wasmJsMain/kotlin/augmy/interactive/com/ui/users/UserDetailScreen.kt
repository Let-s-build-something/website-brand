package augmy.interactive.com.ui.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import augmy.interactive.com.BuildKonfig
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AvatarImage
import augmy.interactive.com.ui.components.ComponentHeaderButton
import augmy.interactive.com.ui.landing.StoreBadgeRow
import augmy.interactive.com.ui.landing.components.ErrorInfoBox
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_demo_others_action_alarm
import website_brand.composeapp.generated.resources.landing_demo_others_action_closeness
import website_brand.composeapp.generated.resources.landing_demo_others_action_interact
import website_brand.composeapp.generated.resources.user_profile_headline
import website_brand.composeapp.generated.resources.user_profile_not_found

@Composable
fun UserDetailScreen(userId: String?) {
    val model = koinViewModel<UserDetailModel>(
        key = userId,
        parameters = {
            parametersOf(userId)
        }
    )
    val response = model.response.collectAsState()

    ModalScreenContent(
        scrollState = rememberScrollState(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (response.value.error != null) {
            ErrorInfoBox(
                modifier = Modifier.fillMaxWidth(.7f),
                text = stringResource(Res.string.user_profile_not_found)
            )
        }
        val avatarMedia = response.value.data?.avatarUrl?.toMatrixMediaUrl(
            thumbnailSize = 100
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.user_profile_headline),
            style = LocalTheme.current.styles.subheading.copy(
                color = LocalTheme.current.colors.disabled
            )
        )

        if (avatarMedia != null) {
            AvatarImage(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 16.dp)
                    .sizeIn(maxHeight = 200.dp, maxWidth = 200.dp),
                media = avatarMedia,
                name = response.value.data?.displayName ?: response.value.data?.userId,
                tag = response.value.data?.copy(userId = userId)?.tag,
            )
        }
        SelectionContainer {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = buildAnnotatedString {
                    response.value.data?.displayName?.let {
                        withStyle(SpanStyle(fontSize = LocalTheme.current.styles.subheading.fontSize)) {
                            append(it)
                        }
                    }
                    withStyle(SpanStyle(color = LocalTheme.current.colors.disabled)) {
                        append(" (${userId})")
                    }
                },
                style = LocalTheme.current.styles.regular.copy(
                    color = LocalTheme.current.colors.secondary
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ComponentHeaderButton(
                endImageVector = Icons.Outlined.TrackChanges,
                text = stringResource(Res.string.landing_demo_others_action_closeness),
            )
            ComponentHeaderButton(
                endImageVector = Icons.Outlined.RoomService,
                text = stringResource(Res.string.landing_demo_others_action_alarm),
            )
            ComponentHeaderButton(
                endImageVector = Icons.AutoMirrored.Outlined.Chat,
                text = stringResource(Res.string.landing_demo_others_action_interact),
            )
        }

        StoreBadgeRow(
            modifier = Modifier.padding(top = 24.dp),
            referrer = userId
        )
        Spacer(Modifier.height(62.dp))
    }
}

fun String?.toMatrixMediaUrl(
    homeserver: String = "homeserver.augmy.org",
    thumbnailSize: Int
): MediaIO? {
    this ?: return null
    if (!startsWith("mxc://")) return MediaIO(url = this, thumbnail = this)

    val withoutScheme = removePrefix("mxc://")

    return MediaIO(
        thumbnail = "https://$homeserver/_matrix/client/v1/media/thumbnail/$withoutScheme" +
                "?width=$thumbnailSize&height=$thumbnailSize&method=crop" +
                "&access_token=${BuildKonfig.MatrixMediaToken}",
        url = "https://$homeserver/_matrix/client/v1/media/download/$withoutScheme" +
                "?access_token=${BuildKonfig.MatrixMediaToken}"
    )
}
