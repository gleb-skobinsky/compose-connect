package presentation.conversation.components

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation

@Composable
actual fun ImageLoadingIndicator() = Text(
    text = "Loading...",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.primary
)

actual fun getTransformation(fontFamily: FontFamily?): VisualTransformation = EmojisTransformation(fontFamily)