package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Movie
import com.example.skillcinema.databinding.PersonMovieItemBinding

class MoviePersonAdapter : ListAdapter<Movie, MoviePersonAdapter.MovieViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            PersonMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            movieTitle.text = item.nameRu
            movieGenreAndDate.text = item.year.toString() + ", " + item.genres.joinToString(", ") { it.genre }
            movieRate.text = item.ratingKinopoisk.toString()
            item?.let {
                Glide.with(moviePoster.context).load(it.posterUrlPreview).into(moviePoster)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item.kinopoiskId)
        }



    }

    interface OnItemClickListener {
        fun onItemClick(itemID: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MovieViewHolder(val binding: PersonMovieItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }
}