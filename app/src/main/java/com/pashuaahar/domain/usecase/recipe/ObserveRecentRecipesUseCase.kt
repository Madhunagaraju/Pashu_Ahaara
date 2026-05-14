package com.pashuaahar.domain.usecase.recipe

import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.observeRecentRecipes()
}
