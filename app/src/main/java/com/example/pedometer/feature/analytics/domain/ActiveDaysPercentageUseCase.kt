package com.example.pedometer.feature.analytics.domain

import javax.inject.Inject

class ActiveDaysPercentageUseCase @Inject constructor() {
    operator fun invoke(activeDays: Int, totalDays: Int): Float {
        return if (totalDays > 0) (activeDays.toFloat() / totalDays) else 0f
    }
}