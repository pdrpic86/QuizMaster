package com.example.quizmaster.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val PremiumDarkColorScheme = darkColorScheme(
    primary = PremiumPurple,
    onPrimary = Color.White,
    primaryContainer = PremiumPlum,
    onPrimaryContainer = Color(0xFFF0E5FF),

    secondary = PremiumSuccess,
    onSecondary = Color(0xFF06140C),
    secondaryContainer = Color(0xFF10391F),
    onSecondaryContainer = Color(0xFFD8FFE3),

    tertiary = PremiumGold,
    onTertiary = Color(0xFF281800),
    tertiaryContainer = Color(0xFF4A3008),
    onTertiaryContainer = Color(0xFFFFE6B2),

    background = PremiumBackground,
    onBackground = PremiumText,

    surface = PremiumSurface,
    onSurface = PremiumText,
    surfaceVariant = PremiumSurfaceVariant,
    onSurfaceVariant = PremiumTextMuted,

    surfaceContainerLowest = Color(0xFF09070F),
    surfaceContainerLow = Color(0xFF120D1C),
    surfaceContainer = PremiumSurface,
    surfaceContainerHigh = PremiumSurfaceVariant,
    surfaceContainerHighest = PremiumSurfaceHigh,

    outline = PremiumOutline,
    outlineVariant = PremiumOutline.copy(alpha = 0.38f),

    error = PremiumError,
    onError = Color.White,
    errorContainer = Color(0xFF4A1519),
    onErrorContainer = Color(0xFFFFDAD8),

    inverseSurface = PremiumText,
    inverseOnSurface = PremiumBackground,
    inversePrimary = Color(0xFFD7B8FF),
    scrim = Color.Black
)

val NeonShapes = Shapes(
    extraSmall = RoundedCornerShape(8),
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(16),
    large = RoundedCornerShape(24),
    extraLarge = RoundedCornerShape(32)
)

object NeonTokens {
    val MainGradient = Brush.linearGradient(
        colors = listOf(
            PremiumBackground,
            Color(0xFF120D1F),
            Color(0xFF1B102B),
            PremiumBackground
        )
    )

    val HeaderGradient = Brush.linearGradient(
        colors = listOf(
            PremiumGold,
            PremiumPurpleBright,
            PremiumPurple
        )
    )

    val AnswerSelectedGradient = Brush.horizontalGradient(
        colors = listOf(
            PremiumPurple,
            PremiumPurpleBright
        )
    )

    val CorrectGradient = Brush.horizontalGradient(
        colors = listOf(
            PremiumSuccess,
            Color(0xFF16A34A)
        )
    )

    val WrongGradient = Brush.horizontalGradient(
        colors = listOf(
            PremiumError,
            Color(0xFFB91C1C)
        )
    )

    val SkipGradient = Brush.horizontalGradient(
        colors = listOf(
            PremiumGold,
            Color(0xFFFFD98A)
        )
    )

    val CardBrush = Brush.linearGradient(
        colors = listOf(
            PremiumSurface.copy(alpha = 0.98f),
            PremiumSurfaceVariant.copy(alpha = 0.92f)
        )
    )

    val PremiumBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.18f),
            PremiumPurple.copy(alpha = 0.44f),
            PremiumGold.copy(alpha = 0.18f),
            Color.Transparent
        )
    )

    val GlowColors = listOf(
        PremiumPurple,
        PremiumPurpleBright,
        PremiumGold,
        PremiumSuccess,
        PremiumError
    )
}

object NeonAnimations {
    const val Fast = 160
    const val Normal = 280
    const val Slow = 520

    val SmoothSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
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
    val infiniteTransition = rememberInfiniteTransition(label = "premiumPulse")

    return infiniteTransition.animateFloat(
        initialValue = 0.72f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "premiumPulseValue"
    )
}

@Composable
fun QuizMasterTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PremiumDarkColorScheme,
        typography = QuizMasterTypography,
        shapes = NeonShapes,
        content = content
    )
}
