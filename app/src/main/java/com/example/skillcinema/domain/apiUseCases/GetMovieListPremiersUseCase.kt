package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListPremiersUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getPremiers(year: Int, month: String, page: Int): List<Movie> {
        return movieListRepository.getPremiers(year, month, page)
    }
}