package com.chirrio.filepicker

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File
import java.io.FilenameFilter

fun awtFileChooser(
    initialDirectory: String,
    extensions: List<String>,
    multipleMode: Boolean = true
): List<JvmFile> {
    val fileDialog = FileDialog(ComposeWindow(), "Choose a file", FileDialog.LOAD).apply {
        directory = initialDirectory
        filenameFilter = AwtFileNameFilter(extensions)
        isMultipleMode = multipleMode
    }
    println(fileDialog.isMultipleMode)
    fileDialog.isVisible = true
    return if (fileDialog.directory != null && !fileDialog.files.isNullOrEmpty()) {
        return fileDialog.files.mapNotNull { JvmFile(it.path, it) }
    } else emptyList()
}

class AwtFileNameFilter(private val extensions: List<String>) : FilenameFilter {
    override fun accept(dir: File?, name: String?): Boolean {
        return extensions.any { name.toString().endsWith(it) }
    }
}