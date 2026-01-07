package augmy.interactive.com.ui.landing.components.avatar

import androidx.compose.ui.unit.Dp
import augmy.interactive.com.theme.BaseColors
import augmy.interactive.com.ui.landing.components.FlowerModel

sealed class AvatarFlowerConfiguration {
    class Generic(): AvatarFlowerConfiguration()

    class Sunflower(): AvatarFlowerConfiguration()

    class Chamomile(): AvatarFlowerConfiguration()

    class Dandelion(): AvatarFlowerConfiguration()

    class Cactus(
        val flower: AvatarFlowerConfiguration = Generic()
    ): AvatarFlowerConfiguration()

    fun toFlowerModel(
        colors: BaseColors,
        configuration: AvatarConfiguration,
        growthScale: Float, // .5f to 1f
        witherAmount: Float,
        stemHeight: Dp
    ): FlowerModel = when (this) {
        is Cactus -> FlowerModel.Cactus(
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight * 1.1f,
            flower = when (this.flower) {
                is Cactus -> FlowerModel.Cactus::class
                else -> FlowerModel.Generic::class
            },
            baseColor = configuration.color.primary ?: colors.brandMain,
            thornColor = configuration.color.secondary ?: colors.brandMainDark,
            flowerColor = configuration.color.tertial ?: colors.tertial
        )
        is Sunflower -> FlowerModel.Sunflower(
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight,
            petalColor = configuration.color.tertial ?: colors.tertial,
            centerColor = configuration.color.primary ?: colors.primary,
            stemColor = configuration.color.secondary ?: colors.brandMainDark
        )
        is Chamomile -> FlowerModel.Chamomile(
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight,
            centerColor = configuration.color.tertial ?: colors.tertial,
            petalColor = configuration.color.primary ?: colors.primary,
            stemColor = configuration.color.secondary ?: colors.brandMainDark
        )
        is Dandelion -> FlowerModel.Dandelion(
            growthScale = growthScale,
            witherAmount = witherAmount,
            stemHeight = stemHeight,
            petalColor = configuration.color.tertial ?: colors.tertial,
            stemColor = configuration.color.secondary ?: colors.brandMainDark
        )
        else -> {
            FlowerModel.Generic(
                growthScale = growthScale,
                witherAmount = witherAmount,
                stemHeight = stemHeight,
                flowerColor = configuration.color.tertial ?: colors.tertial,
                stemColor = configuration.color.secondary ?: colors.brandMainDark
            )
        }
    }
}
