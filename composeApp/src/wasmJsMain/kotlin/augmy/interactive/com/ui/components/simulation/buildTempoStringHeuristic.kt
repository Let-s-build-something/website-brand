package augmy.interactive.com.ui.components.simulation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun buildTempoStringHeuristic(
    key: Any? = null,
    text: AnnotatedString,
    style: SpanStyle,
    enabled: Boolean,
    baseDelayMs: Long = 35L,
    jitterMs: Long = 25L,
    onFinish: () -> Unit
): AnnotatedString {
    val graphemes = remember(text, key) { mutableStateOf<List<String>>(text.text.chunked(1)) }
    val emphasisStarts = remember(text, key) { mutableStateOf<Set<Int>>(emptySet()) }
    val generatedTimings = remember(text, key) { mutableStateOf<List<Long>>(emptyList()) }

    // Precompute heuristics off the main thread
    LaunchedEffect(text, key) {
        withContext(Dispatchers.Default) {
            val g = graphemes.value
            val timings = ArrayList<Long>(g.size)

            // Track word boundaries to detect ALL CAPS / long words
            var currentWord = StringBuilder()
            var currentWordStart = 0
            val emphasis = mutableSetOf<Int>()

            fun flushWord(endIndexExclusive: Int) {
                val w = currentWord.toString()
                if (w.length >= 2 && w == w.uppercase()) {
                    // mark whole word for emphasis
                    for (i in currentWordStart until endIndexExclusive) emphasis.add(i)
                }
                if (w.length >= 10) {
                    // add a small "thinking" pause at end of long word
                    val last = (endIndexExclusive - 1).coerceIn(0, timings.lastIndex)
                    if (last in timings.indices) timings[last] += 120L
                }
                currentWord = StringBuilder()
            }

            for (i in g.indices) {
                val ch = g[i]
                val c = ch.firstOrNull()

                // base delay with jitter
                val jitter = (0..jitterMs.toInt()).random() - (jitterMs.toInt() / 2)
                var d = (baseDelayMs + jitter).coerceAtLeast(5L)

                // build word for emphasis checks
                if (c != null && c.isLetterOrDigit()) {
                    if (currentWord.isEmpty()) currentWordStart = i
                    currentWord.append(c)
                } else {
                    if (currentWord.isNotEmpty()) flushWord(i)
                }

                // punctuation pauses
                d += when (c) {
                    ',', ';', ':' -> 180L
                    '.', '!', '?' -> 520L
                    '\n' -> 700L
                    ' ' -> 10L
                    else -> 0L
                }

                // repeated punctuation (like !! or ??) feels more intense
                if ((c == '!' || c == '?') && g.getOrNull(i + 1) == ch) d += 200L

                timings.add(d)
            }
            if (currentWord.isNotEmpty()) flushWord(g.size)

            generatedTimings.value = timings
            emphasisStarts.value = emphasis
        }
    }

    // Static output (no animation)
    if (!enabled) {
        return buildAnnotatedString {
            append(text)
            emphasisStarts.value.forEach { idx ->
                if (idx in 0 until length) {
                    addStyle(
                        style = style.copy(fontWeight = FontWeight.ExtraBold),
                        start = idx,
                        end = idx + 1
                    )
                }
            }
        }
    }

    // Animated output
    val scope = rememberCoroutineScope()
    val blinkScope = rememberCoroutineScope()

    var currentPosition by rememberSaveable(key, text) { mutableStateOf(-1) }
    val cursorVisible = remember(key) { mutableStateOf(true) }

    LaunchedEffect(enabled, text, key) {
        scope.coroutineContext.cancelChildren()
        blinkScope.coroutineContext.cancelChildren()

        scope.launch {
            delay(250L)
            while (currentPosition < generatedTimings.value.lastIndex) {
                currentPosition++

                // blink when pausing (punctuation) to feel like "thinking"
                val t = generatedTimings.value.getOrNull(currentPosition) ?: baseDelayMs
                if (t > 300L) {
                    blinkScope.launch {
                        cursorVisible.value = true
                        delay(220L)
                        cursorVisible.value = false
                        delay(220L)
                        cursorVisible.value = true
                    }
                }
                delay(t)
            }
            onFinish()
        }
    }

    return buildAnnotatedString {
        graphemes.value.forEachIndexed { index, g ->
            if (index <= currentPosition) append(g)
            if (index == currentPosition && currentPosition < graphemes.value.size) {
                withStyle(
                    style = style.copy(
                        color = style.color.copy(alpha = if (cursorVisible.value) 1f else 0f),
                        letterSpacing = 0.sp
                    )
                ) { append("|") }
            }
        }

        // Apply emphasis to already-typed characters
        emphasisStarts.value.forEach { idx ->
            if (idx <= currentPosition && idx in 0 until length) {
                addStyle(
                    style = style.copy(fontWeight = FontWeight.ExtraBold),
                    start = idx,
                    end = (idx + 1).coerceAtMost(length)
                )
            }
        }
    }
}
