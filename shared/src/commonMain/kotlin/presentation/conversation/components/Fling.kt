package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private var sJob: Job? = null

private const val SCROLL_THRESHOLD = 0.2f

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.desktopSnapFling(
    pagerState: PagerState,
    pageHasChanged: MutableState<Boolean>,
    scrollScope: CoroutineScope
) =
    onScrollCancel {
        val offset = pagerState.currentPageOffsetFraction
        if (!pagerState.isScrollInProgress) {
            sJob?.cancel()
            if (pageHasChanged.value) {
                pageHasChanged.value = false
                sJob = scrollScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage, 0f)
                }
            } else {
                sJob = if (offset > SCROLL_THRESHOLD) {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1, 0f)
                    }
                } else if (offset < -SCROLL_THRESHOLD) {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1, 0f)
                    }
                } else {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage, 0f)
                    }
                }
            }
        }
    }