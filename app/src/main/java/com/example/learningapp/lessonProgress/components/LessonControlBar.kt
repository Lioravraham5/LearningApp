package com.example.learningapp.lessonProgress.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.learningapp.lessonProgress.LessonStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

// ==========================================
// 1. THE WRAPPER COMPONENT
// ==========================================
/**
 * The dynamic control bar at the bottom of the Lesson Progress screen.
 * Uses [AnimatedContent] to smoothly crossfade between different
 * control states based on the current [LessonStep], preventing jarring UI jumps.
 */
@Composable
fun LessonControlBar(
    step: LessonStep,
    score: Int?,
    onPlayClick: () -> Unit,
    onReplayClick: () -> Unit,
    onStartRecordingClick: () -> Unit,
    onStopRecordingClick: () -> Unit,
    onNextClick: () -> Unit,
    getAmplitude: () -> Int,
    modifier: Modifier = Modifier
) {
    // A Box ensures that the controls are always perfectly centered at the bottom of the screen.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp), // Breathing room from the screen edge
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                // BEST PRACTICE: Smooth fade-in and fade-out crossover animation.
                // SizeTransform(clip = false) allows the container to resize smoothly
                // if the new button is wider than the old one.
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300)) using
                        SizeTransform(clip = false)
            },
            label = "ControlBarAnimation"
        ) { targetStep ->
            // This is our State Machine UI mapping
            when (targetStep) {
                LessonStep.READY_TO_START -> {
                    ReadyToStartControl(onPlayClick = onPlayClick)
                }
                LessonStep.AVATAR_SPEAKING -> {
                    AvatarSpeakingControl()
                }
                LessonStep.WAITING_FOR_RECORDING -> {
                    WaitingForRecordingControl(
                        onReplayClick = onReplayClick,
                        onStartRecordingClick = onStartRecordingClick
                    )
                }
                LessonStep.RECORDING -> {
                    // NEW: Active recording state with visualizers
                    ActiveRecordingControl(
                        onStopRecordingClick = onStopRecordingClick,
                        getAmplitude = getAmplitude
                    )
                }
                LessonStep.ANALYZING -> {
                    AnalyzingControl()
                }
                LessonStep.SHOWING_FEEDBACK -> {
                    // NEW: Show the score and the Next button
                    FeedbackControl(
                        score = score ?: 0,
                        onNextClick = onNextClick
                    )
                }
                else -> {
                    Box(modifier = Modifier.size(72.dp))
                }
            }
        }
    }
}

// ==========================================
// 2. STATE COMPONENTS
// ==========================================

/**
 * Control UI for [LessonStep.READY_TO_START].
 * Displays a prominent, primary-colored Play button to begin the interaction.
 */
@Composable
fun ReadyToStartControl(onPlayClick: () -> Unit) {
    FloatingActionButton(
        onClick = onPlayClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(72.dp), // Large, accessible touch target
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = "Play target sentence",
            modifier = Modifier.size(36.dp) // Scaled up icon to match the large button
        )
    }
}

/**
 * Control UI for [LessonStep.AVATAR_SPEAKING].
 * Displays a disabled button with a volume icon to indicate active audio playback.
 * Giving visual feedback that an action is currently happening, preventing the user from repeatedly mashing the play button.
 */
@Composable
fun AvatarSpeakingControl() {
    FloatingActionButton(
        onClick = { /* Intentionally left blank - button is disabled */ },
        containerColor = MaterialTheme.colorScheme.surfaceVariant, // Muted background
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), // Faded icon
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp, // Flat appearance indicates it's unclickable
            pressedElevation = 0.dp
        ),
        modifier = Modifier.size(72.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            contentDescription = "Avatar is currently speaking",
            modifier = Modifier.size(36.dp)
        )
    }
}

/**
 * Control UI for [LessonStep.WAITING_FOR_RECORDING].
 * Displays a secondary Replay button on the left, and a primary Microphone button in the center.
 */
