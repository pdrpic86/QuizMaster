package com.example.quizmaster.ui.theme

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = Color.White,
    secondary = JungleGreen,
    onSecondary = Color.Black,
    tertiary = JungleGold,
    onTertiary = Color.Black,
    background = DeepSpace,
    onBackground = TextMain,
    surface = CardDark,
    onSurface = TextMain,
    surfaceVariant = CardGreenDark,
    onSurfaceVariant = TextMuted,
    error = WrongRed,
    onError = Color.White,
    outline = NeonPurple
)

private val NeonLightColorScheme = lightColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    secondary = JungleGreen,
    onSecondary = Color.Black,
    tertiary = JungleGold,
    onTertiary = Color.Black,
    background = Color(0xFFF7F9F4),
    onBackground = Color(0xFF101010),
    surface = Color.White,
    onSurface = Color(0xFF101010),
    surfaceVariant = Color(0xFFE9F5EC),
    onSurfaceVariant = Color(0xFF4B5A50),
    error = WrongRed,
    onError = Color.White,
    outline = NeonPurple
)

val NeonShapes = Shapes(
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(20),
    large = RoundedCornerShape(28),
    extraLarge = RoundedCornerShape(36)
)

object NeonTokens {
    val MainGradient = Brush.linearGradient(
        colors = listOf(
            DeepSpace,
            ForestBlack,
            Color(0xFF160A2E),
            Color(0xFF071F16)
        )
    )

    val HeaderGradient = Brush.linearGradient(
        colors = listOf(
            NeonPink,
            NeonPurple,
            JungleGreen
        )
    )

    val AnswerSelectedGradient = Brush.horizontalGradient(
        colors = listOf(
            NeonPink.copy(alpha = 0.95f),
            NeonPurple.copy(alpha = 0.95f)
        )
    )

    val CorrectGradient = Brush.horizontalGradient(
        colors = listOf(
            CorrectGreen,
            Color(0xFF13C967)
        )
    )

    val SkipGradient = Brush.horizontalGradient(
        colors = listOf(
            JungleGold,
            Color(0xFFFFE08A)
        )
    )

    val CardBrush = Brush.linearGradient(
        colors = listOf(
            CardDark.copy(alpha = 0.96f),
            CardGreenDark.copy(alpha = 0.90f)
        )
    )

    val GlowColors = listOf(
        NeonPurple,
        NeonPink,
        JungleGreen,
        JungleGold
    )
}

object NeonAnimations {
    const val Fast = 180
    const val Normal = 350
    const val Slow = 700

    val SmoothSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val DefaultTween = tween<Float>(
        durationMillis = Normal,
        easing = FastOutSlowInEasing
    )
}

@Composable
fun rememberNeonPulse(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "neonPulse")

    return infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neonPulseValue"
    )
}

@Composable
fun QuizMasterTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> NeonDarkColorScheme
        else -> NeonLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = NeonShapes,
        content = content
    )
}
