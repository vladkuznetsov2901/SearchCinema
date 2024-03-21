package com.example.skillcinema.data
data class Person(
    val description: String,
    val nameEn: String,
    val nameRu: String,
    val posterUrl: String,
    val professionKey: String,
    val profession: String,
    val professionText: String,
    val films: List<SimilarMovie>,
    val staffId: Int
)
