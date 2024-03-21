package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListByKeywordUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return movieListRepository.getMoviesByKeyword(keyword)
    }
}