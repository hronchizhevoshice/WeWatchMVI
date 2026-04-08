package com.example.wewatchmvi.data.repository

import com.example.wewatchmvi.data.local.MovieDao
import com.example.wewatchmvi.data.remote.OmdbApiService
import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.model.SearchMovie
import com.example.wewatchmvi.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val apiService: OmdbApiService,
    private val apiKey: String
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    override suspend fun addMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie)
    }

    override suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    override suspend fun searchMovies(query: String, year: String?): List<SearchMovie> {
        val response = apiService.searchMovies(apiKey, query, year)
        return response.search?.map { dto ->
            SearchMovie(
                title = dto.title,
                year = dto.year,
                imdbID = dto.imdbID,
                posterUrl = dto.posterUrl
            )
        } ?: emptyList()
    }

    override suspend fun getMovieDetails(imdbID: String): Movie? {
        return try {
            val response = apiService.getMovieDetails(apiKey, imdbID)
            Movie(
                title = response.title,
                year = response.year,
                posterUrl = response.posterUrl,
                imdbID = response.imdbID,
                genre = response.genre,
                isSelected = false
            )
        } catch (e: Exception) {
            null
        }
    }
}