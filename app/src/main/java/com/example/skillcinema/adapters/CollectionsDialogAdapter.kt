package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.databinding.ItemCollectionsListBinding

class CollectionsDialogAdapter :
    ListAdapter<CollectionDB, CollectionsDialogAdapter.MovieViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemCollectionsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            insertIntoCollectionText.text = item.name
            collectionsCnt.text = (item.movieIds.size - 1).toString()
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item.name)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(itemID: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MovieViewHolder(val binding: ItemCollectionsListBinding) :
        RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<CollectionDB>() {
        override fun areItemsTheSame(oldItem: CollectionDB, newItem: CollectionDB): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CollectionDB, newItem: CollectionDB): Boolean =
            oldItem == newItem
    }
}