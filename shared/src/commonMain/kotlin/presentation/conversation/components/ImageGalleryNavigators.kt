package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
expect fun BoxScope.ImageGalleryNavigators(pagerState: PagerState, scrollScope: CoroutineScope)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.ImageNavigators(pagerState: PagerState, scrollScope: CoroutineScope) {
    if (pagerState.canScrollBackward) {
        ImageNavigate(ImageNavigation.PREV, Modifier.align(Alignment.CenterStart)) {
            scrollScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1, 0f)
            }
        }
    }
    if (pagerState.canScrollForward) {
        ImageNavigate(ImageNavigation.NEXT, Modifier.align(Alignment.CenterEnd)) {
            scrollScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1, 0f)
            }
        }
    }
}