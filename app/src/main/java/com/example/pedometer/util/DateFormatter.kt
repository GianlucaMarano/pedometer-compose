package com.example.pedometer.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.topDayFormat(): String =
    this.format(DateTimeFormatter.ofPattern("d/MM/yyyy", Locale("it")))

fun LocalDateTime.topMonthFormat(): String =
    this.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("it")))
        .replaceFirstChar { it.uppercase() }

fun LocalDateTime.topWeekFormat(): String =
    this.format(DateTimeFormatter.ofPattern("d/MM/yyyy", Locale("it")))