package com.example.wewatchmvi.domain.usecase

import com.example.wewatchmvi.domain.model.SearchMovie
import com.example.wewatchmvi.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String, year: String?): List<SearchMovie> {
        return repository.searchMovies(query, year)
    }
}