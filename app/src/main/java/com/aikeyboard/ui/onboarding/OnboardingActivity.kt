package com.aikeyboard.ui.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aikeyboard.ui.theme.AIKeyboardTheme
import com.aikeyboard.ui.settings.SettingsActivity

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIKeyboardTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OnboardingScreen(
                        onComplete = {
                            // Navigate to settings after onboarding
                            startActivity(
                                android.content.Intent(this, SettingsActivity::class.java)
                            )
                            finish()
                        }
                    )
                }
            }
        }
    }
}

