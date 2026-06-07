package com.example.quizmaster.data.repository

import com.example.quizmaster.data.local.CategoryCount
import com.example.quizmaster.data.local.QuestionDao
import com.example.quizmaster.data.local.QuestionEntity

class QuestionRepository(
    private val questionDao: QuestionDao
) {
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