package common.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual open class ViewModelPlatformImpl : ViewModelPlatform {
    actual override val vmScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
}