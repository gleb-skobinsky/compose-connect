package domain.model

import androidx.compose.runtime.Stable
import com.chirrio.filepicker.ImageWithData

@Stable
data class Attachment(
    val isLoading: Boolean = false,
    val images: List<ImageWithData> = emptyList()
) {
    operator fun iterator() = images.iterator()

    operator fun get(index: Int) = images[index]

    val size = images.size
}