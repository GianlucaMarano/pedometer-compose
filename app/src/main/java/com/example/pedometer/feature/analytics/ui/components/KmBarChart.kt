package com.example.pedometer.feature.analytics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale


@Composable
fun KmBarChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    maxBarHeight: Dp = 100.dp,
    barWidth: Dp = 20.dp,
    spacing: Dp = 8.dp
) {
    val maxValue = data.maxOrNull() ?: 1f
    val numIntervals = if (maxValue > 5) 2 else 2
    val intervalValue = maxValue / numIntervals
    val intervals = ((0 until numIntervals).map { it * intervalValue } + maxValue).reversed()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight + 60.dp)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Etichette verticali a sinistra
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .height((maxBarHeight.value + 50).dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Inverti l'ordine delle etichette
                intervals.forEach {
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", it),
                        style = MaterialTheme.typography.labelSmall,
                        color = labelColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                    )
                }

            }
            LazyRow(verticalAlignment = Alignment.Bottom) {
                itemsIndexed(items = data) { index, value ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = spacing / 2)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(((value / maxValue) * maxBarHeight.value).dp)
                                .width(barWidth)
                                .background(barColor, shape = RoundedCornerShape(4.dp))
                        )
                        Text(
                            text = labels.getOrNull(index) ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = labelColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}