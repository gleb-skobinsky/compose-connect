package com.chirrio.filepicker

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

data class AndroidFile(
    override val path: String,
    override val platformFile: Uri,
) : MPFile<Uri>

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    multipleFiles: Boolean,
    onFileSelected: FileSelected
) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments()
        ) { result ->
            onFileSelected(result.map { AndroidFile(it.toString(), it) })
        }

    val mimeTypeMap = MimeTypeMap.getSingleton()
    val mimeTypes = if (fileExtensions.isNotEmpty()) {
        fileExtensions.mapNotNull { ext ->
            mimeTypeMap.getMimeTypeFromExtension(ext)
        }.toTypedArray()
    } else {
        emptyArray()
    }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(mimeTypes)
        }
    }
}

@Composable
actual fun PhotoPicker(
    show: Boolean,
    initialDirectory: String?,
    multiplePhotos: Boolean,
    onFileSelected: FileSelected
) {
    val launcher =
        if (multiplePhotos) {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetMultipleContents()
            ) { result ->
                onFileSelected(result.map { AndroidFile(it.toString(), it) })
            }
        } else {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { result ->
                val file = result?.let { listOf(AndroidFile(it.toString(), it)) }.orEmpty()
                onFileSelected(file)
            }
        }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch("image/*")
        }
    }
}

@Composable
actual fun DirectoryPicker(
    show: Boolean,
    initialDirectory: String?,
    onFileSelected: (String?) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { result ->
            onFileSelected(result?.toString())
        }

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(null)
        }
    }
}