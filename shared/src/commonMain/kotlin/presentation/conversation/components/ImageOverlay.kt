package presentation.conversation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import presentation.conversation.ConversationViewModel

@Composable
fun DetailedImageOverlay(viewModel: ConversationViewModel) {
    val uploadingImages by viewModel.imagesForUpload.collectAsState()
    val displayedImage by viewModel.detailedImage.collectAsState()
    displayedImage?.let { imageIndex ->
        uploadingImages.images.getOrNull(imageIndex)?.let { currentImage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { viewModel.setDetailedImage(null) }
            ) {
                Image(
                    bitmap = currentImage.imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
                ImageNavigate(ImageNavigation.PREV, Modifier.align(Alignment.CenterStart)) {
                    viewModel.setDetailedImage(displayedImage?.let { it - 1 })
                }
                ImageNavigate(ImageNavigation.NEXT, Modifier.align(Alignment.CenterEnd)) {
                    viewModel.setDetailedImage(displayedImage?.let { it + 1 })
                }
            }
        }
    }
}