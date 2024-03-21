package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Movie
import com.example.skillcinema.databinding.DeleteHistoryButtonBinding
import com.example.skillcinema.databinding.MovieItemBinding

class ProfileMovieWantToWatchedAdapter : ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private var onClearAllClickListener: OnClearAllClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> {
                val binding =
                    MovieItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                MovieViewHolder(binding)
            }

            TYPE_CLEAR_ALL_BUTTON -> {
                val binding =
                    DeleteHistoryButtonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                ClearHistoryViewHolder(binding, onClearAllClickListener)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MovieViewHolder -> {
                with(holder.binding) {
                    movieTitle.text = item?.nameRu ?: "Error"
                    movieGenre.text = item?.genres?.joinToString(", ") { it.genre } ?: "Error"
                    movieRate.text = item?.ratingKinopoisk.toString()
                    item?.let {
                        Glide.with(moviePoster.context).load(it.posterUrlPreview).into(moviePoster)
                    }
                }

            }

            is ClearHistoryViewHolder -> {
                with(holder.binding) {
                    deleteHistoryBtn.visibility =
                        if (position == itemCount - 1) View.VISIBLE else View.GONE
                    deleteHistoryText.visibility =
                        if (position == itemCount - 1) View.VISIBLE else View.GONE
                }
            }

        }

        holder.itemView.setOnClickListener {
            item?.let { it1 -> onItemClickListener?.onItemClick(it1.kinopoiskId) }
        }


    }

    interface OnItemClickListener {
        fun onItemClick(itemID: Int)
    }

    interface OnClearAllClickListener {
        fun onClearAllClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnClearAllClickListener(listener: OnClearAllClickListener) {
        onClearAllClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            TYPE_CLEAR_ALL_BUTTON
        } else {
            TYPE_MOVIE
        }
    }

    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)
    class ClearHistoryViewHolder(
        val binding: DeleteHistoryButtonBinding,
        private val onClearAllClickListener: OnClearAllClickListener?,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteHistoryBtn.setOnClickListener {
                onClearAllClickListener?.onClearAllClick()
            }
        }
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.kinopoiskId == newItem.kinopoiskId

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_CLEAR_ALL_BUTTON = 1
    }
}