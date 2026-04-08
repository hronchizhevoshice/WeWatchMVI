package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(imdbID: String): Movie? {
        return repository.getMovieDetails(imdbID)
    }
}