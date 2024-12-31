package com.example.pedometer.feature.analytics.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.pedometer.data.Timeframe
import com.example.pedometer.R
import com.example.pedometer.ui.theme.Typography
import com.example.pedometer.ui.theme.White

@Composable
fun NumericValue(
    value: Int,
    label: String,
    style: TextStyle = Typography.displaySmall,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row (verticalAlignment = Alignment.Bottom){
            Text(
                text = "$value",
                style = style,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alignBy(FirstBaseline),
            )
        }

        Text(
            text = label,
            style =  MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            )


    }
}

@Composable
fun NumericValue(
    value: Float,
    label: String,
    unit: String = "Km",
    style: TextStyle = Typography.displaySmall,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row (verticalAlignment = Alignment.Bottom){
            Text(
                text = "$value",
                style = style,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alignBy(FirstBaseline),
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alignBy(FirstBaseline)
            )
        }

        Text(
            text = label,
            style =  MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,

            )


    }
}

@Preview(showBackground = true)
@Composable
private fun NumericValuePreview() {
    NumericValue(value = 100, label = "Daily average")
}

@Preview(showBackground = true)
@Composable
private fun NumericValueFloatPreview() {
    NumericValue(value = 15f, label = "Max Km")
}