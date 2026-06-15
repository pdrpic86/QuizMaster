# 🧠 Quiz Master

![Android](https://img.shields.io/badge/Android-Kotlin-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-UI-2196F3?style=for-the-badge&logo=materialdesign&logoColor=white)
![Room](https://img.shields.io/badge/Room-Database-0D1117?style=for-the-badge&logo=sqlite&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-Client-000000?style=for-the-badge&logo=kotlin&logoColor=white)

**Quiz Master** is a modern Android quiz app built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and an offline-first architecture using **Room** and **Ktor**.

The app is designed as a clean, fast experience with category selection, difficulty selection, animated screens, and a smooth countdown timer.

---

## ✨ Project Vision

Quiz Master is built around a simple idea:

> Pick a category. Choose difficulty. Beat the timer. Prove your knowledge.

The app uses an **offline-first** strategy, where content is fetched from a remote backend via **Ktor** and cached locally in **Room**.

Current data flow:

```text
Ktor Server → Ktor Client → Room Database → Compose UI
```

---

## 🚀 Current Status

Implemented so far:

- ✅ Kotlin Android project
- ✅ Jetpack Compose UI
- ✅ Material Design 3 theme
- ✅ Black + blue visual identity
- ✅ Native Splash screen (Android 12+ API)
- ✅ Animated Home screen
- ✅ **Glassmorphic Login Screen** with background glow
- ✅ Category selection screen
- ✅ Difficulty selection screen
- ✅ Quiz preview screen
- ✅ **Smooth Timer Animation** (Circular progress + sliding text)
- ✅ Modular screen structure
- ✅ **Room local database** (with automatic question population)
- ✅ **Ktor Client Integration** for remote sync
- ✅ **Offline-first architecture**

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design System | Material Design 3 |
| Networking | Ktor Client |
| Serialization | Kotlinx Serialization |
| Database | Room |
| Build | Gradle Kotlin DSL |
| Annotation Processing | KSP |
| IDE | Android Studio |

---

## 🎨 Design Direction

The visual identity is:

```text
Dominant color: Black
Secondary color: Blue
Style: Modern dark quiz/game UI (Glassmorphism)
```

---

## 🧭 App Flow

```text
System Splash (Native API)
  ↓
Login (Glassmorphic)
  ↓
Home
  ↓
Categories
  ↓
Difficulty
  ↓
Quiz Preview
```

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
│   ├── local/ (Room DB & Dao)
│   ├── remote/ (Ktor API Service)
│   └── repository/ (Data Sync & Repo)
│
├── ui/
│   ├── components/ (Common UI)
│   ├── screens/ (Login, Home, Quiz, etc.)
│   └── theme/ (Colors & Styling)
```

---

## ⏱ Smooth Timer Animation

The quiz features a high-performance timer:
- **Circular Progress**: A canvas-drawn track that depletes linearly.
- **Animated Text**: Numbers slide vertically on every second change.
- **Warning State**: The timer pulses and turns red when 5 seconds remain.

---

## 🌐 Networking (Ktor)

The app is professional-ready with Ktor Client:
- **Offline-First**: Fetches fresh questions and caches them locally.
- **Reliability**: Seamlessly falls back to local SQLite if the network is unavailable.
- **Scalable**: Ready for Compose Multiplatform (iOS).

---

## 🧪 Debug Notes

Useful Gradle commands:

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

---

## 🧔 Author

Built by **Pjer Drpić**.

```text
Quiz Master — beat the timer, master the quiz.
```
