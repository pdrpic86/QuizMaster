package com.example.quizmaster.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    indices = [
        Index(value = ["category"], name = "index_questions_category"),
        Index(value = ["difficulty"], name = "index_questions_difficulty"),
        Index(value = ["category", "difficulty"], name = "index_questions_category_difficulty")
    ]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val category: String,

    val difficulty: Int,

    val question: String,

    @ColumnInfo(name = "correct_answer")
    val correctAnswer: String,

    @ColumnInfo(name = "wrong_answer_1")
    val wrongAnswer1: String,

    @ColumnInfo(name = "wrong_answer_2")
    val wrongAnswer2: String,

    @ColumnInfo(name = "wrong_answer_3")
    val wrongAnswer3: String
)