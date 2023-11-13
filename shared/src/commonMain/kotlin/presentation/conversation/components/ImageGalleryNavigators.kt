package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
expect fun BoxScope.ImageGalleryNavigators(pagerState: LazyListState, scrollScope: CoroutineScope)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.ImageNavigators(pagerState: LazyListState, scrollScope: CoroutineScope) {
    if (pagerState.canScrollBackward) {
        ImageNavigate(ImageNavigation.PREV, Modifier.align(Alignment.CenterStart)) {
            scrollScope.launch {
                pagerState.animateScrollToItem(pagerState.firstVisibleItemIndex - 1, 0)
            }
        }
    }
    if (pagerState.canScrollForward) {
        ImageNavigate(ImageNavigation.NEXT, Modifier.align(Alignment.CenterEnd)) {
            scrollScope.launch {
                pagerState.animateScrollToItem(pagerState.firstVisibleItemIndex + 1, 0)
            }
        }
    }
}