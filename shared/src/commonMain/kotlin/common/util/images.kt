package common.util

import androidx.compose.runtime.Composable
import common.viewmodel.IODispatcher
import data.transport.LocalRoute
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job

val imageFileExtensions = listOf(".jpg", ".jpeg", ".png", ".heic")

@Composable
fun ioPainterResource(path: String) = asyncPainterResource(path) {
    coroutineContext = Job() + IODispatcher
}

fun String.toResourceUrl() = "${LocalRoute.currentUrl}$this"