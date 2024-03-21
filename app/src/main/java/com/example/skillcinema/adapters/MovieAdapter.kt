package com.example.skillcinema.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Movie
import com.example.skillcinema.databinding.MovieItemBinding
import com.example.skillcinema.databinding.ShowAllButtonBinding
import kotlinx.coroutines.flow.MutableStateFlow

class MovieAdapter() :
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private val watchedMovies = MutableStateFlow<List<Movie>>(emptyList())
    private var onShowAllClickListener: OnShowAllClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> {
                val binding = MovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieViewHolder(binding)
            }

            TYPE_SHOW_ALL_BUTTON -> {
                val binding = ShowAllButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShowAllButtonViewHolder(binding, onShowAllClickListener)
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
                    if (watchedMovies.value.any { it.kinopoiskId == item!!.kinopoiskId }) {
                            watchedGradient.visibility = View.VISIBLE
                            watchedIcon.visibility = View.VISIBLE
                    } else {
                        moviePoster.setBackgroundResource(0)
                        watchedIcon.visibility = View.GONE
                    }
                }

                holder.itemView.setOnClickListener {
                    Log.d("MovieAdapter", item?.kinopoiskId.toString())
                    if (item != null) {
                        onItemClickListener?.onItemClick(item.kinopoiskId)
                    }
                }
            }

            is ShowAllButtonViewHolder -> {
                with(holder.binding) {
                    showAllButton.visibility =
                        if (position == itemCount - 1) View.VISIBLE else View.GONE
                    showAllText.visibility =
                        if (position == itemCount - 1) View.VISIBLE else View.GONE
                }

            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            TYPE_SHOW_ALL_BUTTON
        } else {
            TYPE_MOVIE
        }
    }

    interface OnItemClickListener {
        fun onItemClick(itemID: Int)
    }

    interface OnShowAllClickListener {
        fun onShowAllClick()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnShowAllClickListener(listener: OnShowAllClickListener) {
        onShowAllClickListener = listener
    }

    fun updateWatchedMovies(movies: List<Movie>) {
        watchedMovies.value = movies
        notifyDataSetChanged()
    }


    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)
    class ShowAllButtonViewHolder(
        val binding: ShowAllButtonBinding,
        private val onShowAllClickListener: OnShowAllClickListener?,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.showAllButton.setOnClickListener {
                onShowAllClickListener?.onShowAllClick()
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
        private const val TYPE_SHOW_ALL_BUTTON = 1
    }
}