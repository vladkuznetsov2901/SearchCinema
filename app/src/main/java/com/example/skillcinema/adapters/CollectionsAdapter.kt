package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.databinding.CollectionItemBinding

class CollectionsAdapter : ListAdapter<CollectionDB, CollectionsAdapter.MovieViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            CollectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            collectionText.text = item.name
            collectionBtn.text = (item.movieIds.size - 1).toString()
            deleteBtn.setOnClickListener {
                onItemClickListener?.onDeleteClick(item.name)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item.name)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(itemID: String)
        fun onDeleteClick(collectionName: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setOnDeleteClickListener(listener: OnItemClickListener) {
        onDeleteClickListener = listener
    }

    class MovieViewHolder(val binding: CollectionItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<CollectionDB>() {
        override fun areItemsTheSame(oldItem: CollectionDB, newItem: CollectionDB): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CollectionDB, newItem: CollectionDB): Boolean =
            oldItem == newItem
    }
}