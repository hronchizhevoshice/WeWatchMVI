package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.repository.MovieRepository

class DeleteMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.deleteSelectedMovies()
    }
}