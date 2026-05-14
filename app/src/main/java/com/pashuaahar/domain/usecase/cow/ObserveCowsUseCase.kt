package com.pashuaahar.domain.usecase.cow

import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.repository.CowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCowsUseCase @Inject constructor(
    private val repository: CowRepository
) {
    operator fun invoke(): Flow<List<Cow>> = repository.observeAllCows()
}
