package com.pashuaahar.data.repository

import com.pashuaahar.data.local.dao.GrainDao
import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.repository.GrainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrainRepositoryImpl @Inject constructor(
    private val grainDao: GrainDao
) : GrainRepository {
    override fun observeAllGrains(): Flow<List<Grain>> = grainDao.observeAllGrains().map { grains ->
        grains.map { it.toDomain() }
    }

    override suspend fun getGrainById(id: String): Grain? = grainDao.getGrainById(id)?.toDomain()

    override suspend fun upsertGrains(grains: List<Grain>) = grainDao.upsertGrains(grains.map { it.toEntity() })

    override suspend fun updateGrain(grain: Grain) = grainDao.updateGrain(grain.toEntity())
}
