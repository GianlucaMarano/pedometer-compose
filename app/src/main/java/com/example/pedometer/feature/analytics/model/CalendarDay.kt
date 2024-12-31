package com.example.pedometer.feature.analytics.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.UUID

data class CalendarDay(
    val uid: String = UUID.randomUUID().toString(),
    val date: LocalDateTime,
    val dayOfWeek: DayOfWeek,
    val isInCurrentMonth: Boolean,
    val isToday: Boolean = false,
    val noData: Boolean = false,
    val steps: Int = 0,
    val km: Float = 0.0f,
)
