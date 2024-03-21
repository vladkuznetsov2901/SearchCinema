package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.GenreFilter
import com.example.skillcinema.databinding.FiltersItemBinding

class GenresAdapter :
    ListAdapter<GenreFilter, GenresAdapter.FiltersViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private var originalList: List<GenreFilter> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder {
        return FiltersViewHolder(
            FiltersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: FiltersViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            countryTextView.text = item.genre
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(genre: GenreFilter)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class FiltersViewHolder(val binding: FiltersItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<GenreFilter>() {
        override fun areItemsTheSame(oldItem: GenreFilter, newItem: GenreFilter): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GenreFilter, newItem: GenreFilter): Boolean =
            oldItem == newItem
    }

    fun filter(query: String) {
        val filteredList = originalList.filter {
            it.genre.contains(query, ignoreCase = true)
        }
        submitList(filteredList)
    }

    fun setOriginalList(list: List<GenreFilter>) {
        originalList = list
        submitList(list)
    }
}