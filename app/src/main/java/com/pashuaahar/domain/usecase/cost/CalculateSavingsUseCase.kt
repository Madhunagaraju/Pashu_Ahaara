package com.pashuaahar.domain.usecase.cost

import com.pashuaahar.domain.model.CostSummary
import javax.inject.Inject

class CalculateSavingsUseCase @Inject constructor() {
    operator fun invoke(homemadeDaily: Double, marketPricePerKg: Double, intakeKg: Double): CostSummary {
        val marketDaily = marketPricePerKg * intakeKg
        val monthlySavings = (marketDaily - homemadeDaily) * 30.0
        val yearlySavings = monthlySavings * 12.0
        return CostSummary(
            homemadeDaily = homemadeDaily,
            marketDaily = marketDaily,
            monthlySavings = monthlySavings,
            yearlySavings = yearlySavings
        )
    }
}
