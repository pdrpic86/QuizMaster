# Version Catalog notes

The version catalog is valid and is located in the required default location:

```text
gradle/libs.versions.toml
```

Gradle automatically exposes this file as `libs`, so these aliases are valid:

```kotlin
alias(libs.plugins.android.application)
implementation(libs.androidx.core.ktx)
```

If Android Studio reports `Unresolved reference: libs` or acts like it does not accept the TOML catalog, the usual cause is not the TOML syntax. Use this checklist:

1. Open the root `QuizMaster` folder, not the `app` folder.
2. Make sure `settings.gradle.kts` is in the opened root folder.
3. Delete stale local folders before syncing:
   - `.gradle/`
   - `.idea/`
   - `.kotlin/`
   - `app/build/`
4. Use **File > Invalidate Caches / Restart** in Android Studio.
5. Then run:

```powershell
.\gradlew.bat --stop
.\gradlew.bat clean assembleDebug --refresh-dependencies
```

Do not rename `gradle/libs.versions.toml`; Gradle expects exactly that default path/name for the automatic `libs` catalog.
