package com.example.quizmaster.ui.theme

import androidx.compose.ui.graphics.Color

// QuizMaster premium dark Material 3 palette.
// Existing public color names are retained so the current UI keeps compiling
// while the visual layer is redesigned.

val PremiumBackground = Color(0xFF0E0A1A)
val PremiumSurface = Color(0xFF171123)
val PremiumSurfaceVariant = Color(0xFF21172F)
val PremiumSurfaceHigh = Color(0xFF2A1B3D)
val PremiumPlum = Color(0xFF2A1B4A)

val PremiumPurple = Color(0xFF7B2CFF)
val PremiumPurpleBright = Color(0xFFA855F7)
val PremiumGold = Color(0xFFFFB84D)
val PremiumBlue = Color(0xFF3B82F6)

val PremiumSuccess = Color(0xFF22C55E)
val PremiumError = Color(0xFFE53935)
val PremiumWarning = Color(0xFFF59E0B)

val PremiumText = Color(0xFFF7F4FF)
val PremiumTextMuted = Color(0xFFB8B0C6)
val PremiumOutline = Color(0xFF6E5B82)

// Compatibility aliases used by existing screens and components.
val NeonBlue = PremiumBlue
val NeonRed = PremiumError
val NeonPurple = PremiumPurple
val NeonPink = PremiumPurpleBright
val JungleGreen = PremiumSuccess
val JungleGold = PremiumGold
val AquaCyan = Color(0xFF38BDF8)

val DeepBlack = PremiumBackground
val DarkBlue = PremiumSurface
val DarkRed = Color(0xFF3A1117)

val DeepSpace = PremiumBackground
val ForestBlack = Color(0xFF110D1B)
val CardDark = PremiumSurface
val CardGreenDark = PremiumSurfaceVariant
val CardAccentDark = Color(0xFF25141B)

val TextMain = PremiumText
val TextMuted = PremiumTextMuted

val CorrectGreen = PremiumSuccess
val WrongRed = PremiumError
val WarningGold = PremiumGold

// Legacy names kept until all old UI code is replaced.
val QuizBlack = DeepBlack
val QuizBlue = NeonBlue
val QuizRed = NeonRed
val QuizBlueLight = Color(0xFF60A5FA)
val QuizText = TextMain
val QuizBlackSoft = CardDark
val QuizCardBlue = DarkBlue
val QuizCardBlueLight = PremiumSurfaceHigh
val QuizDarkBlue = DarkBlue
val QuizRedDark = DarkRed
