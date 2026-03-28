package com.example.wewatchmvi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wewatchmvi.model.Movie
import com.example.wewatchmvi.model.MovieDetails
import com.example.wewatchmvi.ui.MainIntent
import com.example.wewatchmvi.ui.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    var searchQuery by remember { mutableStateOf(state.searchQuery) }
    var yearQuery by remember { mutableStateOf(state.searchYear) }
    val selectedMovie = state.selectedMovieDetails

    // Обновляем поля когда выбран фильм
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            searchQuery = movie.title
            yearQuery = movie.year
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить фильм") },
                navigationIcon = {
                    IconButton(onClick = { onIntent(MainIntent.NavigateBack) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Поле для поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Название фильма *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Поле для года
            OutlinedTextField(
                value = yearQuery,
                onValueChange = { yearQuery = it },
                label = { Text("Год (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            onIntent(MainIntent.SearchMovies(searchQuery, yearQuery.takeIf { it.isNotBlank() }))
                            onIntent(MainIntent.NavigateToSearch)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = searchQuery.isNotBlank()
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Поиск")
                }

                Button(
                    onClick = {
                        selectedMovie?.let { movie ->
                            val newMovie = Movie(
                                title = movie.title,
                                year = movie.year,
                                posterUrl = movie.posterUrl,
                                imdbID = movie.imdbID,
                                genre = movie.genre,
                                isSelected = false
                            )
                            onIntent(MainIntent.AddMovie(newMovie))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedMovie != null
                ) {
                    Text("Add movie")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Отображение выбранного фильма
            selectedMovie?.let { movie ->
                MovieDetailsView(movie)
            }
        }
    }
}

@Composable
fun MovieDetailsView(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp, 300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = movie.year,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Жанр: ${movie.genre ?: "Не указан"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}