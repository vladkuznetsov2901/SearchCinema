package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.adapters.MoviePersonAdapter
import com.example.skillcinema.data.Movie
import com.example.skillcinema.databinding.FragmentPersonPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PersonPageFragment : Fragment() {

    private var _binding: FragmentPersonPageBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieAdapter = MoviePersonAdapter()
        val movieAdapterRecycler = binding.customFilmsView.getRecycler()
        movieAdapterRecycler.adapter = movieAdapter

        val personId = arguments?.getInt("personId")
        lifecycleScope.launch {
            val person = viewModel.loadPersonInfo(personId!!)
            binding.personName.text = person.nameRu
            binding.personProfession.text = person.profession
            person.let {
                Glide.with(binding.personPhoto.context).load(it.posterUrl).into(binding.personPhoto)
            }

            var filmsStr: String = if (person.films.size % 10 == 2 || person.films.size % 10 == 3 || person.films.size % 10 == 4 &&
                    person.films.size != 12 && person.films.size != 13 && person.films.size != 14
                ) {
                    " фильма"
                } else if (person.films.size == 1) " фильм"
                else " фильмов"

            binding.filmsCnt.text = person.films.size.toString() + filmsStr

            val bestFilmsInfo = mutableListOf<Movie>()
            var i = 0
            for (film in person.films) {
                if (i == 10) break
                bestFilmsInfo.add(viewModel.loadMovieInfo(film.filmId))
                i++
            }
            bestFilmsInfo.sortedByDescending { it.ratingKinopoisk }

            movieAdapter.submitList(bestFilmsInfo.take(10))
        }



        binding.showBtnPersonPage.setOnClickListener {
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putInt("personId", personId!!)
            findNavController().navigate(
                R.id.action_personPageFragment_to_personMoviesListPageFragment,
                bundle
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }


    }


}