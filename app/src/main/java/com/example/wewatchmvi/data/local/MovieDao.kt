package com.example.wewatchmvi.data.local

import androidx.room.*
import com.example.wewatchmvi.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movies WHERE isSelected = 1")
    suspend fun deleteSelectedMovies()

    @Query("UPDATE movies SET isSelected = 0")
    suspend fun clearAllSelections()

    @Query("SELECT COUNT(*) FROM movies WHERE isSelected = 1")
    suspend fun getSelectedCount(): Int
}