package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getMovies()
}