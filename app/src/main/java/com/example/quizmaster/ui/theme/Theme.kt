package com.example.quizmaster.ui.theme

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.State
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ------------------------------------------------------------
// MATERIAL 3 COLOR SCHEME - polished custom default + dynamic support
// ------------------------------------------------------------

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF27124A),
    onPrimaryContainer = Color(0xFFEBDDFF),

    secondary = JungleGreen,
    onSecondary = Color(0xFF06140C),
    secondaryContainer = Color(0xFF0E3E25),
    onSecondaryContainer = Color(0xFFC8FFD9),

    tertiary = JungleGold,
    onTertiary = Color(0xFF221300),
    tertiaryContainer = Color(0xFF4A2F08),
    onTertiaryContainer = Color(0xFFFFE3B0),

    background = DeepSpace,
    onBackground = TextMain,

    surface = CardDark,
    onSurface = TextMain,
    surfaceVariant = CardGreenDark,
    onSurfaceVariant = TextMuted,
    surfaceContainerLowest = Color(0xFF080B15),
    surfaceContainerLow = Color(0xFF0D1320),
    surfaceContainer = Color(0xFF111A26),
    surfaceContainerHigh = Color(0xFF162233),
    surfaceContainerHighest = Color(0xFF1C2A3D),

    error = WrongRed,
    onError = Color.White,
    errorContainer = Color(0xFF5C1020),
    onErrorContainer = Color(0xFFFFD9DF),

    outline = NeonPurple.copy(alpha = 0.72f),
    outlineVariant = AquaCyan.copy(alpha = 0.20f),
    scrim = Color.Black
)

private val NeonLightColorScheme = lightColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEBDDFF),
    onPrimaryContainer = Color(0xFF230B4B),

    secondary = Color(0xFF008A46),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8FFD9),
    onSecondaryContainer = Color(0xFF00210E),

    tertiary = Color(0xFF8A5A00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE3B0),
    onTertiaryContainer = Color(0xFF2A1700),

    background = Color(0xFFF9F7FF),
    onBackground = Color(0xFF171225),

    surface = Color.White,
    onSurface = Color(0xFF171225),
    surfaceVariant = Color(0xFFECE4F4),
    onSurfaceVariant = Color(0xFF50465A),

    error = WrongRed,
    onError = Color.White,
    outline = NeonPurple.copy(alpha = 0.60f)
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
            AquaCyan,
            JungleGreen
        )
    )

    val AnswerSelectedGradient = Brush.horizontalGradient(
        colors = listOf(
            NeonPurple.copy(alpha = 0.95f),
            NeonPink.copy(alpha = 0.88f)
        )
    )

    val CorrectGradient = Brush.horizontalGradient(
        colors = listOf(
            CorrectGreen,
            Color(0xFF13C967)
        )
    )

    val WrongGradient = Brush.horizontalGradient(
        colors = listOf(
            WrongRed,
            Color(0xFFDC2626)
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
            CardGreenDark.copy(alpha = 0.88f)
        )
    )

    val PremiumBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.30f),
            NeonPurple.copy(alpha = 0.36f),
            JungleGreen.copy(alpha = 0.22f),
            Color.Transparent
        )
    )

    val GlowColors = listOf(
        NeonPurple,
        NeonPink,
        AquaCyan,
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

    val SoftSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
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
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "neonPulseValue"
    )
}

@Composable
fun QuizMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        typography = QuizMasterTypography,
        shapes = NeonShapes,
        content = content
    )
}
