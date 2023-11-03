package com.chirrio.filepicker

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import korlibs.image.bitmap.NativeImage
import korlibs.image.bitmap.resized
import korlibs.math.geom.Anchor
import korlibs.math.geom.ScaleMode

fun ImageBitmap.sharedDownscale(): ImageBitmap {
    if (width < 1001 && height < 1001) return this
    val (newW, newH) = calculateDownscale(width, height)
    return nativeScale(newW, newH)
}

internal fun ImageBitmap.nativeScale(width: Int, height: Int): ImageBitmap {
    val pixels: IntArray = toPixelMap().buffer
    val scaled = NativeImage(this.width, this.height, premultiplied = false)
    scaled.writePixelsUnsafe(x = 0, y = 0, this.width, this.height, pixels, offset = 0)
    scaled.resized(width, height, ScaleMode.COVER, Anchor.CENTER, native = true)
    val scaledPixels = scaled.readPixelsUnsafe(0, 0, scaled.width, scaled.height)
    return imageBitmapFromArgb(scaledPixels, scaled.width, scaled.height)
}

private const val BYTES_PER_PIXEL = 4

private fun imageBitmapFromArgb(
    rawArgbImageData: IntArray,
    width: Int,
    height: Int
): ImageBitmap {
    val pixels = rawArgbImageData.toByteArray(width, height)
    return imageBitmapFromArgb(pixels, width, height)
}

fun IntArray.toByteArray(width: Int, height: Int): ByteArray {
    val pixels = ByteArray(width * height * BYTES_PER_PIXEL)

    var k = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val argb = this[y * width + x]
            val a = (argb shr 24) and 0xff
            val r = (argb shr 16) and 0xff
            val g = (argb shr 8) and 0xff
            val b = (argb shr 0) and 0xff
            pixels[k++] = b.toByte()
            pixels[k++] = g.toByte()
            pixels[k++] = r.toByte()
            pixels[k++] = a.toByte()
        }
    }
    return pixels
}