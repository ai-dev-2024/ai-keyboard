package com.aikeyboard.ui.onboarding

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import com.aikeyboard.shared.util.KeyboardUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isKeyboardEnabled by remember { mutableStateOf(false) }
    var isKeyboardDefault by remember { mutableStateOf(false) }
    
    // Check keyboard status periodically
    LaunchedEffect(Unit) {
        while (true) {
            isKeyboardEnabled = KeyboardUtils.isKeyboardEnabled(context)
            isKeyboardDefault = KeyboardUtils.isKeyboardDefault(context)
            delay(500) // Check every 500ms
        }
    }
    
    val steps = listOf(
        OnboardingStep(
            title = "AI Keyboard",
            description = "Offline. Private. Intelligent.",
            icon = Icons.Default.Keyboard,
            showNext = true,
            showAnimatedLogo = true
        ),
        OnboardingStep(
            title = "Enable the Keyboard",
            description = if (isKeyboardEnabled) {
                "✓ AI Keyboard is enabled!\n\nYou can now proceed to set it as default."
            } else {
                "To use AI Keyboard, you need to enable it in your device settings.\n\n1. Tap 'Open Settings' below\n2. Find 'AI Keyboard' in the list\n3. Toggle it ON\n4. Tap 'OK' when prompted"
            },
            icon = Icons.Default.Settings,
            showNext = isKeyboardEnabled,
            actionText = if (isKeyboardEnabled) null else "Open Settings",
            onAction = if (isKeyboardEnabled) null else {
                {
                    KeyboardUtils.openInputMethodSettings(context)
                }
            }
        ),
        OnboardingStep(
            title = "Set as Default",
            description = if (isKeyboardDefault) {
                "✓ AI Keyboard is your default keyboard!\n\nYou're all set to start typing."
            } else if (isKeyboardEnabled) {
                "Make AI Keyboard your default keyboard for seamless typing.\n\n1. Tap 'Set as Default' below\n2. Select 'AI Keyboard' from the list\n3. Confirm your selection"
            } else {
                "Please enable AI Keyboard first (previous step)."
            },
            icon = Icons.Default.Star,
            showNext = isKeyboardDefault,
            actionText = if (isKeyboardDefault) null else if (isKeyboardEnabled) "Set as Default" else null,
            onAction = if (isKeyboardDefault || !isKeyboardEnabled) null else {
                {
                    KeyboardUtils.openInputMethodSettings(context)
                }
            }
        ),
        OnboardingStep(
            title = "Voice Input Setup",
            description = "To use voice typing, you'll need to install an AI model. You can do this later in Settings > Voice Input > Models.",
            icon = Icons.Default.Mic,
            showNext = true
        ),
        OnboardingStep(
            title = "You're All Set!",
            description = "Start typing with AI Keyboard. Long-press the spacebar or use the microphone icon for voice input.",
            icon = Icons.Default.CheckCircle,
            showNext = false,
            actionText = "Get Started",
            onAction = onComplete
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setup") },
                navigationIcon = {
                    if (currentStep > 0) {
                        IconButton(onClick = { currentStep-- }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = (currentStep + 1) / steps.size.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Step content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
                },
                label = "onboarding_step"
            ) { step ->
                val stepData = steps[step]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (stepData.showAnimatedLogo) {
                        AnimatedLogo()
                        Spacer(modifier = Modifier.height(32.dp))
                    } else {
                        Icon(
                            imageVector = stepData.icon,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    
                    Text(
                        text = stepData.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = stepData.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    TextButton(onClick = { currentStep-- }) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
                if (steps[currentStep].showNext) {
                    Button(
                        onClick = {
                            if (currentStep < steps.size - 1) {
                                currentStep++
                            } else {
                                onComplete()
                            }
                        }
                    ) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = steps[currentStep].onAction ?: onComplete
                    ) {
                        Text(steps[currentStep].actionText ?: "Get Started")
                    }
                }
            }
        }
    }
}

data class OnboardingStep(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val showNext: Boolean,
    val actionText: String? = null,
    val onAction: (() -> Unit)? = null,
    val showAnimatedLogo: Boolean = false
)

@Composable
fun AnimatedLogo() {
    var logoAlpha by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        ) { value, _ ->
            logoAlpha = value
        }
    }
    
    Box(
        modifier = Modifier
            .size(120.dp)
            .alpha(logoAlpha)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        com.aikeyboard.ui.theme.ElectricBlue,
                        com.aikeyboard.ui.theme.AITeal
                    )
                ),
                shape = androidx.compose.foundation.shape.CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AI",
            style = MaterialTheme.typography.displayMedium,
            color = com.aikeyboard.ui.theme.PureWhite,
            fontWeight = FontWeight.Bold
        )
    }
}
