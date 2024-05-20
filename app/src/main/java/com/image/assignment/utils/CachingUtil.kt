package com.image.assignment.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CachingUtil {

    companion object {
        private const val TAG = "CachingUtil"
        private const val MAX_FILE_CACHE_SIZE_MB = 200 // Maximum cache size in MB

        val memoryCache: LruCache<String, Bitmap> by lazy {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            val cacheSize = maxMemory / 8
            LruCache<String, Bitmap>(cacheSize)
        }

        // Saving the image in internal storage (Original Size)
        fun storeBitmap(bitmap: Bitmap, context: Context, url: String) {
            val filename = urlToFilename(url)
            val cacheDir = context.cacheDir
            val file = File(cacheDir, filename)
            updateLastModified(file)
            try {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                manageCacheSize(cacheDir)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to store bitmap", e)
            }
        }

        // Loading the cached image from internal storage
        fun loadBitmap(context: Context, url: String): Bitmap? {
            val filename = urlToFilename(url)
            val file = File(context.cacheDir, filename)
            updateLastModified(file)
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
        fun downscaleBitmap(bitmap: Bitmap, viewWidth: Int): Bitmap {
            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()
            val newHeight = (viewWidth / aspectRatio).toInt()

            return Bitmap.createScaledBitmap(bitmap, viewWidth, newHeight, true)
        }

        private fun manageCacheSize(cacheDir: File) {
            val maxCacheSizeBytes = MAX_FILE_CACHE_SIZE_MB * 1024 * 1024
            var cacheSize = getCacheSize(cacheDir)
            val freeSpaceSize = (maxCacheSizeBytes / 100) * 60 // 60% of max size

            Log.d(TAG, "Current Cache Size in MB ${cacheSize / 1024 / 1024}")
            Log.d(TAG, "Current Cache Files ${cacheDir.listFiles()?.size}")
            Log.d(TAG, "Allowed Cache Size in MB $MAX_FILE_CACHE_SIZE_MB")

            if (cacheSize > maxCacheSizeBytes) {
                Log.d(TAG, "Need to reduce Cache Size")

                val files = cacheDir.listFiles()
                if (files != null) {
                    try {
                        files.sortWith(compareBy { it.lastModified() }) // Stable sorting
                        var deletedBytes = 0L

                        for (file in files) {
                            val fileSize = file.length()
                            if (file.delete()) {
                                deletedBytes += fileSize
                                cacheSize -= fileSize
                                if (cacheSize <= freeSpaceSize) {
                                    break
                                }
                            }
                        }
                        Log.d(TAG, "Reduced to in MB" + getCacheSize(cacheDir) / 1024 / 1024)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error while managing cache size: ${e.message}", e)
                    }
                }
            }
        }

        private fun getCacheSize(cacheDir: File): Long {
            return cacheDir.listFiles()?.sumOf { it.length() } ?: 0L
        }

        private fun updateLastModified(file: File) {
            file.setLastModified(System.currentTimeMillis())
        }
    }
}