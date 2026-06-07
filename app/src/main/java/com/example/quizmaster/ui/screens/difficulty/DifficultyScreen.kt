package com.example.quizmaster.ui.screens.difficulty

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizmaster.ui.components.CategoryBadge
import com.example.quizmaster.ui.components.CategoryHeader
import com.example.quizmaster.ui.components.GlowCircle

@Composable
fun DifficultyScreen(
    selectedCategory: String,
    onBackClick: () -> Unit,
    onDifficultySelected: (String) -> Unit
) {
    val screenAlpha = remember { Animatable(0f) }
    val screenScale = remember { Animatable(0.94f) }

    LaunchedEffect(Unit) {
        screenAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 420,
                easing = FastOutSlowInEasing
            )
        )

        screenScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 520,
                easing = EaseOutBack
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        GlowCircle(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = 90.dp)
        )

        GlowCircle(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-70).dp, y = (-80).dp)
                .scale(1.15f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp)
                .alpha(screenAlpha.value)
                .scale(screenScale.value)
        ) {
            CategoryHeader(
                title = "Difficulty",
                subtitle = selectedCategory,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Choose difficulty",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: $selectedCategory",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DifficultyCard(
                    title = "Easy",
                    subtitle = "Simple warm-up questions",
                    badge = "Difficulty 1",
                    icon = Icons.Rounded.Star,
                    onClick = {
                        onDifficultySelected("Easy")
                    }
                )

                DifficultyCard(
                    title = "Medium",
                    subtitle = "Balanced challenge",
                    badge = "Difficulty 2",
                    icon = Icons.Rounded.Bolt,
                    onClick = {
                        onDifficultySelected("Medium")
                    }
                )

                DifficultyCard(
                    title = "Hard",
                    subtitle = "For serious quiz players",
                    badge = "Difficulty 3",
                    icon = Icons.Rounded.Timer,
                    onClick = {
                        onDifficultySelected("Hard")
                    }
                )

                DifficultyCard(
                    title = "Mixed",
                    subtitle = "Random mix of all difficulties",
                    badge = "Recommended",
                    icon = Icons.Rounded.Category,
                    onClick = {
                        onDifficultySelected("Mixed")
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Next: real questions from the database.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DifficultyCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                        )
                    )
                )
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                CategoryBadge(text = badge)
            }

            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
            )
        }
    }
}