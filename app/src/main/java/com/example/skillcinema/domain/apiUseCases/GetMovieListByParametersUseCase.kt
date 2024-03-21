package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListByParametersUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getMoviesByParameters(
        countries: Array<Int?>,
        genres: Array<Int?>,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ): List<Movie> {
        return movieListRepository.getMoviesByParameters(
            countries,
            genres,
            order,
            type,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo
        )
    }
}