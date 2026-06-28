package com.example.quizmaster.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.quizmaster.ui.theme.AquaCyan
import com.example.quizmaster.ui.theme.JungleGold
import com.example.quizmaster.ui.theme.NeonPink
import com.example.quizmaster.ui.theme.NeonPurple

@Composable
fun FloatingThemeModeSwitcher(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Popup(
        alignment = Alignment.TopEnd,
        properties = PopupProperties(
            focusable = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        ThemeModeSwitcher(
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange,
            modifier = modifier
                .statusBarsPadding()
                .padding(top = 12.dp, end = 16.dp)
        )
    }
}

@Composable
fun ThemeModeSwitcher(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (isDarkTheme) "Dark" else "Light"
    val icon = if (isDarkTheme) Icons.Rounded.DarkMode else Icons.Rounded.LightMode

    val borderColor by animateColorAsState(
        targetValue = if (isDarkTheme) AquaCyan.copy(alpha = 0.42f) else JungleGold.copy(alpha = 0.48f),
        label = "theme switch border"
    )

    val glowSize by animateDpAsState(
        targetValue = if (isDarkTheme) 18.dp else 10.dp,
        label = "theme switch glow"
    )

    Surface(
        modifier = modifier
            .shadow(
                elevation = glowSize,
                shape = RoundedCornerShape(999.dp),
                ambientColor = if (isDarkTheme) NeonPurple else JungleGold,
                spotColor = if (isDarkTheme) NeonPink else JungleGold
            ),
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 10.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isDarkTheme) AquaCyan else JungleGold,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Switch(
                checked = isDarkTheme,
                onCheckedChange = onThemeChange,
                thumbContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                        tint = Color.Unspecified
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AquaCyan,
                    checkedTrackColor = NeonPurple.copy(alpha = 0.72f),
                    checkedBorderColor = AquaCyan.copy(alpha = 0.65f),
                    uncheckedThumbColor = JungleGold,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    uncheckedBorderColor = JungleGold.copy(alpha = 0.50f)
                )
            )
        }
    }
}
