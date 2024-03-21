package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListPopularUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getPopular(order: String, type: String, page: Int): List<Movie> {
        return movieListRepository.getPopular(order, type, page)
    }
}