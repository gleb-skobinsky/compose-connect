package com.chirrio.filepicker

import androidx.compose.ui.graphics.ImageBitmap

class ImageWithData(
    val id: String,
    val file: MPFile<Any>,
    val imageBitmap: ImageBitmap
)