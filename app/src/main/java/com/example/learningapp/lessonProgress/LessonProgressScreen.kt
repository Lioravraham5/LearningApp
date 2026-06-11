package com.example.learningapp.lessonProgress

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.lessonProgress.components.AvatarSpeechSection
import com.example.learningapp.lessonProgress.components.LessonControlBar
import com.example.learningapp.lessonProgress.components.PermissionRationaleDialog
import com.example.learningapp.lessonProgress.components.PermissionSettingsDialog
import com.example.learningapp.lessonProgress.models.Sentence
import com.example.learningapp.ui.components.ErrorStateComponent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.learningapp.lessonProgress.components.ExitLessonDialog
import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.PronunciationScores

@Composable
fun LessonProgressScreen(
    lessonId: String,
    viewModel: LessonProgressViewModel = hiltViewModel(),
    onExitLesson: () -> Unit // Callback to navigate back to LessonDetails
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    // State for controlling our custom dialogs
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Listen to the system's lifecycle events for background handling
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // ON_PAUSE triggers exactly when the app goes to the background
            // or the screen is no longer the active foreground window.
            if (event == Lifecycle.Event.ON_PAUSE) {
                viewModel.onAppBackgrounded()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        // Remove the observer when the composable leaves the composition
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // When the screen first appears, trigger the data fetch
    LaunchedEffect(lessonId) {
        // Only load if we haven't loaded sentences yet
        if (uiState.sentences.isEmpty() && uiState.step == LessonStep.LOADING_SENTENCES) {
            viewModel.loadLesson(lessonId)
        }
    }

    // Create the Launcher to handle the result of the permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // The user granted permission from the system dialog.
            viewModel.startRecording()
        } else {
            // The user did not grant permission.
            val shouldShowRationale = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.RECORD_AUDIO
                )
            } ?: false

            if (!shouldShowRationale) {
                // The permission is permanently denied.
                showSettingsDialog = true
            }
        }
    }

    // Intercept the record click action
    val onRecordClickIntercepted = {
        // Check if we ALREADY have the permission
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            // Scenario 1: User already have permission
            viewModel.startRecording()
        } else {
            // User don't have permission.
            // Check if we need to show our custom Rationale Dialog before launching the system request
            val shouldShowRationale = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.RECORD_AUDIO
                )
            } ?: false

            if (shouldShowRationale) {
                // Scenario 2: Soft Denial earlier. Show our custom explanation.
                showRationaleDialog = true
            } else {
                // Scenario 3: First time asking (or returning from settings). Launch system dialog.
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    if (showRationaleDialog) {
        PermissionRationaleDialog(
            onDismiss = {
                showRationaleDialog = false
            },
            onConfirm = {
                showRationaleDialog = false
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        )
    }

    if (showSettingsDialog) {
        PermissionSettingsDialog(
            onDismiss = {
                showSettingsDialog = false
            },
            onOpenSettings = {
                showSettingsDialog = false
                context.openAppSettings()
            }
        )
    }

    // BEST PRACTICE: Pass down all specific ViewModel actions as individual lambdas.
    // This keeps the Content component strictly UI-focused and highly testable.
    LessonProgressContent(
        state = uiState,
        onExitLesson = onExitLesson,
        onRetryLoad = { viewModel.loadLesson(lessonId) },
        onPlayClick = { viewModel.playCurrentSentence() },
        onReplayClick = { viewModel.playCurrentSentence() },
        onStartRecordingClick = onRecordClickIntercepted,
        onStopRecordingClick = { viewModel.stopRecordingAndAnalyze() },
        onNextClick = { viewModel.moveToNextSentence() },
        getAmplitude = { viewModel.getRecordingAmplitude() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonProgressContent(
    state: LessonProgressState,
    onExitLesson: () -> Unit,
    onRetryLoad: () -> Unit,
    // Control Bar Callbacks
    onPlayClick: () -> Unit,
    onReplayClick: () -> Unit,
    onStartRecordingClick: () -> Unit,
    onStopRecordingClick: () -> Unit,
    onNextClick: () -> Unit,
    getAmplitude: () -> Int,
    modifier: Modifier = Modifier
) {
    // State to control the visibility of the exit Lesson dialog
    var showExitConfirmation by remember { mutableStateOf(false) }

    // Handle the back button press to show the exit Lesson dialog
    BackHandler(enabled = true) {
        showExitConfirmation = true
    }

    // Animate the progress bar fill smoothly
    val animatedProgress by animateFloatAsState(
        targetValue = state.progressPercentage,
        label = "Lesson Progress Animation"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = { }, // Empty title for a clean, minimalist look
                    navigationIcon = {
                        // Use an 'X' close button instead of a back arrow
                        IconButton(onClick = { showExitConfirmation = true }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Exit Lesson",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                // Only show progress bar if we actually have sentences loaded
                if (state.sentences.isNotEmpty()) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp), // A thin, elegant progress line
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    ) { innerPadding ->

        // Main Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                // 1. Initial Loading State
                state.step == LessonStep.LOADING_SENTENCES && state.errorMessage == null -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }

                // 2. Fatal Error State (e.g., no internet when fetching sentences)
                state.errorMessage != null && state.sentences.isEmpty() -> {
                    // Requires your ErrorStateComponent to be imported
                    ErrorStateComponent(
                        message = state.errorMessage,
                        onRetry = onRetryLoad
                    )
                }

                // 3. The Active Lesson UI
                else -> {
                    // This Column coordinates the layout between the visual core and the controls
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween // Pushes controls to the bottom
                    ) {

                        // --- STEP 2: AVATAR & SPEECH BUBBLE ---
                        // Using weight(1f) ensures this section takes up all available remaining space
                        // above the control bar, keeping the layout perfectly balanced.
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            AvatarSpeechSection(
                                targetSentence = state.currentSentence?.text,
                                evaluation = state.currentEvaluation,
                                visemeId = state.currentVisemeId,
                                avatarType = state.avatarType
                            )
                        }

                        // --- STEP 3 & 4: THE DYNAMIC CONTROL BAR ---
                        LessonControlBar(
                            step = state.step,
                            score = state.currentEvaluation?.finalScore,
                            onPlayClick = onPlayClick,
                            onReplayClick = onReplayClick,
                            onStartRecordingClick = onStartRecordingClick,
                            onStopRecordingClick = onStopRecordingClick,
                            onNextClick = onNextClick,
                            getAmplitude = getAmplitude,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }

    // ==========================================
    // EXIT CONFIRMATION DIALOG
    // ==========================================
    if (showExitConfirmation) {
        ExitLessonDialog(
            onDismiss = { showExitConfirmation = false },
            onConfirmExit = {
                showExitConfirmation = false
                onExitLesson() // Actually trigger the exit
            }
        )
    }
}

/**
 * Extension function to seamlessly open the app's settings screen.
 */
fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        startActivity(this)
    }
}

// ==========================================
// PREVIEW
// ==========================================

// Dummy empty callbacks to keep the preview code clean
private val emptyAction: () -> Unit = {}
private val emptyAmplitude: () -> Int = { 0 }

// Dummy data for our previews
private val mockSentences = listOf(
    Sentence(id = "1", text = "I am a software engineer.", orderIndex = 0),
    Sentence(id = "2", text = "I love developing apps.", orderIndex = 1)
)

// Dummy Azure Pronunciation Scores for previews
private val dummyScores = PronunciationScores(
    accuracy = 85f, fluency = 90f, completeness = 100f, prosody = 88f, pronunciation = 87f
)

@Preview(name = "1. Loading State", showBackground = true)
@Composable
fun Preview_LessonLoading() {
    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(step = LessonStep.LOADING_SENTENCES),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}

@Preview(name = "2. Ready To Start", showBackground = true)
@Composable
fun Preview_LessonReady() {
    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.READY_TO_START,
                sentences = mockSentences,
                currentSentenceIndex = 0
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}

@Preview(name = "3 Waiting For Recording", showBackground = true)
@Composable
fun Preview_LessonWaitingForRecording() {
    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.WAITING_FOR_RECORDING,
                sentences = mockSentences,
                currentSentenceIndex = 0,
                currentVisemeId = 0
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}

@Preview(name = "4. Recording State", showBackground = true)
@Composable
fun Preview_LessonRecording() {
    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.RECORDING,
                sentences = mockSentences,
                currentSentenceIndex = 0,
                currentVisemeId = 0 // Avatar mouth closed while user speaks
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction,
            getAmplitude = { 12000 } // High amplitude to show active visualizer waves
        )
    }
}

@Preview(name = "5. Feedback - With Mistakes", showBackground = true)
@Composable
fun Preview_LessonFeedback_Mistakes() {
    // Mocking an Azure output where the user missed the word "software"
    val mockEvaluation = AssessmentResponse(
        sentenceId = "1",
        runId = "run123",
        recognizedText = "I am a engineer.",
        targetSentence = "I am a software engineer.",
        scores = dummyScores.copy(accuracy = 70f), // Lower accuracy
        words = emptyList(),
        finalScore = 80,
        isPassed = true, // Passed, but with mistakes
        missingWords = listOf("software"),
        extraWords = emptyList(),
        substitutions = emptyList(),
        mispronouncedWords = emptyList(),
        feedbackPoints = emptyList(),
        feedbackText = "Almost perfect! Don't forget the word 'software'."
    )

    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.SHOWING_FEEDBACK,
                sentences = mockSentences,
                currentSentenceIndex = 0,
                currentEvaluation = mockEvaluation
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}

@Preview(name = "6. Feedback - Perfect Score", showBackground = true)
@Composable
fun Preview_LessonFeedback_Perfect() {
    val perfectEvaluation = AssessmentResponse(
        sentenceId = "1",
        runId = "run123",
        recognizedText = "I am a software engineer.",
        targetSentence = "I am a software engineer.",
        scores = dummyScores,
        words = emptyList(),
        finalScore = 100,
        isPassed = true,
        missingWords = emptyList(),
        extraWords = emptyList(),
        substitutions = emptyList(),
        mispronouncedWords = emptyList(),
        feedbackPoints = emptyList(),
        feedbackText = "Excellent pronunciation!"
    )

    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.SHOWING_FEEDBACK,
                sentences = mockSentences,
                currentSentenceIndex = 0,
                currentEvaluation = perfectEvaluation
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}

@Preview(name = "7. Error State", showBackground = true)
@Composable
fun Preview_LessonError() {
    MaterialTheme {
        LessonProgressContent(
            state = LessonProgressState(
                step = LessonStep.LOADING_SENTENCES,
                errorMessage = "No internet connection. Please check your network and try again."
            ),
            onExitLesson = emptyAction, onRetryLoad = emptyAction,
            onPlayClick = emptyAction, onReplayClick = emptyAction,
            onStartRecordingClick = emptyAction, onStopRecordingClick = emptyAction,
            onNextClick = emptyAction, getAmplitude = emptyAmplitude
        )
    }
}