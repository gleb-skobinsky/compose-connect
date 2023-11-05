package presentation.conversation.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import presentation.common.messagesParser.emojiPattern

class EmojisTransformation(private val fontFamily: FontFamily?) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val stringText = text.toString()
        return TransformedText(
            buildAnnotatedStringWithEmojis(stringText, fontFamily),
            FixedOffsetMapping(stringText)
        )
    }
}

fun buildAnnotatedStringWithEmojis(text: String, fontFamily: FontFamily?): AnnotatedString {
    val matches = emojiPattern.findAll(text).toList()
    val annotated = buildAnnotatedString {
        var currentIndex = 0
        for (match in matches) {
            val emojiRange = match.range
            val textBeforeEmoji = text.substring(currentIndex, emojiRange.first)
            val emoji = text.substring(emojiRange)
            append(textBeforeEmoji)
            withStyle(SpanStyle(fontFamily = fontFamily)) {
                append(emoji)
            }
            currentIndex = emojiRange.last + 1
        }
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }

    return annotated
}


class FixedOffsetMapping(private val text: String) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return if (text.isEmpty()) 0 else offset
    }

    override fun transformedToOriginal(offset: Int): Int {
        return if (text.isEmpty()) 0 else offset
    }

}