package com.chirrio.filepicker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.downscale(): ImageBitmap {
    if (width < 1001 && height < 1001) return this
    val (newW, newH) = calculateDownscale(width, height)
    val scaled = Bitmap.createScaledBitmap(asAndroidBitmap(), newW, newH, true)
    return scaled.asImageBitmap()
}

actual fun imageBitmapFromArgb(
    rawArgbImageData: ByteArray,
    width: Int,
    height: Int
): ImageBitmap =
    BitmapFactory.decodeByteArray(rawArgbImageData, 0, rawArgbImageData.size).asImageBitmap()

actual fun ImageBitmap.toByteArray(): ByteArray =
    asAndroidBitmap().toByteArray()

fun Bitmap.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, baos)
    return baos.toByteArray()
}