package com.example.skillcinema.presentation.searchScreens.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.YearPickerAdapter
import com.example.skillcinema.data.YearItem
import com.example.skillcinema.databinding.FragmentSearchYearPickerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchYearPicker : Fragment() {
    private var selectedYearFrom: Int? = null
    private var selectedYearUntil: Int? = null


    private var _binding: FragmentSearchYearPickerBinding? = null
    private val binding get() = _binding!!

    private val yearList = generateYearList()
    private var visibleYearRange = IntRange(1998, 2009)

    private val adapter1 = YearPickerAdapter(emptyList())

    private val adapter2 = YearPickerAdapter(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchYearPickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter1
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        binding.recyclerView2.adapter = adapter2
        binding.recyclerView2.layoutManager = GridLayoutManager(context, 3)

        updateYearRange(adapter1)
        updateYearRange(adapter2)


        binding.btnNext.setOnClickListener {
            updateVisibleYearRange(leftButtonClicked = false, adapter1)
        }

        binding.btnPrev.setOnClickListener {
            updateVisibleYearRange(leftButtonClicked = true, adapter1)
        }

        binding.btnNext2.setOnClickListener {
            updateVisibleYearRange(leftButtonClicked = false, adapter2)
        }

        binding.btnPrev2.setOnClickListener {
            updateVisibleYearRange(leftButtonClicked = true, adapter2)
        }

        adapter1.setOnYearClickListener { selectedYearItem ->
            selectedYearFrom = selectedYearItem.year
            adapter1.setSelectedPosition(adapter1.years.indexOf(selectedYearItem))
        }



        adapter2.setOnYearClickListener { selectedYearItem ->
            selectedYearUntil = selectedYearItem.year
            adapter2.setSelectedPosition(adapter1.years.indexOf(selectedYearItem))
        }

        binding.choiceBtn.setOnClickListener {
            val bundle = Bundle()
            selectedYearFrom?.let { it1 -> bundle.putInt("selectedYearFrom", it1) }
            selectedYearUntil?.let { it1 -> bundle.putInt("selectedYearUntil", it1) }
            findNavController().navigate(
                R.id.action_searchYearPicker_to_settingsSearchFragment,
                bundle
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateYearRange(yearAdapter: YearPickerAdapter) {
        val filteredYears = yearList.filter { it.year in visibleYearRange }
        yearAdapter.years = filteredYears

        yearAdapter.notifyDataSetChanged()
    }


    private fun updateVisibleYearRange(leftButtonClicked: Boolean, yearAdapter: YearPickerAdapter) {
        val step = 11
        val rangeStart = if (leftButtonClicked) {
            visibleYearRange.first - (step + 1)
        } else {
            visibleYearRange.last + 1
        }
        val rangeEnd = rangeStart + 11

        visibleYearRange = IntRange(rangeStart, rangeEnd)

        updateYearRange(yearAdapter)
    }


    private fun generateYearList(): List<YearItem> {
        return (1890..2025).map { YearItem(it) }
    }


}
