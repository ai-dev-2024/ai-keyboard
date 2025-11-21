package com.aikeyboard.voiceinput.engine

import java.io.File

sealed class EngineLoadResult {
    data object Success : EngineLoadResult()
    data class Error(val message: String) : EngineLoadResult()
}

sealed class TranscriptionResult {
    data class Success(val text: String) : TranscriptionResult()
    data class Partial(val text: String) : TranscriptionResult()
    data class Error(val message: String) : TranscriptionResult()
}

interface VoiceEngine {
    suspend fun loadModel(modelDir: File): EngineLoadResult
    suspend fun unloadModel()
    suspend fun startCapture()
    suspend fun processAudioChunk(pcm16: ShortArray)
    suspend fun stopCapture(): TranscriptionResult
    val supportsPartial: Boolean
    val isLoaded: Boolean
}












