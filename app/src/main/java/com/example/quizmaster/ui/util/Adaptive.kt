package com.example.quizmaster.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

data class AdaptiveScale(
    val scale: Float,
    val compact: Boolean,
    val tiny: Boolean
)

@Composable
fun rememberAdaptiveScale(): AdaptiveScale {
    val config = LocalConfiguration.current

    val width = config.screenWidthDp.toFloat()
    val height = config.screenHeightDp.toFloat()

    /*
        Base cilj nije stvarna veličina ekrana, nego "koliko UI-a želimo ugurati".
        Veći targetHeight = manji UI = više šanse da sve stane na Xiaomi / manje ekrane.
    */
    val targetWidth = 393f

    val targetHeight = when {
        height < 650f -> 1080f
        height < 720f -> 1020f
        height < 800f -> 960f
        else -> 920f
    }

    val widthScale = width / targetWidth
    val heightScale = height / targetHeight

    val scale = minOf(widthScale, heightScale)
        .coerceIn(0.62f, 1.15f)

    return AdaptiveScale(
        scale = scale,
        compact = height < 800f,
        tiny = height < 700f
    )
}

@Composable
fun AdaptiveDensity(
    content: @Composable () -> Unit
) {
    val adaptive = rememberAdaptiveScale()
    val currentDensity = LocalDensity.current

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = currentDensity.density * adaptive.scale,
            fontScale = currentDensity.fontScale * adaptive.scale
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(
                    bottom = when {
                        adaptive.tiny -> 28.dp
                        adaptive.compact -> 20.dp
                        else -> 12.dp
                    }
                )
        ) {
            content()
        }
    }
}
