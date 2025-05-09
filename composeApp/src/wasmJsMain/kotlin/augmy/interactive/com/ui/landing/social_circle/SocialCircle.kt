package augmy.interactive.com.ui.landing.social_circle

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncSvgImage
import augmy.interactive.com.ui.components.AutoResizeText
import augmy.interactive.com.ui.components.FontSizeRange
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.loadKoinModules
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val CIRCLE_MAX_HEIGHT_DP = 500

@Composable
fun SocialCircle(
    modifier: Modifier,
    isVisible: State<Boolean>,
) {
    loadKoinModules(socialCircleModule)
    val model: SocialCircleModel = koinViewModel()
    val density = LocalDensity.current

    val people = model.people.collectAsState(initial = listOf())
    val categories = model.categories.collectAsState()

    var contentSize by rememberSaveable {
        mutableStateOf(0)
    }
    val extendingCircles = remember {
        mutableStateOf(true)
    }
    val itemPaddingPx = with(density) { 2.dp.toPx() }
    val layerPadding = 12.dp

    LaunchedEffect(isVisible.value) {
        // we extend from family and backwards
        while(isVisible.value) {
            delay(500)
            val newCategory = NetworkProximityCategory.entries.getOrNull(categories.value.size) // plus one category
            if (newCategory == null) extendingCircles.value = false
            else if (categories.value.size == 1) extendingCircles.value = true

            model.changeCategories(
                if (extendingCircles.value && newCategory != null) {
                    categories.value.plus(newCategory)
                }else {
                    categories.value.minus(categories.value.last())
                }
            )
            delay(1000)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f, fill = true)
                .heightIn(max = CIRCLE_MAX_HEIGHT_DP.dp)
                .onSizeChanged {
                    contentSize = it.width
                }
                .aspectRatio(1f, matchHeightConstraintsFirst = true),
            contentAlignment = Alignment.Center
        ) {
            val presentShares = categories.value.sumOf { it.share.toDouble() }
            val missingShares = NetworkProximityCategory.entries.minus(categories.value.toSet())
                .sumOf { it.share.toDouble() }

            var previousShares = 0.0
            var startingIndex = 0
            NetworkProximityCategory.entries.forEach { category ->
                if(categories.value.contains(category)) {
                    val additionalShares = category.share / presentShares * missingShares
                    val shares = category.share + additionalShares + previousShares

                    val zIndex = categories.value.size - categories.value.indexOf(category) + 1f

                    val items = mutableListOf<NetworkItemIO?>()
                    var finished = false
                    for(index in startingIndex until people.value.size) {
                        if(!finished) {
                            people.value.getOrNull(index).let { data ->
                                if(data == null || category.range.contains(data.proximity ?: -1f)) {
                                    items.add(data)
                                }else {
                                    startingIndex = index
                                    finished = true
                                }
                            }
                        }
                    }

                    val radius = with(density) {
                        (contentSize * shares / 2).dp.minus(layerPadding).toPx()
                    }
                    val minRadius = with(density) {
                        (contentSize * previousShares / 2).dp.toPx()
                    }
                    val mappings = getMappings(
                        radius = radius,
                        minRadius = minRadius,
                        paddingPx = itemPaddingPx,
                        items = items
                    )
                    val circleSize = mappings.first
                    val mappedItems = mappings.second

                    Layout(
                        modifier = Modifier
                            .background(
                                color = category.color.copy(alpha = .6f),
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .animateContentSize(
                                alignment = Alignment.Center,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                            .zIndex(zIndex)
                            .size((contentSize * shares).dp),
                        content = {
                            items.forEach { data ->
                                NetworkItemCompact(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .animateContentSize()
                                        .zIndex(zIndex),
                                    data = data,
                                    size = with(density) { circleSize.toDp() }
                                )
                            }
                        }
                    ) { measurables, constraints ->
                        val placeables = measurables.map { measurable ->
                            measurable.measure(constraints)
                        }

                        layout(constraints.maxWidth, constraints.minHeight) {
                            var placeableIndex = 0
                            val padding = with(density) { layerPadding.toPx() }
                            val centerX = circleSize / 2 - padding / 2
                            val centerY = circleSize / 2 - padding / 2

                            mappedItems.map { it.value }.forEachIndexed { indexLayer, items ->
                                val radiusOverride = radius - (indexLayer * circleSize) - circleSize / 2

                                val angleStep = (2 * PI) / items.size
                                items.forEachIndexed { index, _ ->
                                    val angle = index * angleStep
                                    val x = centerX + (radiusOverride * cos(angle)).toFloat() - circleSize / 2
                                    val y = centerY + (radiusOverride * sin(angle)).toFloat() - circleSize / 2

                                    placeables[placeableIndex++].placeRelative(
                                        x = x.toInt(),
                                        y = y.toInt()
                                    )
                                }
                            }
                        }
                    }
                    previousShares = shares
                }
            }
        }

        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NetworkProximityCategory.entries.forEach { category ->
                val activated = categories.value.contains(category)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(
                                color = category.color.copy(alpha = if(activated) 1f else .5f),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = stringResource(category.res),
                        style = LocalTheme.current.styles.category.copy(
                            color = LocalTheme.current.colors.primary.copy(
                                alpha = if (activated) 1f else .5f
                            )
                        )
                    )
                }
            }
        }
    }
}

