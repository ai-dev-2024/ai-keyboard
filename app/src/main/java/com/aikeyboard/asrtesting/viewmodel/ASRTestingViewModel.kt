package com.aikeyboard.asrtesting.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aikeyboard.asrtesting.FileTranscriptionService
import com.aikeyboard.asrtesting.TestAudioClip
import com.aikeyboard.asrtesting.TestAudioManager
import com.aikeyboard.voiceinput.engine.onnx.ONNXVoiceEngine
import com.aikeyboard.voiceinput.engine.vosk.VoskVoiceEngine
import com.aikeyboard.asrtesting.benchmark.*
import com.aikeyboard.voiceinput.engine.TranscriptionResult
import com.aikeyboard.voiceinput.modelmanager.InstalledModel
import com.aikeyboard.voiceinput.modelmanager.ModelManager
import com.aikeyboard.voiceinput.service.VoiceInputService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ASRTestingState(
    val models: List<InstalledModel> = emptyList(),
    val selectedModel: InstalledModel? = null,
    val isModelLoaded: Boolean = false,
    val isRecording: Boolean = false,
    val currentMetrics: ASRBenchmarkMetrics = ASRBenchmarkMetrics.empty(),
    val partialTranscript: String? = null,
    val finalTranscript: String? = null,
    val testClips: List<TestAudioClip> = emptyList(),
    val selectedTestClip: TestAudioClip? = null,
    val isTranscribingFile: Boolean = false,
    val fileTranscriptionResult: String? = null,
    val errorMessage: String? = null,
    val testResults: List<TestResult> = emptyList(),
    val isGeneratingReport: Boolean = false
)

