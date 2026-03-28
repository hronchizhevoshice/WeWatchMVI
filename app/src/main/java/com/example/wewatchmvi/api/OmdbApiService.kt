package com.example.wewatchmvi.api

import com.example.wewatchmvi.model.MovieDetails
import com.example.wewatchmvi.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchTerm: String,
        @Query("y") year: String? = null,
        @Query("type") type: String = "movie"
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): MovieDetails
}