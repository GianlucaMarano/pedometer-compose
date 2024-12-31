package com.example.pedometer.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedometer.data.Timeframe
import com.example.pedometer.feature.home.ui.StepsUiIntent

@Composable
fun PeriodSelector(
    options: List<Timeframe>,
    selectedOption: Timeframe,
    onOptionSelected: (StepsUiIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed {idx,  option ->
            val isSelected = option == selectedOption
            val shape = when(idx){
                0 -> RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, bottomEnd = 4.dp, topEnd = 4.dp)
                options.size - 1 -> RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 4.dp, topStart = 4.dp)
                else -> RoundedCornerShape(4.dp)
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                        shape = shape
                    )
                    .clickable { onOptionSelected(StepsUiIntent.PeriodSelected(option)) }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.name,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                    style =  MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun PeriodSelectorPreview() {
    PeriodSelector(
        options = Timeframe.entries,
        selectedOption = Timeframe.DAY,
        onOptionSelected = {}
    )
}