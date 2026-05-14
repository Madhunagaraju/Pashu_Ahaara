package com.pashuaahar.data.repository

import com.pashuaahar.data.local.dao.RecipeDao
import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override fun observeRecipesForCow(cowId: Long): Flow<List<Recipe>> =
        recipeDao.observeRecipesForCow(cowId).map { recipes -> recipes.map { it.toDomain() } }

    override fun observeRecentRecipes(): Flow<List<Recipe>> =
        recipeDao.observeRecentRecipes().map { recipes -> recipes.map { it.toDomain() } }

    override suspend fun saveRecipe(recipe: Recipe): Long {
        return recipeDao.insertRecipe(recipe.toEntity())
    }

    override suspend fun deleteRecipeById(recipeId: Long) {
        recipeDao.deleteRecipeById(recipeId)
    }
}
