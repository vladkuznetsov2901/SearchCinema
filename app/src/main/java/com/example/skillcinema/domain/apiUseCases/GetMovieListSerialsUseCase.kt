package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListSerialsUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getSerials(type: String, page: Int): List<Movie> {
        return movieListRepository.getSerials(type, page)
    }
}