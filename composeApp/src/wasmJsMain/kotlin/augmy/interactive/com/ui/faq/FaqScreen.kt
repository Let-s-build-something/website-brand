package augmy.interactive.com.ui.faq

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.ComponentHeaderButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.faq_0_answer
import website_brand.composeapp.generated.resources.faq_0_question
import website_brand.composeapp.generated.resources.faq_1_answer
import website_brand.composeapp.generated.resources.faq_1_question
import website_brand.composeapp.generated.resources.faq_2_answer
import website_brand.composeapp.generated.resources.faq_2_question
import website_brand.composeapp.generated.resources.faq_3_answer
import website_brand.composeapp.generated.resources.faq_3_question
import website_brand.composeapp.generated.resources.faq_4_answer
import website_brand.composeapp.generated.resources.faq_4_question
import website_brand.composeapp.generated.resources.faq_5_answer
import website_brand.composeapp.generated.resources.faq_5_question
import website_brand.composeapp.generated.resources.faq_6_answer
import website_brand.composeapp.generated.resources.faq_6_question
import website_brand.composeapp.generated.resources.faq_7_answer
import website_brand.composeapp.generated.resources.faq_7_question
import website_brand.composeapp.generated.resources.faq_action
import website_brand.composeapp.generated.resources.faq_footer
import website_brand.composeapp.generated.resources.faq_heading

data class FaqItem(
    val question: StringResource,
    val answer: StringResource,
    val image: DrawableResource? = null
)

@Composable
fun FaqScreen() {
    val navController = LocalNavController.current
    val verticalPadding = (LocalContentSizeDp.current.height / 16).dp

    val scrollState = rememberScrollState()
    val expandedItem = remember {
        mutableStateOf(-1)
    }
    val items = remember {
        listOf(
            FaqItem(
                question = Res.string.faq_0_question,
                answer = Res.string.faq_0_answer,
            ),
            FaqItem(
                question = Res.string.faq_1_question,
                answer = Res.string.faq_1_answer,
            ),
            FaqItem(
                question = Res.string.faq_2_question,
                answer = Res.string.faq_2_answer,
            ),
            FaqItem(
                question = Res.string.faq_3_question,
                answer = Res.string.faq_3_answer,
            ),
            FaqItem(
                question = Res.string.faq_4_question,
                answer = Res.string.faq_4_answer,
            ),
            FaqItem(
                question = Res.string.faq_5_question,
                answer = Res.string.faq_5_answer,
            ),
            FaqItem(
                question = Res.string.faq_6_question,
                answer = Res.string.faq_6_answer,
            ),
            FaqItem(
                question = Res.string.faq_7_question,
                answer = Res.string.faq_7_answer,
            ),
        )
    }

    ModalScreenContent(
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollState = scrollState
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = verticalPadding)
                .align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.faq_heading),
            style = LocalTheme.current.styles.heading
        )

        items.forEachIndexed { index, item ->
            ExpendableItem(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                data = item,
                isExpanded = index == expandedItem.value,
                onClick = {
                    expandedItem.value = if (expandedItem.value == index) {
                        -1
                    }else index
                }
            )
        }
        Text(
            modifier = Modifier
                .padding(top = verticalPadding * 2)
                .align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.faq_footer),
            style = LocalTheme.current.styles.title
        )
        ComponentHeaderButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.faq_action)
        ) {
            navController?.navigate(NavigationNode.Contacts.route)
        }
        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun ExpendableItem(
    modifier: Modifier,
    data: FaqItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val bottomRadius = animateDpAsState(
        if (isExpanded) 0.dp else LocalTheme.current.shapes.componentCornerRadius
    )

    Column(
        modifier = modifier
            .fillMaxWidth(.8f)
            .animateContentSize()
            .scalingClickable(scaleInto = 1f) {
                onClick()
            }
            .border(
                width = 2.dp,
                color = LocalTheme.current.colors.backgroundDark,
                shape = LocalTheme.current.shapes.rectangularActionShape
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = LocalTheme.current.colors.backgroundDark,
                    shape = RoundedCornerShape(
                        topStart = LocalTheme.current.shapes.componentCornerRadius,
                        topEnd = LocalTheme.current.shapes.componentCornerRadius,
                        bottomStart = bottomRadius.value,
                        bottomEnd = bottomRadius.value,
                    )
                )
                .padding(vertical = 16.dp, horizontal = 24.dp),
            text = stringResource(data.question),
            style = LocalTheme.current.styles.title.copy(
                color = LocalTheme.current.colors.secondary
            )
        )

        if (isExpanded) {
            Row(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(data.answer),
                    style = LocalTheme.current.styles.regular
                )
            }
        }
    }
}
