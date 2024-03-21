package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.MovieListRepository
import com.example.skillcinema.data.PersonInfo
import javax.inject.Inject

class GetPersonInfoUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getPersonInfo(id: Int): PersonInfo {
        return movieListRepository.getPersonInfo(id)
    }
}