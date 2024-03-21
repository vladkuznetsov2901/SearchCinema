package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.skillcinema.adapters.CollectionsDialogAdapter
import com.example.skillcinema.databinding.FragmentBottomSheetDialogBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetDialog : DialogFragment() {


    private var _binding: FragmentBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

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
    ): View? {
        _binding = FragmentBottomSheetDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutParams = (view.parent as View).layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            val behavior = layoutParams.behavior
            if (behavior is BottomSheetBehavior<*>) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }


        val filmID = arguments?.getInt("itemID")

        Log.d("IDDDDDDDDDDDDDDDDD", filmID.toString())

        lifecycleScope.launch {
            val film = filmID?.let { viewModel.loadMovieInfo(it) }
            if (film != null) {
                withContext(Dispatchers.Main) {
                    binding.movieRate.text = film.ratingKinopoisk.toString()
                    binding.movieTitle.text = film.nameRu
                    binding.movieGenreAndDate.text =
                        film.year.toString() + ", " + film.genres.first().genre
                    film.let {
                        Glide.with(binding.moviePoster.context).load(it.posterUrl)
                            .into(binding.moviePoster)
                    }
                }
            }
        }

        val collectionsRecycler = binding.collectionsRecycler
        val collectionsAdapter = CollectionsDialogAdapter()

        collectionsRecycler.adapter = collectionsAdapter
        collectionsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        lifecycleScope.launch {
            profileViewModel._collections.collect { collections ->
                collectionsAdapter.submitList(collections)
            }
        }


        collectionsAdapter.setOnItemClickListener(object :
            CollectionsDialogAdapter.OnItemClickListener {
            override fun onItemClick(itemID: String) {
                filmID?.let { profileViewModel.insertMovieToDb(it, itemID) }
            }
        }
        )

        binding.insertIntoCollectionButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("tempID", filmID!!)
            showDialog(bundle)
            dismiss()

        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showDialog(bundle: Bundle) {
        val collectionNameDialog = CollectionNameDialog()
        collectionNameDialog.arguments = bundle
        collectionNameDialog.show(parentFragmentManager, collectionNameDialog.tag)
    }

}