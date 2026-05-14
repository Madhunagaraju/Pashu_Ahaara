package com.pashuaahar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pashuaahar.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE cowId = :cowId ORDER BY createdAtMillis DESC")
    fun observeRecipesForCow(cowId: Long): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes ORDER BY createdAtMillis DESC")
    fun observeRecentRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Long)
}
