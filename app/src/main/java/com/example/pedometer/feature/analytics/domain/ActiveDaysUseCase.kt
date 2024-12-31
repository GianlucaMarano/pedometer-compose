package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import javax.inject.Inject

class ActiveDaysUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>, threshold: Float = 2f): Int {
        return kilometers.count { it.km > threshold }
    }
}