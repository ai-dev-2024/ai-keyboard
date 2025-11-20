package com.aikeyboard.ime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aikeyboard.ime.autocorrect.AutoCorrectEngine
import com.aikeyboard.shared.data.preferences.PreferencesManager
import com.aikeyboard.voiceinput.service.VoiceInputService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class KeyboardViewModel @Inject constructor(
    private val autoCorrectEngine: AutoCorrectEngine,
    private val voiceInputService: VoiceInputService,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    val keycapShape = preferencesManager.keycapShape.map { shapeName ->
        try {
            com.aikeyboard.ui.settings.sections.KeycapShape.valueOf(shapeName)
        } catch (e: IllegalArgumentException) {
            com.aikeyboard.ui.settings.sections.KeycapShape.ROUNDED
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = com.aikeyboard.ui.settings.sections.KeycapShape.ROUNDED
    )

    val suggestions: StateFlow<List<String>> = _currentText
        .debounce(300)
        .flatMapLatest { text ->
            if (text.isNotEmpty()) {
                flow {
                    emit(autoCorrectEngine.getSuggestions(text, 5))
                }
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isVoiceRecording = MutableStateFlow(false)
    val isVoiceRecording: StateFlow<Boolean> = _isVoiceRecording.asStateFlow()

    val partialTranscription = voiceInputService.partialTranscription.asStateFlow()

    fun updateCurrentText(text: String) {
        _currentText.value = text
    }

    suspend fun startVoiceInput() {
        if (!voiceInputService.hasAudioPermission()) {
            return
        }

        val activeModel = preferencesManager.activeModel.first()
        if (activeModel != null) {
            voiceInputService.loadModel(activeModel).onSuccess {
                voiceInputService.startCapture().onSuccess {
                    _isVoiceRecording.value = true
                }
            }
        }
    }

    suspend fun stopVoiceInput() {
        voiceInputService.stopCapture().let { result ->
            _isVoiceRecording.value = false
            if (result is com.aikeyboard.voiceinput.engine.TranscriptionResult.Success) {
                updateCurrentText(result.text)
            }
        }
    }

    fun addWordToDictionary(word: String) {
        viewModelScope.launch {
            autoCorrectEngine.addWord(word)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            if (_isVoiceRecording.value) {
                voiceInputService.stopCapture()
            }
        }
    }
}

