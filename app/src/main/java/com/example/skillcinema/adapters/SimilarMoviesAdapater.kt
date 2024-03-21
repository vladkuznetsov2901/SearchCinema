package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.SimilarMovie
import com.example.skillcinema.databinding.SimilarMovieItemBinding

class SimilarMovieAdapter : ListAdapter<SimilarMovie, SimilarMovieAdapter.SimilarMovieViewHolder>(
    DiffUtilCallback()
) {

    private var onItemClickListener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        return SimilarMovieViewHolder(
            SimilarMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            movieTitle.text = item.nameRu
            item?.let {
                Glide.with(moviePoster.context).load(it.posterUrlPreview).into(moviePoster)
            }

        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item.filmId)
        }



    }

    interface OnItemClickListener {
        fun onItemClick(itemID: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class SimilarMovieViewHolder(val binding: SimilarMovieItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<SimilarMovie>() {
        override fun areItemsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean =
            oldItem.filmId == newItem.filmId

        override fun areContentsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean =
            oldItem == newItem
    }
}