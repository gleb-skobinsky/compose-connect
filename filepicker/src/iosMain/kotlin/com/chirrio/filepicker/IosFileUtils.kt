package com.chirrio.filepicker

import com.chirrio.filepicker.FilePickerLauncher.Mode
import com.chirrio.filepicker.FilePickerLauncher.Mode.Directory
import com.chirrio.filepicker.FilePickerLauncher.Mode.File
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIAdaptivePresentationControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIPresentationController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeContent
import platform.UniformTypeIdentifiers.UTTypeFolder
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.loadFileRepresentationForContentType
import platform.darwin.NSObject
import kotlin.native.concurrent.ThreadLocal

/**
 * Wraps platform specific implementation for launching a
 * File Picker.
 *
 * @param initialDirectory Initial directory that the
 *  file picker should open to.
 * @param pickerMode [Mode] to open the picker with.
 *
 */
class FilePickerLauncher(
    private val initialDirectory: String?,
    private val pickerMode: Mode,
    private val multipleMode: Boolean,
    private val onFileSelected: FileSelected,
) {

    @ThreadLocal
    companion object {
        /**
         * For use only with launching plain (no compose dependencies)
         * file picker. When a function completes iOS deallocates
         * unreferenced objects created within it, so we need to
         * keep a reference of the active launcher.
         */
        internal var activeLauncher: FilePickerLauncher? = null
    }

    /**
     * Identifies the kind of file picker to open. Either
     * [Directory] or [File].
     */
    sealed interface Mode {
        /**
         * Use this mode to open a [FilePickerLauncher] for selecting
         * folders/directories.
         */
        data object Directory : Mode

        /**
         * Use this mode to open a [FilePickerLauncher] for selecting
         * files.
         *
         * @param extensions List of file extensions that can be
         *  selected on this file picker.
         */
        data class File(val extensions: List<String>) : Mode

        data object Images : Mode
    }

    private val pickerDelegate = object : NSObject(),
        UIDocumentPickerDelegateProtocol,
        UIAdaptivePresentationControllerDelegateProtocol,
        PHPickerViewControllerDelegateProtocol {

        override fun documentPicker(
            controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>
        ) {
            val files = didPickDocumentsAtURLs
                .filterIsInstance<NSURL>()
                .mapNotNull { it.path?.let { path -> IosFile(path, it) } }
            onFileSelected(files)
        }

        override fun documentPickerWasCancelled(
            controller: UIDocumentPickerViewController
        ) {
            onFileSelected(emptyList())
        }

        override fun presentationControllerWillDismiss(
            presentationController: UIPresentationController
        ) {
            (presentationController.presentedViewController as? UIDocumentPickerViewController)
                ?.let { documentPickerWasCancelled(it) }
        }

        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            CoroutineScope(Dispatchers.Main).launch {
                val files = mutableListOf<IosFile>()
                didFinishPicking
                    .filterIsInstance<PHPickerResult>()
                    .map {
                        val file = CompletableDeferred<IosFile>()
                        it.itemProvider
                            .loadFileRepresentationForContentType(
                                contentType = UTTypeContent,
                                openInPlace = false
                            ) { url, _, error ->
                                if (error == null && url != null) {
                                    file.complete(IosFile(url.toString(), url, it.itemProvider))
                                }
                            }
                        files += file.await()
                    }
                onFileSelected(files)
                picker.dismissViewControllerAnimated(true) {}
            }
        }
    }

    private val contentTypes: List<UTType>
        get() = when (pickerMode) {
            is Directory -> listOf(UTTypeFolder)
            is File -> pickerMode.extensions
                .mapNotNull { UTType.typeWithFilenameExtension(it) }
                .ifEmpty { listOf(UTTypeContent) }

            is Mode.Images -> listOf(UTTypeImage)
        }

    private fun createPicker(): UIViewController {
        return when (pickerMode) {
            is File, is Directory -> UIDocumentPickerViewController(
                forOpeningContentTypes = contentTypes
            ).apply {
                delegate = pickerDelegate
                initialDirectory?.let { directoryURL = NSURL(string = it) }
                allowsMultipleSelection = multipleMode
            }

            is Mode.Images -> {
                val configuration = PHPickerConfiguration().apply {
                    selectionLimit = if (multipleMode) 10 else 1
                }
                PHPickerViewController(configuration = configuration).apply {
                    delegate = pickerDelegate
                }
            }
        }
    }

    fun launchFilePicker() {
        activeLauncher = this

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            // Reusing a closed/dismissed picker causes problems with
            // triggering delegate functions, launch with a new one.
            viewControllerToPresent = createPicker(),
            animated = true,
            completion = null
        )
    }
}

