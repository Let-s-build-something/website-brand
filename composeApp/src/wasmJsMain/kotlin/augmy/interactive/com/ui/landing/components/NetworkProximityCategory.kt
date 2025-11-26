package augmy.interactive.com.ui.landing.components

import androidx.compose.ui.graphics.Color
import augmy.interactive.com.base.theme.Colors
import org.jetbrains.compose.resources.StringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.category_intimate
import website_brand.composeapp.generated.resources.category_personal
import website_brand.composeapp.generated.resources.category_public
import website_brand.composeapp.generated.resources.category_social

/** Local categories of proximities */
enum class NetworkProximityCategory(
    val range: ClosedFloatingPointRange<Float>,
    val res: StringResource,
    val color: Color,
    val share: Float
) {

    /**
     * Intimate: The closest, most exclusive circle.
     */
    Intimate(
        range = 10f..11f,
        res = Res.string.category_intimate,
        color = Colors.ProximityIntimate,
        share = 0.3762f
    ),

    /**
     * Personal: Trusted close friends / inner circle beyond family.
     */
    Personal(
        range = 8f..9.99999f,
        res = Res.string.category_personal,
        color = Colors.ProximityPersonal,
        share = 0.2183f
    ),

    /**
     * Social: Community, extended friends, people you engage with regularly but not intimately.
     */
    Social(
        range = 5f..7.99999f,
        res = Res.string.category_social,
        color = Colors.ProximitySocial,
        share = 0.2461f
    ),

    /**
     * Public: Acquaintances + general audience (absorbs previous Contacts territory).
     */
    Public(
        range = 1f..4.99999f,
        res = Res.string.category_public,
        color = Colors.ProximityPublic,
        share = 0.1593f
    )
}