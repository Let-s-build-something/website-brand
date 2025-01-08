package augmy.interactive.com.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import augmy.interactive.com.theme.LocalTheme
import kotlinx.browser.window

/** Email address pattern, same as [android.util.Patterns.EMAIL_ADDRESS] */
private val emailRegex = """[a-zA-Z0-9+._%-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9-]{0,25})+""".toRegex()

/** URL pattern, no HTTP or HTTPS needed */
private val urlRegex = """(?<=^|\s)[^\s@]+\.[^\s@]*[a-zA-Z][^\s@]*(?=${'$'}|\s)""".toRegex()

/** URL pattern, no HTTP or HTTPS needed */
private val phoneNumberRegex = """\+?\d{1,4}?[\s-]?\(?(\d{1,4})\)?[\s-]?\d{1,4}[\s-]?\d{1,4}[\s-]?\d{1,9}""".toRegex()

/**
 * Clickable text supporting <a href> HTML tags and can also match email, URL addresses, and phone numbers if needed
 * @param matchEmail whether an email should be considered a link
 * @param matchPhone whether a phone number should be considered a link
 * @param matchUrl whether an url should be considered a link
 * @param onLinkClicked called whenever a link was clicked, propagating the link including prefix for phone number and email
 */
@Composable
fun buildAnnotatedLinkString(
    text: String,
    matchEmail: Boolean = true,
    matchPhone: Boolean = true,
    matchUrl: Boolean = true,
    onLinkClicked: (link: String) -> Unit = {
        window.open(it)
    }
) = buildAnnotatedLinkString(
    text = text,
    linkStyles = LocalTheme.current.styles.link,
    matchEmail = matchEmail,
    matchPhone = matchPhone,
    matchUrl = matchUrl,
    onLinkClicked = onLinkClicked
)

/**
 * Clickable text supporting <a href> HTML tags and can also match email, URL addresses, and phone numbers if needed
 * @param linkStyles styles applied to the found link
 * @param matchEmail whether an email should be considered a link
 * @param matchPhone whether a phone number should be considered a link
 * @param matchUrl whether an url should be considered a link
 * @param onLinkClicked called whenever a link was clicked, propagating the link including prefix for phone number and email
 */
fun buildAnnotatedLinkString(
    text: String,
    linkStyles: TextLinkStyles,
    matchEmail: Boolean = true,
    matchPhone: Boolean = true,
    matchUrl: Boolean = true,
    onLinkClicked: (link: String) -> Unit = {
        window.open(it)
    }
) = buildAnnotatedString {
    // first, we replace all matches with according href link
    val annotatedText = text.run {
        var replaced = this
        if(matchEmail) {
            replaced = replaced.replace(emailRegex, transform = { res ->
                "<a href=\"mailto:${res.value}\">${res.value}<a/>"
            })
        }
        if(matchUrl) {
            replaced = replaced.replace(urlRegex, transform = { res ->
                "<a href=\"${res.value}\">${res.value}<a/>"
            })
        }
        if(matchPhone) {
            replaced = replaced.replace(phoneNumberRegex, transform = { res ->
                "<a href=\"tel:${res.value}\">${res.value}<a/>"
            })
        }
        replaced
    }

    var appendableText = ""
    var tagIteration = false

    annotatedText.forEach { c ->
        // may be a beginning of a tag, let's clear backstack to simplify conditions
        if(!tagIteration && c == '<') {
            append(appendableText)
            appendableText = ""
        }

        // beginning of link tag
        if(appendableText == "<a href=\"") {
            // append text before the link
            append(appendableText.removeSuffix("<a href=\""))
            appendableText = ""
            tagIteration = true
        }

        // end of link tag, all we have at this point is very likely LINK">CONTENT<a/
        if(tagIteration && c == '>' && appendableText.last() == '/') {
            appendableText.let { localAppendedText ->
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "ACTION",
                        styles = linkStyles,
                        linkInteractionListener = {
                            onLinkClicked(
                                localAppendedText.substring(
                                    startIndex = 0,
                                    endIndex = localAppendedText.indexOfLast { it == '"' }
                                )
                            )
                        },
                    ),
                ) {
                    append(
                        localAppendedText.substring(
                            startIndex = localAppendedText.indexOf('>') + 1,
                            endIndex = localAppendedText.indexOf("<")
                        )
                    )
                }
            }
            appendableText = ""
            tagIteration = false
        }else {
            appendableText += c
        }
    }
    append(appendableText)
}

/** Builds text with a single link represented by a text */
@Composable
fun buildAnnotatedLink(
    text: String,
    linkTextWithin: String,
    onLinkClicked: (link: String) -> Unit
) = buildAnnotatedString {
    append(text.substring(
        startIndex = 0,
        endIndex = text.indexOf(linkTextWithin)
    ))
    withLink(
        link = LinkAnnotation.Clickable(
            tag = "ACTION",
            styles = LocalTheme.current.styles.link,
            linkInteractionListener = {
                onLinkClicked(linkTextWithin)
            },
        ),
    ) {
        append(linkTextWithin)
    }
    append(text.substring(
        startIndex = text.indexOf(linkTextWithin) + linkTextWithin.length,
        endIndex = text.length
    ))
}
