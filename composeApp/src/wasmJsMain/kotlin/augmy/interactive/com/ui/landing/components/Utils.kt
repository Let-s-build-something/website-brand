package augmy.interactive.com.ui.landing.components

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.PI

/** Color derived from a user tag */
fun tagToColor(tag: String?) = if (tag != null) Color(("ff$tag").toLong(16)) else null

fun categoryFromProximitySync(
    proximity: Float?,
    default: NetworkProximityCategory = NetworkProximityCategory.Public
): NetworkProximityCategory = NetworkProximityCategory.entries
    .firstOrNull { it.range.contains(proximity ?: -1f) } ?: default

suspend fun categoryFromProximity(
    proximity: Float?,
    default: NetworkProximityCategory = NetworkProximityCategory.Public
) = withContext(Dispatchers.Default) {
    categoryFromProximitySync(proximity, default)
}

/** Converts color to 6 hexadecimal numbers representing it without transparency */
fun Color.asSimpleString() = this.value.toString(16).substring(2, 8)

fun toDegrees(radians: Float): Float = radians * 180f / PI.toFloat()

inline fun <reified T : Enum<T>> T.isIn(vararg entries: T): Boolean {
    return entries.contains(this)
}

inline fun <reified T : Number> T?.orZero(): T {
    return this ?: when (T::class) {
        Int::class -> 0 as T
        Long::class -> 0L as T
        Double::class -> 0.0 as T
        Float::class -> 0f as T
        Short::class -> 0.toShort() as T
        Byte::class -> 0.toByte() as T
        else -> throw IllegalArgumentException("Unsupported numeric type: ${T::class}")
    }
}

inline fun <reified T : Number> T?.orDefault(value: T): T {
    return this ?: value
}
