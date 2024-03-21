package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetMovieListByGenreUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getMoviesByGenre(countries: Array<Int>, genres: Array<Int>, page: Int): List<Movie> {
        return movieListRepository.getMoviesByGenre(countries, genres, page)
    }
}