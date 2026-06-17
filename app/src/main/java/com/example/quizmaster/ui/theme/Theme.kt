package com.example.quizmaster.ui.theme

import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ------------------------------------------------------------
// MATERIAL 3 COLOR SCHEME (Neon Black, Blue, Red)
// ------------------------------------------------------------

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,

    secondary = NeonRed,
    onSecondary = Color.White,

    tertiary = WarningGold,
    onTertiary = Color.Black,

    background = DeepSpace,
    onBackground = TextMain,

    surface = CardDark,
    onSurface = TextMain,

    surfaceVariant = DarkBlue,
    onSurfaceVariant = TextMuted,

    error = WrongRed,
    onError = Color.White,

    outline = NeonBlue
)

private val NeonLightColorScheme = lightColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,

    secondary = NeonRed,
    onSecondary = Color.White,

    tertiary = WarningGold,
    onTertiary = Color.Black,

    background = Color(0xFFF7F9F4),
    onBackground = Color(0xFF101010),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101010),

    surfaceVariant = Color(0xFFE9F1F5),
    onSurfaceVariant = Color(0xFF4B515A),

    error = WrongRed,
    onError = Color.White,

    outline = NeonBlue
)

// ------------------------------------------------------------
// TYPOGRAPHY
// ------------------------------------------------------------

val NeonTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 42.sp,
        letterSpacing = 1.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.4.sp
    )
)

// ------------------------------------------------------------
// SHAPES
// ------------------------------------------------------------

val NeonShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(12),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(20),
    large = androidx.compose.foundation.shape.RoundedCornerShape(28),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(36)
)

// ------------------------------------------------------------
// CUSTOM THEME OBJECT (Tokens)
// ------------------------------------------------------------

object NeonTokens {

    val MainGradient = Brush.linearGradient(
        colors = listOf(
            DeepSpace,
            ForestBlack,
            Color(0xFF0A192F), // Deep Blue
            Color(0xFF1F0707)  // Deep Red
        )
    )

    val HeaderGradient = Brush.linearGradient(
        colors = listOf(
            NeonBlue,
            Color(0xFF60A5FA),
            NeonRed
        )
    )

    val AnswerSelectedGradient = Brush.horizontalGradient(
        colors = listOf(
            NeonBlue.copy(alpha = 0.95f),
            Color(0xFF1E40AF).copy(alpha = 0.95f)
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
            WarningGold,
            Color(0xFFFFE08A)
        )
    )

    val CardBrush = Brush.linearGradient(
        colors = listOf(
            CardDark.copy(alpha = 0.96f),
            Color(0xFF0F1B2E).copy(alpha = 0.90f)
        )
    )

    val GlowColors = listOf(
        NeonBlue,
        NeonRed,
        WarningGold
    )
}

// ------------------------------------------------------------
// ANIMATION TOKENS
// ------------------------------------------------------------

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

// ------------------------------------------------------------
// MAIN APP THEME
// ------------------------------------------------------------

@Composable
fun QuizMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
        typography = NeonTypography,
        shapes = NeonShapes,
        content = content
    )
}
