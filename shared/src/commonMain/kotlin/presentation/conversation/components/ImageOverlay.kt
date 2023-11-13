package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import domain.model.Attachments
import presentation.conversation.ConversationViewModel


expect fun Modifier.onScrollCancel(action: () -> Unit): Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedImageOverlay(viewModel: ConversationViewModel) {
    val images by viewModel.imagesForUpload.collectAsState()
    val displayedImage by viewModel.detailedImage.collectAsState()
    displayedImage?.let { imageIndex ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            HorizontalPagerPlatform(imageIndex, viewModel, images)
        }
    }
}

@Composable
expect fun BoxWithConstraintsScope.HorizontalPagerPlatform(
    imageIndex: Int,
    viewModel: ConversationViewModel,
    images: Attachments
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxWithConstraintsScope.MobileHorizontalPager(
    imageIndex: Int,
    viewModel: ConversationViewModel,
    images: Attachments
) {
    val pagerState = rememberPagerState(imageIndex) {
        images.size
    }
    HorizontalPager(state = pagerState, modifier = Modifier.closeOnScroll(maxHeight) {
        viewModel.setDetailedImage(null)
    }) {
        val image = images[it]
        Image(
            bitmap = image.imageBitmap,
            contentDescription = "Detailed view image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(maxWidth)
                .height(maxHeight)

        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxWithConstraintsScope.DesktopHorizontalPager(
    imageIndex: Int,
    viewModel: ConversationViewModel,
    images: Attachments
) {
    val scrollScope = rememberCoroutineScope()
    val listState = rememberLazyListState(imageIndex)
    val fling = rememberSnapFlingBehavior(listState)
    LazyRow(
        state = listState,
        flingBehavior = fling,
        modifier = Modifier.closeOnScroll(maxHeight) {
            viewModel.setDetailedImage(null)
        }
    ) {
        items(images.size, { it }) {
            val image = images[it]
            Image(
                bitmap = image.imageBitmap,
                contentDescription = "Detailed view image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(maxWidth)
                    .height(maxHeight)
                    .desktopSnapFling(listState) { direction ->
                        val curPage = calcPage(
                            listState.firstVisibleItemIndex,
                            maxWidth.value,
                            listState.firstVisibleItemScrollOffset
                        )
                        val newIndex = when (direction) {
                            ScrollDirection.FORWARD -> curPage + 1
                            ScrollDirection.BACK -> curPage - 1
                        }
                        if (newIndex >= 0 && newIndex < images.size) {
                            listState.animateScrollToItem(newIndex, 0)
                        }
                    }
            )
        }
    }
    ImageGalleryNavigators(listState, scrollScope)
}

fun calcPage(
    firstVisible: Int,
    width: Float,
    visibleOffset: Int
): Int {
    if (visibleOffset > width * 0.5f) {
        return firstVisible + 1
    }
    return firstVisible
}