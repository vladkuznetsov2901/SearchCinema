package com.example.skillcinema.data

data class GenresAndCountries(
    val countries: List<CountryFilter>,
    val genres: List<GenreFilter>
)

data class CountryFilter(
    val country: String,
    val id: Int
)

data class GenreFilter(
    val genre: String,
    val id: Int
)
