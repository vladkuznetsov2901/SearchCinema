package com.example.skillcinema.presentation.homeScreens.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.databinding.SampleFilmsViewBinding

class FilmsView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = SampleFilmsViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }


    fun getRecycler(): RecyclerView {
        return binding.recycler
    }

    fun getButton(): AppCompatButton {
        return binding.buttonAll
    }

}