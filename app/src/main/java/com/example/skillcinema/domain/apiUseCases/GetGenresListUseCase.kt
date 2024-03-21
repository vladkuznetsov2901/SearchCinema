package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.GenreFilter
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetGenresListUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getGenres(): List<GenreFilter> {
        return movieListRepository.getGenres()
    }
}