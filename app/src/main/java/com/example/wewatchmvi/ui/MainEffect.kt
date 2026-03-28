package com.example.wewatchmvi.ui

import com.example.wewatchmvi.model.Movie

sealed class MainEffect {
    object NavigateToMain : MainEffect()
    data class NavigateToAdd(val movie: Movie?) : MainEffect()
    object NavigateToSearch : MainEffect()
    object NavigateBack : MainEffect()
    data class ShowError(val message: String) : MainEffect()
}