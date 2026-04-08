package com.example.wewatchmvi.presentation

import com.example.wewatchmvi.domain.model.Movie

sealed class MainIntent {
    object LoadMovies : MainIntent()

    // Поиск
    data class SearchMovies(val query: String, val year: String? = null) : MainIntent()
    object ClearSearchResults : MainIntent()

    // Добавление/редактирование
    data class AddMovie(val movie: Movie) : MainIntent()
    object NavigateToAdd : MainIntent()
    data class NavigateToEdit(val movie: Movie) : MainIntent()

    // Выбор/удаление
    data class ToggleMovieSelection(val movie: Movie) : MainIntent()
    object ShowDeleteDialog : MainIntent()
    object ConfirmDelete : MainIntent()
    object DismissDeleteDialog : MainIntent()

    // Навигация
    object NavigateBack : MainIntent()
    object NavigateToSearch : MainIntent()

    // Получение деталей фильма
    data class GetMovieDetails(val imdbID: String) : MainIntent()
}