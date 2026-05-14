package com.pashuaahar.domain.usecase.grain

import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.repository.GrainRepository
import javax.inject.Inject

class UpdateGrainUseCase @Inject constructor(
    private val repository: GrainRepository
) {
    suspend operator fun invoke(grain: Grain) = repository.updateGrain(grain)
}
