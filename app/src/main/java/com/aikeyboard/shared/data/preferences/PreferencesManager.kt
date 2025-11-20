package com.aikeyboard.shared.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aikeyboard_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_ACTIVE_MODEL = stringPreferencesKey("active_model")
        private val KEY_VOICE_INPUT_ENABLED = booleanPreferencesKey("voice_input_enabled")
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_THEME_PRESET = stringPreferencesKey("theme_preset")
        private val KEY_IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val KEY_KEYCAP_SHAPE = stringPreferencesKey("keycap_shape")
        private val KEY_KEYBOARD_HEIGHT = intPreferencesKey("keyboard_height")
        private val KEY_AUTO_CORRECT = booleanPreferencesKey("auto_correct")
        private val KEY_GESTURE_TYPING = booleanPreferencesKey("gesture_typing")
        private val KEY_IS_PREMIUM = booleanPreferencesKey("is_premium")
    }

    val activeModel: Flow<String?> = dataStore.data.map { it[KEY_ACTIVE_MODEL] }
    val voiceInputEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_VOICE_INPUT_ENABLED] ?: false }
    val theme: Flow<String> = dataStore.data.map { it[KEY_THEME] ?: "system" }
    val themePreset: Flow<String> = dataStore.data.map { it[KEY_THEME_PRESET] ?: "default" }
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { it[KEY_IS_DARK_THEME] ?: false }
    val keycapShape: Flow<String> = dataStore.data.map { it[KEY_KEYCAP_SHAPE] ?: "ROUNDED" }
    val keyboardHeight: Flow<Int> = dataStore.data.map { it[KEY_KEYBOARD_HEIGHT] ?: 50 }
    val autoCorrect: Flow<Boolean> = dataStore.data.map { it[KEY_AUTO_CORRECT] ?: true }
    val gestureTyping: Flow<Boolean> = dataStore.data.map { it[KEY_GESTURE_TYPING] ?: true }
    val isPremium: Flow<Boolean> = dataStore.data.map { it[KEY_IS_PREMIUM] ?: false }

    suspend fun setActiveModel(modelId: String?) {
        dataStore.edit { it[KEY_ACTIVE_MODEL] = modelId ?: "" }
    }

    suspend fun setVoiceInputEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_VOICE_INPUT_ENABLED] = enabled }
    }

    suspend fun setTheme(theme: String) {
        dataStore.edit { it[KEY_THEME] = theme }
    }

    suspend fun setThemePreset(preset: String) {
        dataStore.edit { it[KEY_THEME_PRESET] = preset }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[KEY_IS_DARK_THEME] = enabled }
    }

    suspend fun setKeycapShape(shape: String) {
        dataStore.edit { it[KEY_KEYCAP_SHAPE] = shape }
    }

    suspend fun setKeyboardHeight(height: Int) {
        dataStore.edit { it[KEY_KEYBOARD_HEIGHT] = height }
    }

    suspend fun setAutoCorrect(enabled: Boolean) {
        dataStore.edit { it[KEY_AUTO_CORRECT] = enabled }
    }

    suspend fun setGestureTyping(enabled: Boolean) {
        dataStore.edit { it[KEY_GESTURE_TYPING] = enabled }
    }
    
    suspend fun setPremiumStatus(isPremium: Boolean) {
        dataStore.edit { it[KEY_IS_PREMIUM] = isPremium }
    }
}

