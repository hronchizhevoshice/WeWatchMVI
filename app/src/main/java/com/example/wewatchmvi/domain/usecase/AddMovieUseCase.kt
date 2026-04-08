package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.repository.MovieRepository

class AddMovieUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.addMovie(movie)
    }
}