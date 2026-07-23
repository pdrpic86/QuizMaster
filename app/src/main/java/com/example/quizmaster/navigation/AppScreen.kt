package com.example.quizmaster.navigation

import kotlinx.serialization.Serializable

sealed interface AppScreen {

    @Serializable
    data object Login : AppScreen

    @Serializable
    data object Home : AppScreen

    @Serializable
    data object Categories : AppScreen

    @Serializable
    data class Difficulty(
        val category: String
    ) : AppScreen

    @Serializable
    data class QuizPreview(
        val category: String,
        val difficulty: String
    ) : AppScreen
}