@Composable
fun WaitingForRecordingControl(
    onReplayClick: () -> Unit,
    onStartRecordingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Replay Button (Secondary action)
        // BEST PRACTICE: FilledTonalIconButton gives it a nice distinct look
        // without competing visually with the main FAB.
        FilledTonalIconButton(
            onClick = onReplayClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Replay,
                contentDescription = "Replay sentence",
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        // 2. Microphone Button (Primary action)
        FloatingActionButton(
            onClick = onStartRecordingClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(72.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Mic,
                contentDescription = "Start recording",
                modifier = Modifier.size(36.dp)
            )
        }

        // 3. Invisible Balance Spacer
        // BEST PRACTICE: We add empty space on the right equal to the width of the replay button + spacing.
        // This ensures the Microphone button stays exactly in the center of the screen!
        Spacer(modifier = Modifier.width(24.dp + 56.dp))
    }
}

/**
 * Control UI for [LessonStep.RECORDING].
 * Displays a Red Stop button in the center, surrounded by dual Audio Visualizers
 * to give immediate feedback that the microphone is picking up sound.
 */
@Composable
fun ActiveRecordingControl(
    onStopRecordingClick: () -> Unit,
    getAmplitude: () -> Int,
    modifier: Modifier = Modifier
) {
    // BEST PRACTICE: We hold the normalized amplitude state locally in the UI.
    var amplitudeProgress by remember { mutableFloatStateOf(0f) }

    // This Coroutine loops continuously AS LONG AS this Composable is on the screen.
    // It automatically cancels itself when we leave the RECORDING state!
    LaunchedEffect(Unit) {
        while (isActive) {
            val rawAmp = getAmplitude().toFloat()
            // Normalize the raw amplitude (usually 0-32767).
            // We cap it around 15,000 so normal speaking volumes fill the visualizer.
            val normalized = (rawAmp / 15000f).coerceIn(0f, 1f)
            amplitudeProgress = normalized
            delay(50) // Poll every 50ms (~20 updates per second for smooth UI)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Visualizer
        AudioVisualizer(
            amplitudeProgress = amplitudeProgress,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(64.dp).height(48.dp)
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Center Stop Button (Red)
        FloatingActionButton(
            onClick = onStopRecordingClick,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.size(72.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Stop,
                contentDescription = "Stop recording",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Right Visualizer (Mirrored feel)
        AudioVisualizer(
            amplitudeProgress = amplitudeProgress,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(64.dp).height(48.dp)
        )
    }
}

/**
 * Control UI for [LessonStep.ANALYZING].
 * Displays a clean loading indicator while waiting for the network response.
 */
@Composable
fun AnalyzingControl(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Evaluating speech and pronunciation...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Control UI for [LessonStep.SHOWING_FEEDBACK].
 * Displays an animated circular score dial and a large "Next" button.
 * BEST PRACTICE: We animate the progress bar filling up to make it feel rewarding.
 */
@Composable
fun FeedbackControl(
    score: Int,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State to trigger the animation once the component enters the screen
    var animationTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Small delay so the user sees the animation start after the transition
        delay(100)
        animationTriggered = true
    }

    val animatedScoreProgress by animateFloatAsState(
        targetValue = if (animationTriggered) (score / 100f) else 0f,
        animationSpec = tween(durationMillis = 1000), // 1 second fill animation
        label = "ScoreAnimation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. The Score Dial
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp)
        ) {
            // Background track
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 6.dp
            )
            // Animated foreground score
            CircularProgressIndicator(
                progress = { animatedScoreProgress },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 6.dp
            )
            // Score Text
            Text(
                text = "$score",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. The Big Next Button
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth(0.7f) // Takes up 70% of the screen width
                .height(56.dp),
            shape = RoundedCornerShape(28.dp) // Fully rounded pill shape
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = "Next sentence"
            )
        }
    }
}