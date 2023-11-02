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
    val pixels: IntArray = this.toPixelMap().buffer
    val scaled = NativeImage(width, height, premultiplied = false)
    scaled.writePixelsUnsafe(x = 0, y = 0, width, height, pixels, offset = 0)
    scaled.resized(width, height, ScaleMode.COVER, Anchor.CENTER, native = true)

    val scaledPixels = scaled.readPixelsUnsafe(0, 0, scaled.width, scaled.height)
    return imageBitmapFromArgb(scaledPixels, width, height)
}

private fun imageBitmapFromArgb(rawArgbImageData: IntArray, width: Int, height: Int): ImageBitmap {
    val bytesPerPixel = 4
    val pixels = ByteArray(width * height * bytesPerPixel)

    var k = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val argb = rawArgbImageData[y * width + x]
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

    return imageBitmapFromArgb(pixels, width, height)
}

fun ImageBitmap.sharedByteArray(): ByteArray {
    val bytesPerPixel = 4
    val pixels: IntArray = this.toPixelMap().buffer
    val nativeImage = NativeImage(width, height, premultiplied = false)
    nativeImage.writePixelsUnsafe(x = 0, y = 0, width, height, pixels, offset = 0)
    val rawData = nativeImage.readPixelsUnsafe(0, 0, nativeImage.width, nativeImage.height)
    val output = ByteArray(width * height * bytesPerPixel)
    var k = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val argb = rawData[y * width + x]
            val a = (argb shr 24) and 0xff
            val r = (argb shr 16) and 0xff
            val g = (argb shr 8) and 0xff
            val b = (argb shr 0) and 0xff
            output[k++] = b.toByte()
            output[k++] = g.toByte()
            output[k++] = r.toByte()
            output[k++] = a.toByte()
        }
    }
    return output
}