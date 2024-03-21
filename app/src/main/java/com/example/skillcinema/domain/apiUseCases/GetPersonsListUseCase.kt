package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.MovieListRepository
import com.example.skillcinema.data.Person
import javax.inject.Inject

class GetPersonsListUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getPersons(filmId: Int): List<Person> {
        return movieListRepository.getPersons(filmId)
    }
}