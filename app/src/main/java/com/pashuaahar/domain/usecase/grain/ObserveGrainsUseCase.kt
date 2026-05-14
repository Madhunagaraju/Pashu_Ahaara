package com.pashuaahar.domain.usecase.grain

import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.repository.GrainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGrainsUseCase @Inject constructor(
    private val repository: GrainRepository
) {
    operator fun invoke(): Flow<List<Grain>> = repository.observeAllGrains()
}
