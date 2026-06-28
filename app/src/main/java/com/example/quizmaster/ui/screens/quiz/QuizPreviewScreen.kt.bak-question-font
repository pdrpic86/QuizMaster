package com.example.quizmaster.ui.screens.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Nightlight
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.quizmaster.ui.theme.NeonTokens
import com.example.quizmaster.ui.theme.QuizMasterTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmaster.data.local.QuestionEntity
import com.example.quizmaster.data.local.QuizDatabase
import com.example.quizmaster.data.remote.QuizApiService
import com.example.quizmaster.data.repository.QuestionRepository
import com.example.quizmaster.ui.components.AppBackground
import com.example.quizmaster.ui.components.CategoryHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

private const val QUIZ_LIMIT = 10
private const val QUESTION_SECONDS = 20
private const val SKIPPED_ANSWER = "__SKIPPED_ANSWER__"

@Composable
fun QuizPreviewScreen(
    selectedCategory: String,
    selectedDifficulty: String,
    onBackClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val context = LocalContext.current
    val screenAlpha = remember { Animatable(0f) }
    val screenScale = remember { Animatable(0.94f) }

    var questions by remember(selectedCategory, selectedDifficulty) { mutableStateOf<List<QuestionEntity>>(emptyList()) }
    var isLoading by remember(selectedCategory, selectedDifficulty) { mutableStateOf(true) }
    var errorMessage by remember(selectedCategory, selectedDifficulty) { mutableStateOf<String?>(null) }
    var currentIndex by remember(selectedCategory, selectedDifficulty) { mutableIntStateOf(0) }
    var score by remember(selectedCategory, selectedDifficulty) { mutableIntStateOf(0) }
    var selectedAnswer by remember(selectedCategory, selectedDifficulty) { mutableStateOf<String?>(null) }
    var isFinished by remember(selectedCategory, selectedDifficulty) { mutableStateOf(false) }
    var secondsLeft by remember(selectedCategory, selectedDifficulty) { mutableIntStateOf(QUESTION_SECONDS) }
    var quizRun by remember(selectedCategory, selectedDifficulty) { mutableIntStateOf(0) }

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

    LaunchedEffect(selectedCategory, selectedDifficulty, quizRun) {
        isLoading = true
        errorMessage = null
        currentIndex = 0
        score = 0
        selectedAnswer = null
        isFinished = false
        secondsLeft = QUESTION_SECONDS

        runCatching {
            withContext(Dispatchers.IO) {
                val database = QuizDatabase.getDatabase(context)
                val repository = QuestionRepository(
                    questionDao = database.questionDao(),
                    apiService = QuizApiService()
                )
                
                // Professional Sync: Attempt to get fresh questions from Ktor
                // This will fail (and be caught) because the URL is a dummy
                repository.syncQuestions(selectedCategory, selectedDifficulty)

                repository.getQuestionsForQuiz(
                    category = selectedCategory,
                    difficulty = selectedDifficulty,
                    limit = QUIZ_LIMIT
                )
            }
        }.onSuccess { loadedQuestions ->
            questions = loadedQuestions
            errorMessage = if (loadedQuestions.isEmpty()) {
                "No questions found for $selectedCategory / $selectedDifficulty."
            } else {
                null
            }
        }.onFailure { throwable ->
            questions = emptyList()
            errorMessage = throwable.message ?: "Database loading failed."
        }

        isLoading = false
    }

    val currentQuestion = questions.getOrNull(currentIndex)

    LaunchedEffect(currentQuestion?.id, selectedAnswer, isFinished) {
        if (currentQuestion == null || selectedAnswer != null || isFinished) return@LaunchedEffect

        secondsLeft = QUESTION_SECONDS
        while (secondsLeft > 0 && selectedAnswer == null && !isFinished) {
            delay(1.seconds)
            secondsLeft--
        }

        if (secondsLeft == 0 && selectedAnswer == null && !isFinished) {
            selectedAnswer = ""
        }
    }

    AppBackground {
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
                title = "Quiz",
                subtitle = "$selectedCategory • $selectedDifficulty",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    CenterMessage(
                        title = "Loading questions...",
                        message = "Database is waking up."
                    )
                }

                errorMessage != null -> {
                    CenterMessage(
                        title = "Quiz cannot start",
                        message = errorMessage.orEmpty()
                    )
                }

                isFinished -> {
                    ResultCard(
                        score = score,
                        total = questions.size,
                        onRestartClick = {
                            quizRun++
                        },
                        onBackClick = onBackClick
                    )
                }

                currentQuestion != null -> {
                    QuizQuestionContent(
                        question = currentQuestion,
                        currentIndex = currentIndex,
                        totalQuestions = questions.size,
                        score = score,
                        secondsLeft = secondsLeft,
                        selectedAnswer = selectedAnswer,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = onThemeToggle,
                        onAnswerSelected = { answer ->
                            if (selectedAnswer == null) {
                                selectedAnswer = answer
                                if (answer == currentQuestion.correctAnswer) {
                                    score++
                                }
                            }
                        },
                        onSkipQuestion = {
                            if (selectedAnswer == null) {
                                selectedAnswer = SKIPPED_ANSWER
                            }
                        },
                        onNextClick = {
                            if (currentIndex < questions.lastIndex) {
                                currentIndex++
                                selectedAnswer = null
                                secondsLeft = QUESTION_SECONDS
                            } else {
                                isFinished = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.QuizQuestionContent(
    question: QuestionEntity,
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    secondsLeft: Int,
    selectedAnswer: String?,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onAnswerSelected: (String) -> Unit,
    onSkipQuestion: () -> Unit,
    onNextClick: () -> Unit
) {
    var showSkipDialog by remember(question.id) { mutableStateOf(false) }

    if (showSkipDialog) {
        AlertDialog(
            onDismissRequest = { showSkipDialog = false },
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                Text(
                    text = "This question will be marked as unanswered. Your score will stay the same.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSkipDialog = false
                        onSkipQuestion()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(text = "Skip")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSkipDialog = false }) {
                    Text(text = "Stay")
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 8.dp
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

    val progress = (currentIndex + 1).toFloat() / totalQuestions.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "question progress"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Question ${currentIndex + 1} / $totalQuestions (Score: $score)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                strokeCap = StrokeCap.Round
            )
        }

        IconButton(onClick = onThemeToggle) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.Nightlight,
                contentDescription = "Toggle Theme",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        CountdownTimer(secondsLeft = secondsLeft)
    }

    Spacer(modifier = Modifier.height(24.dp))

    Spacer(modifier = Modifier.height(28.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Subtle question glow
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .blur(50.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        CircleShape
                    )
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "QUESTION ${currentIndex + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        answers.forEach { answer ->
            AnswerButton(
                text = answer,
                correctAnswer = question.correctAnswer,
                selectedAnswer = selectedAnswer,
                onClick = { onAnswerSelected(answer) }
            )
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    if (selectedAnswer != null) {
        FeedbackFooter(
            isCorrect = selectedAnswer == question.correctAnswer,
            isTimeout = selectedAnswer.isEmpty(),
            isSkipped = selectedAnswer == SKIPPED_ANSWER,
            isLastQuestion = currentIndex == totalQuestions - 1,
            onNextClick = onNextClick
        )
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Timer locks the question at 0 seconds.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.48f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            FilledTonalButton(
                onClick = { showSkipDialog = true },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.78f),
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Skip with confirmation",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CountdownTimer(secondsLeft: Int) {
    val isWarning = secondsLeft in 1..5
    val isFinished = secondsLeft == 0

    val animatedProgress by animateFloatAsState(
        targetValue = secondsLeft.toFloat() / QUESTION_SECONDS.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "Timer progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "timer pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isWarning) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse scale"
    )

    val timerScale by animateFloatAsState(
        targetValue = if (isWarning) 1.15f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "Timer scale"
    )

    val timerColor by animateColorAsState(
        targetValue = when {
            isFinished -> MaterialTheme.colorScheme.error
            isWarning -> Color(0xFFEF4444)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 300),
        label = "Timer color"
    )

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isWarning) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .blur(20.dp)
                    .background(timerColor.copy(alpha = 0.3f), CircleShape)
            )
        }

        Canvas(
            modifier = Modifier
                .size(76.dp)
                .scale(timerScale * pulseScale)
        ) {
            val strokeWidth = 4.dp.toPx()
            
            // Background track
            drawCircle(
                color = timerColor.copy(alpha = 0.12f),
                style = Stroke(width = strokeWidth)
            )

            // Progress arc
            drawArc(
                color = timerColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        AnimatedContent(
            targetState = secondsLeft,
            transitionSpec = {
                if (targetState < initialState) {
                    (slideInVertically { it } + fadeIn()) togetherWith
                            (slideOutVertically { -it } + fadeOut())
                } else {
                    (slideInVertically { -it } + fadeIn()) togetherWith
                            (slideOutVertically { it } + fadeOut())
                }.using(
                    androidx.compose.animation.SizeTransform(clip = false)
                )
            },
            label = "Timer text"
        ) { targetSeconds ->
            Text(
                text = targetSeconds.toString().padStart(2, '0'),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = timerColor
            )
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    correctAnswer: String,
    selectedAnswer: String?,
    onClick: () -> Unit
) {
    val isLocked = selectedAnswer != null
    val isSelected = selectedAnswer == text
    val isCorrectAnswer = text == correctAnswer

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "button scale"
    )

    val backgroundBrush = when {
        isLocked && isCorrectAnswer -> NeonTokens.CorrectGradient
        isLocked && isSelected -> NeonTokens.WrongGradient
        else -> Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.03f))
        )
    }

    val borderAlpha by animateFloatAsState(
        targetValue = if (isSelected || (isLocked && isCorrectAnswer)) 0.8f else 0.2f,
        label = "border alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundBrush)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !isLocked,
                onClick = onClick
            )
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isLocked && (isCorrectAnswer || isSelected)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isLocked && isCorrectAnswer) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            } else if (isLocked && isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun FeedbackFooter(
    isCorrect: Boolean,
    isTimeout: Boolean,
    isSkipped: Boolean,
    isLastQuestion: Boolean,
    onNextClick: () -> Unit
) {
    val title = when {
        isSkipped -> "Question skipped"
        isTimeout -> "Time is up"
        isCorrect -> "Correct"
        else -> "Wrong"
    }

    val message = when {
        isSkipped -> "No points changed. Lock in the next one."
        isTimeout -> "Clock got you this time."
        isCorrect -> "Nice hit. Keep going."
        else -> "Better luck next time."
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = when {
                isCorrect -> MaterialTheme.colorScheme.secondary
                isSkipped -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.error
            },
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        ActionCard(
            text = if (isLastQuestion) "Show result" else "Next question",
            icon = Icons.Rounded.SkipNext,
            onClick = onNextClick
        )
    }
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.86f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quiz finished",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$score / $total",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$percentage% correct",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ActionCard(
            text = "Restart quiz",
            icon = Icons.Rounded.Refresh,
            onClick = onRestartClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        ActionCard(
            text = "Back to difficulty",
            icon = Icons.Rounded.Close,
            onClick = onBackClick
        )
    }
}

@Composable
private fun CenterMessage(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionCard(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun QuizQuestionPreview() {
    QuizMasterTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            Column {
                AnswerButton(
                    text = "Emerald Gradient (Correct)",
                    correctAnswer = "Emerald Gradient (Correct)",
                    selectedAnswer = "Emerald Gradient (Correct)",
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                AnswerButton(
                    text = "Crimson Gradient (Wrong)",
                    correctAnswer = "Other",
                    selectedAnswer = "Crimson Gradient (Wrong)",
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                AnswerButton(
                    text = "Glass Gradient (Normal)",
                    correctAnswer = "Other",
                    selectedAnswer = null,
                    onClick = {}
                )
            }
        }
    }
}
