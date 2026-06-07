# 🧠 Quiz Master

![Android](https://img.shields.io/badge/Android-Kotlin-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-UI-2196F3?style=for-the-badge&logo=materialdesign&logoColor=white)
![Room](https://img.shields.io/badge/Room-Database-0D1117?style=for-the-badge&logo=sqlite&logoColor=white)

**Quiz Master** is a modern Android quiz app built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and a local **Room prebuilt SQLite database**.

The app is designed as a clean, fast, offline-first quiz experience with category selection, difficulty selection, animated screens, and a 15-second quiz timer.

---

## ✨ Project Vision

Quiz Master is built around a simple idea:

> Pick a category. Choose difficulty. Beat the timer. Prove your knowledge.

The app is intentionally local-first for the MVP, meaning no backend is required for the first version.

Current database flow:

```text
CSV source → SQLite database → Room → Compose UI
```

---

## 🚀 Current Status

Implemented so far:

- ✅ Kotlin Android project
- ✅ Jetpack Compose UI
- ✅ Material Design 3 theme
- ✅ Black + blue visual identity
- ✅ Animated Splash screen
- ✅ Animated Home screen
- ✅ Category selection screen
- ✅ Difficulty selection screen
- ✅ Quiz preview screen
- ✅ 15-second countdown timer
- ✅ Warning effect when timer reaches 5 seconds
- ✅ Modular screen structure
- ✅ Local quiz question database
- ✅ Room prebuilt database setup
- ✅ Git workflow after major steps

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design System | Material Design 3 |
| Database | Room |
| Local DB File | Prebuilt SQLite database |
| Build | Gradle Kotlin DSL |
| Annotation Processing | KSP |
| IDE | Android Studio |

---

## 🎨 Design Direction

The visual identity is:

```text
Dominant color: Black
Secondary color: Blue
Style: Modern dark quiz/game UI
```

Design principles:

- 🌑 Dark background
- 🔵 Blue accent color
- 🃏 Rounded Material 3 cards
- ✨ Soft glow effects
- ⚡ Short UI animations
- 🎮 Game-like but clean
- 📱 Mobile-first layout
- 🧘 Simple, readable screens

---

## 🧭 App Flow

Current temporary flow:

```text
Splash
  ↓
Home
  ↓
Categories
  ↓
Difficulty
  ↓
Quiz Preview
```

The current navigation is state-based inside `QuizMasterApp.kt`.

Navigation Compose may be added later when the project grows.

---

## 🗂 Project Structure

```text
app/src/main/java/com/example/quizmaster/
│
├── MainActivity.kt
├── QuizMasterApp.kt
│
├── navigation/
│   └── AppScreen.kt
│
├── data/
│   ├── local/
│   │   ├── CategoryCount.kt
│   │   ├── QuestionDao.kt
│   │   ├── QuestionEntity.kt
│   │   └── QuizDatabase.kt
│   │
│   └── repository/
│       └── QuestionRepository.kt
│
├── ui/
│   ├── components/
│   │   ├── CategoryBadge.kt
│   │   ├── CategoryHeader.kt
│   │   └── GlowCircle.kt
│   │
│   ├── screens/
│   │   ├── splash/
│   │   │   └── SplashScreen.kt
│   │   ├── home/
│   │   │   └── HomeScreen.kt
│   │   ├── category/
│   │   │   └── CategoryScreen.kt
│   │   ├── difficulty/
│   │   │   └── DifficultyScreen.kt
│   │   └── quiz/
│   │       └── QuizPreviewScreen.kt
│   │
│   └── theme/
│       ├── Color.kt
│       └── Theme.kt
```

---

## 🏠 Main Screens

### 🏁 Home Screen

The Home screen contains:

- App title
- Hero quiz card
- Start Quiz button
- Question/category summary cards
- Menu cards for:
  - Categories
  - Difficulty
  - Stats

### 🧩 Category Screen

Current categories:

| Category | Questions |
|---|---:|
| Sports | 120 |
| Movies | 120 |
| History | 120 |
| Animals | 120 |

### 🎚 Difficulty Screen

Difficulty levels:

| Difficulty | Value |
|---|---:|
| Easy | 1 |
| Medium | 2 |
| Hard | 3 |
| Mixed | Random mix |

### ⏱ Quiz Preview Screen

Current quiz preview contains:

- Selected category
- Selected difficulty
- Real question card loaded from the local database
- Four shuffled answer options
- 15-second countdown timer
- Warning visual effect at 5 seconds

Timer behavior:

```text
15 → 14 → 13 → ... → 5
```

At 5 seconds or less:

- timer color changes
- timer circle scales slightly
- warning feel is stronger

---

## 🗄 Database

The app uses a local prebuilt SQLite database through Room.

Database file location:

```text
app/src/main/assets/quiz_master_questions.db
```

Room database name:

```text
quiz_master_questions.db
```

Room table:

```text
questions
```

---

## 📊 Database Content

Current database:

```text
Total questions: 480
```

By category:

| Category | Count |
|---|---:|
| Sports | 120 |
| Movies | 120 |
| History | 120 |
| Animals | 120 |

By difficulty:

| Difficulty | Count |
|---|---:|
| 1 | 120 |
| 2 | 212 |
| 3 | 148 |

---

## 🧱 Database Schema

```sql
CREATE TABLE questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    category TEXT NOT NULL,
    difficulty INTEGER NOT NULL,
    question TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    wrong_answer_1 TEXT NOT NULL,
    wrong_answer_2 TEXT NOT NULL,
    wrong_answer_3 TEXT NOT NULL
);
```

Indexes:

```sql
CREATE INDEX index_questions_category ON questions(category);
CREATE INDEX index_questions_difficulty ON questions(difficulty);
CREATE INDEX index_questions_category_difficulty ON questions(category, difficulty);
```

---

## 🧬 Room Entity

```kotlin
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: Int,

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
```

---

## 🧠 Room Queries

Current DAO supports:

- Count all questions
- Get all questions
- Get category counts
- Get random questions by category
- Get random questions by category and difficulty
- Get mixed random questions

Example query:

```sql
SELECT * FROM questions
WHERE category = :category
AND difficulty = :difficulty
ORDER BY RANDOM()
LIMIT :limit
```

---

## 📦 Dependencies

Main dependencies:

```kotlin
implementation("androidx.room:room-runtime:2.8.3")
implementation("androidx.room:room-ktx:2.8.3")
ksp("androidx.room:room-compiler:2.8.3")

implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
```

KSP plugin:

```kotlin
id("com.google.devtools.ksp")
```

---

## 🏗 Build

From the project root:

```powershell
cd D:\QuizMaster
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

Successful build goal:

```text
BUILD SUCCESSFUL
```

---

## 🌱 Current MVP Strategy

The MVP avoids backend complexity.

Current approach:

```text
Prebuilt DB in assets
        ↓
Room database
        ↓
Repository
        ↓
Compose UI
```

This keeps the app:

- offline-first
- fast
- simple to debug
- easy to ship
- free from backend setup

---

## 🔮 Planned Features

Planned next steps:

- 🎮 Real quiz gameplay
- 🔀 Randomized answer order
- ✅ Correct/wrong answer feedback
- 🧮 Score calculation
- 📈 Result screen
- 📊 Local stats
- 🧭 Proper Navigation Compose setup
- 🧪 Database read test on app startup
- 🎯 Progress indicator per question
- 🔁 Play again flow
- 🏆 Score history
- ⚙️ Settings screen
- 🌐 Optional online sync later

---

## 🧪 Debug Notes

Useful Gradle commands:

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
.\gradlew.bat :app:kspDebugKotlin --stacktrace
```

Useful Git commands:

```bash
git status
git diff
git add .
git commit -m "Describe completed step"
```

---

## 🧯 Known Development Rules

Project rules:

- Commit after every larger working step.
- Do not keep everything inside `MainActivity.kt`.
- UI screens belong in separate files.
- Reusable UI belongs in `ui/components`.
- Database code belongs in `data/local`.
- Repository code belongs in `data/repository`.
- Old `.db` files are not used anymore.
- Current source of truth is `quiz_master_questions.db`.

---

## 🧑‍💻 Git Workflow

After every stable step:

```bash
git status
git add .
git commit -m "Add clear commit message"
```

Example commit messages:

```bash
git commit -m "Add animated home screen"
git commit -m "Add category screen design"
git commit -m "Refactor screens into separate files"
git commit -m "Add Room prebuilt database setup"
git commit -m "Add project README"
```

---

## 🧔 Author

Built by **Pjer Drpić**.

Project codename:

```text
ANDROID-QuizMaster
```

Development style:

```text
One step at a time.
No rushing.
Commit after every major step.
Marijan gets yelled at when he overcomplicates things.
```

---

## ☕ Final Note

This project is powered by:

- caffeine
- stubborn debugging
- late-night commits
- black + blue UI
- one very patient developer
- and one assistant who has been told to slow down

```text
Quiz Master — beat the timer, master the quiz.
```
