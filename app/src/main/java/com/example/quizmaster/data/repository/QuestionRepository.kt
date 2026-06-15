package com.example.quizmaster.data.repository

import com.example.quizmaster.data.local.CategoryCount
import com.example.quizmaster.data.local.QuestionDao
import com.example.quizmaster.data.local.QuestionEntity
import com.example.quizmaster.data.remote.QuizApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val apiService: QuizApiService = QuizApiService()
) {
    suspend fun syncQuestions(category: String, difficulty: String) {
        val difficultyNumber = when (difficulty) {
            "Easy" -> 1
            "Medium" -> 2
            "Hard" -> 3
            else -> 1
        }

        withContext(Dispatchers.IO) {
            val remoteQuestions = apiService.getQuestions(category, difficultyNumber)
            if (remoteQuestions.isNotEmpty()) {
                val entities = remoteQuestions.map { remote ->
                    QuestionEntity(
                        category = remote.category,
                        difficulty = remote.difficulty,
                        question = remote.question,
                        correctAnswer = remote.correctAnswer,
                        wrongAnswer1 = remote.wrongAnswers.getOrNull(0) ?: "",
                        wrongAnswer2 = remote.wrongAnswers.getOrNull(1) ?: "",
                        wrongAnswer3 = remote.wrongAnswers.getOrNull(2) ?: ""
                    )
                }
                questionDao.insertQuestions(entities)
            }
        }
    }

    suspend fun getQuestionCount(): Int {
        return questionDao.getQuestionCount()
    }

    suspend fun getAllQuestions(): List<QuestionEntity> {
        return questionDao.getAllQuestions()
    }

    suspend fun getCategoryCounts(): List<CategoryCount> {
        return questionDao.getCategoryCounts()
    }

    suspend fun getQuestionsForQuiz(
        category: String,
        difficulty: String,
        limit: Int = 10
    ): List<QuestionEntity> {
        return if (difficulty == "Mixed") {
            questionDao.getQuestionsByCategory(
                category = category,
                limit = limit
            )
        } else {
            val difficultyNumber = when (difficulty) {
                "Easy" -> 1
                "Medium" -> 2
                "Hard" -> 3
                else -> 1
            }

            questionDao.getQuestionsByCategoryAndDifficulty(
                category = category,
                difficulty = difficultyNumber,
                limit = limit
            )
        }
    }

    suspend fun getMixedQuestions(
        limit: Int = 10
    ): List<QuestionEntity> {
        return questionDao.getMixedQuestions(limit)
    }
}