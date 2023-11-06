package presentation.conversation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import presentation.conversation.ConversationViewModel


expect fun Modifier.onScrollCancel(action: () -> Unit): Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailedImageOverlay(viewModel: ConversationViewModel) {
    val images by viewModel.imagesForUpload.collectAsState()
    val displayedImage by viewModel.detailedImage.collectAsState()
    val imageOffset = remember { mutableStateOf(0.dp) }
    displayedImage?.let { imageIndex ->
        val pagerState = rememberPagerState(
            initialPage = imageIndex,
            initialPageOffsetFraction = 0f
        ) {
            images.size
        }
        val pageHasChanged = remember { mutableStateOf(false) }
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect {
                pageHasChanged.value = true
            }
        }
        val scrollScope = rememberCoroutineScope()
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.closeOnScroll(maxHeight, imageOffset) {
                    viewModel.setDetailedImage(null)
                    imageOffset.value = 0.dp
                }
            ) {
                val image = images[it]
                Image(
                    bitmap = image.imageBitmap,
                    contentDescription = "Detailed view image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .desktopSnapFling(pagerState, pageHasChanged, scrollScope)
                        .offset(y = imageOffset.value)
                )

            }
            ImageGalleryNavigators(pagerState, scrollScope)
        }
    }
}