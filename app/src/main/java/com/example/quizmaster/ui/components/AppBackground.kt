package com.example.quizmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizmaster.ui.theme.NeonBlue
import com.example.quizmaster.ui.theme.NeonRed
import com.example.quizmaster.ui.theme.NeonTokens

@Composable
fun AppBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonTokens.MainGradient)
    ) {
        // Dynamic Blue Glow (Top End)
        GlowCircle(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = 80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonBlue.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )

        // Dynamic Red Glow (Bottom Start)
        GlowCircle(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-70).dp, y = (-80).dp)
                .scale(1.15f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonRed.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
        )

        content()
    }
}
