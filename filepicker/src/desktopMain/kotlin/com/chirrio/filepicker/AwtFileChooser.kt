package com.chirrio.filepicker

import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Paths

fun awtFileChooser(initialDirectory: String, extensions: List<String>, ): JvmFile? {
    val fileDialog = FileDialog(ComposeWindow(), "Choose a file", FileDialog.LOAD)
    fileDialog.directory = initialDirectory
    // fileDialog.file = "*.oeg"
    fileDialog.filenameFilter = AwtFileNameFilter(extensions)
    fileDialog.isVisible = true
    return if (fileDialog.directory != null && fileDialog.file != null) {
        val file = Paths.get(fileDialog.directory, fileDialog.file).toFile()
        return JvmFile(file.path, file)
    } else null
}

class AwtFileNameFilter(private val extensions: List<String>) : FilenameFilter {
    override fun accept(dir: File?, name: String?): Boolean {
        return extensions.any { name.toString().endsWith(it) }
    }
}