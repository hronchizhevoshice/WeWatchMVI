package com.example.wewatchmvi.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wewatchmvi.data.local.MovieDatabase
import com.example.wewatchmvi.data.remote.RetrofitClient
import com.example.wewatchmvi.data.repository.MovieRepositoryImpl
import com.example.wewatchmvi.domain.repository.MovieRepository
import com.example.wewatchmvi.domain.usecase.*
import com.example.wewatchmvi.presentation.screens.AddScreen
import com.example.wewatchmvi.presentation.screens.MainScreen
import com.example.wewatchmvi.presentation.screens.SearchScreen
import com.example.wewatchmvi.presentation.theme.WeWatchMVITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeWatchMVITheme {
                val repository = provideMovieRepository()
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(repository)
                )

                val state by viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is MainEffect.ShowError -> {
                                Toast.makeText(this@MainActivity, effect.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> { /* навигация через state */ }
                        }
                    }
                }

                when (state.currentScreen) {
                    Screen.MAIN -> MainScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                    Screen.ADD -> AddScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                    Screen.SEARCH -> SearchScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                }
            }
        }
    }

    private fun provideMovieRepository(): MovieRepository {
        val database = MovieDatabase.getDatabase(this)
        val movieDao = database.movieDao()
        val apiService = RetrofitClient.instance
        val apiKey = RetrofitClient.API_KEY
        return MovieRepositoryImpl(movieDao, apiService, apiKey)
    }
}

class MainViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                getMoviesUseCase = GetMoviesUseCase(repository),
                addMovieUseCase = AddMovieUseCase(repository),
                toggleMovieSelectionUseCase = ToggleMovieSelectionUseCase(repository),
                deleteMoviesUseCase = DeleteMoviesUseCase(repository),
                searchMoviesUseCase = SearchMoviesUseCase(repository),
                getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}