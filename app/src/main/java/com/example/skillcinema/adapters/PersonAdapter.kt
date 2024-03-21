package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Person
import com.example.skillcinema.databinding.PersonItemBinding

class PersonAdapter : ListAdapter<Person, PersonAdapter.PersonViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            PersonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = getItem(position)
        with(holder.binding) {
            personName.text = person.nameRu
            personPart.text = person.description
            person?.let {
                Glide.with(personPhoto.context).load(it.posterUrl).into(personPhoto)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(person.staffId)
        }



    }

    interface OnItemClickListener {
        fun onItemClick(personId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class PersonViewHolder(val binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem.staffId == newItem.staffId

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem == newItem
    }
}