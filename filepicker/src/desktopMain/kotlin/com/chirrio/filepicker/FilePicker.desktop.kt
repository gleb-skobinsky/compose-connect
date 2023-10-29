package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.io.File

data class JvmFile(
    override val path: String,
    override val platformFile: File,
) : MPFile<File>

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    onFileSelected: FileSelected
) {
    LaunchedEffect(show) {
        if (show) {
            val initialDir = initialDirectory ?: System.getProperty("user.dir")
            val file = awtFileChooser(
                initialDirectory = initialDir,
                extensions = fileExtensions
            )
            onFileSelected(file)
        }
    }
}

@Composable
actual fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    onFileSelected: (String?) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val initialDir = initialDirectory ?: System.getProperty("user.dir")
            onFileSelected(null) //TODO support directories
        }
    }
}