package common.util

import common.viewmodel.IODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object ImageStorage {
    private val scope = CoroutineScope(SupervisorJob() + IODispatcher)

    suspend fun uploadImage(image: ByteArray) {

    }
}


