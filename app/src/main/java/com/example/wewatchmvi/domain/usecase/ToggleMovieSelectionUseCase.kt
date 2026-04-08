package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.repository.MovieRepository

class ToggleMovieSelectionUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.updateMovie(movie.copy(isSelected = !movie.isSelected))
    }
}