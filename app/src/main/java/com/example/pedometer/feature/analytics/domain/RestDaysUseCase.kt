package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import javax.inject.Inject

class RestDaysUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>, threshold: Float = 2f): Int {
        return kilometers.filter { it.km > 0 }.count {  it.km in 0f.rangeTo(threshold) }
    }
}