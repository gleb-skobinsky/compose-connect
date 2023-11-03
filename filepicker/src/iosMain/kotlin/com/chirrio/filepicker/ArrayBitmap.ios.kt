package com.chirrio.filepicker

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ImageInfo

actual suspend fun ImageBitmap.downscale(): ImageBitmap = sharedDownscale()

actual fun imageBitmapFromArgb(rawArgbImageData: ByteArray, width: Int, height: Int): ImageBitmap {
    val bitmap = Bitmap()
    bitmap.allocPixels(ImageInfo.makeS32(width, height, ColorAlphaType.UNPREMUL))
    bitmap.installPixels(rawArgbImageData)
    return bitmap.asComposeImageBitmap()
}