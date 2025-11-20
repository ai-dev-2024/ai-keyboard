package com.aikeyboard.ime

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

@AndroidEntryPoint
class AIKeyboardService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {
    private var inputConnection: InputConnection? = null
    private var currentInputEditorInfo: EditorInfo? = null
    
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val viewModelStore = ViewModelStore()

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        savedStateRegistryController.performRestore(null)
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        
        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@AIKeyboardService)
            setViewTreeViewModelStoreOwner(this@AIKeyboardService)
            setViewTreeSavedStateRegistryOwner(this@AIKeyboardService)
            setContent {
                com.aikeyboard.ui.theme.AIKeyboardTheme {
                    EnhancedKeyboardView(
                        onTextInput = { text ->
                            inputConnection?.commitText(text, 1)
                        },
                        onBackspace = {
                            inputConnection?.deleteSurroundingText(1, 0)
                        },
                        onEnter = {
                            inputConnection?.sendKeyEvent(
                                android.view.KeyEvent(
                                    android.view.KeyEvent.ACTION_DOWN,
                                    android.view.KeyEvent.KEYCODE_ENTER
                                )
                            )
                        }
                    )
                }
            }
        }
        return composeView
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        inputConnection = currentInputConnection
        currentInputEditorInfo = info
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        inputConnection = null
        currentInputEditorInfo = null
        if (finishingInput) {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = viewModelStore

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}

