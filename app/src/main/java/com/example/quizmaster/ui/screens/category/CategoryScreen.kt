package com.example.quizmaster.ui.screens.category

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class QuizCategoryUi(
    val title: String,
    val emoji: String,
    val subtitle: String,
    val questions: Int,
    val accent: Color
)

@Immutable
private data class CategoryResponsiveTokens(
    val screenName: String,

    val pageHorizontalPadding: Dp,
    val pageVerticalPadding: Dp,
    val sectionGap: Dp,

    val headerBackFont: TextUnit,
    val headerTitleFont: TextUnit,
    val headerTitleLine: TextUnit,
    val headerSubtitleFont: TextUnit,
    val headerSubtitleLine: TextUnit,
    val logoSize: Dp,
    val logoFont: TextUnit,

    val heroRadius: Dp,
    val heroPadding: Dp,
    val heroTitleFont: TextUnit,
    val heroTitleLine: TextUnit,
    val heroBodyFont: TextUnit,
    val heroBodyLine: TextUnit,
    val pillFont: TextUnit,
    val pillHorizontalPadding: Dp,
    val pillVerticalPadding: Dp,

    val cardRadius: Dp,
    val cardPadding: Dp,
    val cardMinHeight: Dp,
    val cardIconBox: Dp,
    val cardIconRadius: Dp,
    val cardEmojiFont: TextUnit,
    val cardTitleFont: TextUnit,
    val cardTitleLine: TextUnit,
    val cardSubtitleFont: TextUnit,
    val cardSubtitleLine: TextUnit,
    val cardChipFont: TextUnit,
    val cardArrowFont: TextUnit
)

@Composable
private fun rememberCategoryResponsiveTokens(): CategoryResponsiveTokens {
    val config = LocalConfiguration.current
    val width = config.screenWidthDp
    val height = config.screenHeightDp

    return when {
        width <= 360 || height <= 720 -> CategoryResponsiveTokens(
            screenName = "Small",
            pageHorizontalPadding = 12.dp,
            pageVerticalPadding = 8.dp,
            sectionGap = 10.dp,

            headerBackFont = 28.sp,
            headerTitleFont = 20.sp,
            headerTitleLine = 22.sp,
            headerSubtitleFont = 10.sp,
            headerSubtitleLine = 13.sp,
            logoSize = 36.dp,
            logoFont = 11.sp,

            heroRadius = 22.dp,
            heroPadding = 13.dp,
            heroTitleFont = 20.sp,
            heroTitleLine = 22.sp,
            heroBodyFont = 11.sp,
            heroBodyLine = 15.sp,
            pillFont = 9.sp,
            pillHorizontalPadding = 8.dp,
            pillVerticalPadding = 4.dp,

            cardRadius = 20.dp,
            cardPadding = 10.dp,
            cardMinHeight = 86.dp,
            cardIconBox = 48.dp,
            cardIconRadius = 16.dp,
            cardEmojiFont = 23.sp,
            cardTitleFont = 17.sp,
            cardTitleLine = 19.sp,
            cardSubtitleFont = 10.sp,
            cardSubtitleLine = 13.sp,
            cardChipFont = 9.sp,
            cardArrowFont = 25.sp
        )

        width <= 411 || height <= 891 -> CategoryResponsiveTokens(
            screenName = "Normal",
            pageHorizontalPadding = 14.dp,
            pageVerticalPadding = 9.dp,
            sectionGap = 12.dp,

            headerBackFont = 31.sp,
            headerTitleFont = 23.sp,
            headerTitleLine = 25.sp,
            headerSubtitleFont = 11.sp,
            headerSubtitleLine = 15.sp,
            logoSize = 40.dp,
            logoFont = 12.sp,

            heroRadius = 25.dp,
            heroPadding = 16.dp,
            heroTitleFont = 23.sp,
            heroTitleLine = 25.sp,
            heroBodyFont = 12.sp,
            heroBodyLine = 17.sp,
            pillFont = 10.sp,
            pillHorizontalPadding = 9.dp,
            pillVerticalPadding = 4.dp,

            cardRadius = 23.dp,
            cardPadding = 12.dp,
            cardMinHeight = 96.dp,
            cardIconBox = 56.dp,
            cardIconRadius = 19.dp,
            cardEmojiFont = 27.sp,
            cardTitleFont = 19.sp,
            cardTitleLine = 21.sp,
            cardSubtitleFont = 11.sp,
            cardSubtitleLine = 15.sp,
            cardChipFont = 10.sp,
            cardArrowFont = 29.sp
        )

        else -> CategoryResponsiveTokens(
            screenName = "Large",
            pageHorizontalPadding = 16.dp,
            pageVerticalPadding = 10.dp,
            sectionGap = 14.dp,

            headerBackFont = 34.sp,
            headerTitleFont = 25.sp,
            headerTitleLine = 28.sp,
            headerSubtitleFont = 12.sp,
            headerSubtitleLine = 16.sp,
            logoSize = 42.dp,
            logoFont = 13.sp,

            heroRadius = 28.dp,
            heroPadding = 18.dp,
            heroTitleFont = 25.sp,
            heroTitleLine = 28.sp,
            heroBodyFont = 13.sp,
            heroBodyLine = 18.sp,
            pillFont = 11.sp,
            pillHorizontalPadding = 10.dp,
            pillVerticalPadding = 5.dp,

            cardRadius = 26.dp,
            cardPadding = 14.dp,
            cardMinHeight = 108.dp,
            cardIconBox = 64.dp,
            cardIconRadius = 22.dp,
            cardEmojiFont = 30.sp,
            cardTitleFont = 21.sp,
            cardTitleLine = 23.sp,
            cardSubtitleFont = 12.sp,
            cardSubtitleLine = 16.sp,
            cardChipFont = 11.sp,
            cardArrowFont = 32.sp
        )
    }
}

