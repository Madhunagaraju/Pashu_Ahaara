package com.pashuaahar.domain.usecase.cow

import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.repository.CowRepository
import javax.inject.Inject

class AddCowUseCase @Inject constructor(
    private val repository: CowRepository
) {
    suspend operator fun invoke(cow: Cow): Long = repository.addCow(cow)
}
