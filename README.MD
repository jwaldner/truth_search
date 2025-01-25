# <Project Name>

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📱 Overview

Truth Search is an Android application that Provides Bible search in kjv english and displays 
results in more modern translations like esv. This project is built using

<tools/technologies used> 

and adheres to modern Android development practices.

---

## 🛠 Features

- Feature 1: side by side esv kjv
- Feature 2: search esv with kjv terms
- Feature 3: study notes and verse lists saved to 

🛠 Tech Stack
Programming Language: [Kotlin/Java]
UI Framework: [Jetpack Compose/XML]
Dependency Injection: [Dagger/Hilt/Koin]
Networking: [Retrofit/OkHttp]
Database: [Room]


---

## 🚀 Getting Started

### Prerequisites

- Android Studio Koala or newer
- Minimum SDK: 26
- Target SDK: 35
- Gradle 8.13

### Updating Bible Data

To update the Bible text or search index, follow these steps:

1.  **HTML Files**: Place updated `.html` files in `app/src/main/assets/bibles/`. The app automatically copies these to the internal cache on startup.
    *   **Note**: If you are developing in Android Studio, you may need to **Clean Project** and **Rebuild Project** to ensure the new assets are bundled into the APK.
2.  **JSON Indices**: Update the index files:
    *   `app/src/main/assets/bibles/kjv/search_index.json`
    *   `app/src/main/assets/bibles/esv/full_verse_index.json`
    *   `app/src/main/assets/bibles/esv/search_index.json`
3.  **Trigger Database Update**: Open each of the JSON files above and increment the `"version"` integer value. The app compares this version against the one stored in `SharedPreferences`; if the version in the JSON is higher, it will automatically re-populate the Room database with the new content.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/<project-name>.git
   cd <project-name>

### ssl cert
## keytool -genkeypair -alias localserver -keyalg RSA -keysize 2048 -validity 10000 -keystore server.keystore

https://www.blueletterbible.org/lexicon/g5206/lxx/lxx/0-1/

https://www.openbible.info/labs/cross-references/search?q=deu+32%3A8