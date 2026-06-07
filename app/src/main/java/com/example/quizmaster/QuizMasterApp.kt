package com.example.quizmaster

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.quizmaster.navigation.AppScreen
import com.example.quizmaster.ui.screens.category.CategoryScreen
import com.example.quizmaster.ui.screens.difficulty.DifficultyScreen
import com.example.quizmaster.ui.screens.home.HomeScreen
import com.example.quizmaster.ui.screens.quiz.QuizPreviewScreen
import com.example.quizmaster.ui.screens.splash.SplashScreen

@Composable
fun QuizMasterApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    // Temporary state for selected category and difficulty.
    // This can move to a ViewModel when the app grows.
    var selectedCategory by remember { mutableStateOf("Sports") }
    var selectedDifficulty by remember { mutableStateOf("Mixed") }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(250)
            ) togetherWith fadeOut(
                animationSpec = tween(180)
            )
        },
        label = "Screen transition"
    ) { screen ->
        when (screen) {
            AppScreen.Splash -> {
                SplashScreen(
                    onSplashFinished = {
                        currentScreen = AppScreen.Home
                    }
                )
            }

            AppScreen.Home -> {
                HomeScreen(
                    onCategoriesClick = {
                        currentScreen = AppScreen.Categories
                    }
                )
            }

            AppScreen.Categories -> {
                CategoryScreen(
                    onBackClick = {
                        currentScreen = AppScreen.Home
                    },
                    onCategorySelected = { category ->
                        selectedCategory = category
                        currentScreen = AppScreen.Difficulty
                    }
                )
            }

            AppScreen.Difficulty -> {
                DifficultyScreen(
                    selectedCategory = selectedCategory,
                    onBackClick = {
                        currentScreen = AppScreen.Categories
                    },
                    onDifficultySelected = { difficulty ->
                        selectedDifficulty = difficulty
                        currentScreen = AppScreen.QuizPreview
                    }
                )
            }

            AppScreen.QuizPreview -> {
                QuizPreviewScreen(
                    selectedCategory = selectedCategory,
                    selectedDifficulty = selectedDifficulty,
                    onBackClick = {
                        currentScreen = AppScreen.Difficulty
                    }
                )
            }
        }
    }
}