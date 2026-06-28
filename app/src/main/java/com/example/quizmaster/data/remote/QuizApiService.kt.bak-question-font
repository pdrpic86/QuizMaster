package com.example.quizmaster.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RemoteQuestion(
    val id: Int,
    val category: String,
    val difficulty: Int,
    val question: String,
    val correctAnswer: String,
    val wrongAnswers: List<String>
)

@Serializable
data class RemoteCategory(
    val name: String,
    val description: String,
    val iconUrl: String? = null
)

class QuizApiService {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    // Replace with your actual Ktor server URL
    private val baseUrl = "https://api.quizmaster.example.com"

    suspend fun getCategories(): List<RemoteCategory> {
        return try {
            client.get("$baseUrl/categories").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getQuestions(category: String, difficulty: Int): List<RemoteQuestion> {
        return try {
            client.get("$baseUrl/questions") {
                parameter("category", category)
                parameter("difficulty", difficulty)
            }.body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun close() {
        client.close()
    }
}
