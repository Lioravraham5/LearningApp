package com.example.learningapp.lessonProgress

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.lessonProgress.components.AvatarSpeechSection
import com.example.learningapp.lessonProgress.components.LessonControlBar
import com.example.learningapp.lessonProgress.models.ASRCombinedOut
import com.example.learningapp.lessonProgress.models.LLMOut
import com.example.learningapp.lessonProgress.models.Sentence
import com.example.learningapp.ui.components.ErrorStateComponent

@Composable
fun LessonProgressScreen(
    lessonId: String,
    viewModel: LessonProgressViewModel = hiltViewModel(),
    onExitLesson: () -> Unit // Callback to navigate back to LessonDetails
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // When the screen first appears, trigger the data fetch
    LaunchedEffect(lessonId) {
        // Only load if we haven't loaded sentences yet
        if (uiState.sentences.isEmpty() && uiState.step == LessonStep.LOADING_SENTENCES) {
            viewModel.loadLesson(lessonId)
        }
    }

    // BEST PRACTICE: Pass down all specific ViewModel actions as individual lambdas.
    // This keeps the Content component strictly UI-focused and highly testable.
    LessonProgressContent(
        state = uiState,
        onExitLesson = onExitLesson,
        onRetryLoad = { viewModel.loadLesson(lessonId) },
        onPlayClick = { viewModel.playCurrentSentence() },
        onReplayClick = { viewModel.playCurrentSentence() },
        onStartRecordingClick = { viewModel.startRecording() },
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
    // State to control the visibility of the "Are you sure you want to exit?" dialog
    var showExitConfirmation by remember { mutableStateOf(false) }

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
                                visemeId = state.currentVisemeId
                                // avatarType = AvatarType.MALE // Assuming it defaults to MALE
                            )
                        }

                        // --- STEP 3 & 4: THE DYNAMIC CONTROL BAR ---
                        LessonControlBar(
                            step = state.step,
                            score = state.currentEvaluation?.llm?.score,
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
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text(text = "Exit Lesson?") },
            text = { Text(text = "Your progress in this current session will be lost.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitConfirmation = false
                        onExitLesson() // Actually trigger the exit
                    }
                ) {
                    Text("Exit", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
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
                sentences = mockSentences, // משתמש באותה רשימה שיצרנו בשאר ה-Previews
                currentSentenceIndex = 0,
                currentVisemeId = 0 // פה סגור (ממתין למשתמש שידבר)
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
    // Mocking an LLM output where the user missed the word "software"
    val mockEvaluation = ASRCombinedOut(
        transcript = "I am a engineer.",
        llm = LLMOut(
            isCorrect = false,
            correctedText = "I am a software engineer.",
            missingWords = listOf("software"),
            extraWords = emptyList(),
            substitutions = emptyList(),
            feedback = listOf("Almost perfect! Don't forget the word 'software'."),
            score = 80,
            detectedLanguage = "en"
        )
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
    val perfectEvaluation = ASRCombinedOut(
        transcript = "I am a software engineer.",
        llm = LLMOut(
            isCorrect = true,
            correctedText = "I am a software engineer.",
            missingWords = emptyList(),
            extraWords = emptyList(),
            substitutions = emptyList(),
            feedback = listOf("Excellent pronunciation!"),
            score = 100,
            detectedLanguage = "en"
        )
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