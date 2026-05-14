package com.pashuaahar.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.usecase.cow.ObserveCowsUseCase
import com.pashuaahar.domain.usecase.recipe.ObserveRecentRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val cows: List<Cow> = emptyList(),
    val recentRecipes: List<Recipe> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeCowsUseCase: ObserveCowsUseCase,
    private val observeRecentRecipesUseCase: ObserveRecentRecipesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeCowsUseCase().collect { cows -> _uiState.update { it.copy(cows = cows) } }
        }
        viewModelScope.launch {
            observeRecentRecipesUseCase().collect { recipes ->
                _uiState.update { it.copy(recentRecipes = recipes.take(5)) }
            }
        }
    }
}
