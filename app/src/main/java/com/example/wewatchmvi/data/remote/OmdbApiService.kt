package com.example.wewatchmvi.data.remote

import com.example.wewatchmvi.data.remote.model.MovieDetailsDto
import com.example.wewatchmvi.data.remote.model.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchTerm: String,
        @Query("y") year: String? = null
    ): SearchResponseDto

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): MovieDetailsDto
}