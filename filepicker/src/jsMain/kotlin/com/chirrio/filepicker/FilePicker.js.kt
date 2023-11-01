package com.chirrio.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.document
import kotlinx.coroutines.await
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.Document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

data class WebFile(
    override val path: String,
    override val platformFile: File,
) : MPFile<File> {
    override suspend fun readAsBytes(): ByteArray {
        val promise = readFileAsByteArray(platformFile)
        return promise.await()
    }
}

fun readFileAsByteArray(file: File): Promise<ByteArray> {
    val reader = FileReader()

    return Promise { resolve, reject ->
        reader.onload = { event ->
            val arrayBuffer = event.target.asDynamic().result as ArrayBuffer
            val byteArray = ByteArray(arrayBuffer.byteLength)
            val view = Int8Array(arrayBuffer)

            for (i in byteArray.indices) {
                byteArray[i] = view[i]
            }

            resolve(byteArray)
        }

        reader.onerror = { _ ->
            reject(reader.error as Throwable)
        }

        reader.readAsArrayBuffer(file)
    }
}

@Composable
actual fun localContext(): Any = remember { Any() }

actual fun ByteArray.toImageBitmap(context: Any, file: MPFile<Any>) =
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
            val files: List<File> =
                document.selectFilesFromDisk(fileExtensions.joinToString(","), multipleFiles)
            onFileSelected(files.take(maxNumberOfFiles).map { WebFile(it.name, it) })
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
    // in a browser we can not pick directories
    throw NotImplementedError("DirectoryPicker is not supported on the web")
}

private suspend fun Document.selectFilesFromDisk(
    accept: String,
    isMultiple: Boolean
): List<File> = suspendCoroutine {
    val tempInput = (createElement("input") as HTMLInputElement).apply {
        type = "file"
        style.display = "none"
        this.accept = accept
        multiple = isMultiple
    }

    tempInput.onchange = { changeEvt ->
        val files = (changeEvt.target.asDynamic().files as ItemArrayLike<File>).asList()
        it.resume(files)
    }

    body!!.append(tempInput)
    tempInput.click()
    tempInput.remove()
}

suspend fun readFileAsText(file: File): String = suspendCoroutine {
    val reader = FileReader()
    reader.onload = { loadEvt ->
        val content = loadEvt.target.asDynamic().result as String
        it.resumeWith(Result.success(content))
    }
    reader.readAsText(file, "UTF-8")
}