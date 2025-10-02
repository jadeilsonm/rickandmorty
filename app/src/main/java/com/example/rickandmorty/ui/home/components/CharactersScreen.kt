import coil.compose.AsyncImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.rickandmorty.ui.home.components.CharacterViewModel

@Composable
fun CharactersScreen(
    characterViewModel: CharacterViewModel = viewModel()
) {
    val uiState by characterViewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
        color = MaterialTheme.colorScheme.background
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

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters) { character ->
                        CharacterItem(character = character)
                    }

                    item(
                        span = { GridItemSpan(maxCurrentLineSpan) }){
                        Button(
                            onClick = { characterViewModel.loadNextPage() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Text("Loading more")
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
        modifier = Modifier.wrapContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).size(width = 150.dp, height = 235.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                AsyncImage(
                    model = character.image,
                    contentDescription = "image of $character.name",
                    alignment = Alignment.Center,
                    modifier = Modifier
                    .size(150.dp)
                        .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(130.dp)
                )
                Text(
                    text = "Status: ${character.status}",
                    style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Species: ${character.species}",
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
