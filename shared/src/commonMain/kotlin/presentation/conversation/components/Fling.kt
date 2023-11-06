package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private var sJob: Job? = null

private const val SCROLL_THRESHOLD = 0.2f

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.desktopSnapFling(pagerState: PagerState, scrollScope: CoroutineScope) = onScrollCancel {
    val offset = pagerState.currentPageOffsetFraction
    if (!pagerState.isScrollInProgress) {
        sJob?.cancel()
        if (offset > SCROLL_THRESHOLD) {
            sJob = scrollScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        } else if (offset < -SCROLL_THRESHOLD) {
            sJob = scrollScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        } else if (offset != 0f) {
            sJob = scrollScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage, 0f)
            }
        }
    } else {
        sJob?.cancel()
        sJob = scrollScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage, 0f)
        }
    }
}