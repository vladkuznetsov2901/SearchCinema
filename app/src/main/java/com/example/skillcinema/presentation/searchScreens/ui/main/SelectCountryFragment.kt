package com.example.skillcinema.presentation.searchScreens.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.CountriesAdapter
import com.example.skillcinema.data.CountryFilter
import com.example.skillcinema.databinding.FragmentSelectCountryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectCountryFragment : Fragment() {

    private var _binding: FragmentSelectCountryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val viewModel: SearchViewModel by viewModels{searchViewModelFactory}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSelectCountryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countryListAdapter = CountriesAdapter()


        binding.countryRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.countryRecycler.adapter = countryListAdapter

        lifecycleScope.launch {
            val countries = viewModel.loadCountries()
            countryListAdapter.setOriginalList(countries)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryListAdapter.filter(newText.orEmpty())
                return true
            }

        })

        countryListAdapter.setOnItemClickListener(object : CountriesAdapter.OnItemClickListener {
            override fun onItemClick(country: CountryFilter) {
                val bundle = Bundle()
                bundle.putInt("countryId", country.id)
                bundle.putString("countryString", country.country)
                findNavController().navigate(
                    R.id.action_selectCountryFragment_to_settingsSearchFragment,
                    bundle
                )
            }

        })

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}