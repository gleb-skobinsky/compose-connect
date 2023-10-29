package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import platform.Foundation.NSURL

data class IosFile(
    override val path: String,
    override val platformFile: NSURL,
) : MPFile<NSURL>

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    onFileSelected: FileSelected
) {
    val launcher = remember {
        FilePickerLauncher(
            initialDirectory = initialDirectory,
            pickerMode = FilePickerLauncher.Mode.File(fileExtensions),
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
            onFileSelected = { onFileSelected(it?.path) },
        )
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launchFilePicker()
        }
    }
}