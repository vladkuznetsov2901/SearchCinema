package com.example.skillcinema.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovieToDb(movieDB: MovieDB)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieDB>
}