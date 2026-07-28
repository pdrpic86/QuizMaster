package com.example.quizmaster.ui.screens.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizmaster.data.local.QuestionEntity
import com.example.quizmaster.data.local.QuizDatabase
import com.example.quizmaster.data.remote.QuizApiService
import com.example.quizmaster.data.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

internal const val QUESTION_SECONDS = 30
internal const val SKIPPED_ANSWER = "__SKIPPED_ANSWER__"

private const val QUIZ_LIMIT = 10

data class QuizUiState(
    val category: String = "",
    val difficulty: String = "",
    val questions: List<QuestionEntity> = emptyList(),
    val answerOptions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val isFinished: Boolean = false,
    val secondsLeft: Int = QUESTION_SECONDS
) {
    val currentQuestion: QuestionEntity?
        get() = questions.getOrNull(currentIndex)
}

class QuizViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        QuizDatabase.getDatabase(application)

    private val repository = QuestionRepository(
        questionDao = database.questionDao(),
        apiService = QuizApiService()
    )

    private val _uiState =
        MutableStateFlow(QuizUiState())

    val uiState: StateFlow<QuizUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null
    private var timerJob: Job? = null

    fun startQuiz(
        category: String,
        difficulty: String
    ) {
        val state = _uiState.value

        val sameQuiz =
            state.category == category &&
                state.difficulty == difficulty

        val alreadyInitialized =
            state.isLoading ||
                state.questions.isNotEmpty() ||
                state.errorMessage != null

        if (sameQuiz && alreadyInitialized) {
            return
        }

        loadQuiz(
            category = category,
            difficulty = difficulty
        )
    }

    fun restartQuiz() {
        val state = _uiState.value

        if (
            state.category.isBlank() ||
            state.difficulty.isBlank()
        ) {
            return
        }

        loadQuiz(
            category = state.category,
            difficulty = state.difficulty
        )
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        val question = state.currentQuestion ?: return

        if (
            state.selectedAnswer != null ||
            state.isFinished
        ) {
            return
        }

        timerJob?.cancel()

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                score = if (
                    answer == question.correctAnswer
                ) {
                    it.score + 1
                } else {
                    it.score
                }
            )
        }
    }

    fun skipQuestion() {
        val state = _uiState.value

        if (
            state.currentQuestion == null ||
            state.selectedAnswer != null ||
            state.isFinished
        ) {
            return
        }

        timerJob?.cancel()

        _uiState.update {
            it.copy(
                selectedAnswer = SKIPPED_ANSWER
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value

        if (
            state.currentQuestion == null ||
            state.isFinished
        ) {
            return
        }

        if (state.currentIndex < state.questions.lastIndex) {
            val nextIndex = state.currentIndex + 1
            val nextQuestion =
                state.questions.getOrNull(nextIndex)
                    ?: return

            _uiState.update {
                it.copy(
                    currentIndex = nextIndex,
                    answerOptions =
                        createAnswerOptions(nextQuestion),
                    selectedAnswer = null,
                    secondsLeft = QUESTION_SECONDS
                )
            }

            startTimer()
        } else {
            timerJob?.cancel()

            _uiState.update {
                it.copy(isFinished = true)
            }
        }
    }

    private fun loadQuiz(
        category: String,
        difficulty: String
    ) {
        loadingJob?.cancel()
        timerJob?.cancel()

        _uiState.value = QuizUiState(
            category = category,
            difficulty = difficulty,
            isLoading = true
        )

        loadingJob = viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    repository.syncQuestions(
                        category = category,
                        difficulty = difficulty
                    )

                    repository.getQuestionsForQuiz(
                        category = category,
                        difficulty = difficulty,
                        limit = QUIZ_LIMIT
                    )
                }
            }.onSuccess { loadedQuestions ->
                val firstQuestion =
                    loadedQuestions.firstOrNull()

                val emptyMessage =
                    if (loadedQuestions.isEmpty()) {
                        "No questions found for " +
                            "$category / $difficulty."
                    } else {
                        null
                    }

                _uiState.update {
                    it.copy(
                        questions = loadedQuestions,
                        answerOptions =
                            firstQuestion
                                ?.let(::createAnswerOptions)
                                .orEmpty(),
                        isLoading = false,
                        errorMessage = emptyMessage,
                        currentIndex = 0,
                        score = 0,
                        selectedAnswer = null,
                        isFinished = false,
                        secondsLeft = QUESTION_SECONDS
                    )
                }

                if (loadedQuestions.isNotEmpty()) {
                    startTimer()
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        questions = emptyList(),
                        answerOptions = emptyList(),
                        isLoading = false,
                        errorMessage =
                            throwable.message
                                ?: "Database loading failed."
                    )
                }
            }
        }
    }

    private fun createAnswerOptions(
        question: QuestionEntity
    ): List<String> {
        return listOf(
            question.correctAnswer,
            question.wrongAnswer1,
            question.wrongAnswer2,
            question.wrongAnswer3
        ).shuffled()
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1.seconds)

                val state = _uiState.value

                if (
                    state.currentQuestion == null ||
                    state.selectedAnswer != null ||
                    state.isFinished
                ) {
                    return@launch
                }

                val newSeconds =
                    (state.secondsLeft - 1)
                        .coerceAtLeast(0)

                if (newSeconds == 0) {
                    _uiState.update {
                        it.copy(
                            secondsLeft = 0,
                            selectedAnswer = ""
                        )
                    }

                    return@launch
                }

                _uiState.update {
                    it.copy(secondsLeft = newSeconds)
                }
            }
        }
    }
}