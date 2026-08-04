# SpeakUp - Android application

SpeakUp is a native Android app that helps users practice spoken-language pronunciation with an animated AI tutor. Users pick lesson categories, listen to a talking avatar speak target sentences, record their own attempt, and receive AI-generated pronunciation scoring and spoken feedback.

The app is built with **Kotlin** and **Jetpack Compose**, follows an **MVVM + Repository** architecture, and talks to a separate AI backend service over REST for lesson content, speech evaluation, and progress tracking.

> This repository contains the **Android client only**. The AI/backend service that powers speech evaluation and lesson data lives in a separate repository (see [Backend / API Integration](#backend--api-integration)).

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Backend / API Integration](#backend--api-integration)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Running the App](#running-the-app)

## Architecture Overview

### System Architecture

```mermaid
flowchart LR
    subgraph Client["Android App"]
        App["SpeakUp Android App"]
    end

    subgraph Backend["AI Learning Backend"]
        API["FastAPI REST API"]
        DB[("PostgreSQL")]
    end

    Firebase["Firebase Auth"]
    AzureASR["Azure Speech - Pronunciation Assessment"]
    AzureTTS["Azure Speech - Neural TTS + Visemes"]
    LLM["LLM Feedback Model (Ollama)"]

    App -->|"Sign in / sign up"| Firebase
    App -->|"REST/JSON, Bearer: Firebase ID token"| API
    App -->|"Avatar's line to synthesize"| AzureTTS
    AzureTTS -->|"Synthesized speech + viseme events"| App

    API -->|"Verify ID token"| Firebase
    API -->|"Score recorded audio"| AzureASR
    LLM -->|"Generate spoken feedback"| API
    API --> DB
```

The app is a client over a dedicated [ **AI learning backend**](#backend--api-integration):

- Owns lesson content, user progress, and all AI/ML processing (speech-to-text, pronunciation scoring, feedback generation)
- Built with **Python / FastAPI**, backed by **PostgreSQL** (SQLAlchemy + Alembic)
- Verifies the same **Firebase** identity the app authenticates with
- Integrates **Azure Cognitive Services Speech** (pronunciation assessment) and an LLM feedback stage (OpenAI-compatible API, served locally via Ollama)

### Application Architecture

The app follows **MVVM (Model-View-ViewModel)** with a **Repository** pattern, organized as a **feature-first package structure**. Each feature (`auth`, `home`, `categoryDetails`, `lessonDetails`, `lessonProgress`, `lessonEnd`, `progress`, `badgeCelebration`, `profile`) owns its own `ui/`, `data/`, `di/`, and `models/` sub-packages.

- **UI layer** — Jetpack Compose screens (`*Screen.kt`) driven by a `StateFlow<UiState>` exposed from a `@HiltViewModel`.
- **ViewModel layer** — one `ViewModel` per screen, updating a single immutable UI-state via `MutableStateFlow.update {}`. A shared [`UiState<T>`](app/src/main/java/com/example/learningapp/core/UiState.kt) sealed class standardizes loading/error states across features.
- **Repository layer** — each feature defines a repository *interface* backed by Retrofit, bound via **Hilt** per feature module.
- **Navigation** — a single Compose `NavHost` with two nested graphs, `AUTH` and `MAIN`, defined in [`AppNavGraph.kt`](app/src/main/java/com/example/learningapp/navigation/AppNavGraph.kt).
- **Dependency Injection** — **Hilt** end-to-end; each feature has its own `di/` module, plus app-wide modules for networking (`network/NetworkModule.kt`) and local storage (`core/DataStoreModule.kt`).

```mermaid
flowchart TD
    UI["Compose UI Screen"]
    VM["ViewModel (Hilt-injected)"]
    Repo["Repository Interface"]
    Api["ApiService (Retrofit)"]
    Http["OkHttp Client<br/>AuthInterceptor"]
    Backend[("Backend REST API")]

    UI -->|"user actions"| VM
    VM -->|"UI state"| UI
    VM --> Repo
    Repo --> Api
    Api --> Http
    Http -->|"Firebase ID token"| Backend
```
## Features

| Feature | Description | Implementation |
|---|---|---|
| **Firebase Authentication (Email/Password + Google Sign-In)** | Login, registration, and Google One Tap sign-in via Credential Manager, all routed through a single repository abstraction. | [FirebaseAuthRepositoryImpl](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/auth/data/FirebaseAuthRepositoryImpl.kt#L10-L107) |
| **Interactive Lesson Player** | A per-sentence state machine (`READY_TO_START → AVATAR_SPEAKING → WAITING_FOR_RECORDING → RECORDING → ANALYZING → SHOWING_FEEDBACK`) that drives the avatar, microphone, and network calls in lockstep, including run resumption and lifecycle-aware pause/cleanup. | [LessonProgressViewModel](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/lessonProgress/ui/LessonProgressViewModel.kt#L25-L237) |
| **Real-Time Talking Avatar (Viseme Lip-Sync)** | A Compose avatar whose mouth shape updates live from Azure TTS viseme events, scaled proportionally to any avatar size. | [Avatar](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/avatar/Avatar.kt#L26-L69) · [VisemeMapper](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/avatar/VisemeMapper.kt#L5-L31) |
| **Azure Neural Text-to-Speech with Viseme Streaming** | Synthesizes lesson sentences and spoken feedback aloud, streaming viseme IDs to the UI as speech plays, with clean start/stop/voice-switch lifecycle handling. | [AzureTtsService](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/lessonProgress/data/services/AzureTtsService.kt#L20-L133) |
| **On-Device Audio Recording for Pronunciation Assessment** | Records the user's spoken attempt via `MediaRecorder` (AAC/MPEG-4) to app-private cache storage, then hands the file off for upload and scoring. | [AndroidAudioRecorderService](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/lessonProgress/data/services/AndroidAudioRecorderService.kt#L13-L113) |
| **Badge & Achievement Celebration System** | A process-wide durable queue that reconciles badges earned inline (from a lesson-completion response) with badges fetched via a resume-time fallback check, guaranteeing a celebration is never silently lost, and renders as a global overlay above any screen. | [BadgeCelebrationCoordinator](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/core/BadgeCelebrationCoordinator.kt#L28-L82) · [BadgeCelebrationHost](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/badgeCelebration/ui/BadgeCelebrationHost.kt#L30-L67) |
| **Selectable AI Tutor Persona** | Users choose a male/female avatar and voice, persisted locally via DataStore Preferences and applied to both the visual avatar and the TTS voice. | [AvatarSelectionSection](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/profile/ui/components/AvatarSelectionSection.kt#L37-L80) · [DataStoreModule](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/core/DataStoreModule.kt#L18-L31) |
| **Progress Dashboard (Lazy-Loaded Tabs)** | Overview / Category Achievements / Badges tabs, each independently lazy-loaded on first visit and force-refreshed reactively whenever a new badge is earned elsewhere in the app. | [ProgressViewModel](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/progress/ui/ProgressViewModel.kt#L34-L145) |
| **Type-Safe Nested Navigation Graph** | A single `NavHost` split into `AUTH` and `MAIN` nested graphs with typed route argument passing (category ID, lesson ID, run ID) and explicit back-stack management (e.g. popping the lesson player off the stack on completion). | [AppNavGraph](https://github.com/<ORG>/LearningApp/blob/main/app/src/main/java/com/example/learningapp/navigation/AppNavGraph.kt#L26-L203) |

## Tech Stack

Extracted from [`app/build.gradle.kts`](app/build.gradle.kts) and [`gradle/libs.versions.toml`](gradle/libs.versions.toml):

| Category | Library | Version | Notes |
|---|---|---|---|
| Language | Kotlin | `2.0.21` | |
| UI | Jetpack Compose BOM | `2026.02.00` | |
| UI | Material 3 | via Compose BOM | + Material Icons Extended |
| Navigation | Navigation Compose | `2.9.7` | |
| Architecture | Lifecycle ViewModel Compose | `2.10.0` | |
| DI | Dagger Hilt | `2.55` | + `hilt-navigation-compose 1.3.0`, KSP for annotation processing |
| Local storage | DataStore Preferences | `1.2.0` | Persisted user settings (e.g. selected tutor avatar) |
| Networking | Retrofit | `2.11.0` | + `converter-gson` |
| Networking | OkHttp | `4.12.0` | + `logging-interceptor` |
| Auth | Firebase Authentication | BOM `34.9.0` | |
| Auth | AndroidX Credential Manager | `1.5.0` | + Google Identity `googleid 1.2.0` for Google Sign-In |
| Speech / AI | Microsoft Cognitive Services Speech SDK | `1.47.0` | Azure neural text-to-speech with viseme events |
| Speech / AI | Android `MediaRecorder` | native | Microphone capture, no external dependency |
| Images | Coil Compose | `2.7.0` | Image loading |
| Build tooling | Google Services plugin | `4.4.4` | Firebase config |
| Build tooling | Secrets Gradle Plugin | `2.0.1` | Build-time secret injection into `BuildConfig` |
| Build | Android Gradle Plugin | `8.13.2` | |
| Build | Gradle | `8.13` | |
| Build | `compileSdk` / `targetSdk` / `minSdk` | `36` / `36` / `26` | Java/Kotlin bytecode target: 11 |
| Testing | JUnit4, AndroidX Test, Espresso Core, Compose UI Test | — | `ui-test-junit4`, `ui-test-manifest` |

## Project Structure

```
LearningApp/
├── app/
│   └── src/main/java/com/example/learningapp/
│       ├── auth/               # Login, registration, Firebase auth repository
│       │   ├── data/           #   AuthRepository + FirebaseAuthRepositoryImpl
│       │   ├── login/ui/       #   LoginScreen + LoginViewModel
│       │   ├── register/ui/    #   RegisterScreen + RegisterViewModel
│       │   └── di/             #   Hilt bindings for auth
│       ├── home/                # Home screen: category list
│       ├── categoryDetails/     # Category detail screen: lessons within a category
│       ├── lessonDetails/       # Lesson detail / overview before starting
│       ├── lessonProgress/      # The interactive lesson player (avatar, mic, ASR)
│       │   └── data/services/  #   TtsService, AudioRecorderService implementations
│       ├── lessonEnd/           # Post-lesson summary, score, feedback
│       ├── progress/            # Progress dashboard (overview / achievements / badges tabs)
│       ├── badgeCelebration/    # Global badge-earned celebration overlay
│       ├── profile/             # User profile, tutor avatar selection, account settings
│       ├── avatar/              # Shared Avatar composable + viseme → drawable mapping
│       ├── navigation/          # AppNavGraph, routes
│       ├── network/             # Retrofit ApiService, OkHttp/Retrofit DI, auth interceptor
│       ├── core/                # Cross-feature singletons: UiState, DataStore, badge coordinator
│       ├── ui/                  # Shared Compose components + Material 3 theme
│       ├── MainActivity.kt
│       └── MyApplication.kt     # @HiltAndroidApp entry point
│   └── src/
│       ├── test/                # Local JVM unit tests
│       └── androidTest/         # Instrumented / Compose UI tests
├── gradle/libs.versions.toml    # Version catalog
├── app/build.gradle.kts         # App module build config & dependencies
└── build.gradle.kts             # Root build config
```

Each feature package generally follows the same internal shape: `ui/` (Composables + ViewModel), `data/` (repository interface + implementation(s)), `models/` (DTOs/UI models), and `di/` (Hilt module binding the repository).

## Backend / API Integration

All network calls are defined in a single Retrofit interface, [`network/ApiService.kt`](app/src/main/java/com/example/learningapp/network/ApiService.kt), with the base URL and OkHttp/Retrofit setup in [`network/NetworkModule.kt`](app/src/main/java/com/example/learningapp/network/NetworkModule.kt). Every request is authenticated with a Firebase ID token via `AuthInterceptor`.

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/categories/` | List all lesson categories for the home screen |
| `GET` | `/categories/{category_id}` | Fetch a category's details and its lessons |
| `GET` | `/lessons/{lesson_id}` | Fetch a single lesson's details |
| `POST` | `/lessons/{lesson_id}/start?is_resume={bool}` | Start (or resume) a lesson run; returns a `run_id` used to correlate all subsequent calls for that session |
| `GET` | `/lessons/{lesson_id}/sentences` | Fetch the sentences for an interactive lesson session |
| `POST` | `/asr` (multipart) | Upload recorded audio (`file`, `sentence_id`, `run_id`, optional `language`) for speech recognition + pronunciation scoring |
| `POST` | `/lessons/{lesson_id}/complete` | Finalize a lesson run; returns overall score, spoken feedback text, and any newly-earned badges |
| `GET` | `/progress/overview` | Aggregated progress stats for the Progress dashboard |
| `GET` | `/progress/categories` | Per-category achievement stats |
| `GET` | `/progress/badges` | All badges earned by the user |
| `GET` | `/progress/badges/unseen` | Badges earned but not yet shown to the user (celebration fallback) |
| `POST` | `/progress/badges/seen` | Acknowledge that a badge's celebration has been shown (`204 No Content`) |

This contract was cross-referenced against the backend's route definitions to confirm paths and methods match exactly.

The sequence below traces the app's core AI loop end-to-end — recording a pronunciation attempt, scoring it, and speaking the feedback back — since it's the only flow that hits an external AI service mid-request:

```mermaid
sequenceDiagram
    actor User
    participant UI as LessonProgressScreen
    participant VM as LessonProgressViewModel
    participant Mic as AudioRecorderService
    participant Repo as LessonProgressRepository
    participant API as Backend /asr (FastAPI)
    participant Azure as Azure Speech (Pronunciation Assessment)
    participant LLM as Feedback Model (Ollama)
    participant DB as PostgreSQL

    User->>UI: Tap record, speak sentence, tap stop
    UI->>VM: stopRecordingAndAnalyze()
    VM->>Mic: stopRecording()
    Mic-->>VM: audio file (.m4a)
    VM->>Repo: evaluateSpeech(audioFile, sentenceId, runId)
    Repo->>API: POST /asr (multipart, Bearer Firebase token)
    API->>API: verify Firebase token, look up target sentence
    API->>Azure: assess_pronunciation(audio, target_sentence)
    Azure-->>API: recognized text + word/phoneme scores
    API->>API: Stage 1 deterministic analysis (score, errors)
    API->>LLM: generate_feedback(analysis report)
    LLM-->>API: spoken feedback text
    API->>DB: record_attempt(score, run_id)
    API-->>Repo: AssessmentResponse (score, feedback text, ...)
    Repo-->>VM: Result.success(evaluation)
    VM->>UI: uiState = SHOWING_FEEDBACK
    VM->>VM: ttsService.speakText(feedback text)
    Note over UI: Avatar lip-syncs via Azure TTS viseme events
```

Backend service: https://github.com/YuvalHaski/AIAvatarLearningApp

## Setup & Installation

### Prerequisites

- **Android Studio** (latest stable — Ladybug/Koala or newer recommended for Compose + AGP 8.13 support)
- **JDK 17** (required to run Gradle/AGP 8.13.2; the app's own Kotlin/Java bytecode target is 11)
- **Android SDK Platform 36** and **SDK Build-Tools** matching `compileSdk`/`targetSdk` 36
- A device or emulator running **Android 8.0 (API 26)** or higher (`minSdk = 26`)
- A running instance of the [backend service](#backend--api-integration) (or access to a deployed instance) for the app to be functional beyond static UI

### Clone & Open

```bash
git clone https://github.com/<ORG>/LearningApp.git
cd LearningApp
```

Open the project in Android Studio and let it sync Gradle (the wrapper pins Gradle `8.13`, so no local Gradle install is required).

### Firebase

The app authenticates via Firebase Auth and expects a `google-services.json` for your Firebase project at `app/google-services.json` (a placeholder/example project file is included in this repo — replace it with your own Firebase project's config for a working build). Enable **Email/Password** and **Google** sign-in providers in your Firebase project's Authentication settings.

## Configuration

Two things need to be set before the app can talk to speech services and to your backend:

1. **Azure Speech credentials** — the app reads `BuildConfig.AZURE_SPEECH_KEY` / `AZURE_SPEECH_REGION`, injected at build time by the `secrets-gradle-plugin`. Add the following to your local (git-ignored) `local.properties`:

   ```properties
   AZURE_SPEECH_KEY=your_azure_speech_resource_key
   AZURE_SPEECH_REGION=your_azure_region
   ```

2. **Backend base URL** — currently hardcoded in [`NetworkModule.kt`](app/src/main/java/com/example/learningapp/network/NetworkModule.kt) as `BASE_URL`. Update it to point at your backend instance:
   - Android **emulator** talking to a backend running on the same machine: `http://10.0.2.2:8000/`
   - **Physical device** on the same Wi-Fi network: `http://<your-computer's-LAN-IP>:8000/`

   The app also requires cleartext (non-HTTPS) traffic for local development (`android:usesCleartextTraffic="true"` in the manifest) — switch to HTTPS and remove this flag for any non-local deployment.

## Running the App

1. Ensure the backend service is running and reachable at the URL configured above.
2. Select a run configuration (`app`) and a target device/emulator in Android Studio.
3. Run (`Shift+F10` / the green ▶ button), or from the command line:

   ```bash
   ./gradlew installDebug
   ```

### Permissions

Declared in [`AndroidManifest.xml`](app/src/main/AndroidManifest.xml):

| Permission | Reason |
|---|---|
| `INTERNET` | REST calls to the backend and direct calls to Azure Speech (TTS) |
| `RECORD_AUDIO` | Capturing the user's spoken attempt during a lesson for pronunciation scoring |
