package augmy.interactive.com.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.draggable
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.landing.demo.GardenContent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.app_calendar
import website_brand.composeapp.generated.resources.app_calendar_cs
import website_brand.composeapp.generated.resources.app_graph
import website_brand.composeapp.generated.resources.app_graph_cs
import website_brand.composeapp.generated.resources.app_home
import website_brand.composeapp.generated.resources.app_home_cs
import website_brand.composeapp.generated.resources.landing_demo_disclaimer
import website_brand.composeapp.generated.resources.landing_demo_others_heading
import website_brand.composeapp.generated.resources.landing_demo_you_heading
import website_brand.composeapp.generated.resources.landing_garden_description
import website_brand.composeapp.generated.resources.landing_problem_heading
import website_brand.composeapp.generated.resources.landing_problem_quote_0
import website_brand.composeapp.generated.resources.landing_problem_quote_1
import website_brand.composeapp.generated.resources.landing_problem_quote_2

@Composable
internal fun CompactLayout(verticalPadding: Dp) {
    val density = LocalDensity.current
    val language = Locale.current.language
    val contentWidthDp = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.onSizeChanged {
            with(density) {
                contentWidthDp.value = it.width.toDp().value
            }
        }
    ) {
        SelectionContainer(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = stringResource(Res.string.landing_garden_description),
                style = LocalTheme.current.styles.subheading.copy(textAlign = TextAlign.Center)
            )
        }
        AnimatedGarden()

        Spacer(Modifier.height(verticalPadding))

        SelectionContainer(modifier = Modifier.padding(horizontal = 12.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.landing_problem_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )

                Quote(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(Res.string.landing_problem_quote_2),
                    author = "-Petra, 16"
                )
                Quote(
                    text = stringResource(Res.string.landing_problem_quote_0),
                    author = "-Adam, 12"
                )
                Quote(
                    text = stringResource(Res.string.landing_problem_quote_1),
                    author = "-Lucie, 35"
                )
            }
        }

        Spacer(Modifier.height(verticalPadding))

        val demoYouScrollState = rememberScrollState()
        SelectionContainer(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.landing_demo_you_heading),
                style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
            )
        }
        Row(
            modifier = Modifier
                .horizontalScroll(demoYouScrollState)
                .draggable(state = demoYouScrollState, orientation = Orientation.Horizontal)
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(
                    color = Colors.ProximityIntimate.copy(alpha = .4f)
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(Modifier.width(8.dp))
            if (contentWidthDp.value > 0f) {
                GardenContent(
                    modifier = Modifier
                        .width(contentWidthDp.value.dp * .8f)
                        .padding(LocalTheme.current.shapes.componentCornerRadius)
                        .background(
                            color = LocalTheme.current.colors.backgroundLight,
                            shape = LocalTheme.current.shapes.componentShape
                        )
                )
            }
            Image(
                modifier = Modifier
                    .width(
                        contentWidthDp.value.dp + LocalTheme.current.shapes.componentCornerRadius * 2
                    )
                    .padding(LocalTheme.current.shapes.componentCornerRadius)
                    .background(
                        color = LocalTheme.current.colors.backgroundLight,
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(LocalTheme.current.shapes.componentCornerRadius),
                painter = painterResource(
                    if (language == "cs") Res.drawable.app_graph_cs else Res.drawable.app_graph
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Image(
                modifier = Modifier
                    .width(
                        contentWidthDp.value.dp + LocalTheme.current.shapes.componentCornerRadius * 2
                    )
                    .padding(LocalTheme.current.shapes.componentCornerRadius)
                    .background(
                        color = LocalTheme.current.colors.backgroundLight,
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(LocalTheme.current.shapes.componentCornerRadius),
                painter = painterResource(
                    if (language == "cs") Res.drawable.app_calendar_cs else Res.drawable.app_calendar
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.width(8.dp))
        }
        SelectionContainer(
            modifier = Modifier
                .padding(top = 2.dp)
                .padding(horizontal = 12.dp)
                .align(Alignment.End)
        ) {
            Text(
                text = stringResource(Res.string.landing_demo_disclaimer),
                style = LocalTheme.current.styles.regular.copy(
                    fontSize = 12.sp,
                    color = LocalTheme.current.colors.disabled
                )
            )
        }

        Spacer(Modifier.height(verticalPadding))

        val demoOthersScrollState = rememberScrollState()
        SelectionContainer(
            modifier = Modifier
                .padding(top = verticalPadding)
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(Res.string.landing_demo_others_heading),
                style = LocalTheme.current.styles.heading
            )
        }
        Row(
            modifier = Modifier
                .horizontalScroll(demoOthersScrollState)
                .draggable(state = demoOthersScrollState, orientation = Orientation.Horizontal)
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(
                    color = Colors.ProximityContacts.copy(alpha = .4f)
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.width(8.dp))
            DemoOthersUser(modifier = Modifier.width(contentWidthDp.value.dp * .8f))
            Image(
                modifier = Modifier
                    .width(contentWidthDp.value.dp)
                    .padding(LocalTheme.current.shapes.componentCornerRadius)
                    .background(
                        color = LocalTheme.current.colors.backgroundLight,
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(LocalTheme.current.shapes.componentCornerRadius),
                painter = painterResource(
                    if (language == "cs") Res.drawable.app_home_cs else Res.drawable.app_home
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.width(8.dp))
        }
        SelectionContainer(
            modifier = Modifier
                .padding(top = 2.dp)
                .align(Alignment.End)
        ) {
            Text(
                text = stringResource(Res.string.landing_demo_disclaimer),
                style = LocalTheme.current.styles.regular.copy(
                    fontSize = 12.sp,
                    color = LocalTheme.current.colors.disabled
                )
            )
        }

        Spacer(Modifier.height(verticalPadding))
    }
}
