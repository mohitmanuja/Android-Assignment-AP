package com.image.assignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class CachingUtil {

    companion object {
        val memoryCache: LruCache<String, Bitmap> = LruCache(calculateMemoryCacheSize())

        // Calculate cache size as 1/8th of the available memory
        private fun calculateMemoryCacheSize(): Int {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            return maxMemory / 8
        }

        // Saving the image in internal storage
        fun storeBitmap(bitmap: Bitmap, context: Context, url: String) {
            val filename = urlToFilename(url)
            val file = File(context.cacheDir, filename)
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
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

    }
}