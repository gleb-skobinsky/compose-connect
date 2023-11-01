package common.util

import androidx.compose.runtime.Composable
import common.viewmodel.IODispatcher
import data.transport.LocalRoute
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job

@Composable
fun ioPainterResource(path: String) = asyncPainterResource(path) {
    coroutineContext = Job() + IODispatcher
}

fun String.toResourceUrl(): String {
    if (isBlank()) return LocalRoute.currentUrl
    if (this[0] == '/') return "${LocalRoute.currentUrl}$this"
    return "${LocalRoute.currentUrl}/$this"
}