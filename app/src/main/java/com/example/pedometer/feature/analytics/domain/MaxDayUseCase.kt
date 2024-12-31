package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.feature.analytics.ui.DayStat
import com.example.pedometer.model.DayData
import javax.inject.Inject

class MaxDayUseCase @Inject constructor() {
    operator fun invoke(kilometers: List<DayData>): DayStat {
        return kilometers.map{  DayStat(it.date, it.km) }
            .maxByOrNull { it.kilometers } ?: DayStat()
    }
}