package com.aikeyboard.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aikeyboard.shared.data.preferences.PreferencesManager
import com.aikeyboard.ui.settings.sections.KeycapShape
import com.aikeyboard.voiceinput.modelmanager.ModelManager
import com.aikeyboard.voiceinput.service.VoiceInputService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val modelManager: ModelManager,
    private val voiceInputService: VoiceInputService
) : ViewModel() {

    val voiceInputEnabled = preferencesManager.voiceInputEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val activeModel = preferencesManager.activeModel.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val installedModels = modelManager.listInstalledModels().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val themePreset = preferencesManager.themePreset.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "default"
    )

    val isDarkTheme = preferencesManager.isDarkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val keycapShape = preferencesManager.keycapShape.map { shapeName ->
        try {
            KeycapShape.valueOf(shapeName)
        } catch (e: IllegalArgumentException) {
            KeycapShape.ROUNDED
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = KeycapShape.ROUNDED
    )

    val keyboardHeight = preferencesManager.keyboardHeight.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 50
    )

    fun setVoiceInputEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setVoiceInputEnabled(enabled)
        }
    }

    fun setActiveModel(modelId: String) {
        viewModelScope.launch {
            preferencesManager.setActiveModel(modelId)
            voiceInputService.loadModel(modelId)
        }
    }

    fun setThemePreset(preset: String) {
        viewModelScope.launch {
            preferencesManager.setThemePreset(preset)
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(enabled)
        }
    }

    fun setKeycapShape(shape: KeycapShape) {
        viewModelScope.launch {
            preferencesManager.setKeycapShape(shape.name)
        }
    }

    fun setKeyboardHeight(height: Int) {
        viewModelScope.launch {
            preferencesManager.setKeyboardHeight(height)
        }
    }
}

