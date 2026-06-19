package com.example.quizmaster.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizmaster.ui.theme.AquaCyan
import com.example.quizmaster.ui.theme.JungleGreen
import com.example.quizmaster.ui.theme.NeonPink
import com.example.quizmaster.ui.theme.NeonPurple
import com.example.quizmaster.ui.theme.NeonTokens

@Composable
fun AppBackground(
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "ambient background")
    val pulse by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient scale"
    )
    val drift by transition.animateFloat(
        initialValue = -14f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient drift"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonTokens.MainGradient)
    ) {
        GlowCircle(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (42 + drift).dp, y = 70.dp)
                .scale(1.05f * pulse)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonPurple.copy(alpha = 0.30f), Color.Transparent)
                    )
                )
        )

        GlowCircle(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-110).dp, y = drift.dp)
                .scale(1.35f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AquaCyan.copy(alpha = 0.14f), Color.Transparent)
                    )
                )
        )

        GlowCircle(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-76).dp, y = (-90 - drift).dp)
                .scale(1.12f * pulse)
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonPink.copy(alpha = 0.22f), Color.Transparent)
                    )
                )
        )

        GlowCircle(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 54.dp, y = (-36).dp)
                .scale(0.82f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(JungleGreen.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        content()
    }
}
