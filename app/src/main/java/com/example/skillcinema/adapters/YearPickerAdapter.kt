package com.example.skillcinema.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.data.YearItem
import com.example.skillcinema.databinding.ItemYearBinding

class YearPickerAdapter(
    var years: List<YearItem>,
) : RecyclerView.Adapter<YearPickerAdapter.YearViewHolder>() {

    private var onYearClickListener: ((YearItem) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class YearViewHolder(private val binding: ItemYearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(yearItem: YearItem) {
            binding.yearTextView.text = yearItem.year.toString()

            val backgroundResId =
                if (yearItem.isSelected) R.drawable.selector_year_item_selected else R.drawable.selector_year_item
            binding.root.setBackgroundResource(backgroundResId)

            binding.root.setOnClickListener {
                yearItem.isSelected = !yearItem.isSelected
                notifyItemChanged(adapterPosition)

                onYearClickListener?.invoke(yearItem)
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
        }
    }

    fun setOnYearClickListener(listener: (YearItem) -> Unit) {
        this.onYearClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val yearItem = years[position]
        holder.bind(yearItem)
    }

    override fun getItemCount(): Int {
        return years.size
    }
}

