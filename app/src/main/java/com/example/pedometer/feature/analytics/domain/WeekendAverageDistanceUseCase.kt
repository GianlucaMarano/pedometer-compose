package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import java.time.DayOfWeek
import javax.inject.Inject

class WeekendAverageDistanceUseCase @Inject constructor() {
    operator fun invoke(item: List<DayData>): Float {
        val weekendDistances = item.mapNotNull { item ->
            if (item.date.dayOfWeek == DayOfWeek.SATURDAY || item.date.dayOfWeek == DayOfWeek.SUNDAY) item.km else null
        }
        return if (weekendDistances.isNotEmpty()) weekendDistances.average().toFloat() else 0f
    }
}