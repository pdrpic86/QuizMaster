# QuizMaster UI Patch

Changes included:

- Removed `SKIP QUESTION` from the answer list.
- Added Material 3 confirmation dialog for skipping a question.
- Skipped questions are marked as unanswered and do not change the score.
- Relaxed login email validation for testing: login only requires non-empty email and password.
- Reworked theme tokens with a richer Material 3 neon palette.
- Added polished background ambient glow animation.
- Added richer MD3 color scheme, surfaces, containers, outlines, and gradients.

Local build command:

```powershell
.\gradlew.bat clean :app:assembleDebug :app:installDebug
```

If Gradle cannot find Android SDK, recreate `local.properties` locally:

```properties
sdk.dir=C\:\\Users\\pdrpic86\\AppData\\Local\\Android\\Sdk
```
