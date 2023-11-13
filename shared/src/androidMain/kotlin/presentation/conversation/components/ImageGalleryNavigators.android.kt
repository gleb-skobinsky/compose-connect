package presentation.conversation.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import domain.model.Attachments
import kotlinx.coroutines.CoroutineScope
import presentation.conversation.ConversationViewModel

@Composable
actual fun BoxScope.ImageGalleryNavigators(
    pagerState: LazyListState,
    scrollScope: CoroutineScope
) = Unit

@Composable
actual fun BoxWithConstraintsScope.HorizontalPagerPlatform(
    imageIndex: Int,
    viewModel: ConversationViewModel,
    images: Attachments
) = MobileHorizontalPager(imageIndex, viewModel, images)