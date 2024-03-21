package com.example.skillcinema.domain.apiUseCases

import com.example.skillcinema.data.CountryFilter
import com.example.skillcinema.data.MovieListRepository
import javax.inject.Inject

class GetCountriesListUseCase @Inject constructor(private val movieListRepository: MovieListRepository) {

    suspend fun getCountries(): List<CountryFilter> {
        return movieListRepository.getCountries()
    }
}