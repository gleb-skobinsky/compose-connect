package presentation.conversation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
actual fun ImageLoadingIndicator() = CircularProgressIndicator(
    modifier = Modifier.size(36.dp),
    color = MaterialTheme.colorScheme.primary,
    backgroundColor = Color.Transparent
)

actual fun getTransformation(fontFamily: FontFamily?): VisualTransformation = VisualTransformation.None