package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.CountryFilter
import com.example.skillcinema.databinding.FiltersItemBinding

class CountriesAdapter :
    ListAdapter<CountryFilter, CountriesAdapter.FiltersViewHolder>(DiffUtilCallback()) {

    private var onItemClickListener: OnItemClickListener? = null
    private var originalList: List<CountryFilter> = emptyList()

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
            countryTextView.text = item.country
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }

    }


    interface OnItemClickListener {
        fun onItemClick(country: CountryFilter)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class FiltersViewHolder(val binding: FiltersItemBinding) : RecyclerView.ViewHolder(binding.root)


    class DiffUtilCallback : DiffUtil.ItemCallback<CountryFilter>() {
        override fun areItemsTheSame(oldItem: CountryFilter, newItem: CountryFilter): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CountryFilter, newItem: CountryFilter): Boolean =
            oldItem == newItem

    }

    fun filter(query: String) {
        val filteredList = originalList.filter {
            it.country.contains(query, ignoreCase = true)
        }
        submitList(filteredList)
    }

    fun setOriginalList(list: List<CountryFilter>) {
        originalList = list
        submitList(list)
    }
}