package com.example.pedometer.feature.easteregg.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedometer.R
import com.example.pedometer.feature.easteregg.ui.components.HeartCanvas
import com.example.pedometer.feature.easteregg.ui.components.vibrateHeartBeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EasterEggScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val scale = remember { Animatable(1f) }
    val translation = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val draggableState = rememberDraggable2DState { delta ->
        coroutineScope.launch {
            translation.snapTo(translation.value.plus(delta * scale.value))
        }
    }
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.made_with),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
        )
        HeartCanvas(
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    translation.value.let {
                        translationX = it.x
                        translationY = it.y
                    }
                }
                .draggable2D(
                    state = draggableState,
                    onDragStopped = { velocity ->
                        coroutineScope.launch {
                            launch {
                                scale.animateTo(
                                    targetValue = scale.value - 0.2f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium,
                                    )
                                )
                            }
                            launch {
                                translation.animateTo(
                                    Offset(0f, 0f),
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMediumLow,
                                    )
                                )
                            }
                        }
                    })
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        coroutineScope.launch {
                            vibrateHeartBeat(context)
                            val newScale = scale.value + 0.2f
                            scale.animateTo(
                                targetValue = newScale,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium,
                                )
                            )
                        }
                    }, onTap = {
                        coroutineScope.launch {
                            vibrateHeartBeat(context)
                            val newScale = if (scale.value < 2f) scale.value - 0.1f else 1f
                            scale.animateTo(
                                targetValue = newScale,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioHighBouncy,
                                    stiffness = Spring.StiffnessMediumLow,
                                )
                            )
                        }
                    })
                }

        )
    }


}



@Preview
@Composable
private fun EGPreview() {
    EasterEggScreen()
}
