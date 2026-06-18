import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp

// ------------------------------------------------------------
// NEON JUNGLE COLORS
// ------------------------------------------------------------

val NeonPurple = Color(0xFF9D4DFF)
val NeonPink = Color(0xFFFF4DDB)
val JungleGreen = Color(0xFF39FF8A)
val JungleGold = Color(0xFFFFB84D)

val DeepSpace = Color(0xFF050711)
val ForestBlack = Color(0xFF07120D)
val CardDark = Color(0xFF101822)
val CardGreenDark = Color(0xFF102417)

val TextMain = Color(0xFFF7F4FF)
val TextMuted = Color(0xFFB9B2C8)

val CorrectGreen = Color(0xFF39FF8A)
val WrongRed = Color(0xFFFF4D6D)
val WarningGold = Color(0xFFFFC857)

// ------------------------------------------------------------
// MATERIAL 3 COLOR SCHEME
// ------------------------------------------------------------

private val NeonJungleDarkColorScheme = darkColorScheme(
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

private val NeonJungleLightColorScheme = lightColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,

    secondary = JungleGreen,
    onSecondary = Color.Black,

    tertiary = JungleGold,
    onTertiary = Color.Black,

    background = Color(0xFFF7F9F4),
    onBackground = Color(0xFF101010),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101010),

    surfaceVariant = Color(0xFFE9F5EC),
    onSurfaceVariant = Color(0xFF4B5A50),

    error = WrongRed,
    onError = Color.White,

    outline = NeonPurple
)

// ------------------------------------------------------------
// TYPOGRAPHY
// Poppins/Nunito fallback. Add fonts later if you want real files.
// ------------------------------------------------------------

val NeonJungleTypography = Typography(
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

val NeonJungleShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(12),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(20),
    large = androidx.compose.foundation.shape.RoundedCornerShape(28),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(36)
)

// ------------------------------------------------------------
// CUSTOM THEME OBJECT
// Use this in screens for gradients, glow colors, etc.
// ------------------------------------------------------------

object NeonJungleTokens {

    val MainGradient = Brush.linearGradient(
        colors = listOf(
            DeepSpace,
            ForestBlack,
            Color(0xFF160A2E),
            Color(0xFF071F16)
        )
    )

    val NeonHeaderGradient = Brush.linearGradient(
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
            JungleGreen,
            Color(0xFF13C967)
        )
    )

    val GoldGradient = Brush.horizontalGradient(
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

// ------------------------------------------------------------
// ANIMATION TOKENS
// ------------------------------------------------------------

object NeonJungleAnimations {

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

// ------------------------------------------------------------
// ANIMATED GLOW VALUE
// Usage:
// val glow by rememberNeonPulse()
// ------------------------------------------------------------

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
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> NeonJungleDarkColorScheme
        darkTheme -> NeonJungleDarkColorScheme
        else -> NeonJungleLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NeonJungleTypography,
        shapes = NeonJungleShapes,
        content = content
    )
}