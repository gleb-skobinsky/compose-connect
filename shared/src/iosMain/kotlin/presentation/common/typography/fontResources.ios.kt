package presentation.common.typography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun fontResources(
    font: String,
    weight: FontWeight,
    style: FontStyle
): Font? {
    var fontload: Font? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    scope.launch {
        fontload = androidx.compose.ui.text.platform.Font(
            identity = font,
            data = resource("font/$font").readBytes(),
            weight = weight,
            style = style
        )
    }
    return fontload
}