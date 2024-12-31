package com.example.pedometer.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Float.roundTo(decimals: Int): Float = BigDecimal(this.toDouble())
    .setScale(decimals, RoundingMode.HALF_UP)
    .toFloat()