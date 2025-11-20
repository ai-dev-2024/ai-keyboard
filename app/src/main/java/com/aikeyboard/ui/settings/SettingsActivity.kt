package com.aikeyboard.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ui.theme.AIKeyboardTheme
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SettingsViewModel = hiltViewModel()
            val themePreset by viewModel.themePreset.collectAsState()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            
            val darkTheme = isDarkTheme || isSystemInDarkTheme()
            
            AIKeyboardTheme(
                darkTheme = darkTheme,
                themePreset = themePreset
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen(
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

