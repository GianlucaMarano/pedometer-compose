package com.example.pedometer.feature.analytics.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pedometer.feature.analytics.model.CalendarDay
import com.example.pedometer.feature.analytics.ui.animatePlacement
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun KmCalendarChart(items: List<CalendarDay>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .animatePlacement()
            .fillMaxWidth()
            .height(if (items.size > 35) 400.dp else 340.dp)
    ) {
        items(items = DayOfWeek.entries.map { dayOfWeek ->
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }) { item: String ->
            Text(
                text = item.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        items(
            items = items,
            key = { item -> item.uid }
        ) { item ->
            CalendarGridItem(
                item = item,
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(),
                    placementSpec = tween(),
                    fadeOutSpec = tween()
                )
            )
        }
    }
}