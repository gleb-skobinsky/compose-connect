package com.chirrio.filepicker

import androidx.compose.runtime.Composable

interface MPFile<out T : Any> {
    // on JS this will be a file name, on other platforms it will be a file path
    val path: String
    val platformFile: T
}

val imageFileExtensions = listOf(".jpg", ".jpeg", ".png", ".heic")

typealias FileSelected = (List<MPFile<Any>>) -> Unit

@Composable
expect fun FilePicker(
    show: Boolean,
    initialDirectory: String? = null,
    fileExtensions: List<String> = emptyList(),
    multipleFiles: Boolean = true,
    onFileSelected: FileSelected
)

@Composable
expect fun PhotoPicker(
    show: Boolean,
    initialDirectory: String? = null,
    multiplePhotos: Boolean = true,
    onFileSelected: FileSelected
)

@Composable
expect fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String? = null,
    onFileSelected: (String?) -> Unit
)