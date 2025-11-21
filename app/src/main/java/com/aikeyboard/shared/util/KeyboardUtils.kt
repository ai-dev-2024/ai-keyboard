package com.aikeyboard.shared.util

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    private const val TAG = "KeyboardUtils"
    
    /**
     * Check if the keyboard is enabled in system settings
     */
    fun isKeyboardEnabled(context: Context): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethods = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_INPUT_METHODS
        ) ?: return false
        
        val componentName = ComponentName(context, "com.aikeyboard.ime.AIKeyboardService")
        val keyboardId = componentName.flattenToShortString()
        
        return enabledInputMethods.contains(keyboardId)
    }
    
    /**
     * Check if the keyboard is set as the default input method
     */
    fun isKeyboardDefault(context: Context): Boolean {
        val defaultInputMethod = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.DEFAULT_INPUT_METHOD
        ) ?: return false
        
        val componentName = ComponentName(context, "com.aikeyboard.ime.AIKeyboardService")
        val keyboardId = componentName.flattenToShortString()
        
        return defaultInputMethod == keyboardId
    }
    
    /**
     * Get the keyboard component name
     */
    fun getKeyboardComponentName(context: Context): ComponentName {
        return ComponentName(context, "com.aikeyboard.ime.AIKeyboardService")
    }
    
    /**
     * Open the input method settings screen
     */
    fun openInputMethodSettings(context: Context) {
        try {
            val intent = android.content.Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open input method settings", e)
        }
    }
    
    /**
     * Show the input method picker to select the keyboard
     */
    fun showInputMethodPicker(context: Context) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to show input method picker", e)
        }
    }
    
    /**
     * Enable the keyboard programmatically (requires special permission)
     * Note: This may not work on all devices/emulators. User may need to enable manually.
     * Note: setInputMethodEnabled is not available in standard Android API.
     * This method opens the input method settings instead.
     */
    fun enableKeyboard(context: Context): Boolean {
        return try {
            // Cannot programmatically enable IME without system permissions
            // Instead, open the settings screen for user to enable manually
            openInputMethodSettings(context)
            Log.d(TAG, "Opened input method settings for manual enable")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open input method settings", e)
            false
        }
    }
}



