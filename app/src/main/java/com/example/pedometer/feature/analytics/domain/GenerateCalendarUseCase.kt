package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.feature.analytics.model.CalendarDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GenerateCalendarUseCase @Inject constructor(){

    operator fun invoke(selectedDate: LocalDateTime): Flow<List<CalendarDay>> = flow {
        val firstDayOfMonth = selectedDate.toLocalDate().withDayOfMonth(1)
        val firstDayOfCalendar = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val daysInMonth = firstDayOfMonth.lengthOfMonth()
        val lastDayOfMonth = firstDayOfMonth.plusDays((daysInMonth - 1).toLong())
        val lastDayOfCalendar = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val totalDays = (lastDayOfCalendar.toEpochDay() - firstDayOfCalendar.toEpochDay() + 1).toInt()
        val gridDays = if (totalDays <= 35) 35 else 42

        val calendarDays = (0 until gridDays).map { index ->
            val currentDate = firstDayOfCalendar.plusDays(index.toLong())
            CalendarDay(
                date = currentDate.atStartOfDay(),
                dayOfWeek = currentDate.dayOfWeek,
                isInCurrentMonth = currentDate.month == firstDayOfMonth.month
            )
        }
        emit(calendarDays)
    }
}