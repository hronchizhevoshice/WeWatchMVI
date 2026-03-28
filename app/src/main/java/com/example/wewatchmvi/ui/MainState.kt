package com.example.wewatchmvi.ui

import com.example.wewatchmvi.model.Movie

data class MainState(
    val movies: List<Movie> = emptyList(),
    val searchResults: List<Movie> = emptyList(),
    val selectedMovieDetails: Movie? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCount: Int = 0,
    val isDeleteDialogVisible: Boolean = false,
    val currentScreen: Screen = Screen.MAIN,
    val searchQuery: String = "",
    val searchYear: String = ""
)

enum class Screen {
    MAIN, ADD, SEARCH
}