package com.example.skillcinema.data

import com.example.skillcinema.api.retrofit
import javax.inject.Inject

class MovieListRepository @Inject constructor() {

    suspend fun getPremiers(year: Int, month: String, page: Int): List<Movie> {
        return retrofit.getMoviesPremiers(year, month, page).items
    }

    suspend fun getPopular(order: String, type: String, page: Int): List<Movie> {
        return retrofit.getMoviesTop(order, type, page).items
    }

    suspend fun getMoviesByGenre(countries: Array<Int>, genres: Array<Int>, page: Int): List<Movie> {
        return retrofit.getMoviesByGenre(countries, genres, page).items
    }


    suspend fun getSerials(type: String, page: Int): List<Movie> {
        return retrofit.getSerials(type, page).items
    }

    suspend fun getMovieInfo(id: Int): Movie {
        return retrofit.getMovieInfo(id)
    }

    suspend fun getPersons(filmId: Int): List<Person> {
        return retrofit.getPersons(filmId)
    }

    suspend fun getMovieImages(id: Int, type: String): List<Image> {
        return retrofit.getMovieGallery(id, type).items
    }

    suspend fun getSimilarMovies(id: Int): List<SimilarMovie> {
        return retrofit.getSimilarMovies(id).items
    }

    suspend fun getPersonInfo(id: Int): PersonInfo {
        return retrofit.getPersonInfo(id)
    }

    suspend fun getMoviesByParameters(
        countries: Array<Int?>,
        genres: Array<Int?>,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ): List<Movie> {
        return retrofit.getMoviesByParameters(
            countries,
            genres,
            order,
            type,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo
        ).items
    }

    suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return retrofit.getMoviesByKeyword(keyword).items
    }

    suspend fun getCountries(): List<CountryFilter> {
        return retrofit.getFilters().countries
    }

    suspend fun getGenres(): List<GenreFilter> {
        return retrofit.getFilters().genres
    }

}