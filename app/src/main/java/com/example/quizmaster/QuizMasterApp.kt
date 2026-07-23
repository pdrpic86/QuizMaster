package com.example.quizmaster

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizmaster.navigation.AppScreen
import com.example.quizmaster.ui.components.FloatingThemeModeSwitcher
import com.example.quizmaster.ui.screens.category.CategoryScreen
import com.example.quizmaster.ui.screens.difficulty.DifficultyScreen
import com.example.quizmaster.ui.screens.home.HomeScreen
import com.example.quizmaster.ui.screens.login.LoginScreen
import com.example.quizmaster.ui.screens.quiz.QuizPreviewScreen
import com.example.quizmaster.ui.theme.QuizMasterTheme

@Composable
fun QuizMasterApp() {
    val context = LocalContext.current

    val themePrefs = remember {
        context.getSharedPreferences(
            "quizmaster_settings",
            Context.MODE_PRIVATE
        )
    }

    var isDarkTheme by remember {
        mutableStateOf(
            themePrefs.getBoolean("dark_theme", true)
        )
    }

    LaunchedEffect(isDarkTheme) {
        themePrefs
            .edit()
            .putBoolean("dark_theme", isDarkTheme)
            .apply()
    }

    QuizMasterTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Login
        ) {
            composable<AppScreen.Login> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onLoginClick = {
                            navController.navigate(AppScreen.Home) {
                                popUpTo<AppScreen.Login> {
                                    inclusive = true
                                }
                            }
                        }
                    )

                    FloatingThemeModeSwitcher(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it }
                    )
                }
            }

            composable<AppScreen.Home> {
                Box(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(
                        onCategoriesClick = {
                            navController.navigate(
                                AppScreen.Categories
                            )
                        }
                    )

                    FloatingThemeModeSwitcher(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it }
                    )
                }
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
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = {
                        isDarkTheme = !isDarkTheme
                    }
                )
            }
        }
    }
}
