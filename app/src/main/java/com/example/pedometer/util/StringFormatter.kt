package com.example.pedometer.util

import java.util.Locale

fun Int.toCompactString(): String {
    return when {
        this >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.getDefault(),"%.1fk", this / 1_000.0)
        else -> this.toString()
    }
}

fun Float.toCompactKm(): String = "${String.format(Locale.getDefault(), "%.1f", this)} Km"
