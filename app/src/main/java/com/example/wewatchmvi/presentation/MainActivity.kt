package com.example.wewatchmvi.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wewatchmvi.data.MovieRepositoryImpl
import com.example.wewatchmvi.presentation.screens.AddScreen
import com.example.wewatchmvi.presentation.screens.MainScreen
import com.example.wewatchmvi.presentation.screens.SearchScreen
import com.example.wewatchmvi.presentation.theme.WeWatchMVITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeWatchMVITheme {
                val repository = MovieRepositoryImpl(applicationContext)
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(repository)
                )

                val state by viewModel.state.collectAsState()

                // Обрабатываем эффекты
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

                // Отображаем текущий экран
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
}

// Factory для ViewModel
class MainViewModelFactory(
    private val repository: MovieRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}