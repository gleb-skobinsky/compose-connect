package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun BoxScope.ImageGalleryNavigators(
    pagerState: PagerState,
    scrollScope: CoroutineScope
) = ImageNavigators(pagerState, scrollScope)