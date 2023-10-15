package common.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val IODispatcher: CoroutineDispatcher = Dispatchers.IO