data class NetworkItemIO(
    val displayName: String? = null,
    val avatar: String? = null,
    val proximity: Float? = null
)

@Composable
private fun NetworkItemCompact(
    modifier: Modifier = Modifier,
    size: Dp,
    data: NetworkItemIO?
) {
    Column(
        modifier = modifier
            .requiredSize(size)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserProfileImage(
            modifier = Modifier
                .aspectRatio(1f)
                .weight(1f),
            media = data?.avatar,
            name = data?.displayName
        )
        /*Text(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally),
            text = data?.displayName ?: "",
            style = TextStyle(
                fontFamily = FontFamily(fontQuicksandSemiBold),
                fontSize = with(density) { (size / 6).toSp() },
                textAlign = TextAlign.Center,
                color = Color.White
            ),
            maxLines = 1
        )*/
    }
}

fun generateRandomTag(): String {
    val hexChars = "0123456789abcdef"
    return (1..6)
        .map { hexChars.random() }
        .joinToString("")
}

/** Color derived from a user tag */
fun tagToColor(tag: String?) = if(tag != null) Color(("ff$tag").toLong(16)) else null

@Composable
private fun UserProfileImage(
    modifier: Modifier = Modifier,
    media: String?,
    name: String?
) {
    val tag = generateRandomTag()
    val fallbackContent = @Composable {
        val backgroundColor = tagToColor(tag) ?: LocalTheme.current.colors.tetrial
        val textColor = if(backgroundColor.luminance() > .5f) Colors.Coffee else Colors.GrayLight

        AutoResizeText(
            modifier = Modifier.padding(vertical = 6.dp),
            text = name?.split("""\s""".toRegex())?.let {
                if(it.size > 1) {
                    it[0].take(1) + it[1].take(1)
                }else it.firstOrNull()?.take(1) ?: ""
            } ?: "",
            style = LocalTheme.current.styles.subheading.copy(color = textColor),
            fontSizeRange = FontSizeRange(
                min = 6.sp,
                max = LocalTheme.current.styles.subheading.fontSize * 1.5f
            )
        )
    }

    Crossfade(!media.isNullOrBlank()) { isValid ->
        if(isValid) {
            AsyncSvgImage(
                modifier = modifier
                    .clip(CircleShape)
                    .aspectRatio(1f),
                contentDescription = name,
                model = media,
                contentScale = ContentScale.Crop,
                fallbackContent = fallbackContent
            )
        }else if(name != null) {
            fallbackContent()
        }
    }
}

/** Utils function which calculates the appropriate circleSize and maps items based on their layers */
private fun <T> getMappings(
    radius: Float,
    minRadius: Float,
    paddingPx: Float,
    items: List<T?>
): Pair<Float, HashMap<Int, List<T?>>> {
    val itemsLeft = items.toMutableList()
    var circleSize = radius
    val mappedItems = hashMapOf<Int, List<T?>>()

    //step 1 -> first circle with the largest value
    //step 2 -> check if the circle fits all the items
    //step 3 -> take the number of items it does fit
    //step 4 -> reiterate until in the middle of the circle
    //step 5 -> if there are items left, decrease the circleSize and start at the step 2 at index 0
    var index = 0
    while (itemsLeft.isNotEmpty()) {
        val circleSizeWPadding = circleSize + paddingPx
        val currentRadius = radius - (index * circleSize) - circleSizeWPadding / 2

        val circumference = 2 * PI * currentRadius
        val itemsToFit = (circumference / circleSizeWPadding).toInt()

        if (itemsToFit > 0
            && circleSizeWPadding < currentRadius
            && currentRadius >= minRadius + circleSize / 2
        ) {
            val itemsToBeTaken = itemsLeft.take(itemsToFit.coerceAtMost(itemsLeft.size))
            mappedItems[index] = itemsToBeTaken
            itemsLeft.removeAll(itemsToBeTaken)

            // Check if there can be another layer
            if(itemsLeft.size > 0) index++
        } else {
            // If there's no more space for a new layer, reduce circleSize
            circleSize = (circleSize - 1).coerceAtLeast(1f)
            index = 0
            mappedItems.clear()
            itemsLeft.clear()
            itemsLeft.addAll(items)
        }

        if(circleSize == 1f) break
    }
    return circleSize to mappedItems
}
