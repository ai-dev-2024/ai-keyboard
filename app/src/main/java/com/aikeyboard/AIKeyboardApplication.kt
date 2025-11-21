package com.aikeyboard

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AIKeyboardApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        try {
            // Initialize native libraries in background to prevent startup crashes
            Thread {
                try {
                    // Pre-load ONNX runtime classes (lazy loading)
                    Class.forName("ai.onnxruntime.OrtEnvironment")
                    Log.d("AIKeyboardApplication", "ONNX Runtime classes loaded")
                } catch (e: ClassNotFoundException) {
                    Log.w("AIKeyboardApplication", "ONNX Runtime not available", e)
                } catch (e: Exception) {
                    Log.w("AIKeyboardApplication", "Error pre-loading ONNX", e)
                }
                
                try {
                    // Pre-load Vosk classes (lazy loading)
                    Class.forName("com.alphacephei.vosk.Model")
                    Log.d("AIKeyboardApplication", "Vosk classes loaded")
                } catch (e: ClassNotFoundException) {
                    Log.w("AIKeyboardApplication", "Vosk classes not available", e)
                } catch (e: Exception) {
                    Log.w("AIKeyboardApplication", "Error pre-loading Vosk", e)
                }
            }.start()
            
            Log.d("AIKeyboardApplication", "Application initialized successfully")
        } catch (e: Exception) {
            Log.e("AIKeyboardApplication", "Error initializing application", e)
            // Don't crash - log and continue
        }
    }
}











