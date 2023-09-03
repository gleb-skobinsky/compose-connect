package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class ViewModelPlatformImpl : ViewModelPlatform, ViewModel() {
    actual override val vmScope: CoroutineScope = viewModelScope
}