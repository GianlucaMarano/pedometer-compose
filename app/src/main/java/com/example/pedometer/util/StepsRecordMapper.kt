package com.example.pedometer.util


import com.example.pedometer.model.DayData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun List<DayData>.toStepsThisMonth(): Int {
    val startOfMonth = LocalDateTime.now()
        .withDayOfMonth(1)
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
        .atZone(ZoneId.systemDefault()).toInstant()
    return this.filter {
        it.date.atZone(ZoneId.systemDefault())
            .toLocalDate() >= startOfMonth.atZone(ZoneId.systemDefault()).toLocalDate()
    }.sumOf { it.steps }.toInt()
}

fun List<DayData>.toStepsToday(): Int =
    this.find { it.date.toLocalDate() == LocalDate.now() }?.steps?.toInt() ?: 0