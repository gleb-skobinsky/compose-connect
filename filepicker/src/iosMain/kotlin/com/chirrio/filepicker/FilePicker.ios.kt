package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import platform.Foundation.NSURL

data class IosFile(
    override val path: String,
    override val platformFile: NSURL,
) : MPFile<NSURL> {
    override suspend fun readAsBytes(): ByteArray {
        TODO("Not yet implemented")
    }
}

actual fun ByteArray.toImageBitmap() = Image.makeFromEncoded(this).toComposeImageBitmap()

@Composable
actual fun PhotoPicker(
    show: Boolean,
    initialDirectory: String?,
    multiplePhotos: Boolean,
    onFileSelected: FileSelected
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.Images,
            multipleMode = multiplePhotos,
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
    onFileSelected: FileSelected
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.File(fileExtensions),
            multipleMode = multipleFiles,
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