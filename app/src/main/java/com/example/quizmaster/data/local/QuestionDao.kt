package com.example.quizmaster.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDao {

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<QuestionEntity>

    @Query(
        """
        SELECT * FROM questions
        WHERE category = :category
        ORDER BY RANDOM()
        LIMIT :limit
        """
    )
    suspend fun getQuestionsByCategory(
        category: String,
        limit: Int = 10
    ): List<QuestionEntity>

    @Query(
        """
        SELECT * FROM questions
        WHERE category = :category
        AND difficulty = :difficulty
        ORDER BY RANDOM()
        LIMIT :limit
        """
    )
    suspend fun getQuestionsByCategoryAndDifficulty(
        category: String,
        difficulty: Int,
        limit: Int = 10
    ): List<QuestionEntity>

    @Query(
        """
        SELECT * FROM questions
        ORDER BY RANDOM()
        LIMIT :limit
        """
    )
    suspend fun getMixedQuestions(
        limit: Int = 10
    ): List<QuestionEntity>

    @Query(
        """
        SELECT category AS category, COUNT(*) AS count
        FROM questions
        GROUP BY category
        ORDER BY category
        """
    )
    suspend fun getCategoryCounts(): List<CategoryCount>
}