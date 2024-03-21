package com.example.skillcinema.data.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("INSERT INTO collections (name, movieIds) VALUES ('Любимое', 1)")
    suspend fun insertFavoritesCollection()

    @Query("INSERT INTO collections (name, movieIds) VALUES ('Хочу посмотреть', 1)")
    suspend fun insertToWatchCollection()

    @Query("INSERT INTO collections (name, movieIds) VALUES ('Просмотрено', 1)")
    suspend fun insertWatchedCollection()

    @Insert
    suspend fun insertCollection(collection: CollectionDB)

    @Query("SELECT * FROM collections")
    fun getAllCollections(): Flow<List<CollectionDB>>

    @Query("SELECT * FROM collections WHERE name = :name")
    suspend fun getCollectionByName(name: String?): CollectionDB


    @Delete
    suspend fun deleteCollection(collectionDB: CollectionDB)


    @Query("UPDATE collections SET movieIds = :movieIds WHERE name = :collectionName")
    suspend fun addMovieToCollection(collectionName: String, movieIds: List<Int?>)

    @Query("DELETE FROM collections WHERE name = :collectionName")
    suspend fun clearCollection(collectionName: String)

    @Query("UPDATE collections SET movieIds = :movieIds WHERE name = :collectionName")
    suspend fun updateMoviesInCollection(collectionName: String, movieIds: List<Int>)

}