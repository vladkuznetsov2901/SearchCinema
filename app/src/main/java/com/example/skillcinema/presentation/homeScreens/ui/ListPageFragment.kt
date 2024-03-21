package com.example.skillcinema.presentation.homeScreens.ui

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
import com.example.skillcinema.adapters.MovieAdapter
import com.example.skillcinema.adapters.PersonAdapter
import com.example.skillcinema.adapters.SimilarMovieAdapter
import com.example.skillcinema.databinding.FragmentListPageBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListPageFragment : Fragment() {

    private var _binding: FragmentListPageBinding? = null
    private val binding get() = _binding!!


    private val personListAdapter = PersonAdapter()
    private val similarListAdapter = SimilarMovieAdapter()
    private var movieListAdapter = MovieAdapter()

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            profileViewModel.watchedMovies.collect { movies ->
                movieListAdapter.updateWatchedMovies(movies)
            }
        }



        binding.recyclerViewFull.layoutManager = GridLayoutManager(context, 2)

        binding.recyclerViewFull.adapter = movieListAdapter

        val title = arguments?.getString("Title")
        val movies = arguments?.getString("Movies")
        val filmID = arguments?.getInt("filmId")

        binding.title.text = title

        when (movies) {
            "moviesPremiers" -> {
                viewModel.pagedMoviesPremiers.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "moviesPopular" -> {
                viewModel.pagedMoviesPopular.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "moviesActionUSA" -> {
                viewModel.pagedByGenreMovies.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "moviesTop250" -> {
                viewModel.pagedMoviesTop250.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "moviesFranceDrams" -> {
                viewModel.pagedByGenreMovies.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "serials" -> {
                viewModel.pagedSerials.onEach {
                    movieListAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            "actors" -> {
                binding.recyclerViewFull.adapter = personListAdapter
                lifecycleScope.launch {
                    viewModel.loadPersons(filmID!!)
                    viewModel.personsInfo.collect { persons ->
                        val actors = persons.filter { it.professionKey == "ACTOR" }
                        personListAdapter.submitList(actors)
                    }
                }
            }

            "otherProfessions" -> {
                binding.recyclerViewFull.adapter = personListAdapter
                lifecycleScope.launch {
                    viewModel.loadPersons(filmID!!)
                    viewModel.personsInfo.collect { persons ->
                        val otherProfessions = persons.filter { it.professionKey != "ACTOR" }
                        personListAdapter.submitList(otherProfessions)
                    }
                }
            }

            "similarMovies" -> {
                binding.recyclerViewFull.adapter = similarListAdapter
                lifecycleScope.launch {
                    viewModel.loadSimilarMovies(filmID!!)
                    viewModel.similarMovies.collect { similarMovies ->
                        similarListAdapter.submitList(similarMovies)
                    }
                }
            }

        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        movieListAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_listPage_to_fragmentMoviePage, bundle)
            }
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}