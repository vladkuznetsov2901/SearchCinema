package com.example.skillcinema.data

data class FilterParams(
    val countryId: Int? = null,
    val genreId: Int? = null,
    val order: String = "RATING",
    val type: String = "FILM",
    val ratingFrom: Int = 0,
    val ratingTo: Int = 10,
    val yearFrom: Int = 1998,
    val yearTo: Int = 2017
)

