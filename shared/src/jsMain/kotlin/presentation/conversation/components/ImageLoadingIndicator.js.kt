package presentation.conversation.components

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun ImageLoadingIndicator() = Text(
    text = "Loading...",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.primary
)