package com.pashuaahar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pashuaahar.data.local.entity.GrainEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrainDao {

    @Query("SELECT * FROM grains ORDER BY name ASC")
    fun observeAllGrains(): Flow<List<GrainEntity>>

    @Query("SELECT * FROM grains WHERE id = :id LIMIT 1")
    suspend fun getGrainById(id: String): GrainEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGrains(grains: List<GrainEntity>)

    @Update
    suspend fun updateGrain(grain: GrainEntity)
}
