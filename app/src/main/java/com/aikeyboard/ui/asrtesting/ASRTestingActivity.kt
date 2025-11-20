package com.aikeyboard.ui.asrtesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aikeyboard.ui.theme.AIKeyboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ASRTestingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIKeyboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ASRTestingScreen(
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

