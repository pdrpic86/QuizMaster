package com.example.quizmaster.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val QuizMasterBlackColors = darkColorScheme(
    primary = QuizBlueLight,
    onPrimary = QuizBlack,

    secondary = QuizBlue,
    onSecondary = QuizText,

    tertiary = QuizRed,
    onTertiary = QuizText,

    background = QuizBlack,
    onBackground = QuizText,

    surface = QuizBlackSoft,
    onSurface = QuizText,

    surfaceVariant = QuizCardBlue,
    onSurfaceVariant = QuizText,

    primaryContainer = QuizCardBlueLight,
    onPrimaryContainer = QuizText,

    secondaryContainer = QuizDarkBlue,
    onSecondaryContainer = QuizText,

    tertiaryContainer = QuizRedDark,
    onTertiaryContainer = QuizText,

    error = WrongRed,
    onError = QuizText
)

@Composable
fun QuizMasterTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuizMasterBlackColors,
        typography = QuizMasterTypography,
        content = content
    )
}