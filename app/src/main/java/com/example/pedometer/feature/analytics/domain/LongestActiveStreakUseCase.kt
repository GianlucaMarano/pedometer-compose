package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import javax.inject.Inject

class LongestActiveStreakUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>, threshold: Float = 2f): Int {
        return kilometers
            .map {
                when {
                    (it.km > 0 && it.km < threshold) -> "R"
                    it.km > threshold -> "A"
                    else -> ""
                }
            }
            .joinToString("")
            .split("R")
            .maxOfOrNull { it.length } ?: 0
    }
}