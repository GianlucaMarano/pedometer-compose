package com.example.pedometer.util

const val METER_TO_KM = 1000

fun Long.toKm() = (this * 0.7f)/METER_TO_KM

fun Int.toKm() = (this * 0.7f)/METER_TO_KM