package com.example.skillcinema.domain

import com.example.skillcinema.data.entity.CollectionDB
import com.example.skillcinema.data.entity.CollectionDao
import com.example.skillcinema.data.entity.MovieDao
import javax.inject.Inject

class GetMoviesDaoUseCase @Inject constructor(
    private val movieDao: MovieDao,
    private val collectionDao: CollectionDao,
) {

    suspend fun insertCollection(collection: CollectionDB) {
        collectionDao.insertCollection(collection)
    }


    suspend fun insertCollectionFavorites() {
        collectionDao.insertFavoritesCollection()
    }

    suspend fun insertCollectionToWatch() {
        collectionDao.insertToWatchCollection()
    }

    suspend fun insertWatchedCollection() {
        collectionDao.insertWatchedCollection()
    }


    suspend fun deleteCollection(collection: CollectionDB) {
        collectionDao.deleteCollection(collection)
    }

    suspend fun addMovieToCollection(collectionName: String, movieIds: List<Int?>) {
        collectionDao.addMovieToCollection(collectionName, movieIds)
    }

    suspend fun clearAllMoviesFromCollection(collectionName: String) {
        collectionDao.clearCollection(collectionName)
    }

    suspend fun updateMoviesInCollection(collectionName: String, movieIds: List<Int>) {
        collectionDao.updateMoviesInCollection(collectionName, movieIds)
    }


}