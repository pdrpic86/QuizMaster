package com.example.quizmaster.ui.screens.difficulty

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class DifficultyUi(
    val value: String,
    val title: String,
    val subtitle: String,
    val stars: String,
    val badge: String,
    val accent: Color
)

private val difficultyLevels = listOf(
    DifficultyUi(
        value = "Easy",
        title = "Easy",
        subtitle = "Warm-up round. Good for quick testing and casual play.",
        stars = "★☆☆☆",
        badge = "Warm-up",
        accent = Color(0xFF22C55E)
    ),
    DifficultyUi(
        value = "Medium",
        title = "Medium",
        subtitle = "Balanced quiz mode with questions that need actual thinking.",
        stars = "★★☆☆",
        badge = "Pub quiz",
        accent = Color(0xFF38BDF8)
    ),
    DifficultyUi(
        value = "Hard",
        title = "Hard",
        subtitle = "Sharper questions, less guessing, more pain. In a good way.",
        stars = "★★★☆",
        badge = "Serious",
        accent = Color(0xFFF59E0B)
    ),
    DifficultyUi(
        value = "Expert",
        title = "Expert",
        subtitle = "Millionaire-ish mode. Not impossible, but it should bite.",
        stars = "★★★★",
        badge = "Final boss",
        accent = Color(0xFFEF4444)
    )
)

@Composable
fun DifficultyScreen(
    selectedCategory: String,
    onBackClick: () -> Unit,
    onDifficultySelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .safeDrawingPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PremiumDifficultyHeader(
                    title = "Choose level",
                    subtitle = selectedCategory,
                    onBackClick = onBackClick
                )

                DifficultyHeroCard(selectedCategory = selectedCategory)

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    difficultyLevels.forEach { level ->
                        PremiumDifficultyCard(
                            level = level,
                            onClick = { onDifficultySelected(level.value) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun PremiumDifficultyHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onBackClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
        ) {
            Text(
                text = "‹",
                fontSize = 34.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 25.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "$subtitle category",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        CategoryEmojiBadge(category = subtitle)
    }
}

@Composable
private fun DifficultyHeroCard(
    selectedCategory: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.88f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = selectedCategory,
                fontSize = 30.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Pick a level. Your database can stay unsorted while testing, then you map questions into these four levels later.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.76f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TinyPill(text = "Easy")
                TinyPill(text = "Medium")
                TinyPill(text = "Hard")
                TinyPill(text = "Expert")
            }
        }
    }
}

@Composable
private fun PremiumDifficultyCard(
    level: DifficultyUi,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.975f else 1f,
        animationSpec = tween(durationMillis = 110),
        label = "difficulty card press scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.64f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.8.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            level.accent.copy(alpha = 0.62f),
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    level.accent.copy(alpha = 0.34f),
                                    level.accent.copy(alpha = 0.10f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.stars,
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = level.accent,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = level.title,
                            modifier = Modifier.weight(1f),
                            fontSize = 21.sp,
                            lineHeight = 23.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        AssistChip(
                            onClick = onClick,
                            label = {
                                Text(
                                    text = level.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = level.accent.copy(alpha = 0.14f),
                                labelColor = level.accent
                            ),
                            border = null
                        )
                    }

                    Text(
                        text = level.subtitle,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "›",
                    fontSize = 32.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = level.accent
                )
            }
        }
    }
}

@Composable
private fun CategoryEmojiBadge(
    category: String
) {
    val emoji = when (category) {
        "Sports" -> "⚽"
        "Movies" -> "🎬"
        "History" -> "🏛️"
        "Animals" -> "🐾"
        else -> "🎯"
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun TinyPill(
    text: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
