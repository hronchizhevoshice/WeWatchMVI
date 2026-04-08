package com.example.wewatchmvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val toggleMovieSelectionUseCase: ToggleMovieSelectionUseCase,
    private val deleteMoviesUseCase: DeleteMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    init {
        handleIntent(MainIntent.LoadMovies)
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMovies -> loadMovies()
            is MainIntent.SearchMovies -> searchMovies(intent.query, intent.year)
            is MainIntent.ClearSearchResults -> clearSearchResults()
            is MainIntent.AddMovie -> addMovie(intent.movie)
            is MainIntent.NavigateToAdd -> navigateToAdd()
            is MainIntent.NavigateToEdit -> navigateToEdit(intent.movie)
            is MainIntent.ToggleMovieSelection -> toggleMovieSelection(intent.movie)
            is MainIntent.ShowDeleteDialog -> showDeleteDialog()
            is MainIntent.DismissDeleteDialog -> dismissDeleteDialog()
            is MainIntent.ConfirmDelete -> confirmDelete()
            is MainIntent.NavigateBack -> navigateBack()
            is MainIntent.NavigateToSearch -> navigateToSearch()
            is MainIntent.GetMovieDetails -> getMovieDetails(intent.imdbID)
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            getMoviesUseCase().collect { movies ->
                _state.update { currentState ->
                    currentState.copy(
                        movies = movies,
                        selectedCount = movies.count { it.isSelected }
                    )
                }
            }
        }
    }

    private fun searchMovies(query: String, year: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val results = searchMoviesUseCase(query, year)
                _state.update {
                    it.copy(
                        searchResults = results.map { searchMovie ->
                            Movie(
                                title = searchMovie.title,
                                year = searchMovie.year,
                                posterUrl = searchMovie.posterUrl,
                                imdbID = searchMovie.imdbID
                            )
                        },
                        isLoading = false,
                        errorMessage = if (results.isEmpty()) "Фильмы не найдены" else null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, errorMessage = "Ошибка: ${e.message}")
                }
            }
        }
    }

    private fun addMovie(movie: Movie) {
        viewModelScope.launch {
            addMovieUseCase(movie)
            _state.update {
                it.copy(currentScreen = Screen.MAIN, selectedMovieDetails = null)
            }
        }
    }

    private fun toggleMovieSelection(movie: Movie) {
        viewModelScope.launch {
            toggleMovieSelectionUseCase(movie)
        }
    }

    private fun showDeleteDialog() {
        _state.update { it.copy(isDeleteDialogVisible = true) }
    }

    private fun dismissDeleteDialog() {
        _state.update { it.copy(isDeleteDialogVisible = false) }
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            deleteMoviesUseCase()
            _state.update { it.copy(isDeleteDialogVisible = false) }
        }
    }

    private fun getMovieDetails(imdbID: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val movie = getMovieDetailsUseCase(imdbID)
            movie?.let {
                _state.update { currentState ->
                    currentState.copy(
                        searchQuery = it.title,
                        searchYear = it.year,
                        selectedMovieDetails = it,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun clearSearchResults() {
        _state.update {
            it.copy(searchResults = emptyList(), errorMessage = null)
        }
    }

    private fun navigateToAdd() {
        _state.update { it.copy(currentScreen = Screen.ADD, selectedMovieDetails = null, searchQuery = "", searchYear = "") }
    }

    private fun navigateToEdit(movie: Movie) {
        _state.update {
            it.copy(
                currentScreen = Screen.ADD,
                selectedMovieDetails = movie,
                searchQuery = movie.title,
                searchYear = movie.year
            )
        }
    }

    private fun navigateToSearch() {
        _state.update { it.copy(currentScreen = Screen.SEARCH) }
    }

    private fun navigateBack() {
        when (_state.value.currentScreen) {
            Screen.ADD -> _state.update {
                it.copy(
                    currentScreen = Screen.MAIN,
                    selectedMovieDetails = null,
                    searchQuery = "",
                    searchYear = ""
                )
            }
            Screen.SEARCH -> _state.update { it.copy(currentScreen = Screen.ADD) }
            Screen.MAIN -> _state.update { it.copy(currentScreen = Screen.MAIN) }
        }
    }
}