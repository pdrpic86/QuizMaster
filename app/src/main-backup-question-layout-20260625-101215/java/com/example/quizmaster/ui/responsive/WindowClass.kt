package com.example.quizmaster.ui.responsive

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowClass {
    Compact,
    Medium,
    Expanded
}

data class ResponsiveInfo(
    val widthClass: WindowClass,
    val screenWidth: Dp,
    val horizontalPadding: Dp,
    val maxContentWidth: Dp,
    val cardSpacing: Dp,
    val gridMinCellSize: Dp,
    val useTwoColumns: Boolean
)

@Composable
fun rememberResponsiveInfo(): ResponsiveInfo {
    val config = LocalConfiguration.current
    val width = config.screenWidthDp.dp

    val widthClass = when {
        width < 600.dp -> WindowClass.Compact
        width < 840.dp -> WindowClass.Medium
        else -> WindowClass.Expanded
    }

    return when (widthClass) {
        WindowClass.Compact -> ResponsiveInfo(
            widthClass = widthClass,
            screenWidth = width,
            horizontalPadding = 16.dp,
            maxContentWidth = 520.dp,
            cardSpacing = 14.dp,
            gridMinCellSize = 160.dp,
            useTwoColumns = false
        )

        WindowClass.Medium -> ResponsiveInfo(
            widthClass = widthClass,
            screenWidth = width,
            horizontalPadding = 24.dp,
            maxContentWidth = 720.dp,
            cardSpacing = 18.dp,
            gridMinCellSize = 220.dp,
            useTwoColumns = true
        )

        WindowClass.Expanded -> ResponsiveInfo(
            widthClass = widthClass,
            screenWidth = width,
            horizontalPadding = 32.dp,
            maxContentWidth = 980.dp,
            cardSpacing = 22.dp,
            gridMinCellSize = 260.dp,
            useTwoColumns = true
        )
    }
}