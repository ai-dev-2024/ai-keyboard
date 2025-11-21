package com.aikeyboard.voiceinput.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.content.ContextCompat
import com.aikeyboard.voiceinput.engine.TranscriptionResult
import com.aikeyboard.voiceinput.engine.VoiceEngine
import com.aikeyboard.voiceinput.engine.onnx.ONNXVoiceEngine
import com.aikeyboard.voiceinput.engine.vosk.VoskVoiceEngine
import com.aikeyboard.voiceinput.modelmanager.ModelManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceInputService @Inject constructor(
    private val context: Context,
    private val modelManager: ModelManager
) {
    private var currentEngine: VoiceEngine? = null
    private var audioRecord: AudioRecord? = null
    private var captureJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _partialTranscription = MutableStateFlow<String?>(null)
    val partialTranscription: StateFlow<String?> = _partialTranscription.asStateFlow()

    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    suspend fun loadModel(modelId: String): Result<Unit> {
        return withContext(Dispatchers.Default) {
            try {
                val modelDir = File(modelManager.getModelsDir(), modelId)
                if (!modelDir.exists()) {
                    return@withContext Result.failure(Exception("Model not found: $modelId"))
                }

                val manifestFile = File(modelDir, "manifest.json")
                if (!manifestFile.exists()) {
                    return@withContext Result.failure(Exception("Manifest not found"))
                }

                // Create engine based on manifest
                val engine = when {
                    modelDir.name.contains("vosk", ignoreCase = true) -> VoskVoiceEngine()
                    else -> ONNXVoiceEngine()
                }

                val loadResult = engine.loadModel(modelDir)
                if (loadResult is com.aikeyboard.voiceinput.engine.EngineLoadResult.Success) {
                    currentEngine?.unloadModel()
                    currentEngine = engine
                    Result.success(Unit)
                } else {
                    Result.failure(Exception((loadResult as com.aikeyboard.voiceinput.engine.EngineLoadResult.Error).message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading model", e)
                Result.failure(e)
            }
        }
    }

    suspend fun startCapture(): Result<Unit> {
        if (currentEngine == null) {
            return Result.failure(Exception("No model loaded"))
        }

        if (!hasAudioPermission()) {
            return Result.failure(Exception("Audio permission not granted"))
        }

        return withContext(Dispatchers.Default) {
            try {
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize * 2
                )

                if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                    return@withContext Result.failure(Exception("AudioRecord initialization failed"))
                }

                currentEngine?.startCapture()
                audioRecord?.startRecording()

                captureJob = scope.launch {
                    val buffer = ShortArray(bufferSize)
                    while (isActive && audioRecord != null) {
                        val read = audioRecord!!.read(buffer, 0, buffer.size)
                        if (read > 0) {
                            currentEngine?.processAudioChunk(buffer.sliceArray(0 until read))
                        }
                    }
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting capture", e)
                Result.failure(e)
            }
        }
    }

    suspend fun stopCapture(): TranscriptionResult {
        return withContext(Dispatchers.Default) {
            try {
                captureJob?.cancel()
                captureJob = null

                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null

                val result = currentEngine?.stopCapture() ?: TranscriptionResult.Error("No engine loaded")
                
                _partialTranscription.value = null
                result
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping capture", e)
                TranscriptionResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun cleanup() {
        scope.cancel()
        scope.launch {
            currentEngine?.unloadModel()
        }
        currentEngine = null
    }

    companion object {
        private const val TAG = "VoiceInputService"
    }
}

