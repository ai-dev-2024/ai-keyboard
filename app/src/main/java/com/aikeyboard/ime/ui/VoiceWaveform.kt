package com.aikeyboard.ime.ui

import androidx.compose.animation.core.*
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.aikeyboard.ui.theme.AITeal
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun VoiceWaveform(
    isRecording: Boolean,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 60.dp
) {
    val waveformData = remember { mutableStateListOf<Float>() }
    val animationState = rememberInfiniteTransition(label = "waveform")
    
    // Generate random waveform data when recording
    LaunchedEffect(isRecording) {
        while (isRecording) {
            waveformData.clear()
            repeat(50) {
                waveformData.add(Random.nextFloat() * 0.8f + 0.1f)
            }
            delay(100)
        }
    }
    
    // Animated color
    val colorTransition = rememberInfiniteTransition(label = "waveform_color")
    val color by colorTransition.animateColor(
        initialValue = AITeal.copy(alpha = 0.6f),
        targetValue = AITeal.copy(alpha = 1f),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveform_color_anim"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isRecording && waveformData.isNotEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val centerY = size.height / 2
                val barWidth = width / waveformData.size
                
                waveformData.forEachIndexed { index, amplitude ->
                    val barHeight = amplitude * size.height * 0.8f
                    val x = index * barWidth + barWidth / 2
                    
                    drawLine(
                        color = color,
                        start = Offset(x, centerY - barHeight / 2),
                        end = Offset(x, centerY + barHeight / 2),
                        strokeWidth = barWidth * 0.6f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }
        } else {
            // Static waveform when not recording
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val centerY = size.height / 2
                val barCount = 20
                val barWidth = width / barCount
                
                repeat(barCount) { index ->
                    val amplitude = 0.2f + (index % 3) * 0.1f
                    val barHeight = amplitude * size.height * 0.4f
                    val x = index * barWidth + barWidth / 2
                    
                    drawLine(
                        color = AITeal.copy(alpha = 0.3f),
                        start = Offset(x, centerY - barHeight / 2),
                        end = Offset(x, centerY + barHeight / 2),
                        strokeWidth = barWidth * 0.6f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }
        }
    }
}

