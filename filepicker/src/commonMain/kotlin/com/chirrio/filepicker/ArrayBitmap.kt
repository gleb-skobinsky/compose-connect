package com.chirrio.filepicker

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(context: Any, file: MPFile<Any>): ImageBitmap

expect fun ImageBitmap.toByteArray(): ByteArray

expect fun ImageBitmap.downscale(): ImageBitmap

expect fun imageBitmapFromArgb(
    rawArgbImageData: ByteArray,
    width: Int,
    height: Int
): ImageBitmap

fun calculateDownscale(width: Int, height: Int, target: Int = 1000): Pair<Int, Int> {
    val maxSide = maxOf(width, height)
    val ratio = target.toFloat() / maxSide
    val newWidth = (width * ratio).toInt()
    val newHeight = (height * ratio).toInt()
    return newWidth to newHeight
}