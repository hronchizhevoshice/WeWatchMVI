package com.example.wewatchmvi.domain.model

data class SearchMovie(
    val title: String,
    val year: String,
    val imdbID: String,
    val posterUrl: String
)