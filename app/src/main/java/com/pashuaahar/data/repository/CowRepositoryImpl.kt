package com.pashuaahar.data.repository

import com.pashuaahar.data.local.dao.CowDao
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.repository.CowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CowRepositoryImpl @Inject constructor(
    private val cowDao: CowDao
) : CowRepository {

    override fun observeAllCows(): Flow<List<Cow>> = cowDao.observeAllCows().map { cows ->
        cows.map { it.toDomain() }
    }

    override suspend fun getCowById(id: Long): Cow? = cowDao.getCowById(id)?.toDomain()

    override suspend fun addCow(cow: Cow): Long {
        return cowDao.insertCow(cow.toEntity())
    }

    override suspend fun updateCow(cow: Cow) {
        cowDao.updateCow(cow.toEntity())
    }

    override suspend fun deleteCow(cow: Cow) {
        cowDao.deleteCow(cow.toEntity())
    }
}
