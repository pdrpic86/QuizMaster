package com.example.quizmaster

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizmaster.navigation.AppScreen
import com.example.quizmaster.ui.screens.category.CategoryScreen
import com.example.quizmaster.ui.screens.difficulty.DifficultyScreen
import com.example.quizmaster.ui.screens.home.HomeScreen
import com.example.quizmaster.ui.screens.login.LoginScreen
import com.example.quizmaster.ui.screens.quiz.QuizPreviewScreen
import com.example.quizmaster.ui.theme.QuizMasterTheme

@Composable
fun QuizMasterApp() {
    QuizMasterTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Login
        ) {
            composable<AppScreen.Login> {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(AppScreen.Home) {
                            popUpTo<AppScreen.Login> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<AppScreen.Home> {
                HomeScreen(
                    onCategoriesClick = {
                        navController.navigate(
                            AppScreen.Categories
                        )
                    }
                )
            }

            composable<AppScreen.Categories> {
                CategoryScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCategorySelected = { category ->
                        navController.navigate(
                            AppScreen.Difficulty(
                                category = category
                            )
                        )
                    }
                )
            }

            composable<AppScreen.Difficulty> { backStackEntry ->
                val route =
                    backStackEntry.toRoute<AppScreen.Difficulty>()

                DifficultyScreen(
                    selectedCategory = route.category,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDifficultySelected = { difficulty ->
                        navController.navigate(
                            AppScreen.QuizPreview(
                                category = route.category,
                                difficulty = difficulty
                            )
                        )
                    }
                )
            }

            composable<AppScreen.QuizPreview> { backStackEntry ->
                val route =
                    backStackEntry.toRoute<AppScreen.QuizPreview>()

                QuizPreviewScreen(
                    selectedCategory = route.category,
                    selectedDifficulty = route.difficulty,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}