package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import javax.inject.Inject

class TotalKmUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>): Float {
        return kilometers.map { it.km }.sum()
    }
}