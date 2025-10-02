package com.example.rickandmorty.ui.home.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.datasource.api.APIClient
import com.example.rickandmorty.data.datasource.api.RickAndMorty
import com.example.rickandmorty.data.model.Result
import com.example.rickandmorty.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.logging.Logger

class CharacterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Result>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val target = "CharacterViewModel"
    private var currentPage = 1
    private var allCharacters = mutableListOf<Result>()
    private var hasMorePages = true

    init {
        fetchCharacters()
    }

    fun refreshData() {
        currentPage = 1
        allCharacters.clear()
        hasMorePages = true
        fetchCharacters()
    }

    fun loadNextPage() {
        if (hasMorePages) {
            fetchCharacters(page = currentPage)
        }
    }

    private fun fetchCharacters(page: Int = 1) {
        if (page == 1) {
            _uiState.value = UiState.Loading
        }

        viewModelScope.launch {
            try {
                val instance = APIClient.getInstance().create(RickAndMorty::class.java)
                Log.i(target,"instance: $instance")
                val response = instance.getCharacter(page = page)
                Log.i(target,"response: $response")
                Log.i(target,"response body: ${response.body()}")
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        allCharacters.addAll(data.results)

                        _uiState.update {
                            val currentList = (it as? UiState.Success)?.data ?: emptyList()
                            val newList = currentList + data.results
                            UiState.Success(newList)
                        }

                        hasMorePages = data.info.next != null
                        if(hasMorePages) {
                            currentPage++
                        }

                    } else {
                        _uiState.value = UiState.Error("Response not found")
                    }
                } else {
                    _uiState.value = UiState.Error("Error in request: ${response.code()}")
                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "unknow error")
            }
        }
    }
}
