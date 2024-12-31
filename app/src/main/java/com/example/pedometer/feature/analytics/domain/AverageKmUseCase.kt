package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import javax.inject.Inject

class AverageKmUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>): Float {
        val km = kilometers.map { it.km }
        val activeDays = km.count { it > 0 }
        return if (activeDays > 0) km.sum() / activeDays else 0f
    }
}