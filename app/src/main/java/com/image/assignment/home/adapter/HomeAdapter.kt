package com.image.assignment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.image.assignment.databinding.ItemMediaBinding
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.home.viewholder.MediaViewHolder
import kotlinx.coroutines.CoroutineScope

class HomeAdapter(
    private val context: Context,
    private var items: List<MediaResponse>,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<MediaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context))
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.onBind(context, items[position],scope)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
