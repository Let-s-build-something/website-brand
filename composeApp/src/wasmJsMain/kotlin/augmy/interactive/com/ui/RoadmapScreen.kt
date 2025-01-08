package augmy.interactive.com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.accessibility_button_expand
import website_brand.composeapp.generated.resources.roadmap_0_description
import website_brand.composeapp.generated.resources.roadmap_0_title
import website_brand.composeapp.generated.resources.roadmap_1_description
import website_brand.composeapp.generated.resources.roadmap_1_title
import website_brand.composeapp.generated.resources.roadmap_2_description
import website_brand.composeapp.generated.resources.roadmap_2_title
import website_brand.composeapp.generated.resources.roadmap_3_description
import website_brand.composeapp.generated.resources.roadmap_3_title
import website_brand.composeapp.generated.resources.roadmap_4_description
import website_brand.composeapp.generated.resources.roadmap_4_title
import website_brand.composeapp.generated.resources.roadmap_5_description
import website_brand.composeapp.generated.resources.roadmap_5_title
import website_brand.composeapp.generated.resources.roadmap_6_description
import website_brand.composeapp.generated.resources.roadmap_6_title
import website_brand.composeapp.generated.resources.roadmap_7_description
import website_brand.composeapp.generated.resources.roadmap_7_title
import website_brand.composeapp.generated.resources.roadmap_8_description
import website_brand.composeapp.generated.resources.roadmap_8_title
import website_brand.composeapp.generated.resources.roadmap_description
import website_brand.composeapp.generated.resources.roadmap_expand_button
import website_brand.composeapp.generated.resources.roadmap_heading
import website_brand.composeapp.generated.resources.roadmap_last_update

/** Screen  for outlining the project's roadmap and timeline */
@Composable
fun RoadmapScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 16).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    ModalScreenContent(scrollState = rememberScrollState()) {
        SelectionContainer {
            Column(
                modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(verticalPadding)
            ) {
                if(LocalDeviceType.current == WindowWidthSizeClass.Compact) {
                    CompactLayout(verticalPadding)
                }else LargeLayout(
                    verticalPadding = verticalPadding,
                    horizontalPadding = horizontalPadding
                )

                Spacer(Modifier.height(verticalPadding * 2))
            }
        }
    }
}

@Composable
private fun CompactLayout(verticalPadding: Dp) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.roadmap_heading),
            style = LocalTheme.current.styles.heading
        )
        Text(
            text = stringResource(Res.string.roadmap_last_update),
            style = LocalTheme.current.styles.category
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
                asset = Asset.Image.CustomDesign
            )
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.roadmap_description),
            style = LocalTheme.current.styles.regular
        )
    }

    VerticalContent()
}

@Composable
private fun LargeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.roadmap_heading),
                style = LocalTheme.current.styles.heading
            )
            Text(
                text = stringResource(Res.string.roadmap_last_update),
                style = LocalTheme.current.styles.category
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.roadmap_description),
                style = LocalTheme.current.styles.regular
            )
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
                asset = Asset.Image.CustomDesign
            )
        }
    }

    VerticalContent(fraction = .75f)
}

@Composable
private fun VerticalContent(fraction: Float = 1f) {
    val progressIndex = 1

    listOf(
        Res.string.roadmap_0_title to Res.string.roadmap_0_description,
        Res.string.roadmap_1_title to Res.string.roadmap_1_description,
        Res.string.roadmap_2_title to Res.string.roadmap_2_description,
        Res.string.roadmap_3_title to Res.string.roadmap_3_description,
        Res.string.roadmap_4_title to Res.string.roadmap_4_description,
        Res.string.roadmap_5_title to Res.string.roadmap_5_description,
        Res.string.roadmap_6_title to Res.string.roadmap_6_description,
        Res.string.roadmap_7_title to Res.string.roadmap_7_description,
        Res.string.roadmap_8_title to Res.string.roadmap_8_description,
    ).forEachIndexed { index, item ->
        val isExpanded = rememberSaveable(index) {
            mutableStateOf(false)
        }
        val rotation = animateFloatAsState(
            targetValue = if(isExpanded.value) 180f else 0f,
            label = "expansionArrowAnimation_$index"
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = stringResource(item.first),
                style = LocalTheme.current.styles.subheading.copy(
                    textDecoration = if(index < progressIndex) TextDecoration.LineThrough else null
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .clip(LocalTheme.current.shapes.componentShape)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = ripple(bounded = true),
                        onClick = {
                            isExpanded.value = !isExpanded.value
                        }
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.roadmap_expand_button),
                    style = LocalTheme.current.styles.category
                )
                Icon(
                    modifier = Modifier
                        .rotate(rotation.value)
                        .size(24.dp),
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = stringResource(Res.string.accessibility_button_expand),
                    tint = LocalTheme.current.colors.secondary
                )
            }
            AnimatedVisibility(isExpanded.value) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .fillMaxWidth(fraction),
                    text = stringResource(item.second),
                    style = LocalTheme.current.styles.regular
                )
            }
        }
    }
}