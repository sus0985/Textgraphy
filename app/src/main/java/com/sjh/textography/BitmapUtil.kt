package com.sjh.textography

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri

object BitmapUtil {

    private const val BLOCK_SIZE = 512

    fun loadBitmapFromUri(uri: Uri, context: Context): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun splitImage(bitmap: Bitmap): List<Bitmap> {
        val blocks = mutableListOf<Bitmap>()
        val imageWidth = bitmap.width
        val imageHeight = bitmap.height
        val rows = imageHeight / BLOCK_SIZE + if (imageHeight % BLOCK_SIZE != 0) 1 else 0
        val cols = imageWidth / BLOCK_SIZE + if (imageWidth % BLOCK_SIZE != 0) 1 else 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val width = if (j == cols - 1 && imageWidth % BLOCK_SIZE != 0) imageWidth % BLOCK_SIZE else BLOCK_SIZE
                val height = if (i == rows - 1 && imageHeight % BLOCK_SIZE != 0) imageHeight % BLOCK_SIZE else BLOCK_SIZE

                val block = Bitmap.createBitmap(bitmap, j * BLOCK_SIZE, i * BLOCK_SIZE, width, height)
                blocks.add(block)
            }
        }
        return blocks
    }

    fun mergeImage(blocks: List<Bitmap>, width: Int, height: Int): Bitmap {
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)
        val paint = Paint()
        val rows = height / BLOCK_SIZE + if (height % BLOCK_SIZE != 0) 1 else 0
        val cols = width / BLOCK_SIZE + if (width % BLOCK_SIZE != 0) 1 else 0

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val block = blocks[i * cols + j]
                canvas.drawBitmap(block, j * BLOCK_SIZE.toFloat(), i * BLOCK_SIZE.toFloat(), paint)
            }
        }

        return result
    }

}