private val premiumCategories = listOf(
    QuizCategoryUi(
        title = "Sports",
        emoji = "⚽",
        subtitle = "Legends, records, tournaments and moments that decided everything.",
        questions = 220,
        accent = Color(0xFF22C55E)
    ),
    QuizCategoryUi(
        title = "Movies",
        emoji = "🎬",
        subtitle = "Cinema classics, actors, directors, Oscars and iconic scenes.",
        questions = 220,
        accent = Color(0xFFE879F9)
    ),
    QuizCategoryUi(
        title = "History",
        emoji = "🏛️",
        subtitle = "Empires, revolutions, leaders, battles and turning points.",
        questions = 220,
        accent = Color(0xFFF59E0B)
    ),
    QuizCategoryUi(
        title = "Animals",
        emoji = "🐾",
        subtitle = "Wildlife, biology, habitats, behavior and curious creatures.",
        questions = 220,
        accent = Color(0xFF38BDF8)
    )
)

@Composable
fun CategoryScreen(
    onBackClick: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val tokens = rememberCategoryResponsiveTokens()

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
                    .padding(
                        horizontal = tokens.pageHorizontalPadding,
                        vertical = tokens.pageVerticalPadding
                    ),
                verticalArrangement = Arrangement.spacedBy(tokens.sectionGap)
            ) {
                PremiumHeader(
                    tokens = tokens,
                    title = "Choose category",
                    subtitle = "Pick your battlefield. Difficulty comes next.",
                    onBackClick = onBackClick
                )

                HeroCategoryCard(tokens = tokens)

                Column(
                    verticalArrangement = Arrangement.spacedBy(tokens.sectionGap)
                ) {
                    premiumCategories.forEach { category ->
                        PremiumCategoryCard(
                            tokens = tokens,
                            category = category,
                            onClick = { onCategorySelected(category.title) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PremiumHeader(
    tokens: CategoryResponsiveTokens,
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
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
        ) {
            Text(
                text = "‹",
                fontSize = tokens.headerBackFont,
                lineHeight = tokens.headerBackFont,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = tokens.headerTitleFont,
                lineHeight = tokens.headerTitleLine,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                fontSize = tokens.headerSubtitleFont,
                lineHeight = tokens.headerSubtitleLine,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .size(tokens.logoSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "QM",
                fontSize = tokens.logoFont,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun HeroCategoryCard(
    tokens: CategoryResponsiveTokens
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.heroRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.88f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(tokens.heroPadding),
            verticalArrangement = Arrangement.spacedBy(tokens.sectionGap * 0.70f)
        ) {
            Text(
                text = "880 questions loaded",
                fontSize = tokens.heroTitleFont,
                lineHeight = tokens.heroTitleLine,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Four quiz categories, each with 220 questions ready for sorting, testing and flexing.",
                fontSize = tokens.heroBodyFont,
                lineHeight = tokens.heroBodyLine,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.76f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                TinyPill(tokens = tokens, text = "RoomDB")
                TinyPill(tokens = tokens, text = "Offline")
                TinyPill(tokens = tokens, text = tokens.screenName)
            }
        }
    }
}

@Composable
private fun PremiumCategoryCard(
    tokens: CategoryResponsiveTokens,
    category: QuizCategoryUi,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.975f else 1f,
        animationSpec = tween(durationMillis = 110),
        label = "category card press scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = tokens.cardMinHeight)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(tokens.cardRadius),
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
                            category.accent.copy(alpha = 0.60f),
                            Color.White.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(tokens.cardRadius)
                )
                .padding(tokens.cardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(tokens.cardIconBox)
                        .clip(RoundedCornerShape(tokens.cardIconRadius))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    category.accent.copy(alpha = 0.34f),
                                    category.accent.copy(alpha = 0.10f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.emoji,
                        fontSize = tokens.cardEmojiFont
                    )
                }

                Spacer(modifier = Modifier.width(tokens.cardPadding))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.title,
                            modifier = Modifier.weight(1f),
                            fontSize = tokens.cardTitleFont,
                            lineHeight = tokens.cardTitleLine,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        AssistChip(
                            onClick = onClick,
                            label = {
                                Text(
                                    text = "${category.questions}",
                                    fontSize = tokens.cardChipFont,
                                    fontWeight = FontWeight.Black,
                                    maxLines = 1
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = category.accent.copy(alpha = 0.14f),
                                labelColor = category.accent
                            ),
                            border = null
                        )
                    }

                    Text(
                        text = category.subtitle,
                        fontSize = tokens.cardSubtitleFont,
                        lineHeight = tokens.cardSubtitleLine,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "›",
                    fontSize = tokens.cardArrowFont,
                    lineHeight = tokens.cardArrowFont,
                    fontWeight = FontWeight.Black,
                    color = category.accent
                )
            }
        }
    }
}

@Composable
private fun TinyPill(
    tokens: CategoryResponsiveTokens,
    text: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(
                horizontal = tokens.pillHorizontalPadding,
                vertical = tokens.pillVerticalPadding
            )
    ) {
        Text(
            text = text,
            fontSize = tokens.pillFont,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1
        )
    }
}
