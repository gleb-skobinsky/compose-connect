package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UniformTypeIdentifiers.UTTypeContent
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType
import platform.posix.memcpy

data class IosFile(
    override val path: String,
    override val platformFile: NSURL,
    val provider: NSItemProvider? = null
) : MPFile<NSURL> {
    override suspend fun readAsBytes(): ByteArray? = withContext(Dispatchers.Default) {
        if (provider != null) {
            val bytesToComplete = CompletableDeferred<ByteArray?>(null)
            provider.loadDataRepresentationForContentType(UTTypeContent) { data, error ->
                if (error != null) {
                    bytesToComplete.complete(null)
                } else {
                    bytesToComplete.complete(data?.toByteArray())
                }
            }
            bytesToComplete.await()
        } else {
            val data = NSData.dataWithContentsOfURL(platformFile)
            data?.toByteArray()
        }
    }
}


@OptIn(ExperimentalForeignApi::class)
internal fun NSData.toByteArray(): ByteArray {
    val result = ByteArray(length.toInt())
    if (result.isEmpty()) return result
    result.usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }
    return result
}

@Composable
actual fun localContext(): Any = remember { Any() }

actual fun ByteArray.toImageBitmap(context: Any, file: MPFile<Any>) =
    Image.makeFromEncoded(this).toComposeImageBitmap()

@Composable
actual fun PhotoPicker(
    show: Boolean,
    initialDirectory: String?,
    multiplePhotos: Boolean,
    maxNumberOfPhotos: Int,
    onFileSelected: FileSelected
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.Images,
            multipleMode = multiplePhotos,
            maxNumber = maxNumberOfPhotos,
            onFileSelected = onFileSelected,
        )
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launchFilePicker()
        }
    }
}

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    multipleFiles: Boolean,
    maxNumberOfFiles: Int,
    onFileSelected: FileSelected
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.File(fileExtensions),
            multipleMode = multipleFiles,
            maxNumber = maxNumberOfFiles,
            onFileSelected = onFileSelected,
        )
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launchFilePicker()
        }
    }
}

@Composable
actual fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    onFileSelected: (String?) -> Unit
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.Directory,
            multipleMode = false,
            onFileSelected = { files ->
                onFileSelected(files.firstOrNull()?.path)
            },
        )
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launchFilePicker()
        }
    }
}