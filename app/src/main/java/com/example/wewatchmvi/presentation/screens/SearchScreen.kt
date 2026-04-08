package com.example.wewatchmvi.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wewatchmvi.domain.model.Movie
import com.example.wewatchmvi.presentation.MainIntent
import com.example.wewatchmvi.presentation.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результаты поиска") },
                navigationIcon = {
                    IconButton(onClick = { onIntent(MainIntent.NavigateBack) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage!!,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.searchResults.isEmpty() -> {
                    Text(
                        text = "Ничего не найдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(state.searchResults) { movie ->
                            SearchResultItem(
                                movie = movie,
                                onItemClick = {
                                    onIntent(MainIntent.GetMovieDetails(movie.imdbID))
                                    onIntent(MainIntent.NavigateBack)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    movie: Movie,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.size(60.dp, 80.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Text(
                    text = movie.year,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Жанр: ${movie.genre ?: "Загрузка..."}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}