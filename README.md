# SMG InternApp
Small Android app for the intern task — shows property listings from an API, detail screen, and favorites saved on the phone.
## What it does
- Main screen: list of properties
- Tap item → details (photos, price, address, etc.)
- Heart icon → save to favorites (stored in Room)
- Favorites screen from the top bar link (same list UI)
## Built with
Kotlin, Jetpack Compose, Hilt, Retrofit, Room, Coil
## How to run
1. Open project in Android Studio
2. Gradle sync
3. Run **app** on emulator or phone  
   API URL is in `Constants.kt`.
## Tests
```bash
./gradlew :app:testDebugUnitTest
