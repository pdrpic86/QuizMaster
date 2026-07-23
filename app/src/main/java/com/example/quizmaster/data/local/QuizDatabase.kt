package com.example.quizmaster.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [QuestionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_master_questions.db"
                )
                    .createFromAsset("quiz_master_questions.db")
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateInitialQuestions(database.questionDao())
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialQuestions(dao: QuestionDao) {
            val sportsEasy = dao.getQuestionsByCategoryAndDifficulty("Sports", 1, 1)
            if (sportsEasy.isEmpty()) {
                val questions = listOf(
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "Which sport uses a shuttlecock?",
                        correctAnswer = "Badminton",
                        wrongAnswer1 = "Tennis",
                        wrongAnswer2 = "Squash",
                        wrongAnswer3 = "Table Tennis"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "How many players are on a standard soccer team on the field?",
                        correctAnswer = "11",
                        wrongAnswer1 = "10",
                        wrongAnswer2 = "9",
                        wrongAnswer3 = "12"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "In which sport would you use a 'puck'?",
                        correctAnswer = "Ice Hockey",
                        wrongAnswer1 = "Lacrosse",
                        wrongAnswer2 = "Cricket",
                        wrongAnswer3 = "Polo"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "What is the maximum score possible in a single frame of ten-pin bowling?",
                        correctAnswer = "30",
                        wrongAnswer1 = "10",
                        wrongAnswer2 = "20",
                        wrongAnswer3 = "15"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "Which country won the FIFA World Cup in 2022?",
                        correctAnswer = "Argentina",
                        wrongAnswer1 = "France",
                        wrongAnswer2 = "Brazil",
                        wrongAnswer3 = "Croatia"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "How many holes are played in a standard round of golf?",
                        correctAnswer = "18",
                        wrongAnswer1 = "9",
                        wrongAnswer2 = "24",
                        wrongAnswer3 = "12"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "What is the color of the jersey worn by the leader in the Tour de France?",
                        correctAnswer = "Yellow",
                        wrongAnswer1 = "Green",
                        wrongAnswer2 = "Red",
                        wrongAnswer3 = "White"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "In basketball, how many points is a free throw worth?",
                        correctAnswer = "1",
                        wrongAnswer1 = "2",
                        wrongAnswer2 = "3",
                        wrongAnswer3 = "0"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "Which grand slam tennis tournament is played on red clay courts?",
                        correctAnswer = "French Open",
                        wrongAnswer1 = "Wimbledon",
                        wrongAnswer2 = "US Open",
                        wrongAnswer3 = "Australian Open"
                    ),
                    QuestionEntity(
                        category = "Sports",
                        difficulty = 1,
                        question = "What is the distance of a standard marathon?",
                        correctAnswer = "42.195 km",
                        wrongAnswer1 = "21.097 km",
                        wrongAnswer2 = "10 km",
                        wrongAnswer3 = "50 km"
                    )
                )
                dao.insertQuestions(questions)
            }
        }
    }
}

