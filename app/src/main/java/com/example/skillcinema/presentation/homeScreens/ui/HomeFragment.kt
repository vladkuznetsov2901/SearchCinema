package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.adapters.MovieAdapter
import com.example.skillcinema.databinding.FragmentHomeBinding
import com.example.skillcinema.databinding.MovieItemBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val _movieItemBinding: MovieItemBinding? = null
    private val movieItemBinding get() = _movieItemBinding

    companion object {
        fun newInstance() = HomeFragment()
    }

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private val movieAdapterPremiers = MovieAdapter()
    private val movieAdapterPopular = MovieAdapter()
    private val movieAdapterActionUSA = MovieAdapter()
    private val movieAdapterTop250 = MovieAdapter()
    private val movieAdapterFranceDrams = MovieAdapter()
    private val serialsAdapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.getWatchedMovies()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getWatchedMovies()
            profileViewModel.watchedMovies.onEach { movies ->
                movieAdapterPremiers.updateWatchedMovies(movies)
                movieAdapterPopular.updateWatchedMovies(movies)
                movieAdapterActionUSA.updateWatchedMovies(movies)
                movieAdapterTop250.updateWatchedMovies(movies)
                movieAdapterFranceDrams.updateWatchedMovies(movies)
                serialsAdapter.updateWatchedMovies(movies)
            }.launchIn(viewLifecycleOwner.lifecycleScope)



        val recyclerPremiers = binding.customFilmsView.getRecycler()
        val recyclerPopular = binding.customFilmsViewPopular.getRecycler()
        val recyclerActionUSA = binding.customFilmsViewActionUSA.getRecycler()
        val recyclerTop250 = binding.customFilmsViewTOP250.getRecycler()
        val recyclerFranceDrams = binding.customFilmsViewFranceDrams.getRecycler()
        val recyclerSerials = binding.customFilmsViewSerials.getRecycler()

        recyclerPremiers.adapter = movieAdapterPremiers
        recyclerPopular.adapter = movieAdapterPopular
        recyclerActionUSA.adapter = movieAdapterActionUSA
        recyclerTop250.adapter = movieAdapterTop250
        recyclerFranceDrams.adapter = movieAdapterFranceDrams
        recyclerSerials.adapter = serialsAdapter

        viewModel.pagedMoviesPremiers.onEach {
            movieAdapterPremiers.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        movieAdapterPremiers.setOnShowAllClickListener(object :
            MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.premiers, "moviesPremiers")
            }
        })



        viewModel.pagedMoviesPopular.onEach {
            movieAdapterPopular.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        movieAdapterPopular.setOnShowAllClickListener(object : MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.popularText, "moviesPopular")
            }
        })

        viewModel.pagedByGenreMovies.onEach {
            movieAdapterActionUSA.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        movieAdapterActionUSA.setOnShowAllClickListener(object :
            MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.actionText, "moviesActionUSA")
            }
        })

        viewModel.pagedMoviesTop250.onEach {
            movieAdapterTop250.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        movieAdapterTop250.setOnShowAllClickListener(object : MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.top250Text, "moviesTop250")
            }
        })

        viewModel.pagedByGenreMovies.onEach {
            movieAdapterFranceDrams.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        movieAdapterFranceDrams.setOnShowAllClickListener(object :
            MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.franceDramsText, "moviesFranceDrams")
            }
        })

        viewModel.pagedSerials.onEach {
            serialsAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        serialsAdapter.setOnShowAllClickListener(object : MovieAdapter.OnShowAllClickListener {
            override fun onShowAllClick() {
                onClickButtonShowAll(binding.serialsText, "serials")
            }
        })

        onClickButton(binding.customFilmsView.getButton(), binding.premiers, "moviesPremiers")
        onClickButton(
            binding.customFilmsViewPopular.getButton(),
            binding.popularText,
            "moviesPopular"
        )
        onClickButton(
            binding.customFilmsViewActionUSA.getButton(),
            binding.actionText,
            "moviesActionUSA"
        )
        onClickButton(binding.customFilmsViewTOP250.getButton(), binding.top250Text, "moviesTop250")
        onClickButton(
            binding.customFilmsViewFranceDrams.getButton(),
            binding.franceDramsText,
            "moviesFranceDrams"
        )
        onClickButton(binding.customFilmsViewSerials.getButton(), binding.serialsText, "serials")

        movieAdapterPremiers.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })

        movieAdapterPopular.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })

        movieAdapterActionUSA.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })

        movieAdapterTop250.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })

        movieAdapterFranceDrams.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })

        serialsAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.action_homeFragment_to_fragmentMoviePage, bundle)
            }
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onClickButton(button: Button, textView: TextView, movies: String) {

        val bundle = Bundle()
        button.setOnClickListener {
            bundle.putString("Title", textView.text.toString())
            bundle.putString("Movies", movies)
            findNavController().navigate(R.id.action_homeFragment_to_listPage, bundle)
        }

    }

    private fun onClickButtonShowAll(textView: TextView, movies: String) {

        val bundle = Bundle()
        bundle.putString("Title", textView.text.toString())
        bundle.putString("Movies", movies)
        findNavController().navigate(R.id.action_homeFragment_to_listPage, bundle)


    }


}