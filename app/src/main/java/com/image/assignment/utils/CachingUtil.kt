package com.image.assignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import com.image.assignment.utils.Util.dpToPx
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class CachingUtil {

    companion object {
        val memoryCache: LruCache<String, Bitmap> = LruCache(calculateMemoryCacheSize())
        const val VIEW_WIDTH_DP = 250f
        const val VIEW_HEIGHT_DP = 150f

        // Calculate cache size as 1/8th of the available memory
        private fun calculateMemoryCacheSize(): Int {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            return maxMemory / 8
        }

        // Saving the image in internal storage
        fun storeBitmap(bitmap: Bitmap, context: Context, url: String) {
            val viewWidthPx = dpToPx(VIEW_WIDTH_DP)
            val viewHeightPx = dpToPx(VIEW_HEIGHT_DP)
            val filename = urlToFilename(url)
            val scaledBitmap = downscaleBitmap(bitmap, viewWidthPx, viewHeightPx)
            val file = File(context.cacheDir, filename)
            FileOutputStream(file).use {
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

        // Loading the cached image from internal storage
        fun loadBitmap(context: Context, url: String): Bitmap? {
            val filename = urlToFilename(url)
            val file = File(context.cacheDir, filename)
            return if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        }

        // Download an image from the URL
        fun getBitmapFromURL(url: String): Bitmap? {
            return try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                BitmapFactory.decodeStream(connection.inputStream)
            } catch (e: Exception) {
                null
            }
        }

        private fun urlToFilename(url: String): String {
            return url.hashCode().toString()
        }

        // Downscale the bitmap
        private fun downscaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {

            val width = bitmap.width
            val height = bitmap.height

            val scaleFactor = Math.min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

            val scaledWidth = (width * scaleFactor).toInt()
            val scaledHeight = (height * scaleFactor).toInt()

            return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
        }

    }
}