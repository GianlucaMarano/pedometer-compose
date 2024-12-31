package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import java.time.DayOfWeek
import javax.inject.Inject

class WeekdayAverageDistanceUseCase @Inject constructor() {
    operator fun invoke(item: List<DayData>): Float {
        val weekdayDistances = item.mapNotNull { item ->
            if (item.date.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) item.km else null
        }
        return if (weekdayDistances.isNotEmpty()) weekdayDistances.average().toFloat() else 0f
    }
}