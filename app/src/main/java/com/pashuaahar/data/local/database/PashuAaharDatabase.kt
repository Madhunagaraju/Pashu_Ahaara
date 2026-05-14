package com.pashuaahar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pashuaahar.data.local.dao.CowDao
import com.pashuaahar.data.local.dao.GrainDao
import com.pashuaahar.data.local.dao.RecipeDao
import com.pashuaahar.data.local.entity.CowEntity
import com.pashuaahar.data.local.entity.GrainEntity
import com.pashuaahar.data.local.entity.RecipeEntity

@Database(
    entities = [CowEntity::class, GrainEntity::class, RecipeEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PashuAaharDatabase : RoomDatabase() {
    abstract fun cowDao(): CowDao
    abstract fun grainDao(): GrainDao
    abstract fun recipeDao(): RecipeDao
}
