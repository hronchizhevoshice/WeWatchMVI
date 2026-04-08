package com.example.wewatchmvi.domain.repository

import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.model.SearchMovie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Local
    fun getMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)
    suspend fun deleteSelectedMovies()

    // Remote
    suspend fun searchMovies(query: String, year: String?): List<SearchMovie>
    suspend fun getMovieDetails(imdbID: String): Movie?
}