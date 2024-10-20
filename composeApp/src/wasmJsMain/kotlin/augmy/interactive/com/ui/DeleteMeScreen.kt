package augmy.interactive.com.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SpeakerNotes
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.IndicatedAction
import kotlinx.browser.window
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.delete_me_button_send
import website_brand.composeapp.generated.resources.delete_me_contents_hint
import website_brand.composeapp.generated.resources.delete_me_contents_label
import website_brand.composeapp.generated.resources.delete_me_description
import website_brand.composeapp.generated.resources.delete_me_heading
import website_brand.composeapp.generated.resources.delete_me_reason_long_hint
import website_brand.composeapp.generated.resources.delete_me_reason_long_label
import website_brand.composeapp.generated.resources.delete_me_reason_short_0
import website_brand.composeapp.generated.resources.delete_me_reason_short_1
import website_brand.composeapp.generated.resources.delete_me_reason_short_2
import website_brand.composeapp.generated.resources.delete_me_reason_short_3
import website_brand.composeapp.generated.resources.delete_me_reason_short_4
import website_brand.composeapp.generated.resources.delete_me_reason_short_5
import website_brand.composeapp.generated.resources.delete_me_user_id_hint
import website_brand.composeapp.generated.resources.delete_me_user_uid_label

/**
 * Page informing user about the ways he can remove data associated with him from our databases
 */
@Composable
fun DeleteMeScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val fraction = if(LocalDeviceType.current == WindowWidthSizeClass.Expanded) .75f else 1f

    val userUid = remember {
        mutableStateOf("")
    }
    val deleteContents = remember {
        mutableStateOf("")
    }
    val isUserUidError = remember {
        mutableStateOf(false)
    }
    val reasonLong = remember {
        mutableStateOf("")
    }
    val selectedReasonIndex = remember {
        mutableStateOf(-1)
    }

    val deleteMeReasons = listOf(
        Res.string.delete_me_reason_short_0,
        Res.string.delete_me_reason_short_1,
        Res.string.delete_me_reason_short_2,
        Res.string.delete_me_reason_short_3,
        Res.string.delete_me_reason_short_4,
        Res.string.delete_me_reason_short_5,
    )
    val currentReason = stringResource(
        deleteMeReasons.getOrNull(selectedReasonIndex.value) ?: Res.string.delete_me_reason_short_5
    )

    ModalScreenContent(
        scrollState = rememberScrollState(),
        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.delete_me_heading),
            style = LocalTheme.current.styles.heading
        )
        Text(
            modifier = Modifier.fillMaxWidth(fraction),
            text = stringResource(Res.string.delete_me_description),
            style = LocalTheme.current.styles.regular
        )

        Spacer(Modifier.height(verticalPadding))

        DeleteTextField(
            modifier = Modifier.fillMaxWidth(fraction),
            value = userUid.value,
            label = stringResource(Res.string.delete_me_user_uid_label),
            iconPrefix = Icons.Outlined.Fingerprint,
            placeholder = stringResource(Res.string.delete_me_user_id_hint),
            isError = isUserUidError.value,
            onValueChange = {
                userUid.value = it
                isUserUidError.value = false
            }
        )

        DeleteTextField(
            modifier = Modifier.fillMaxWidth(fraction),
            value = deleteContents.value,
            label = stringResource(Res.string.delete_me_contents_label),
            iconPrefix = Icons.Outlined.DeleteSweep,
            placeholder = stringResource(Res.string.delete_me_contents_hint),
            isError = isUserUidError.value,
            onValueChange = {
                deleteContents.value = it
                isUserUidError.value = false
            }
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            deleteMeReasons.forEachIndexed { index, key ->
                Row(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .clickable {
                            selectedReasonIndex.value = index
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RadioButton(
                        selected = selectedReasonIndex.value == index,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = LocalTheme.current.colors.brandMain,
                            unselectedColor = LocalTheme.current.colors.secondary
                        ),
                        onClick = {
                            selectedReasonIndex.value = index
                        }
                    )
                    Text(
                        text = stringResource(key),
                        style = LocalTheme.current.styles.regular
                    )
                }
            }
        }

        DeleteTextField(
            modifier = Modifier.fillMaxWidth(fraction),
            value = reasonLong.value,
            minLines = 4,
            label = stringResource(Res.string.delete_me_reason_long_label),
            iconPrefix = Icons.AutoMirrored.Outlined.SpeakerNotes,
            placeholder = stringResource(Res.string.delete_me_reason_long_hint),
            onValueChange = {
                reasonLong.value = it
            }
        )

        IndicatedAction(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(fraction),
            content = { modifier ->
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(Res.string.delete_me_button_send),
                    style = LocalTheme.current.styles.subheading
                )
            },
            onPress = {
                if(userUid.value.isNotBlank() && deleteContents.value.isNotBlank()) {
                    window.open("mailto:info@augmy.org?subject=Data deletion&body=UserUid: ${userUid.value}," +
                            "%0D%0AInformation to be deleted: ${deleteContents.value}" +
                            "%0D%0AShort reason: $currentReason" +
                            "%0D%0ALong reason: ${reasonLong.value}")
                }else isUserUidError.value = true
            }
        )

        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun DeleteTextField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    minLines: Int = 1,
    onValueChange: (String) -> Unit,
    iconPrefix: ImageVector,
    label: String,
    placeholder: String
) {
    OutlinedTextField(
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = LocalTheme.current.colors.secondary,
            focusedIndicatorColor = LocalTheme.current.colors.brandMain,
            focusedContainerColor = LocalTheme.current.colors.backgroundLight,
            disabledContainerColor = LocalTheme.current.colors.backgroundLight,
            errorContainerColor = LocalTheme.current.colors.backgroundLight,
            unfocusedContainerColor = LocalTheme.current.colors.backgroundLight
        ),
        value = value,
        minLines = minLines,
        label = {
            Text(
                text = label,
                style = LocalTheme.current.styles.regular
            )
        },
        prefix = {
            Icon(
                imageVector = iconPrefix,
                contentDescription = null,
                tint = LocalTheme.current.colors.disabled
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                style = LocalTheme.current.styles.regular.copy(
                    color = LocalTheme.current.colors.disabled
                )
            )
        },
        isError = isError,
        textStyle = LocalTheme.current.styles.regular,
        onValueChange = onValueChange
    )
}