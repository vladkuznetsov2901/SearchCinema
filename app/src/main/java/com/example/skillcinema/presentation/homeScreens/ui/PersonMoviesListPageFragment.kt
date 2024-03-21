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
import com.example.skillcinema.adapters.MoviePersonAdapter
import com.example.skillcinema.data.Film
import com.example.skillcinema.data.Movie
import com.example.skillcinema.databinding.FragmentPersonMoviesListPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonMoviesListPageFragment : Fragment() {


    private var _binding: FragmentPersonMoviesListPageBinding? = null
    private val binding get() = _binding!!

    private var movieListAdapter = MoviePersonAdapter()


    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonMoviesListPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personId = arguments?.getInt("personId")


        binding.recyclerViewFull.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerViewFull.adapter = movieListAdapter

        binding.chipActrees.isChecked = true

        lifecycleScope.launch {
            delay(2000)
            binding.progressBar2.visibility = View.GONE
        }
        var films: List<Film>

        val actressFilms = mutableListOf<Movie>()
        val dubbingFilms = mutableListOf<Movie>()
        val herselfFilms = mutableListOf<Movie>()

        lifecycleScope.launch {
            binding.chipActrees.isChecked = true
            binding.chipActressDubbing.isChecked = false
            binding.chipActressHerself.isChecked = false
            val person = viewModel.loadPersonInfo(personId!!)
            films = person.films

            if (person.sex == "MALE") {
                binding.chipActrees.text = "Актер"
                binding.chipActressDubbing.text = "Актер дубляжа"
                binding.chipActressHerself.text = "Актер: играет сам себя"
            }

            binding.personName.text = person.nameRu

            var i = 0


            for (film in films) {
                if (i == 40) break
                when (film.professionKey) {
                    "ACTOR" -> actressFilms.add(viewModel.loadMovieInfo(film.filmId))
                    "VOICE_DIRECTOR" -> dubbingFilms.add(
                        viewModel.loadMovieInfo(
                            film.filmId
                        )
                    )

                    "HIMSELF", "HERSELF" -> herselfFilms.add(
                        viewModel.loadMovieInfo(
                            film.filmId
                        )
                    )
                }
                i++

            }
            val actressText = "${binding.chipActrees.text}  ${actressFilms.size}"
            val dubbingText = "${binding.chipActressDubbing.text}  ${dubbingFilms.size}"
            val herselfText = "${binding.chipActressHerself.text}  ${herselfFilms.size}"


            binding.chipActrees.text = actressText
            binding.chipActressDubbing.text = dubbingText
            binding.chipActressHerself.text = herselfText

            movieListAdapter.submitList(actressFilms)

        }

        binding.chipActrees.setOnClickListener {
            binding.chipActrees.isChecked = true
            binding.chipActressDubbing.isChecked = false
            binding.chipActressHerself.isChecked = false

            movieListAdapter.submitList(actressFilms)

        }

        binding.chipActressDubbing.setOnClickListener {
            binding.chipActrees.isChecked = false
            binding.chipActressDubbing.isChecked = true
            binding.chipActressHerself.isChecked = false

            movieListAdapter.submitList(dubbingFilms)

        }

        binding.chipActressHerself.setOnClickListener {
            binding.chipActrees.isChecked = false
            binding.chipActressDubbing.isChecked = false
            binding.chipActressHerself.isChecked = true

            movieListAdapter.submitList(herselfFilms)

        }

        movieListAdapter.setOnItemClickListener(object : MoviePersonAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                val bundle = Bundle()
                bundle.putInt("itemID", itemID)
                findNavController().navigate(
                    R.id.action_personMoviesListPageFragment_to_fragmentMoviePage,
                    bundle
                )
            }
        })


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}