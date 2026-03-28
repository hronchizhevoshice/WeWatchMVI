package com.example.wewatchmvi.data

import android.content.Context
import com.example.wewatchmvi.api.RetrofitClient
import com.example.wewatchmvi.model.Movie
import com.example.wewatchmvi.model.MovieDetails
import com.example.wewatchmvi.model.SearchMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(context: Context) {
    private val movieDao = MovieDatabase.getDatabase(context).movieDao()
    private val apiService = RetrofitClient.instance
    private val apiKey = RetrofitClient.API_KEY

    // Room
    val allMovies: Flow<List<Movie>> = movieDao.getAllMovies()

    suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie.copy(isSelected = false))
    }

    suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie)
    }

    suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    suspend fun clearAllSelections() {
        movieDao.clearAllSelections()
    }

    suspend fun getSelectedCount(): Int {
        return movieDao.getSelectedCount()
    }

    // API
    suspend fun searchMovies(query: String, year: String? = null): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchMovies(apiKey, query, year)
            if (response.Response == "True" && response.Search != null) {
                response.Search.map { searchMovie ->
                    Movie(
                        title = searchMovie.Title,
                        year = searchMovie.Year,
                        posterUrl = searchMovie.Poster,
                        imdbID = searchMovie.imdbID,
                        genre = null,
                        isSelected = false
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getMovieDetails(imdbID: String): MovieDetails? = withContext(Dispatchers.IO) {
        try {
            apiService.getMovieDetails(apiKey, imdbID)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}