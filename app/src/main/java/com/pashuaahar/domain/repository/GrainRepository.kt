package com.pashuaahar.domain.repository

import com.pashuaahar.domain.model.Grain
import kotlinx.coroutines.flow.Flow

interface GrainRepository {
    fun observeAllGrains(): Flow<List<Grain>>
    suspend fun getGrainById(id: String): Grain?
    suspend fun upsertGrains(grains: List<Grain>)
    suspend fun updateGrain(grain: Grain)
}
