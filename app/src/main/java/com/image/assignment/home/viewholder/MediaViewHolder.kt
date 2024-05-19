package com.image.assignment.home.viewholder

import android.content.Context
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.image.assignment.R
import com.image.assignment.databinding.ItemMediaBinding
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.utils.CachingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewHolder(
    private val binding: ItemMediaBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        context: Context,
        item: MediaResponse?,
        position: Int,
        jobHash: HashMap<Int, Job>,
        scope: CoroutineScope
    ) {
        // Cancel any previous job for this position
        jobHash[position]?.cancel()

        val imageView = binding.itemImageview
        imageView.setImageResource(R.drawable.placeholder) // Set a placeholder

        val job: Job = scope.launch {
            val bitmap = fetchImage(context, item?.getThumbUrl() ?: "")
            withContext(Dispatchers.Main) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    imageView.setImageResource(R.drawable.error) // Error placeholder
                }
            }
        }
        jobHash[position] = job
    }

    private suspend fun fetchImage(context: Context, url: String): Bitmap? {
        // Check memory cache first
        val cachedBitmap = CachingUtil.memoryCache.get(url)
        if (cachedBitmap != null) {
            return cachedBitmap
        }

        // Check disk cache
        val diskBitmap = CachingUtil.loadBitmap(context, url)
        if (diskBitmap != null) {
            CachingUtil.memoryCache.put(url, diskBitmap) // Insert into memory cache here
            return diskBitmap
        }

        // Download from network
        return withContext(Dispatchers.IO) {
            val bitmap = CachingUtil.getBitmapFromURL(url)
            if (bitmap != null) {
                CachingUtil.storeBitmap(bitmap, context, url)
                CachingUtil.memoryCache.put(url, bitmap)
            }
            bitmap
        }
    }
}
