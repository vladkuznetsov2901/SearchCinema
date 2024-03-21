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
import com.example.skillcinema.adapters.GenresAdapter
import com.example.skillcinema.data.GenreFilter
import com.example.skillcinema.databinding.FragmentSelectGenreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectGenreFragment : Fragment() {

    private var _binding: FragmentSelectGenreBinding? = null
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
        _binding = FragmentSelectGenreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genreListAdapter = GenresAdapter()


        binding.genreRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.genreRecycler.adapter = genreListAdapter
        lifecycleScope.launch {
            val genres = viewModel.loadGenres()
            genreListAdapter.setOriginalList(genres)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                genreListAdapter.filter(newText.orEmpty())
                return true
            }

        })

        genreListAdapter.setOnItemClickListener(object : GenresAdapter.OnItemClickListener {
            override fun onItemClick(genre: GenreFilter) {
                val bundle = Bundle()
                bundle.putInt("genreId", genre.id)
                bundle.putString("genreString", genre.genre)
                findNavController().navigate(
                    R.id.action_selectGenreFragment_to_settingsSearchFragment,
                    bundle
                )
            }

        })

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}