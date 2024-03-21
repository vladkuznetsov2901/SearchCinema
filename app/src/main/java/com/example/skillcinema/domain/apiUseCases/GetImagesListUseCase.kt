package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.Image
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetImagesListUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getMovieImages(id: Int, type: String): List<Image> {
        return movieListRepository.getMovieImages(id, type)
    }
}