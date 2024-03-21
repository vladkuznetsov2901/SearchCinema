package com.example.skillcinema.presentation.homeScreens.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.adapters.ImagesAdapter
import com.example.skillcinema.adapters.PersonAdapter
import com.example.skillcinema.adapters.SimilarMovieAdapter
import com.example.skillcinema.databinding.FragmentMoviePageBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class FragmentMoviePage : Fragment() {

    private var _binding: FragmentMoviePageBinding? = null
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
    ): View {
        _binding = FragmentMoviePageBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personAdapter = PersonAdapter()
        val personWorkedAdapter = PersonAdapter()
        val imagesAdapter = ImagesAdapter()
        val similarMoviesAdapter = SimilarMovieAdapter()

        val personRecycler = binding.moviePageRecycler
        personRecycler.layoutManager =
            GridLayoutManager(context, 4, LinearLayoutManager.HORIZONTAL, false)
        personRecycler.adapter = personAdapter

        val personWorkedRecycler = binding.movieWorkedRecycler
        personWorkedRecycler.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false)
        personWorkedRecycler.adapter = personWorkedAdapter

        val imagesRecycler = binding.movieImgRecycler
        imagesRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imagesRecycler.adapter = imagesAdapter

        val similarMoviesRecycler = binding.customFilmsViewSimilar.getRecycler()
        similarMoviesRecycler.adapter = similarMoviesAdapter

        val filmID = arguments?.getInt("itemID")
        var link = ""

        lifecycleScope.launch {
            try {
                val film = viewModel.loadMovieInfo(filmID!!)
                link = film.webUrl
                Log.d("MoviePage", link)
                withContext(Dispatchers.Main) {
                    binding.movieRate.text = film.ratingKinopoisk.toString()
                    binding.movieTitle2.text = film.nameRu
                    binding.movieYearAndGenre.text =
                        film.year.toString() + ", " + film.genres.first().genre
                    binding.movieCountryAndTime.text =
                        film.countries.first().country + ", " + formatMinutesToHoursAndMinutes(film.filmLength.toInt())
                    film.let {
                        Glide.with(binding.poster.context).load(it.posterUrl).into(binding.poster)
                    }
                    film.let {
                        Glide.with(binding.logo.context).load(it.logoUrl).into(binding.logo)
                    }
                    binding.shortDescription.text = film.shortDescription
                    binding.description.text = film.description
                }
            } catch (e: HttpException) {
                Log.e("MoviePage", "HTTP 400: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Can not load movie page!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("MoviePage", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Can not load movie page!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                Log.e("MoviePage", "Error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            if (filmID != null) {
                val isMovieWatchedViewed =
                    profileViewModel.isMoviesInCollection(filmID, "Просмотрено")
                changeBtnColor(binding.viewedBtn, isMovieWatchedViewed, R.drawable.viewed_icn)
                val isMovieWatchedFavorites =
                    profileViewModel.isMoviesInCollection(filmID, "Любимое")
                changeBtnColor(
                    binding.favouritesBtn,
                    isMovieWatchedFavorites,
                    R.drawable.favourites_icn
                )
                val isMovieWatchedMarked =
                    profileViewModel.isMoviesInCollection(filmID, "Хочу посмотреть")
                changeBtnColor(binding.markedBtn, isMovieWatchedMarked, R.drawable.marked_icn)
            }
        }




        lifecycleScope.launch {
            viewModel.loadPersons(filmID!!)
            viewModel.personsInfo.collect { persons ->
                val actors = persons.filter { it.professionKey == "ACTOR" }
                val otherProfessions = persons.filter { it.professionKey != "ACTOR" }

                personAdapter.submitList(actors)
                personWorkedAdapter.submitList(otherProfessions)
                binding.cntPersons.text = actors.size.toString() + " >"
                binding.cntWorkedPersons.text = otherProfessions.size.toString() + " >"
            }
        }

        lifecycleScope.launch {
            val images = viewModel.loadMovieImg(filmID!!, "STILL")
            imagesAdapter.submitList(images)
        }

        lifecycleScope.launch {
            viewModel.loadSimilarMovies(filmID!!)
            viewModel.similarMovies.collect { similarMovies ->
                similarMoviesAdapter.submitList(similarMovies)
            }
        }


        onClickButton(binding.cntPersons, "В фильме снимались", "actors", filmID!!)
        onClickButton(
            binding.cntWorkedPersons, "Над фильмом работали", "otherProfessions",
            filmID
        )
        onClickButton(
            binding.customFilmsViewSimilar.getButton(),
            "Похожие фильмы",
            "similarMovies",
            filmID
        )

        binding.allImagesBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("filmId", filmID)
            findNavController().navigate(R.id.action_fragmentMoviePage_to_imagesListPage, bundle)
        }

        similarMoviesAdapter.setOnItemClickListener(object :
            SimilarMovieAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(R.id.fragmentMoviePage, bundle)
            }
        })

        personAdapter.setOnItemClickListener(object : PersonAdapter.OnItemClickListener {
            override fun onItemClick(personId: Int) {
                val bundle = Bundle()
                bundle.putInt("personId", personId)
                findNavController().navigate(
                    R.id.action_fragmentMoviePage_to_personPageFragment,
                    bundle
                )
            }
        })




        binding.viewedBtn.setOnClickListener {
            lifecycleScope.launch {
                val isMovieWatched = profileViewModel.isMoviesInCollection(filmID, "Просмотрено")

                if (isMovieWatched) {
                    profileViewModel.removeMovieFromCollection(filmID, "Просмотрено")
                } else {
                    profileViewModel.insertMovieToDb(filmID, "Просмотрено")
                }

                profileViewModel.moviesCollectionUpdated.collect {
                    val updatedIsMovieWatched =
                        profileViewModel.isMoviesInCollection(filmID, "Просмотрено")

                    changeBtnColor(binding.viewedBtn, updatedIsMovieWatched, R.drawable.viewed_icn)
                }
            }
        }



        binding.favouritesBtn.setOnClickListener {
            lifecycleScope.launch {
                val isMovieWatched = profileViewModel.isMoviesInCollection(filmID, "Любимое")

                if (isMovieWatched) {
                    profileViewModel.removeMovieFromCollection(filmID, "Любимое")
                } else {
                    profileViewModel.insertMovieToDb(filmID, "Любимое")
                }

                profileViewModel.moviesCollectionUpdated.collect {
                    val updatedIsMovieWatched =
                        profileViewModel.isMoviesInCollection(filmID, "Любимое")

                    changeBtnColor(
                        binding.favouritesBtn,
                        updatedIsMovieWatched,
                        R.drawable.favourites_icn
                    )
                }
            }
        }


        binding.markedBtn.setOnClickListener {
            lifecycleScope.launch {
                val isMovieWatched =
                    profileViewModel.isMoviesInCollection(filmID, "Хочу посмотреть")

                if (isMovieWatched) {
                    profileViewModel.removeMovieFromCollection(filmID, "Хочу посмотреть")
                } else {
                    profileViewModel.insertMovieToDb(filmID, "Хочу посмотреть")
                }

                profileViewModel.moviesCollectionUpdated.collect {
                    val updatedIsMovieWatched =
                        profileViewModel.isMoviesInCollection(filmID, "Хочу посмотреть")

                    changeBtnColor(
                        binding.markedBtn,
                        updatedIsMovieWatched,
                        R.drawable.marked_icn
                    )
                }
            }
        }

        binding.shareBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, link)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        binding.otherBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("itemID", filmID)
            showDialog(bundle)
        }



        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun formatMinutesToHoursAndMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return if (hours > 0) {
            "$hours ч $remainingMinutes мин"
        } else {
            "$minutes мин"
        }
    }

    private fun onClickButton(button: Button, text: String, value: String, filmId: Int) {

        val bundle = Bundle()
        button.setOnClickListener {
            bundle.putString("Title", text)
            bundle.putString("Movies", value)
            bundle.putInt("filmId", filmId)
            findNavController().navigate(R.id.action_fragmentMoviePage_to_listPage, bundle)
        }

    }

    private fun showDialog(bundle: Bundle) {
        val bottomSheetDialog = BottomSheetDialog()
        bottomSheetDialog.arguments = bundle
        bottomSheetDialog.show(parentFragmentManager, bottomSheetDialog.tag)
    }

    private fun changeBtnColor(imageButton: ImageButton, isMovieWatched: Boolean, drawable: Int) {
        if (isMovieWatched) {
            val originalDrawable = imageButton.drawable
            val coloredDrawable = originalDrawable.constantState?.newDrawable()?.mutate()
            coloredDrawable?.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.light_blue_600),
                PorterDuff.Mode.SRC_IN
            )
            imageButton.setImageDrawable(coloredDrawable)
        } else {
            imageButton.setImageResource(drawable)
        }
    }


}