@HiltViewModel
class ASRTestingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val voiceInputService: VoiceInputService,
    private val modelManager: ModelManager
) : AndroidViewModel(context.applicationContext as Application) {
    
    private val _state = MutableStateFlow(ASRTestingState())
    val state: StateFlow<ASRTestingState> = _state.asStateFlow()
    
    private val testAudioManager = TestAudioManager(context)
    private val memoryMonitor = MemoryMonitor(context)
    private val latencyTracker = LatencyTracker()
    private val reportGenerator = ASRTestReportGenerator(
        File(context.filesDir, "ai_keyboard_tests")
    )
    
    private var testEngine: com.aikeyboard.voiceinput.engine.VoiceEngine? = null
    private var loadStartTime = 0L
    private var warmupStartTime = 0L
    private var testStartTime = 0L
    private var totalTranscriptionTime = 0L
    private var partialResultCount = 0
    
    init {
        loadModels()
        loadTestClips()
    }
    
    private fun loadModels() {
        viewModelScope.launch {
            modelManager.listInstalledModels().collect { models ->
                _state.update { it.copy(models = models) }
                if (models.isNotEmpty() && _state.value.selectedModel == null) {
                    selectModel(models.first())
                }
            }
        }
    }
    
    private fun loadTestClips() {
        _state.update { it.copy(testClips = testAudioManager.getTestClips()) }
    }
    
    fun selectModel(model: InstalledModel) {
        _state.update { it.copy(selectedModel = model) }
    }
    
    fun loadModel() {
        val model = _state.value.selectedModel ?: return
        
        viewModelScope.launch {
            try {
                _state.update { it.copy(errorMessage = null) }
                loadStartTime = System.currentTimeMillis()
                memoryMonitor.startMonitoring()
                
                // Load model through service (for microphone testing)
                val result = voiceInputService.loadModel(model.id)
                
                // Also create a test engine instance for file transcription
                testEngine = when {
                    model.engine.lowercase() == "vosk" -> VoskVoiceEngine()
                    else -> ONNXVoiceEngine()
                }
                
                val engineLoadResult = testEngine?.loadModel(model.modelDir)
                
                if (result.isSuccess && engineLoadResult is com.aikeyboard.voiceinput.engine.EngineLoadResult.Success) {
                    val loadTime = System.currentTimeMillis() - loadStartTime
                    memoryMonitor.sampleMemory()
                    
                    // Warmup
                    warmupStartTime = System.currentTimeMillis()
                    performWarmup()
                    val warmupTime = System.currentTimeMillis() - warmupStartTime
                    
                    val modelSize = File(model.modelDir, model.manifest.file).length() / (1024 * 1024)
                    
                    _state.update {
                        it.copy(
                            isModelLoaded = true,
                            currentMetrics = it.currentMetrics.copy(
                                modelName = model.displayName,
                                modelSize = modelSize,
                                loadTimeMs = loadTime,
                                warmupTimeMs = warmupTime,
                                peakMemoryUsageMB = memoryMonitor.getMemoryIncreaseMB(),
                                averageMemoryUsageMB = memoryMonitor.getAverageMemoryMB()
                            )
                        )
                    }
                } else {
                    testEngine = null
                    _state.update {
                        it.copy(errorMessage = result.exceptionOrNull()?.message ?: "Failed to load model")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading model", e)
                testEngine = null
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    private suspend fun performWarmup() {
        // Perform a dummy warmup inference if possible
        // This is engine-specific, but we'll just sample memory
        memoryMonitor.sampleMemory()
    }
    
    fun unloadModel() {
        viewModelScope.launch {
            voiceInputService.cleanup()
            testEngine?.unloadModel()
            testEngine = null
            _state.update {
                it.copy(
                    isModelLoaded = false,
                    currentMetrics = ASRBenchmarkMetrics.empty()
                )
            }
            latencyTracker.clear()
            partialResultCount = 0
            totalTranscriptionTime = 0L
        }
    }
    
    fun startRecording() {
        if (!_state.value.isModelLoaded) {
            _state.update { it.copy(errorMessage = "Model not loaded") }
            return
        }
        
        viewModelScope.launch {
            try {
                memoryMonitor.startMonitoring()
                latencyTracker.clear()
                partialResultCount = 0
                totalTranscriptionTime = 0L
                testStartTime = System.currentTimeMillis()
                
                val result = voiceInputService.startCapture()
                
                if (result.isSuccess) {
                    latencyTracker.clear()
                    latencyTracker.startChunk()
                    
                    _state.update {
                        it.copy(
                            isRecording = true,
                            partialTranscript = null,
                            finalTranscript = null,
                            errorMessage = null
                        )
                    }
                    
                    // Monitor partial results
                    monitorPartialResults()
                } else {
                    _state.update {
                        it.copy(errorMessage = result.exceptionOrNull()?.message ?: "Failed to start recording")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error starting recording", e)
                _state.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    private fun monitorPartialResults() {
        viewModelScope.launch {
            voiceInputService.partialTranscription.collect { partial ->
                if (partial != null) {
                    latencyTracker.endChunk()
                    partialResultCount++
                    memoryMonitor.sampleMemory()
                    
                    _state.update {
                        it.copy(
                            partialTranscript = partial,
                            currentMetrics = it.currentMetrics.copy(
                                partialResultCount = partialResultCount,
                                averageLatencyMs = latencyTracker.getAverageLatencyMs(),
                                peakMemoryUsageMB = maxOf(
                                    it.currentMetrics.peakMemoryUsageMB,
                                    memoryMonitor.getPeakMemoryMB()
                                ),
                                averageMemoryUsageMB = memoryMonitor.getAverageMemoryMB()
                            )
                        )
                    }
                    
                    latencyTracker.startChunk()
                }
            }
        }
    }
    
    fun stopRecording() {
        viewModelScope.launch {
            try {
                latencyTracker.endChunk()
                val stopTime = System.currentTimeMillis()
                totalTranscriptionTime = stopTime - testStartTime
                
                val result = voiceInputService.stopCapture()
                
                when (result) {
                    is TranscriptionResult.Success -> {
                        val wordsPerSecond = calculateWordsPerSecond(
                            result.text,
                            totalTranscriptionTime / 1000.0
                        )
                        
                        _state.update {
                            it.copy(
                                isRecording = false,
                                finalTranscript = result.text,
                                currentMetrics = it.currentMetrics.copy(
                                    totalTranscriptionTimeMs = totalTranscriptionTime,
                                    wordsPerSecond = wordsPerSecond,
                                    totalChunksProcessed = latencyTracker.getCount(),
                                    averageLatencyMs = latencyTracker.getAverageLatencyMs(),
                                    peakMemoryUsageMB = memoryMonitor.getPeakMemoryMB(),
                                    averageMemoryUsageMB = memoryMonitor.getAverageMemoryMB()
                                )
                            )
                        }
                    }
                    is TranscriptionResult.Error -> {
                        _state.update {
                            it.copy(
                                isRecording = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    else -> {
                        _state.update { it.copy(isRecording = false) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping recording", e)
                _state.update {
                    it.copy(
                        isRecording = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
    
    fun selectTestClip(clip: TestAudioClip) {
        _state.update { it.copy(selectedTestClip = clip) }
    }
    
    fun transcribeTestFile() {
        val clip = _state.value.selectedTestClip ?: return
        val audioFile = testAudioManager.getClipFile(clip) ?: run {
            _state.update { it.copy(errorMessage = "Audio file not found: ${clip.filename}") }
            return
        }
        
        if (!_state.value.isModelLoaded) {
            _state.update { it.copy(errorMessage = "Model not loaded") }
            return
        }
        
        viewModelScope.launch {
            try {
                _state.update {
                    it.copy(
                        isTranscribingFile = true,
                        fileTranscriptionResult = null,
                        errorMessage = null
                    )
                }
                
                // Use file transcription service
                val engine = testEngine ?: run {
                    _state.update {
                        it.copy(
                            isTranscribingFile = false,
                            errorMessage = "Test engine not loaded"
                        )
                    }
                    return@launch
                }
                
                memoryMonitor.startMonitoring()
                latencyTracker.clear()
                val transcriptionStart = System.currentTimeMillis()
                
                latencyTracker.startChunk()
                val transcriptionService = FileTranscriptionService(engine)
                val result = transcriptionService.transcribeAudioFile(audioFile)
                latencyTracker.endChunk()
                
                val transcriptionTime = System.currentTimeMillis() - transcriptionStart
                val transcriptionTime = System.currentTimeMillis() - transcriptionStart
                val duration = testAudioManager.getClipDuration(audioFile) / 1000.0
                
                val duration = testAudioManager.getClipDuration(audioFile) / 1000.0
                
                when (result) {
                    is TranscriptionResult.Success -> {
                        val wer = clip.expectedText?.let { expected ->
                            calculateWER(expected, result.text)
                        }
                        
                        val wordsPerSecond = calculateWordsPerSecond(result.text, duration)
                        
                        val testResult = TestResult(
                            testName = clip.name,
                            modelId = _state.value.selectedModel?.id ?: "",
                            expectedText = clip.expectedText,
                            transcribedText = result.text,
                            latencyMs = transcriptionTime,
                            memoryUsageMB = memoryMonitor.getPeakMemoryMB(),
                            wordErrorRate = wer,
                            confidenceScore = null
                        )
                        
                        val updatedResults = _state.value.testResults + testResult
                        
                        _state.update {
                            it.copy(
                                isTranscribingFile = false,
                                fileTranscriptionResult = result.text,
                                testResults = updatedResults,
                                currentMetrics = it.currentMetrics.copy(
                                    averageLatencyMs = latencyTracker.getAverageLatencyMs(),
                                    wordsPerSecond = wordsPerSecond,
                                    wordErrorRate = updatedResults.mapNotNull { it.wordErrorRate }.average().takeIf { !it.isNaN() } ?: 0.0,
                                    peakMemoryUsageMB = memoryMonitor.getPeakMemoryMB()
                                )
                            )
                        }
                    }
                    is TranscriptionResult.Error -> {
                        _state.update {
                            it.copy(
                                isTranscribingFile = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    else -> {
                        _state.update { it.copy(isTranscribingFile = false) }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error transcribing file", e)
                _state.update {
                    it.copy(
                        isTranscribingFile = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
    
    fun generateReport() {
        val model = _state.value.selectedModel ?: return
        val metrics = _state.value.currentMetrics
        val testResults = _state.value.testResults
        
        if (testResults.isEmpty() && metrics.modelSize == 0L) {
            _state.update { it.copy(errorMessage = "No test results or model metrics to generate report") }
            return
        }
        
        viewModelScope.launch {
            try {
                _state.update { it.copy(isGeneratingReport = true) }
                
                val deviceInfo = reportGenerator.getDeviceInfo(context)
                
                // Enhance metrics with latency tracker stats if available
                val enhancedMetrics = metrics.copy(
                    averageLatencyMs = if (metrics.averageLatencyMs > 0) metrics.averageLatencyMs else latencyTracker.getAverageLatencyMs()
                )
                
                val report = ModelBenchmarkReport.generate(
                    modelId = model.id,
                    modelName = model.displayName,
                    engine = model.engine,
                    modelSizeMB = if (metrics.modelSize > 0) metrics.modelSize.toDouble() else (File(model.modelDir, model.manifest.file).length() / (1024 * 1024)).toDouble(),
                    loadTimeMs = metrics.loadTimeMs,
                    warmupTimeMs = metrics.warmupTimeMs,
                    metrics = enhancedMetrics,
                    testResults = testResults,
                    deviceInfo = deviceInfo
                )
                
                val reportFile = reportGenerator.saveReport(report)
                
                _state.update {
                    it.copy(
                        isGeneratingReport = false,
                        errorMessage = null
                    )
                }
                
                Log.d(TAG, "Report saved to: ${reportFile.absolutePath}")
            } catch (e: Exception) {
                Log.e(TAG, "Error generating report", e)
                _state.update {
                    it.copy(
                        isGeneratingReport = false,
                        errorMessage = "Failed to generate report: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
    
    companion object {
        private const val TAG = "ASRTestingViewModel"
    }
}

