package com.aikeyboard.ime

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.aikeyboard.ime.ui.EnhancedKeyboardView
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log

@AndroidEntryPoint
class AIKeyboardService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {
    companion object {
        private const val TAG = "AIKeyboardService"
    }
    
    private var inputConnection: InputConnection? = null
    private var currentInputEditorInfo: EditorInfo? = null
    
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val viewModelStore: ViewModelStore = ViewModelStore()

    init {
        try {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
            savedStateRegistryController.performRestore(null)
            Log.d(TAG, "AI Keyboard Service initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Error in init block", e)
            // Continue anyway - Hilt might not be ready yet
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            Log.d(TAG, "AI Keyboard Service onCreate")
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            // Don't crash - continue
        }
    }

    override fun onCreateInputView(): View {
        Log.d(TAG, "AI Keyboard Service onCreateInputView")
        return try {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
            
            // Get input connection early
            inputConnection = currentInputConnection
            
            ComposeView(this).apply {
                setViewTreeLifecycleOwner(this@AIKeyboardService)
                setViewTreeViewModelStoreOwner(this@AIKeyboardService)
                setViewTreeSavedStateRegistryOwner(this@AIKeyboardService)
                setContent {
                    com.aikeyboard.ui.theme.AIKeyboardTheme {
                        EnhancedKeyboardView(
                            onTextInput = { text ->
                                try {
                                    val ic = currentInputConnection ?: inputConnection
                                    if (ic != null) {
                                        ic.commitText(text, 1)
                                        Log.d(TAG, "Committed text: $text")
                                    } else {
                                        Log.w(TAG, "InputConnection is null, cannot commit text")
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error committing text", e)
                                }
                            },
                            onBackspace = {
                                try {
                                    val ic = currentInputConnection ?: inputConnection
                                    if (ic != null) {
                                        ic.deleteSurroundingText(1, 0)
                                        Log.d(TAG, "Backspace pressed")
                                    } else {
                                        Log.w(TAG, "InputConnection is null, cannot delete")
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error deleting text", e)
                                }
                            },
                            onEnter = {
                                try {
                                    val ic = currentInputConnection ?: inputConnection
                                    if (ic != null) {
                                        val enterEvent = android.view.KeyEvent(
                                            android.view.KeyEvent.ACTION_DOWN,
                                            android.view.KeyEvent.KEYCODE_ENTER
                                        )
                                        ic.sendKeyEvent(enterEvent)
                                        ic.sendKeyEvent(
                                            android.view.KeyEvent(
                                                android.view.KeyEvent.ACTION_UP,
                                                android.view.KeyEvent.KEYCODE_ENTER
                                            )
                                        )
                                        Log.d(TAG, "Enter pressed")
                                    } else {
                                        Log.w(TAG, "InputConnection is null, cannot send enter")
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error sending enter", e)
                                }
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Critical error in onCreateInputView", e)
            // Return a simple fallback view to prevent crash
            android.widget.TextView(this).apply {
                text = "Keyboard initialization error"
            }
        }
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "AI Keyboard Service onStartInputView - restarting: $restarting")
        inputConnection = currentInputConnection
        currentInputEditorInfo = info
        
        if (inputConnection == null) {
            Log.w(TAG, "InputConnection is null in onStartInputView")
        } else {
            Log.d(TAG, "InputConnection is available")
        }
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        Log.d(TAG, "AI Keyboard Service onFinishInputView")
        inputConnection = null
        currentInputEditorInfo = null
        if (finishingInput) {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }

    override fun onBindInput() {
        super.onBindInput()
        Log.d(TAG, "AI Keyboard Service onBindInput - Service is now active!")
        inputConnection = currentInputConnection
        if (inputConnection != null) {
            Log.d(TAG, "InputConnection bound successfully")
        } else {
            Log.w(TAG, "InputConnection is null after onBindInput")
        }
    }

    override fun onUnbindInput() {
        super.onUnbindInput()
        Log.d(TAG, "AI Keyboard Service onUnbindInput")
    }

    override fun onWindowShown() {
        super.onWindowShown()
        Log.d(TAG, "Keyboard window shown - ensuring input connection")
        try {
            inputConnection = currentInputConnection
            if (inputConnection != null) {
                Log.d(TAG, "InputConnection available after window shown")
            } else {
                Log.w(TAG, "InputConnection still null after window shown")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onWindowShown", e)
        }
    }

    override fun onWindowHidden() {
        super.onWindowHidden()
        Log.d(TAG, "Keyboard window hidden")
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AI Keyboard Service onDestroy")
        viewModelStore.clear()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}

