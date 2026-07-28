package com.example.quizmaster.ui.screens.quiz

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizmaster.data.local.QuestionEntity
import com.example.quizmaster.ui.components.AppBackground
import com.example.quizmaster.ui.components.CategoryHeader

@Composable
fun QuizPreviewScreen(
    selectedCategory: String,
    selectedDifficulty: String,
    onBackClick: () -> Unit,
    quizViewModel: QuizViewModel = viewModel()
) {
    val uiState by quizViewModel.uiState.collectAsState()

    LaunchedEffect(
        selectedCategory,
        selectedDifficulty
    ) {
        quizViewModel.startQuiz(
            category = selectedCategory,
            difficulty = selectedDifficulty
        )
    }

    val currentQuestion = uiState.currentQuestion

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CategoryHeader(
                title = "Quiz",
                subtitle = "$selectedCategory • $selectedDifficulty",
                onBackClick = onBackClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        CenterMessage(
                            title = "Loading questions...",
                            message = "Database is waking up."
                        )
                    }

                    uiState.errorMessage != null -> {
                        CenterMessage(
                            title = "Quiz cannot start",
                            message = uiState.errorMessage.orEmpty()
                        )
                    }

                    uiState.isFinished -> {
                        ResultCard(
                            score = uiState.score,
                            total = uiState.questions.size,
                            onRestartClick =
                                quizViewModel::restartQuiz,
                            onBackClick = onBackClick
                        )
                    }

                    currentQuestion != null -> {
                        CompactQuizContent(
                            question = currentQuestion,
                            currentIndex = uiState.currentIndex,
                            totalQuestions = uiState.questions.size,
                            score = uiState.score,
                            secondsLeft = uiState.secondsLeft,
                            selectedAnswer = uiState.selectedAnswer,
                            answers = uiState.answerOptions,
                            onAnswerSelected =
                                quizViewModel::selectAnswer,
                            onSkipQuestion =
                                quizViewModel::skipQuestion,
                            onNextClick =
                                quizViewModel::nextQuestion
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun CompactQuizContent(
    question: QuestionEntity,
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    secondsLeft: Int,
    selectedAnswer: String?,
    answers: List<String>,
    onAnswerSelected: (String) -> Unit,
    onSkipQuestion: () -> Unit,
    onNextClick: () -> Unit
) {
    var showSkipDialog by remember(question.id) { mutableStateOf(false) }

    if (showSkipDialog) {
        CompactSkipDialog(
            onDismiss = { showSkipDialog = false },
            onConfirm = {
                showSkipDialog = false
                onSkipQuestion()
            }
        )
    }

    val answers = remember(question.id) {
        listOf(
            question.correctAnswer,
            question.wrongAnswer1,
            question.wrongAnswer2,
            question.wrongAnswer3
        ).shuffled()
    }

    val progress = if (totalQuestions == 0) 0f else (currentIndex + 1).toFloat() / totalQuestions.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "question progress"
    )

    val config = LocalConfiguration.current
    val tiny = config.screenHeightDp < 720
    val spacing = if (tiny) 4.dp else 6.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        CompactStatusBar(
            currentIndex = currentIndex,
            totalQuestions = totalQuestions,
            score = score,
            animatedProgress = animatedProgress,
            secondsLeft = secondsLeft,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.16f)
        )

        CompactQuestionCard(
            questionText = question.question,
            questionNumber = currentIndex + 1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.34f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (selectedAnswer == null) 0.40f else 0.34f),
            verticalArrangement = Arrangement.spacedBy(if (tiny) 4.dp else 5.dp)
        ) {
            answers.forEach { answer ->
                CompactAnswerButton(
                    text = answer,
                    correctAnswer = question.correctAnswer,
                    selectedAnswer = selectedAnswer,
                    onClick = { onAnswerSelected(answer) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        CompactFooter(
            selectedAnswer = selectedAnswer,
            correctAnswer = question.correctAnswer,
            isLastQuestion = currentIndex == totalQuestions - 1,
            onSkipClick = { showSkipDialog = true },
            onNextClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (selectedAnswer == null) 0.10f else 0.16f)
        )
    }
}

@Composable
private fun CompactStatusBar(
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    animatedProgress: Float,
    secondsLeft: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Question ${currentIndex + 1} / $totalQuestions • Score $score",
                fontSize = 11.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .height(5.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                strokeCap = StrokeCap.Round
            )
        }


        CompactCountdownTimer(secondsLeft = secondsLeft)
    }
}

