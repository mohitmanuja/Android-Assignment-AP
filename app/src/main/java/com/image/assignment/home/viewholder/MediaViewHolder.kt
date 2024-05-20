package com.image.assignment.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.image.assignment.R
import com.image.assignment.databinding.ItemMediaBinding
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.library.ImageLoader
import kotlinx.coroutines.CoroutineScope

class MediaViewHolder(
    private val binding: ItemMediaBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(context: Context, item: MediaResponse?, scope: CoroutineScope) {
        item?.getThumbUrl()?.let { url ->
            ImageLoader.with(context).load(url).into(binding.itemImageview)
                .placeholder(R.drawable.placeholder).error(R.drawable.error).scope(scope)
                .downscale(200f, 150f).load()
        }
    }
}
