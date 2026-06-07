package com.example.quizmaster.ui.screens.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizmaster.data.local.QuestionEntity
import com.example.quizmaster.data.local.QuizDatabase
import com.example.quizmaster.data.repository.QuestionRepository
import com.example.quizmaster.ui.components.CategoryHeader
import com.example.quizmaster.ui.components.GlowCircle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

private const val QUIZ_LIMIT = 10
private const val QUESTION_SECONDS = 15

@Composable
fun QuizPreviewScreen(
    selectedCategory: String,
    selectedDifficulty: String,
    onBackClick: () -> Unit
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
                val repository = QuestionRepository(database.questionDao())
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
                .offset(x = 40.dp, y = 80.dp)
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
                        onAnswerSelected = { answer ->
                            if (selectedAnswer == null) {
                                selectedAnswer = answer
                                if (answer == currentQuestion.correctAnswer) {
                                    score++
                                }
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
    onAnswerSelected: (String) -> Unit,
    onNextClick: () -> Unit
) {
    val answers = remember(question.id) {
        listOf(
            question.correctAnswer,
            question.wrongAnswer1,
            question.wrongAnswer2,
            question.wrongAnswer3
        ).shuffled()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Question ${currentIndex + 1} / $totalQuestions",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
            )
        }

        CountdownTimer(secondsLeft = secondsLeft)
    }

    Spacer(modifier = Modifier.height(28.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.82f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Difficulty ${question.difficulty}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
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
            correctAnswer = question.correctAnswer,
            isLastQuestion = currentIndex == totalQuestions - 1,
            onNextClick = onNextClick
        )
    } else {
        Text(
            text = "Timer locks the question at 0 seconds.",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CountdownTimer(secondsLeft: Int) {
    val isWarning = secondsLeft in 1..5
    val isFinished = secondsLeft == 0

    val timerScale by animateFloatAsState(
        targetValue = if (isWarning) 1.12f else 1f,
        animationSpec = tween(durationMillis = 280),
        label = "Timer scale"
    )

    val timerColor by animateColorAsState(
        targetValue = when {
            isFinished -> MaterialTheme.colorScheme.error
            isWarning -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 250),
        label = "Timer color"
    )

    Box(
        modifier = Modifier
            .size(74.dp)
            .scale(timerScale)
            .background(
                color = timerColor.copy(alpha = 0.14f),
                shape = CircleShape
            )
            .border(
                width = if (isWarning) 2.dp else 1.dp,
                color = timerColor.copy(alpha = 0.75f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = secondsLeft.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = timerColor
        )
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

    val containerColor: Color = when {
        isLocked && isCorrectAnswer -> Color(0xFF14532D).copy(alpha = 0.88f)
        isLocked && isSelected -> MaterialTheme.colorScheme.error.copy(alpha = 0.72f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f)
    }

    val borderColor: Color = when {
        isLocked && isCorrectAnswer -> Color(0xFF22C55E)
        isLocked && isSelected -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
    }

    Card(
        onClick = {
            if (!isLocked) onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isLocked && isCorrectAnswer) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF22C55E)
                )
            } else if (isLocked && isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
private fun FeedbackFooter(
    isCorrect: Boolean,
    isTimeout: Boolean,
    correctAnswer: String,
    isLastQuestion: Boolean,
    onNextClick: () -> Unit
) {
    val title = when {
        isTimeout -> "Time is up"
        isCorrect -> "Correct"
        else -> "Wrong"
    }

    val message = when {
        isCorrect -> "Nice hit. Keep going."
        else -> "Correct answer: $correctAnswer"
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (isCorrect) Color(0xFF22C55E) else MaterialTheme.colorScheme.error,
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
