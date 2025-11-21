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
        try {
            setContent {
                AIKeyboardTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        OnboardingScreen(
                            onComplete = {
                                try {
                                    // Navigate to settings after onboarding
                                    startActivity(
                                        android.content.Intent(this@OnboardingActivity, SettingsActivity::class.java)
                                    )
                                    finish()
                                } catch (e: Exception) {
                                    android.util.Log.e("OnboardingActivity", "Error navigating to settings", e)
                                    // Try to continue anyway
                                    finish()
                                }
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("OnboardingActivity", "Critical error in onCreate", e)
            // Show a simple error message
            setContentView(android.widget.TextView(this).apply {
                text = "Error initializing app. Please restart."
                setPadding(50, 50, 50, 50)
            })
        }
    }
}

