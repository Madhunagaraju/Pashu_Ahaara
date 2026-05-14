package com.pashuaahar.domain.repository

import com.pashuaahar.domain.model.Cow
import kotlinx.coroutines.flow.Flow

interface CowRepository {
    fun observeAllCows(): Flow<List<Cow>>
    suspend fun getCowById(id: Long): Cow?
    suspend fun addCow(cow: Cow): Long
    suspend fun updateCow(cow: Cow)
    suspend fun deleteCow(cow: Cow)
}
