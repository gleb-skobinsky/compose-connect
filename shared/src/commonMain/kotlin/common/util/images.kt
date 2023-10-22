package common.util

import androidx.compose.runtime.Composable
import common.viewmodel.IODispatcher
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job

@Composable
fun ioPainterResource(path: String) = asyncPainterResource(path) {
    coroutineContext = Job() + IODispatcher
}