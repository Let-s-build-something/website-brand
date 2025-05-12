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

/**
 * Visualization of person typing their message in the past.
 * It replicates the 1) tempo of typing 2) the stops, and 3) the emphasis
 */
@Composable
fun buildTempoString(
    key: Any? = null,
    text: AnnotatedString,
    style: SpanStyle,
    enabled: Boolean,
    timings: List<Long>,
    onFinish: () -> Unit
): AnnotatedString {
    val aboveMedianIndexes = remember(key, timings) {
        mutableStateOf<MutableSet<Int>?>(null)
    }
    val graphemes = remember(text, key) {
        mutableStateOf<List<String>?>(null)
    }

    if(timings.isNotEmpty()) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                graphemes.value = text.chunked(1)
                println("kostka_test, graphemes: ${graphemes.value}")

                val fourthQuartile = timings[timings.size / 4]
                val aboveMedianList = mutableSetOf<Int>()
                timings.forEachIndexed { index, timing ->
                    if(index < (graphemes.value?.count() ?: 0)
                        && timing > FULL_STOP_LENGTH / 2
                        && timing > fourthQuartile
                    ) {
                        aboveMedianList.add(
                            graphemes.value?.take(
                                // we skip possible space before the actual emphasis
                                if(graphemes.value?.elementAt(index).isNullOrBlank()
                                    && (timings.getOrNull(index + 1) ?: 0L) < FULL_STOP_LENGTH
                                ) {
                                    index + 1
                                } else index
                            )?.sumOf { it.length } ?: 0
                        )
                    }
                }
                aboveMedianIndexes.value = aboveMedianList
            }
        }
    }

    return when {
        timings.isEmpty() -> buildAnnotatedString {
            withStyle(style) { append(text) }
        }
        !enabled -> {
            buildAnnotatedString {
                append(text)
                graphemes.value?.toList()?.forEachIndexed { index, matchResult ->
                    if (aboveMedianIndexes.value?.contains(index) == true) {
                        addStyle(
                            style = style.copy(fontWeight = FontWeight.ExtraBold),
                            start = index,
                            end = index + 1
                        )
                    }
                }
            }
        }
        else -> {
            val cancellableScope = rememberCoroutineScope()
            val scope = rememberCoroutineScope()

            var currentPosition by rememberSaveable(key, timings) {
                mutableStateOf(-1)
            }
            val isTicked = remember(key) {
                mutableStateOf(true)
            }

            LaunchedEffect(Unit, enabled) {
                scope.coroutineContext.cancelChildren()
                scope.launch {
                    delay(FLICKER_DELAY_LOOSE)
                    while (currentPosition <= timings.size) {
                        val index = currentPosition + 1
                        val timing = timings.getOrNull(index) ?: 1L

                        println("kostka_test, next index: $index")

                        // if the index is 0, there may be a copy paste section we should skip
                        currentPosition = if(timing == 0L) {
                            var final = index
                            for (i in index .. timings.size) {
                                if((timings.getOrNull(i) ?: 0L) == 0L) final++ else break
                            }
                            final
                        }else index

                        if(timing > 0) {
                            if(currentPosition < text.length) {
                                isTicked.value = true
                                cancellableScope.coroutineContext.cancelChildren()
                                cancellableScope.launch {
                                    delay(FULL_STOP_LENGTH)
                                    while(index < text.length) {
                                        isTicked.value = isTicked.value == false
                                        delay(400L)
                                    }
                                }
                            }
                            delay(timing)
                        }
                    }
                    onFinish()
                }
            }

            buildAnnotatedString {
                graphemes.value?.toList()?.forEachIndexed { index, matchResult ->
                    if(index <= currentPosition) {
                        append(matchResult)
                    }
                    if(index == currentPosition && currentPosition < (graphemes.value?.count() ?: 0)) {
                        withStyle(
                            style = style.copy(
                                color = style.color.copy(alpha = if(isTicked.value) 1f else 0f),
                                letterSpacing = 0.sp
                            )
                        ) {
                            append("|")
                        }
                    }
                }
            }
        }
    }
}

private const val FLICKER_DELAY_LOOSE = 400L
private const val FULL_STOP_LENGTH = 800L
