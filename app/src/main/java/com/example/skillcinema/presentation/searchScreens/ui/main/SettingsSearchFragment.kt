package com.example.skillcinema.presentation.searchScreens.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSettingsSearchBinding
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsSearchFragment : Fragment() {

    private var selectedSortChipId: Int = View.NO_ID
    private var selectedTypeChipId: Int = View.NO_ID
    private var isValuesLoaded: Boolean = false
    private var isLoadedFromArguments = false
    private var genreString = ""
    private var countryString = ""
    private var _binding: FragmentSettingsSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels { searchViewModelFactory }

    private lateinit var preferences: SharedPreferences

    private var countryId: Int? = 1
    private var genreId: Int? = 1
    var selectedMinValue = 0
    var selectedMaxValue = 10
    var selectedYearFrom: Int? = 1998
    var selectedYearUntil: Int? = 2017
    private var selectedSortChipName: String = "RATING"
    private var selectedTypeChipName: String = "FILM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = requireContext().getSharedPreferences("shared_pref", Context.MODE_APPEND)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsSearchBinding.inflate(layoutInflater)

        val savedSelectedYearFrom = retrieveFromSharedPreferences("selectedYearFrom", 1998)
        val savedSelectedYearUntil = retrieveFromSharedPreferences("selectedYearUntil", 2017)

        binding.yearChangeTextView.text = "с $savedSelectedYearFrom до $savedSelectedYearUntil"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isLoadedFromArguments) {
            loadValuesFromArguments()
            isLoadedFromArguments = true
        }

        binding.rangeSlider.valueFrom = 0f
        binding.rangeSlider.valueTo = 10f

        binding.rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            selectedMinValue = slider.values[0]?.toInt()!!
            selectedMaxValue = slider.values[1]?.toInt()!!

            binding.selectedMinValue.text = selectedMinValue.toString()
            binding.selectedMaxValue.text = selectedMaxValue.toString()
            saveToSharedPreferences("selectedMinValue", selectedMinValue)
            saveToSharedPreferences("selectedMaxValue", selectedMaxValue)
        })


        binding.chipAll.setOnClickListener {
            binding.chipAll.isChecked = true
            binding.chipFilms.isChecked = false
            binding.chipSerials.isChecked = false
            selectedTypeChipName = "ALL"
            selectedTypeChipId = binding.chipAll.id
            saveToSharedPreferences("selectedTypeChipName", selectedTypeChipName)
            saveToSharedPreferences("selectedTypeChipId", selectedTypeChipId)
        }

        binding.chipFilms.setOnClickListener {
            binding.chipAll.isChecked = false
            binding.chipFilms.isChecked = true
            binding.chipSerials.isChecked = false
            selectedTypeChipName = "FILM"
            selectedTypeChipId = binding.chipFilms.id

            saveToSharedPreferences("selectedTypeChipName", selectedTypeChipName)
            saveToSharedPreferences("selectedTypeChipId", selectedTypeChipId)

        }

        binding.chipSerials.setOnClickListener {
            binding.chipAll.isChecked = false
            binding.chipFilms.isChecked = false
            binding.chipSerials.isChecked = true
            selectedTypeChipName = "TV_SERIES"
            selectedTypeChipId = binding.chipSerials.id

            saveToSharedPreferences("selectedTypeChipName", selectedTypeChipName)
            saveToSharedPreferences("selectedTypeChipId", selectedTypeChipId)

        }

        binding.chipDate.setOnClickListener {
            binding.chipDate.isChecked = true
            binding.chipPopular.isChecked = false
            binding.chipRating.isChecked = false
            selectedSortChipName = "YEAR"
            selectedSortChipId = binding.chipDate.id

            saveToSharedPreferences("selectedSortChipName", selectedSortChipName)
            saveToSharedPreferences("selectedSortChipId", selectedSortChipId)

        }

        binding.chipPopular.setOnClickListener {
            binding.chipDate.isChecked = false
            binding.chipPopular.isChecked = true
            binding.chipRating.isChecked = false
            selectedSortChipName = "NUM_VOTE"
            selectedSortChipId = binding.chipPopular.id

            saveToSharedPreferences("selectedSortChipName", selectedSortChipName)
            saveToSharedPreferences("selectedSortChipId", selectedSortChipId)

        }

        binding.chipRating.setOnClickListener {
            binding.chipDate.isChecked = false
            binding.chipPopular.isChecked = false
            binding.chipRating.isChecked = true
            selectedSortChipName = "RATING"
            selectedSortChipId = binding.chipRating.id

            saveToSharedPreferences("selectedSortChipName", selectedSortChipName)
            saveToSharedPreferences("selectedSortChipId", selectedSortChipId)

        }

        checkTypeIdChips(binding.chipAll)
        checkTypeIdChips(binding.chipFilms)
        checkTypeIdChips(binding.chipSerials)
        checkSortIdChips(binding.chipDate)
        checkSortIdChips(binding.chipPopular)
        checkSortIdChips(binding.chipRating)

        binding.dontWatchedChip.setOnClickListener {
            saveToSharedPreferences("dontWatchedChip", binding.dontWatchedChip.isChecked)
        }

        binding.countryBtn.setOnClickListener {
            saveAllValuesToSharedPreferences()
            findNavController().navigate(R.id.action_settingsSearchFragment_to_selectCountryFragment)
        }

        binding.genreBtn.setOnClickListener {
            saveAllValuesToSharedPreferences()
            findNavController().navigate(R.id.action_settingsSearchFragment_to_selectGenreFragment)
        }

        binding.yearBtn.setOnClickListener {
            saveAllValuesToSharedPreferences()
            findNavController().navigate(R.id.action_settingsSearchFragment_to_searchYearPicker)
        }

        binding.backBtn.setOnClickListener {
            saveAllValuesToSharedPreferences()
            findNavController().navigate(R.id.action_settingsSearchFragment_to_searchFragment)
        }
    }

    private fun loadValuesFromArguments() {
        countryId =
            arguments?.getInt("countryId") ?: retrieveFromSharedPreferences("countryId", 1) as Int

        if (countryId == 0) {
            countryId = retrieveFromSharedPreferences("countryId", 1) as Int
        }

        Log.d("SETTINGSSSSSSSSSS", countryId.toString())
        countryString = arguments?.getString("countryString") ?: retrieveFromSharedPreferences(
            "countryString",
            "США"
        ).toString()

        genreId = arguments?.getInt("genreId") ?: retrieveFromSharedPreferences("genreId", 1) as Int

        if (genreId == 0) {
            genreId = retrieveFromSharedPreferences("genreId", 1) as Int
        }

        genreString = arguments?.getString("genreString") ?: retrieveFromSharedPreferences(
            "genreString",
            "Комедия"
        ).toString()

        selectedYearFrom = arguments?.getInt("selectedYearFrom")
            ?: retrieveFromSharedPreferences("selectedYearFrom", 1998) as Int
        selectedYearUntil = arguments?.getInt("selectedYearUntil") ?: retrieveFromSharedPreferences(
            "selectedYearUntil",
            2017
        ) as Int

        selectedMinValue = retrieveFromSharedPreferences("selectedMinValue", 0) as Int
        selectedMaxValue = retrieveFromSharedPreferences("selectedMaxValue", 10) as Int
        Log.d("SETTINGSSSSSSSSSS", selectedMinValue.toString())

        selectedSortChipName =
            retrieveFromSharedPreferences("selectedSortChipName", "RATING").toString()

        selectedTypeChipName =
            retrieveFromSharedPreferences("selectedTypeChipName", "FILM").toString()

        isValuesLoaded = true

        binding.rangeSlider.values = listOf(selectedMinValue.toFloat(), selectedMaxValue.toFloat())

        binding.selectedMinValue.text = selectedMinValue.toString()
        binding.selectedMaxValue.text = selectedMaxValue.toString()

        binding.rangeSlider.valueFrom = binding.rangeSlider.values[0]
        binding.rangeSlider.valueTo = binding.rangeSlider.values[1]

        binding.countryChangeTextView.text = countryString
        binding.genreChangeTextView.text = genreString
        binding.yearChangeTextView.text = "с $selectedYearFrom до $selectedYearUntil"

        binding.dontWatchedChip.isChecked =
            retrieveFromSharedPreferences("dontWatchedChip", false) as Boolean

    }


    private fun saveToSharedPreferences(key: String, value: Any) {
        val editor = preferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> {
                if (value != 0) {
                    editor.putInt(key, value)
                } else {
                    editor.putInt(key, 1)
                }
            }
        }
        editor.apply()
    }


    private fun retrieveFromSharedPreferences(key: String, defaultValue: Any): Any {
        return when (defaultValue) {
            is String -> preferences.getString(key, defaultValue) ?: defaultValue
            is Int -> preferences.getInt(key, defaultValue)
            else -> defaultValue
        }
    }

    private fun saveAllValuesToSharedPreferences() {
        if (countryId == 0 && genreId == 0) {
            countryId = 1
            genreId = 1
        }

        saveToSharedPreferences("countryId", countryId!!)
        saveToSharedPreferences("genreId", genreId!!)
        saveToSharedPreferences("countryString", countryString)
        saveToSharedPreferences("genreString", genreString)
        saveToSharedPreferences("selectedYearFrom", selectedYearFrom!!)
        saveToSharedPreferences("selectedYearUntil", selectedYearUntil!!)
        saveToSharedPreferences("selectedMinValue", selectedMinValue)
        saveToSharedPreferences("selectedMaxValue", selectedMaxValue)
        saveToSharedPreferences("selectedSortChipName", selectedSortChipName)
        saveToSharedPreferences("selectedSortChipId", selectedSortChipId)
        saveToSharedPreferences("selectedTypeChipName", selectedTypeChipName)
        saveToSharedPreferences("selectedTypeChipId", selectedTypeChipId)
        saveToSharedPreferences("dontWatchedChip", true)
    }

    private fun checkTypeIdChips(chip: Chip) {
        val savedTypeChipId = retrieveFromSharedPreferences("selectedTypeChipId", View.NO_ID)
        if (chip.id == savedTypeChipId) chip.isChecked = true

    }

    private fun checkSortIdChips(chip: Chip) {
        val savedSortChipId = retrieveFromSharedPreferences("selectedSortChipId", View.NO_ID)
        if (chip.id == savedSortChipId) chip.isChecked = true
        Log.d("CHIPPSPSPSPSP", chip.id.toString())

    }

}
