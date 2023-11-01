package com.chirrio.filepicker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AndroidFile(
    override val path: String,
    override val platformFile: Uri,
    private val content: ByteArray? = null
) : MPFile<Uri> {
    override suspend fun readAsBytes(): ByteArray {
        return content ?: withContext(Dispatchers.IO) {
            platformFile.path?.let { File(it).readBytes() } ?: byteArrayOf()
        }
    }
}

@Composable
actual fun localContext(): Any = LocalContext.current

actual fun ByteArray.toImageBitmap(context: Any, file: MPFile<Any>): ImageBitmap {
    return toAndroidBitmap().asImageBitmap().rotate(context as Context, file as AndroidFile)
}

fun ImageBitmap.rotate(context: Context, file: AndroidFile): ImageBitmap {
    val inputStream = context.contentResolver.openInputStream(file.platformFile)
    return inputStream?.let {
        val exif = ExifInterface(inputStream)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        val rotatedBitmap =
            Bitmap.createBitmap(asAndroidBitmap(), 0, 0, width, height, matrix, true)
        rotatedBitmap.asImageBitmap()
    } ?: this
}

private fun ByteArray.toAndroidBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
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
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenMultipleDocuments()
        ) { result ->
            onFileSelected(result.take(maxNumberOfFiles).map { AndroidFile(it.toString(), it) })
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
    maxNumberOfPhotos: Int,
    onFileSelected: FileSelected
) {
    val imageLoadingScope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher =
        if (multiplePhotos) {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetMultipleContents()
            ) { result ->
                imageLoadingScope.launch {
                    val files = result.take(maxNumberOfPhotos).map { uri ->
                        async(Dispatchers.IO) {
                            val item = context.contentResolver.openInputStream(uri)
                            val bytes = item?.readBytes().also { item?.close() }
                            AndroidFile(uri.toString(), uri, bytes)
                        }
                    }
                    onFileSelected(files.awaitAll())
                }
            }
        } else {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { result ->
                result?.let { uri ->
                    imageLoadingScope.launch {
                        val file = async(Dispatchers.IO) {
                            val item = context.contentResolver.openInputStream(uri)
                            val bytes = item?.readBytes()
                            item?.close()
                            listOf(AndroidFile(uri.toString(), uri, bytes))
                        }
                        onFileSelected(file.await())
                    }
                } ?: run {
                    onFileSelected(emptyList())
                }
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