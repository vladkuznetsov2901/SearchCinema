package com.example.skillcinema.presentation.searchScreens.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.CountryFilter
import com.example.skillcinema.data.GenreFilter
import com.example.skillcinema.data.Movie
import com.example.skillcinema.domain.apiUseCases.GetCountriesListUseCase
import com.example.skillcinema.domain.apiUseCases.GetGenresListUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListByKeywordUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListByParametersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getMovieListByParametersUseCase: GetMovieListByParametersUseCase,
    private val getMovieListByKeywordUseCase: GetMovieListByKeywordUseCase,
    private val getCountriesListUseCase: GetCountriesListUseCase,
    private val getGenresListUseCase: GetGenresListUseCase,
) :
    ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _moviesByKeywords = MutableStateFlow<List<Movie>>(emptyList())
    val moviesByKeywords = _moviesByKeywords.asStateFlow()

    private val _moviesByParameters = MutableStateFlow<List<Movie>>(emptyList())
    val moviesByParameters = _moviesByParameters.asStateFlow()


    fun getMoviesByParameters(
        countries: Array<Int?>,
        genres: Array<Int?>,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMovieListByParametersUseCase.getMoviesByParameters(
                    countries,
                    genres,
                    order,
                    type,
                    ratingFrom,
                    ratingTo,
                    yearFrom,
                    yearTo
                )
            }.fold(
                onSuccess = { movies ->
                    _moviesByParameters.value = movies
                },
                onFailure = {
                    Log.d("HomeViewModelPersons", it.message ?: "")
                }
            )
        }
    }


    suspend fun getMoviesByKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMovieListByKeywordUseCase.getMoviesByKeyword(keyword)
            }.fold(
                onSuccess = { _moviesByKeywords.value = it },
                onFailure = { Log.d("HomeViewModelPersons", it.message ?: "") }
            )
        }
    }

    suspend fun loadCountries(): List<CountryFilter> {
        return getCountriesListUseCase.getCountries()
    }

    suspend fun loadGenres(): List<GenreFilter> {
        return getGenresListUseCase.getGenres()
    }
}