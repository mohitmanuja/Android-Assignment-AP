package com.image.assignment.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.image.assignment.databinding.ItemMediaBinding
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.home.viewholder.MediaViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class HomeAdapter(
    val context: Context,
    var items: List<MediaResponse>,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<MediaViewHolder>() {

    val jobHash: HashMap<Int, Job> = hashMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context))
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        jobHash[position]?.cancel()
        holder.onBind(context, items[position], position, jobHash,scope)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewDetachedFromWindow(holder: MediaViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.adapterPosition
        jobHash[position]?.cancel()
        jobHash.remove(position)
    }

}
