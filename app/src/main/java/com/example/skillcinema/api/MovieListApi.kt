package com.example.skillcinema.api

import com.example.skillcinema.data.GenresAndCountries
import com.example.skillcinema.data.Images
import com.example.skillcinema.data.Movie
import com.example.skillcinema.data.MovieList
import com.example.skillcinema.data.Person
import com.example.skillcinema.data.PersonInfo
import com.example.skillcinema.data.SimilarMovies
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieListApi {

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/premieres")
    suspend fun getMoviesPremiers(
        @Query("year") year: Int,
        @Query("month") month: String,
        @Query("page") page: Int,
    ): MovieList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films")
    suspend fun getMoviesTop(
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("page") page: Int,
    ): MovieList

    @Headers("X-API-KEY: $api_key5")
    @GET("/api/v2.2/films")
    suspend fun getMoviesByGenre(
        @Query("countries") countries: Array<Int>, @Query("genres") genres: Array<Int>,
        @Query("page") page: Int,
    ): MovieList


    @Headers("X-API-KEY: $api_key5")
    @GET("/api/v2.2/films")
    suspend fun getSerials(
        @Query("type") type: String,
        @Query("page") page: Int,
    ): MovieList

    @Headers("X-API-KEY: $api_key5")
    @GET("/api/v2.2/films/{id}")
    suspend fun getMovieInfo(
        @Path("id") type: Int,
    ): Movie

    @Headers("X-API-KEY: $api_key3")
    @GET("/api/v1/staff")
    suspend fun getPersons(
        @Query("filmId") type: Int,
    ): List<Person>

    @Headers("X-API-KEY: $api_key3")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getMovieGallery(
        @Path("id") id: Int, @Query("type") type: String,
    ): Images

    @Headers("X-API-KEY: $api_key3")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilarMovies(
        @Path("id") type: Int,
    ): SimilarMovies

    @Headers("X-API-KEY: $api_key3")
    @GET("/api/v1/staff/{id}")
    suspend fun getPersonInfo(
        @Path("id") type: Int,
    ): PersonInfo

    @Headers("X-API-KEY: $api_key4")
    @GET("/api/v2.2/films")
    suspend fun getMoviesByParameters(
        @Query("countries") countries: Array<Int?>, @Query("genres") genres: Array<Int?>,
        @Query("order") order: String, @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Number, @Query("ratingTo") ratingTo: Number,
        @Query("yearFrom") yearFrom: Int, @Query("yearTo") yearTo: Int,
    ): MovieList

    @Headers("X-API-KEY: $api_key5")
    @GET("/api/v2.2/films")
    suspend fun getMoviesByKeyword(
        @Query("keyword") keyword: String,
    ): MovieList

    @Headers("X-API-KEY: $api_key5")
    @GET("/api/v2.2/films/filters")
    suspend fun getFilters(
    ): GenresAndCountries


    private companion object {
        private const val api_key = "e95207b0-9101-4e18-a2ad-ad44d8f1562f"
        private const val api_key3 = "578dd7f7-a014-496c-8a49-06d6fa3dce54"
        private const val api_key2 = "293b94f0-f9e6-4bcb-9af6-89689a95490b"
        private const val api_key4 = "590f5fb0-a3c8-4df3-a0b2-68fa8a09ba1d"
        private const val api_key5 = "6f5c2e8c-8d53-4f40-9fb0-211f8e2b269b"
        private const val temp2 = ""

    }

}

val retrofit = Retrofit.Builder().client(
    OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }).build()
).baseUrl("https://kinopoiskapiunofficial.tech")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(MovieListApi::class.java)


