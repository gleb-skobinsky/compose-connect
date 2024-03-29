package com.chirrio.filepicker

import androidx.compose.runtime.Composable

@Composable
expect fun localContext(): Any

interface MPFile<out T : Any> {
    // on JS this will be a file name, on other platforms it will be a file path
    val path: String
    val platformFile: T

    suspend fun readAsBytes(): ByteArray?
}

val imageFileExtensions = listOf(".jpg", ".jpeg", ".png", ".heic")

typealias FileSelected = (List<MPFile<Any>>) -> Unit

@Composable
expect fun FilePicker(
    show: Boolean,
    initialDirectory: String? = null,
    fileExtensions: List<String> = emptyList(),
    multipleFiles: Boolean = true,
    maxNumberOfFiles: Int = 10,
    onFileSelected: FileSelected
)

@Composable
expect fun PhotoPicker(
    show: Boolean,
    initialDirectory: String? = null,
    multiplePhotos: Boolean = true,
    maxNumberOfPhotos: Int = 10,
    onFileSelected: FileSelected
)

@Composable
@Suppress("Unused")
expect fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String? = null,
    onFileSelected: (String?) -> Unit
)