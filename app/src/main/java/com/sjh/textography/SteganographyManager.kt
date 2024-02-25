package com.sjh.textography

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SteganographyManager {

    private val startMessageConstant = "*&^"
    private val endMessageConstant = "^&*"

    private val _progressFlow = MutableStateFlow(false)
    val progressFlow = _progressFlow.asStateFlow()

    suspend fun embedTextIntoBitmap(bitmap: Bitmap, text: String): Bitmap {
        _progressFlow.emit(true)
        val embeddedText = startMessageConstant + text + endMessageConstant

        val byte = embeddedText.toByteArray()

        val embeddedBitmaps = mutableListOf<Bitmap>()
        val blocks = BitmapUtil.splitImage(bitmap)
        blocks.forEach { block ->
            val embeddedBitmap = embedTextIntoBitmapBlock(block, text)
            embeddedBitmaps.add(embeddedBitmap)
        }
        _progressFlow.emit(false)

        return BitmapUtil.mergeImage(embeddedBitmaps, bitmap.width, bitmap.height)
    }

    private fun embedTextIntoBitmapBlock(block: Bitmap, message: String): Bitmap {
        val embeddedBitmap = Bitmap.createBitmap(block.width, block.height, Bitmap.Config.ARGB_8888)

        val density = block.density

        var messageIndex = 0

        for (y in 0 until block.height) {
            for (x in 0 until block.width) {
                val pixel = block.getPixel(x, y)

                val alpha = (pixel shr 24) and 0xFF
                var red = (pixel shr 16) and 0xFF
                var green = (pixel shr 8) and 0xFF
                var blue = pixel and 0xFF

                if (messageIndex < message.length) {
                    val bit = (message[messageIndex].code shr 7 and 1 - messageIndex % 8) and 1

                    red = red and 0xFE or bit
                    green = green and 0xFE or bit
                    blue = blue and 0xFE or bit

                    messageIndex++
                }

                embeddedBitmap.setPixel(
                    x,
                    y,
                    (alpha shl 24) or (red shl 16) or (green shl 8) or blue
                )
            }
        }

        embeddedBitmap.density = density

        return embeddedBitmap
    }

    companion object {
        private const val TAG = "SteganographyManager"
    }

}