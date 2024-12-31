package com.example.pedometer.model

import java.time.LocalDateTime
import java.util.UUID

data class DayData(
    val uid: String = UUID.randomUUID().toString(),
    val date: LocalDateTime,
    val steps: Long,
    val km: Float = 0f,
)
