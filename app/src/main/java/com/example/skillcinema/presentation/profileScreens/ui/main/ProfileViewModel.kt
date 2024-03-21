package com.example.skillcinema.presentation.profileScreens.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.data.entity.CollectionDao
import com.example.skillcinema.data.entity.MovieDB
import com.example.skillcinema.domain.GetMoviesDaoUseCase
import com.example.skillcinema.domain.apiUseCases.GetMovieInfoUseCase
import com.example.skillcinema.domain.daoUseCases.GetCollectionByNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getMoviesDaoUseCase: GetMoviesDaoUseCase,
    collectionDao: CollectionDao,
    private val getCollectionByNameUseCase: GetCollectionByNameUseCase,
    private val getMovieInfoUseCase: GetMovieInfoUseCase,
) :
    ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _watchedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val watchedMovies = _watchedMovies.asStateFlow()

    private val _wantToWatchedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val wantToWatchedMovies = _wantToWatchedMovies.asStateFlow()

    val _collections = collectionDao.getAllCollections().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        listOf()
    )
    val collections: StateFlow<List<CollectionDB>> = _collections

    private val _moviesCollectionUpdated = MutableSharedFlow<Unit>()
    val moviesCollectionUpdated = _moviesCollectionUpdated.asSharedFlow()

    init {
        getWatchedMovies()
        getWantToWatchedMovies()
    }


    fun insertMovieToDb(filmID: Int, collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieDB = MovieDB(kinopoiskId = filmID)

            val collection = getCollectionByNameUseCase.getCollectionByNameDao(collectionName)

            if (movieDB.kinopoiskId !in collection.movieIds) {
                getMoviesDaoUseCase.addMovieToCollection(
                    collection.name,
                    collection.movieIds + movieDB.kinopoiskId
                )
                Log.d("ProfileVM", "Inserting movie: $movieDB into collection: $collection")
                _moviesCollectionUpdated.emit(Unit)

            }
        }
    }

    fun getMoviesFromCollection(collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val moviesFromDb = getCollectionByNameUseCase.getCollectionByNameDao(collectionName)
            val movies = mutableListOf<Movie>()

            if (moviesFromDb.movieIds.isNotEmpty()) {
                for (movieId in moviesFromDb.movieIds) {
                    kotlin.runCatching {
                        val movie = getMovieInfoUseCase.getMovieInfo(movieId)
                        movies.add(movie)
                    }.onFailure { e ->
                        Log.d("ProfileViewModel", "Error loading movie: $e")
                    }
                }
            }

            _movies.postValue(movies)
        }
    }

    fun removeMovieFromCollection(movieID: Int, collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val collection = getCollectionByNameUseCase.getCollectionByNameDao(collectionName)

            if (collection.movieIds.contains(movieID)) {
                val updatedMovieIds = collection.movieIds.toMutableList()
                updatedMovieIds.remove(movieID)

                getMoviesDaoUseCase.updateMoviesInCollection(collectionName, updatedMovieIds)
                Log.d("ProfileVM", "Removing movie: $movieID from collection: $collectionName")
                _moviesCollectionUpdated.emit(Unit)

            }
        }
    }


    fun getWatchedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val moviesFromDb = getCollectionByNameUseCase.getCollectionByNameDao("Просмотрено")
            val movies = mutableListOf<Movie>()

            if (moviesFromDb?.movieIds != null) {
                for (movieId in moviesFromDb.movieIds) {
                    kotlin.runCatching {
                        val movie = getMovieInfoUseCase.getMovieInfo(movieId)
                        movies.add(movie)
                    }.onFailure { e ->
                        Log.d("ProfileViewModel", "Error loading movie: $e")
                    }
                }
            }

            _watchedMovies.value = movies
        }
    }

    suspend fun isMoviesInCollection(movieID: Int?, collectionName: String): Boolean {
        val movies = getCollectionByNameUseCase.getCollectionByNameDao(collectionName)
        return movieID in movies.movieIds
    }


    fun getWantToWatchedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val moviesFromDb = getCollectionByNameUseCase.getCollectionByNameDao("Хочу посмотреть")
            val movies = mutableListOf<Movie>()

            if (moviesFromDb?.movieIds != null) {
                for (movieId in moviesFromDb.movieIds) {
                    kotlin.runCatching {
                        val movie = getMovieInfoUseCase.getMovieInfo(movieId)
                        movies.add(movie)
                    }.onFailure { e ->
                        Log.d("ProfileViewModel", "Error loading movie: $e")
                    }
                }
            }

            _wantToWatchedMovies.value = movies
        }
    }


    fun createNewCollection(collectionName: String) {
        viewModelScope.launch {
            getMoviesDaoUseCase.insertCollection(
                CollectionDB(
                    name = collectionName,
                    movieIds = listOf(1)
                )
            )
        }
    }

    fun deleteCollection(collectionName: String) {
        viewModelScope.launch {
            for (collection in collections.value) {
                if (collectionName == collection.name) getMoviesDaoUseCase.deleteCollection(
                    collection
                )
            }
        }
    }

    fun checkNamesCollections(collectionName: String): Flow<Boolean> =
        collections.map { collections ->
            collections.any { it.name == collectionName }
        }

    suspend fun clearAllMoviesFromCollection(collectionName: String) {
        getMoviesDaoUseCase.clearAllMoviesFromCollection(collectionName)
        getMoviesDaoUseCase.insertWatchedCollection()
        getWatchedMovies()
    }


    suspend fun insertCollections() {
        if (collections.value.isEmpty()) {
            getMoviesDaoUseCase.insertCollectionFavorites()
            getMoviesDaoUseCase.insertCollectionToWatch()
            getMoviesDaoUseCase.insertWatchedCollection()

        }
    }


}