@Composable
private fun CompactQuestionCard(
    questionText: String,
    questionNumber: Int,
    modifier: Modifier = Modifier
) {
    val fontSize = when {
        questionText.length > 190 -> 10.sp
        questionText.length > 150 -> 11.sp
        questionText.length > 110 -> 12.sp
        questionText.length > 75 -> 13.sp
        else -> 14.sp
    }

    val lineHeight = when {
        questionText.length > 150 -> 13.sp
        questionText.length > 110 -> 14.sp
        else -> 16.sp
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.055f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 0.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.14f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "QUESTION $questionNumber",
                    fontSize = 9.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                    letterSpacing = 1.2.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = questionText,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    maxLines = 7,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CompactAnswerButton(
    text: String,
    correctAnswer: String,
    selectedAnswer: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLocked = selectedAnswer != null
    val isSelected = selectedAnswer == text
    val isCorrectAnswer = text == correctAnswer

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "answer press scale"
    )

    val backgroundBrush = when {
        isLocked && isCorrectAnswer -> Brush.linearGradient(
            listOf(Color(0xFF10B981), Color(0xFF059669))
        )

        isLocked && isSelected -> Brush.linearGradient(
            listOf(Color(0xFFEF4444), Color(0xFFB91C1C))
        )

        else -> Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = 0.075f),
                Color.White.copy(alpha = 0.028f)
            )
        )
    }

    val borderAlpha by animateFloatAsState(
        targetValue = if (isSelected || (isLocked && isCorrectAnswer)) 0.78f else 0.18f,
        animationSpec = tween(160),
        label = "answer border alpha"
    )

    val answerFont = when {
        text.length > 85 -> 9.sp
        text.length > 62 -> 10.sp
        text.length > 40 -> 11.sp
        else -> 12.sp
    }

    Box(
        modifier = modifier
            .scale(pressScale)
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundBrush)
            .border(
                width = 0.8.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = borderAlpha),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !isLocked,
                onClick = onClick
            )
            .padding(horizontal = 9.dp, vertical = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                fontSize = answerFont,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLocked && (isCorrectAnswer || isSelected)) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )

            if (isLocked && isCorrectAnswer) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            } else if (isLocked && isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactFooter(
    selectedAnswer: String?,
    correctAnswer: String,
    isLastQuestion: Boolean,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (selectedAnswer != null) {
            val isSkipped = selectedAnswer == SKIPPED_ANSWER
            val isTimeout = selectedAnswer.isEmpty()
            val isCorrect = selectedAnswer == correctAnswer

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when {
                        isSkipped -> "Skipped"
                        isTimeout -> "Time is up"
                        isCorrect -> "Correct"
                        else -> "Wrong"
                    },
                    fontSize = 12.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        isCorrect -> Color(0xFF10B981)
                        isSkipped -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                CompactActionButton(
                    text = if (isLastQuestion) "Show result" else "Next question",
                    onClick = onNextClick
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Timer locks at 0 sec.",
                    modifier = Modifier.weight(1f),
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                TextButton(
                    onClick = onSkipClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Skip",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactActionButton(
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.heightIn(min = 32.dp, max = 36.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = null,
            modifier = Modifier.size(15.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
    }
}



@Composable
private fun CompactSkipDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
        },
        title = {
            Text(
                text = "Skip this question?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        },
        text = {
            Text(
                text = "This question will be marked as unanswered.\nYour score will stay the same.",
                fontSize = 13.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text(text = "Skip")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Stay")
            }
        },
        shape = RoundedCornerShape(22.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 8.dp
    )
}

@Composable
private fun ResultCard(
    score: Int,
    total: Int,
    onRestartClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val percentage = if (total == 0) 0 else (score * 100) / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.86f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quiz finished",
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$score / $total",
                    fontSize = 36.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$percentage% correct",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        CompactActionButton(
            text = "Restart quiz",
            onClick = onRestartClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        FilledTonalButton(
            onClick = onBackClick,
            modifier = Modifier.heightIn(min = 34.dp, max = 38.dp),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
                modifier = Modifier.size(15.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Back to difficulty",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun CenterMessage(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CompactCountdownTimer(
    secondsLeft: Int,
    totalSeconds: Int = QUESTION_SECONDS,
    modifier: Modifier = Modifier
) {
    val safeSeconds = secondsLeft.coerceAtLeast(0)
    val critical = safeSeconds <= 4
    val warning = safeSeconds in 5..7

    val blink = rememberInfiniteTransition(label = "timerRedBlink")

    val blinkAlpha by blink.animateFloat(
        initialValue = if (critical) 0.28f else 1f,
        targetValue = 1f,
        animationSpec = if (critical) {
            infiniteRepeatable(
                animation = tween(durationMillis = 260),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            infiniteRepeatable(
                animation = tween(durationMillis = 100000),
                repeatMode = RepeatMode.Restart
            )
        },
        label = "timerRedBlinkAlpha"
    )

    val timerColor = when {
        critical -> Color(0xFFEF4444)
        warning -> Color(0xFFF59E0B)
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .size(if (critical) 54.dp else 50.dp)
            .alpha(blinkAlpha)
            .clip(CircleShape)
            .background(
                timerColor.copy(
                    alpha = if (critical) 0.22f else 0.13f
                )
            )
            .border(
                width = if (critical) 2.dp else 1.2.dp,
                color = timerColor.copy(
                    alpha = if (critical) 0.95f else 0.62f
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = safeSeconds.toString(),
            fontSize = if (critical) 18.sp else 15.sp,
            fontWeight = FontWeight.Black,
            color = timerColor
        )
    }
}


