package com.example.skillcinema.presentation.searchScreens.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.MoviePersonAdapter
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var selectedTypeChipName = "ALL"
    private var selectedSortChipName = "RATING"
    private var savedSelectedYearUntil = 2008
    private var savedSelectedYearFrom = 1998
    private var savedSelectedMinValue = 0
    private var savedSelectedMaxValue = 10
    private var savedGenreId = 1
    private var savedCountryId = 1
    private var dontWatched = false

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var movieListAdapter = MoviePersonAdapter()

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels { searchViewModelFactory }

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences =
            requireContext().getSharedPreferences("shared_pref", Context.MODE_APPEND)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSearchBinding.inflate(layoutInflater)

        savedCountryId = retrieveFromSharedPreferences("countryId", 1) as Int
        savedGenreId = retrieveFromSharedPreferences("genreId", 1) as Int
        savedSelectedYearFrom = retrieveFromSharedPreferences("selectedYearFrom", 1998) as Int
        savedSelectedYearUntil = retrieveFromSharedPreferences("selectedYearUntil", 2017) as Int
        savedSelectedMinValue = retrieveFromSharedPreferences("selectedMinValue", 0) as Int
        Log.d("SEARCHHHHHHHHHHHH", savedSelectedMinValue.toString())

        savedSelectedMaxValue = retrieveFromSharedPreferences("selectedMaxValue", 10) as Int
        selectedTypeChipName =
            retrieveFromSharedPreferences("selectedTypeChipName", "ALL").toString()
        Log.d("SEARCHHHHHHHHHHHH", selectedTypeChipName)

        selectedSortChipName =
            retrieveFromSharedPreferences("selectedSortChipName", "RATING").toString()
        Log.d("SEARCHHHHHHHHHHHH", selectedSortChipName)
        dontWatched = retrieveFromSharedPreferences("dontWatchedChip", false) as Boolean

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFull.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerViewFull.adapter = movieListAdapter


        lifecycleScope.launch {


            viewModel.getMoviesByParameters(
                arrayOf(savedCountryId),
                arrayOf(savedGenreId),
                selectedSortChipName,
                selectedTypeChipName,
                savedSelectedMinValue,
                savedSelectedMaxValue,
                savedSelectedYearFrom,
                savedSelectedYearUntil
            )
            profileViewModel.getWatchedMovies()

            viewModel.moviesByParameters.collect { moviesByParameters ->
                val filteredMovies = moviesByParameters


                movieListAdapter.submitList(filteredMovies)

                binding.nothing.visibility = if (filteredMovies.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                binding.progressBar3.visibility = View.GONE
            }

        }

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                lifecycleScope.launch {
                    viewModel.getMoviesByKeyword(p0.toString())

                    viewModel.moviesByParameters.collect { moviesByParameters ->

                        val filteredBySearch = moviesByParameters.filter {
                            it.nameRu?.contains(p0.toString(), true) == true
                        }


                        movieListAdapter.submitList(filteredBySearch)

                        if (filteredBySearch.isEmpty()) {
                            binding.nothing.visibility = View.VISIBLE
                        } else {
                            binding.nothing.visibility = View.GONE
                        }
                    }
                }
                return true
            }
        })


        movieListAdapter.setOnItemClickListener(object : MoviePersonAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(
                    R.id.fragmentMoviePage,
                    bundle
                )
            }
        })





        binding.setFiltersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_settingsSearchFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun retrieveFromSharedPreferences(key: String, defaultValue: Any): Any {
        return when (defaultValue) {
            is String -> preferences.getString(key, defaultValue) ?: defaultValue
            is Int -> preferences.getInt(key, defaultValue)
            else -> defaultValue
        }
    }

}