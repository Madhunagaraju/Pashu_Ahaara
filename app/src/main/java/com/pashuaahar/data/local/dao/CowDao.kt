package com.pashuaahar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pashuaahar.data.local.entity.CowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CowDao {

    @Query("SELECT * FROM cows ORDER BY createdAtMillis DESC")
    fun observeAllCows(): Flow<List<CowEntity>>

    @Query("SELECT * FROM cows WHERE id = :id LIMIT 1")
    suspend fun getCowById(id: Long): CowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCow(cow: CowEntity): Long

    @Update
    suspend fun updateCow(cow: CowEntity)

    @Delete
    suspend fun deleteCow(cow: CowEntity)
}
