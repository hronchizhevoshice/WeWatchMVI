package com.example.wewatchmvi.data.remote.model

import com.example.wewatchmvi.domain.model.SearchMovie
import com.google.gson.annotations.SerializedName

data class SearchMovieDto(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Poster") val posterUrl: String
)

data class SearchResponseDto(
    @SerializedName("Search") val search: List<SearchMovieDto>?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)