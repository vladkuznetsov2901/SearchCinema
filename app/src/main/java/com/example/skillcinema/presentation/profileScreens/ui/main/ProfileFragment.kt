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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.CollectionsAdapter
import com.example.skillcinema.adapters.ProfileMovieAdapter
import com.example.skillcinema.databinding.FragmentProfileBinding
import com.example.skillcinema.presentation.homeScreens.ui.ErrorFragment
import com.example.skillcinema.presentation.homeScreens.ui.HomeViewModel
import com.example.skillcinema.presentation.homeScreens.ui.HomeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    private val movieAdapterFromDb = ProfileMovieAdapter()
    private val wantToWatchedMovies = ProfileMovieAdapter()
    private val collectionsAdapter = CollectionsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerFavorites = binding.customFilmsView.getRecycler()
        val recyclerWantToWatchedMovies = binding.customFilmsView2.getRecycler()
        val recyclerCollections = binding.collectionsRecycler

        recyclerFavorites.adapter = movieAdapterFromDb
        recyclerFavorites.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerWantToWatchedMovies.adapter = wantToWatchedMovies
        recyclerWantToWatchedMovies.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerCollections.adapter = collectionsAdapter
        recyclerCollections.layoutManager =
            GridLayoutManager(context, 2)

        profileViewModel.getWatchedMovies()
        profileViewModel.getWantToWatchedMovies()


        lifecycleScope.launch {
            profileViewModel.watchedMovies.collect { movies ->
                movieAdapterFromDb.submitList(movies)
            }
        }

        lifecycleScope.launch {
            profileViewModel.wantToWatchedMovies.collect { movies ->
                wantToWatchedMovies.submitList(movies)
            }
        }

        lifecycleScope.launch {
            profileViewModel._collections.collect { collections ->
                collectionsAdapter.submitList(collections)
            }
        }

        binding.customFilmsView.getButton().setOnClickListener {
            val bundle = Bundle()
            bundle.putString("collectionName", "Просмотрено")
            findNavController().navigate(
                R.id.action_profileFragment_to_profileListPageFragment,
                bundle
            )
        }

        movieAdapterFromDb.setOnClearAllClickListener(object :
            ProfileMovieAdapter.OnClearAllClickListener {
            override fun onClearAllClick() {
                lifecycleScope.launch {
                    profileViewModel.clearAllMoviesFromCollection("Просмотрено")
                }


            }

        })

        collectionsAdapter.setOnItemClickListener(object : CollectionsAdapter.OnItemClickListener {
            override fun onItemClick(itemID: String) {
                val bundle = Bundle()
                bundle.putString("collectionName", itemID)
                findNavController().navigate(
                    R.id.action_profileFragment_to_profileListPageFragment,
                    bundle
                )
            }

            override fun onDeleteClick(collectionName: String) {
                if (collectionName == "Любимое" ||
                    collectionName == "Просмотрено" ||
                    collectionName == "Хочу посмотреть"
                )
                    showErrorDialog()
                else profileViewModel.deleteCollection(collectionName)
            }

        })

        binding.createNewCollectionBtn.setOnClickListener {
            showDialog()
        }

        movieAdapterFromDb.setOnItemClickListener(object : ProfileMovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(
                    R.id.fragmentMoviePage,
                    bundle
                )

            }
        })


    }

    private fun showDialog() {
        val collectionNameDialog = CollectionNameDialogProfile()
        collectionNameDialog.show(parentFragmentManager, collectionNameDialog.tag)
    }

    private fun showErrorDialog() {
        val errorDialog = ErrorFragment()
        errorDialog.show(parentFragmentManager, errorDialog.tag)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}