package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieInfoUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getMovieInfo(id: Int): Movie {
        return movieListRepository.getMovieInfo(id)
    }
}