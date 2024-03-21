package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.FullscreenImgItemBinding

class FullscreenImagesAdapter : RecyclerView.Adapter<FullscreenImagesAdapter.ImageViewHolder>() {

    private var imageUrls: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = FullscreenImgItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size

    fun submitList(images: List<String>) {
        imageUrls = images
        notifyDataSetChanged()
    }


    inner class ImageViewHolder(private val binding: FullscreenImgItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            Glide.with(binding.image)
                .load(imageUrl)
                .into(binding.image)
        }
    }


}
