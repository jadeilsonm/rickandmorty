package com.example.rickandmorty.ui.home.components

import coil.compose.AsyncImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.ui.UiState

@Composable
fun CharactersScreen(
    characterViewModel: CharacterViewModel = viewModel()
) {
    val uiState by characterViewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 24.dp),
        color = MaterialTheme.colorScheme.onSurface
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val characters = state.data

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters) { character ->
                        CharacterItem(character = character)
                    }
                    item(
                        span = StaggeredGridItemSpan.FullLine
                    )
                    {
                        Button(
                            onClick = { characterViewModel.loadNextPage() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Loading more",
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: ${state.message}")
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Result) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(
            modifier =
                Modifier.padding(8.dp)
                .fillMaxWidth()
                .height(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                AsyncImage(
                    model = character.image,
                    contentDescription = "image of $character.name",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = "Status: ${character.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = "Species: ${character.species}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}
