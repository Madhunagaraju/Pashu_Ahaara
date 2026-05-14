package com.pashuaahar.domain.usecase.recipe

import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.repository.RecipeRepository
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Long = repository.saveRecipe(recipe)
}
