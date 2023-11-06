package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import presentation.conversation.ConversationViewModel


expect fun Modifier.onScrollCancel(action: () -> Unit): Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedImageOverlay(viewModel: ConversationViewModel) {
    val images by viewModel.imagesForUpload.collectAsState()
    val displayedImage by viewModel.detailedImage.collectAsState()
    displayedImage?.let { imageIndex ->
        val pagerState = rememberPagerState(
            initialPage = imageIndex,
            initialPageOffsetFraction = 0f
        ) {
            images.size
        }
        val scrollScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { viewModel.setDetailedImage(null) }
        ) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val image = images[it]
                Image(
                    bitmap = image.imageBitmap,
                    contentDescription = "Detailed view image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .desktopSnapFling(pagerState, scrollScope)
                )
            }
            if (pagerState.canScrollBackward) {
                ImageNavigate(ImageNavigation.PREV, Modifier.align(Alignment.CenterStart)) {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            }
            if (pagerState.canScrollForward) {
                ImageNavigate(ImageNavigation.NEXT, Modifier.align(Alignment.CenterEnd)) {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }
    }
}