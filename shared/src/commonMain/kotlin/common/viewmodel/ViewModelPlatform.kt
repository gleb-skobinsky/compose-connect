package common.viewmodel

import kotlinx.coroutines.CoroutineScope

interface ViewModelPlatform {
    val vmScope: CoroutineScope
}

expect open class ViewModelPlatformImpl() : ViewModelPlatform {
    override val vmScope: CoroutineScope
}