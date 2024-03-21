package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.MovieListRepository
import com.example.skillcinema.data.SimilarMovie
import javax.inject.Inject

class GetSimilarMovieListUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getSimilarMovies(id: Int): List<SimilarMovie> {
        return movieListRepository.getSimilarMovies(id)
    }
}