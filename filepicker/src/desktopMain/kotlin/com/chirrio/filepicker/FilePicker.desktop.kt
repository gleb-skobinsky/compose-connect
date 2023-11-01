package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.io.File

data class JvmFile(
    override val path: String,
    override val platformFile: File,
) : MPFile<File> {
    override suspend fun readAsBytes(): ByteArray {
        return withContext(Dispatchers.IO) { platformFile.readBytes() }
    }
}

@Composable
actual fun localContext(): Any = remember { Any() }


actual fun ByteArray.toImageBitmap(context: Any, file: MPFile<Any>): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    multipleFiles: Boolean,
    maxNumberOfFiles: Int,
    onFileSelected: FileSelected
) {
    LaunchedEffect(show) {
        if (show) {
            val initialDir = initialDirectory ?: System.getProperty("user.dir")
            val files = awtFileChooser(
                initialDirectory = initialDir,
                extensions = fileExtensions,
                multipleMode = multipleFiles,
                maxFiles = maxNumberOfFiles
            )
            onFileSelected(files)
        }
    }
}

@Composable
actual fun PhotoPicker(
    show: Boolean,
    initialDirectory: String?,
    multiplePhotos: Boolean,
    maxNumberOfPhotos: Int,
    onFileSelected: FileSelected
) = FilePicker(
    show = show,
    initialDirectory = initialDirectory,
    fileExtensions = imageFileExtensions,
    multipleFiles = multiplePhotos,
    maxNumberOfFiles = maxNumberOfPhotos,
    onFileSelected = onFileSelected
)

@Composable
actual fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    onFileSelected: (String?) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val initialDir = initialDirectory ?: System.getProperty("user.dir")
            val files = awtFileChooser(
                initialDirectory = initialDir,
                extensions = emptyList()
            )
            onFileSelected(
                files.firstOrNull()?.path
            )
        }
    }
}