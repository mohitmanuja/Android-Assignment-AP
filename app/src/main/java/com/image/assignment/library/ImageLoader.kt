package com.image.assignment.library

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.image.assignment.R
import com.image.assignment.utils.CachingUtil
import com.image.assignment.utils.CustomDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ImageLoader private constructor(
    private val context: Context,
    private val imageView: ImageView,
    private val url: String?,
    private val placeholderResId: Int,
    private val errorResId: Int,
    private val scope: CoroutineScope,
    private val downscaleOptions: DownscaleOptions?
) {

    companion object {
        private val jobHash: HashMap<Int, Job> = hashMapOf()

        fun with(context: Context): Builder {
            return Builder(context)
        }
        fun cancelJob(imageview: ImageView) {
            if (jobHash.containsKey(imageview.hashCode())){
                Log.d(TAG, "cancelling job : job status: "+ jobHash[imageview.hashCode()]?.isCompleted)
            }
            jobHash[imageview.hashCode()]?.cancel()
            jobHash.remove(imageview.hashCode())
        }

        private const val TAG = "ImageLoader"
    }
    init {
        setupDetachListener()
    }

    private fun setupDetachListener() {
        imageView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                // Do nothing when view is attached
            }

            override fun onViewDetachedFromWindow(v: View?) {
                // Cancel any previous job for this ImageView
                cancelJob(imageView)
            }
        })
    }

    fun load() {
        // check any previous job for this ImageView
        jobHash[imageView.hashCode()]?.cancel()

        // Set placeholder
        imageView.setImageResource(placeholderResId)

        // Start loading image
        url?.let {
            val job: Job = scope.launch(CustomDispatchers.IO) {
                val bitmap = fetchImage(context, it)
                withContext(Dispatchers.Main) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                    } else {
                        imageView.setImageResource(errorResId)
                    }
                }
            }
            jobHash[imageView.hashCode()] = job
        }
    }

    // doing all tasks on background thread
    private suspend fun fetchImage(context: Context, url: String): Bitmap? {
        // Check memory cache on the main thread
        val requiredWidth = downscaleOptions?.widthPx ?: -1
        val cachedBitmap = withContext(Dispatchers.Main) {
            CachingUtil.memoryCache.get(url)
        }
        if (cachedBitmap != null) {
            val cachedWidth = cachedBitmap.width
            Log.d(TAG, "Cached Bitmap Found, Height -> $cachedWidth, required -> $requiredWidth")
            if (requiredWidth <= 0 || requiredWidth <= cachedWidth) {
                Log.d(TAG, "Returned from cache memory")
                return@fetchImage cachedBitmap
            } else {
                Log.d(TAG, "Not matching")
            }
        } else {
            Log.d(TAG, "Cached Not Found")
        }

        // Continue the rest of the operations in the background thread
        return withContext(CustomDispatchers.IO) {
            // Check disk cache
            val diskBitmap = CachingUtil.loadBitmap(context, url)
            if (diskBitmap != null) {
                // Insert into memory cache if not already present
                val downscaledBitmap = downscaleOptions?.let {
                    CachingUtil.downscaleBitmap(diskBitmap, it.widthPx)
                } ?: diskBitmap
                Log.d(TAG, "Returned from disk memory")

                CachingUtil.memoryCache.put(url, downscaledBitmap)
                return@withContext downscaledBitmap
            }

            // Download from network
            try {
                val bitmap = CachingUtil.getBitmapFromURL(url)
                if (bitmap != null) {
                    val downscaledBitmap = downscaleOptions?.let {
                        CachingUtil.downscaleBitmap(bitmap, it.widthPx)
                    } ?: bitmap

                    CachingUtil.storeBitmap(bitmap, context, url)
                    CachingUtil.memoryCache.put(url, downscaledBitmap)
                    return@withContext downscaledBitmap
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            }
        }
    }

    class Builder(private val context: Context) {
        private lateinit var imageView: ImageView
        private var url: String? = null
        private var placeholderResId: Int = R.drawable.placeholder
        private var errorResId: Int = R.drawable.error
        private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        private var downscaleOptions: DownscaleOptions? = null

        fun load(url: String): Builder {
            this.url = url
            return this
        }

        fun into(imageView: ImageView): Builder {
            this.imageView = imageView
            return this
        }

        fun placeholder(@DrawableRes placeholderResId: Int): Builder {
            this.placeholderResId = placeholderResId
            return this
        }

        fun error(@DrawableRes errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        fun scope(scope: CoroutineScope): Builder {
            this.scope = scope
            return this
        }

        fun downscale(widthDp: Float, heightDp: Float): Builder {
            this.downscaleOptions = DownscaleOptions(widthDp, heightDp)
            return this
        }

        private fun build(): ImageLoader {
            return ImageLoader(
                context,
                imageView,
                url,
                placeholderResId,
                errorResId,
                scope,
                downscaleOptions
            )
        }

        fun load() {
            build().load()
        }
    }

}
