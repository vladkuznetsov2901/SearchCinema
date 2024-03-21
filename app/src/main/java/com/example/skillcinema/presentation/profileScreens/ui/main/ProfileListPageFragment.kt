package com.example.skillcinema.presentation.profileScreens.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.MoviePersonAdapter
import com.example.skillcinema.databinding.FragmentProfileListPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileListPageFragment : Fragment() {

    private var _binding: FragmentProfileListPageBinding? = null
    private val binding get() = _binding!!

    private var movieListAdapter = MoviePersonAdapter()


    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileListPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFull.layoutManager = GridLayoutManager(context, 2)

        binding.recyclerViewFull.adapter = movieListAdapter

        val collectionName = arguments?.getString("collectionName")
        binding.title.text = collectionName


        viewLifecycleOwner.lifecycleScope.launch {
            val movies = collectionName?.let {
                viewModel.getMoviesFromCollection(it)
                viewModel.movies.observe(viewLifecycleOwner) { updatedMovies ->
                    movieListAdapter.submitList(updatedMovies)
                }
            }
        }

        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }

        movieListAdapter.setOnItemClickListener(
            object : MoviePersonAdapter.OnItemClickListener {
                override fun onItemClick(itemID: Int) {
                    val bundle = Bundle()
                    bundle.putInt("itemID", itemID)
                    findNavController().navigate(R.id.fragmentMoviePage, bundle)
                }
            })


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}