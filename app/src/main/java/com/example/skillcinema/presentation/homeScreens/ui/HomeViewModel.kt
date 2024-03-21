package com.example.skillcinema.presentation.homeScreens.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.Image
import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.Person
import com.example.skillcinema.data.PersonInfo
import com.example.skillcinema.data.SimilarMovie
import com.example.skillcinema.domain.apiUseCases.GetImagesListUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieInfoUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListByGenreUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListPopularUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListPremiersUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieListSerialsUseCase
import com.example.skillcinema.domain.apiUseCases.GetPersonInfoUseCase
import com.example.skillcinema.domain.apiUseCases.GetPersonsListUseCase
import com.example.skillcinema.domain.apiUseCases.GetSimilarMovieListUseCase
import com.example.skillcinema.sources.MovieByGenrePagingSource
import com.example.skillcinema.sources.MoviePagingSerialsSource
import com.example.skillcinema.sources.MoviePagingSource
import com.example.skillcinema.sources.MoviePopularPagingSource
import com.example.skillcinema.sources.MovieTop250PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMovieListPremiersUseCase: GetMovieListPremiersUseCase,
    private val getMovieListPopularUseCase: GetMovieListPopularUseCase,
    private val getMovieListByGenreUseCase: GetMovieListByGenreUseCase,
    private val getMovieListSerialsUseCase: GetMovieListSerialsUseCase,
    private val getMovieInfoUseCase: GetMovieInfoUseCase,
    private val getPersonsListUseCase: GetPersonsListUseCase,
    private val getImagesListUseCase: GetImagesListUseCase,
    private val getSimilarMovieListUseCase: GetSimilarMovieListUseCase,
    private val getPersonInfoUseCase: GetPersonInfoUseCase,
) :
    ViewModel() {

    private val _personsInfo = MutableStateFlow<List<Person>>(emptyList())
    val personsInfo = _personsInfo.asStateFlow()

    private val _similarMovies = MutableStateFlow<List<SimilarMovie>>(emptyList())
    val similarMovies = _similarMovies.asStateFlow()

    val pagedMoviesPremiers: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MoviePagingSource(getMovieListPremiersUseCase) }
    ).flow.cachedIn(viewModelScope)

    val pagedMoviesPopular: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MoviePopularPagingSource(getMovieListPopularUseCase) }
    ).flow.cachedIn(viewModelScope)

    val pagedByGenreMovies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MovieByGenrePagingSource(getMovieListByGenreUseCase) }
    ).flow.cachedIn(viewModelScope)

    val pagedMoviesTop250: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MovieTop250PagingSource(getMovieListPopularUseCase) }
    ).flow.cachedIn(viewModelScope)

    val pagedSerials: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MoviePagingSerialsSource(getMovieListSerialsUseCase) }
    ).flow.cachedIn(viewModelScope)



    suspend fun loadMovieInfo(id: Int): Movie {
        return getMovieInfoUseCase.getMovieInfo(id)
    }

    fun loadPersons(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getPersonsListUseCase.getPersons(filmId)
            }.fold(
                onSuccess = { _personsInfo.value = it },
                onFailure = { Log.d("HomeViewModelPersons", it.message ?: "") }
            )
        }
    }

    suspend fun loadMovieImg(id: Int, type: String): List<Image> {
        return getImagesListUseCase.getMovieImages(id, type)
    }

    fun loadSimilarMovies(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getSimilarMovieListUseCase.getSimilarMovies(id)
            }.fold(
                onSuccess = { _similarMovies.value = it },
                onFailure = { Log.d("HomeViewModelSimilarsMovie", it.message ?: "") }
            )
        }
    }

    suspend fun loadPersonInfo(id: Int): PersonInfo {
        return getPersonInfoUseCase.getPersonInfo(id)
    }


}
