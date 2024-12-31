package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.model.DayData
import java.time.DayOfWeek
import javax.inject.Inject

class WeekdayPercentageUseCase @Inject constructor() {

    operator fun invoke(item: List<DayData>): Float {
        val totalKilometers = item.map { it.km }.sum()
        val weekendKilometers = item.mapNotNull { item ->
            if (item.date.dayOfWeek !in listOf(
                    DayOfWeek.SATURDAY,
                    DayOfWeek.SUNDAY
                )
            ) item.km else null
        }.sum()

        return if (totalKilometers > 0) (weekendKilometers / totalKilometers) else 0f
    }
}