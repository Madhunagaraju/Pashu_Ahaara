package com.pashuaahar.domain.repository

import com.pashuaahar.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun observeRecipesForCow(cowId: Long): Flow<List<Recipe>>
    fun observeRecentRecipes(): Flow<List<Recipe>>
    suspend fun saveRecipe(recipe: Recipe): Long
    suspend fun deleteRecipeById(recipeId: Long)
}
