package com.example.wewatchmvi.data.remote.model

import com.example.wewatchmvi.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Poster") val posterUrl: String,
    @SerializedName("imdbID") val imdbID: String
) {
    fun toMovie() = Movie(
        title = title,
        year = year,
        posterUrl = posterUrl,
        imdbID = imdbID,
        genre = genre,
        isSelected = false
    )
}