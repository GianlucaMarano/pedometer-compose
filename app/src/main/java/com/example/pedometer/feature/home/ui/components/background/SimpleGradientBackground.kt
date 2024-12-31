package com.example.pedometer.feature.home.ui.components.background


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import com.example.pedometer.ui.theme.Purple40
import com.example.pedometer.ui.theme.Purple80
import com.example.pedometer.ui.theme.White

fun Modifier.simpleGradient(): Modifier =
    drawWithCache {
        val gradientBrush = Brush.verticalGradient(listOf(Purple40, Purple80, White))
        onDrawBehind {
            drawRect(gradientBrush, alpha = 1f)
        }
    }