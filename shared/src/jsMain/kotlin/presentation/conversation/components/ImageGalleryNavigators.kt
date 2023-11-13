package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun BoxScope.ImageGalleryNavigators(
    pagerState: LazyListState,
    scrollScope: CoroutineScope
) = ImageNavigators(pagerState, scrollScope)