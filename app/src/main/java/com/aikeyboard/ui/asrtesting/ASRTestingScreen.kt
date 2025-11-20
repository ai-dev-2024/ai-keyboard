package com.aikeyboard.ui.asrtesting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.asrtesting.viewmodel.ASRTestingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ASRTestingScreen(
    viewModel: ASRTestingViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ASR Engine Testing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Model Selection Section
            ModelSelectionSection(
                models = state.models,
                selectedModel = state.selectedModel,
                isModelLoaded = state.isModelLoaded,
                onSelectModel = { viewModel.selectModel(it) },
                onLoadModel = { viewModel.loadModel() },
                onUnloadModel = { viewModel.unloadModel() },
                metrics = state.currentMetrics
            )
            
            Divider()
            
            // Microphone Testing Section
            MicrophoneTestingSection(
                isRecording = state.isRecording,
                partialTranscript = state.partialTranscript,
                finalTranscript = state.finalTranscript,
                onStartRecording = { viewModel.startRecording() },
                onStopRecording = { viewModel.stopRecording() },
                metrics = state.currentMetrics
            )
            
            Divider()
            
            // File Transcription Section
            FileTranscriptionSection(
                testClips = state.testClips,
                selectedClip = state.selectedTestClip,
                isTranscribing = state.isTranscribingFile,
                transcriptionResult = state.fileTranscriptionResult,
                onSelectClip = { viewModel.selectTestClip(it) },
                onTranscribe = { viewModel.transcribeTestFile() }
            )
            
            Divider()
            
            // Test Results & Report Section
            TestResultsSection(
                testResults = state.testResults,
                isGeneratingReport = state.isGeneratingReport,
                onGenerateReport = { viewModel.generateReport() }
            )
            
            // Error Message
            state.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    // Auto-dismiss error after 5 seconds
                    kotlinx.coroutines.delay(5000)
                    viewModel.clearError()
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        IconButton(onClick = { viewModel.clearError() }) {
                            Icon(Icons.Default.Close, "Dismiss", tint = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModelSelectionSection(
    models: List<com.aikeyboard.voiceinput.modelmanager.InstalledModel>,
    selectedModel: com.aikeyboard.voiceinput.modelmanager.InstalledModel?,
    isModelLoaded: Boolean,
    onSelectModel: (com.aikeyboard.voiceinput.modelmanager.InstalledModel) -> Unit,
    onLoadModel: () -> Unit,
    onUnloadModel: () -> Unit,
    metrics: com.aikeyboard.asrtesting.benchmark.ASRBenchmarkMetrics
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Model Selection",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            if (models.isEmpty()) {
                Text(
                    text = "No models installed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // Model Dropdown
                var expanded by remember { mutableStateOf(false) }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedModel?.displayName ?: "Select a model",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Model") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        models.forEach { model ->
                            DropdownMenuItem(
                                text = { Text(model.displayName) },
                                onClick = {
                                    onSelectModel(model)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                // Model Info
                selectedModel?.let { model ->
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MetricRow("Model Name", model.displayName)
                        MetricRow("Engine", model.engine.uppercase())
                        MetricRow("Size", "${model.manifest.size_bytes / 1_000_000} MB")
                        if (model.manifest.quantization != null) {
                            MetricRow("Quantization", model.manifest.quantization.uppercase())
                        }
                    }
                }
                
                // Load/Unload Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isModelLoaded) {
                        Button(
                            onClick = onLoadModel,
                            modifier = Modifier.weight(1f),
                            enabled = selectedModel != null
                        ) {
                            Icon(Icons.Default.Download, "Load Model")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Load Model")
                        }
                    } else {
                        Button(
                            onClick = onUnloadModel,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Delete, "Unload Model")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Unload Model")
                        }
                    }
                }
                
                // Load Metrics
                if (isModelLoaded && metrics.loadTimeMs > 0) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Load Metrics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    MetricRow("Load Time", "${metrics.loadTimeMs} ms")
                    MetricRow("Warmup Time", "${metrics.warmupTimeMs} ms")
                    MetricRow("Model Size", "${metrics.modelSize} MB")
                    MetricRow("Peak Memory", String.format("%.2f MB", metrics.peakMemoryUsageMB))
                    MetricRow("Avg Memory", String.format("%.2f MB", metrics.averageMemoryUsageMB))
                }
            }
        }
    }
}

@Composable
fun MicrophoneTestingSection(
    isRecording: Boolean,
    partialTranscript: String?,
    finalTranscript: String?,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    metrics: com.aikeyboard.asrtesting.benchmark.ASRBenchmarkMetrics
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Microphone Testing",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isRecording) {
                    Button(
                        onClick = onStartRecording,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Mic, "Start Recording")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start Recording")
                    }
                } else {
                    Button(
                        onClick = onStopRecording,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, "Stop Recording")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Stop Recording")
                    }
                }
            }
            
            // Recording Indicator
            if (isRecording) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.FiberManualRecord,
                        "Recording",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Recording...",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Transcripts
            if (partialTranscript != null || finalTranscript != null) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                if (partialTranscript != null) {
                    Text(
                        text = "Partial Transcript",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = partialTranscript,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                if (finalTranscript != null) {
                    Text(
                        text = "Final Transcript",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = finalTranscript,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // Real-time Metrics
            if (isRecording || metrics.averageLatencyMs > 0) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Real-time Metrics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                MetricRow("Avg Latency", String.format("%.2f ms", metrics.averageLatencyMs))
                MetricRow("Words/Second", String.format("%.2f", metrics.wordsPerSecond))
                MetricRow("Partial Results", "${metrics.partialResultCount}")
                MetricRow("Chunks Processed", "${metrics.totalChunksProcessed}")
            }
        }
    }
}

@Composable
fun FileTranscriptionSection(
    testClips: List<com.aikeyboard.asrtesting.TestAudioClip>,
    selectedClip: com.aikeyboard.asrtesting.TestAudioClip?,
    isTranscribing: Boolean,
    transcriptionResult: String?,
    onSelectClip: (com.aikeyboard.asrtesting.TestAudioClip) -> Unit,
    onTranscribe: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "File-based Transcription",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            if (testClips.isEmpty()) {
                Text(
                    text = "No test clips available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // Test Clip Selection
                var expanded by remember { mutableStateOf(false) }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedClip?.name ?: "Select a test clip",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Test Clip") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        testClips.forEach { clip ->
                            DropdownMenuItem(
                                text = { 
                                    Column {
                                        Text(clip.name)
                                        Text(
                                            clip.description,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                },
                                onClick = {
                                    onSelectClip(clip)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                selectedClip?.let { clip ->
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MetricRow("Description", clip.description)
                        MetricRow("Language", clip.language)
                        clip.expectedText?.let { expected ->
                            MetricRow("Expected Text", expected)
                        }
                    }
                }
                
                Button(
                    onClick = onTranscribe,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedClip != null && !isTranscribing
                ) {
                    if (isTranscribing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Transcribing...")
                    } else {
                        Icon(Icons.Default.PlayArrow, "Transcribe")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Transcribe File")
                    }
                }
                
                // Transcription Result
                transcriptionResult?.let { result ->
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Transcription Result",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = result,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TestResultsSection(
    testResults: List<com.aikeyboard.asrtesting.benchmark.TestResult>,
    isGeneratingReport: Boolean,
    onGenerateReport: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Test Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                if (testResults.isNotEmpty()) {
                    Button(
                        onClick = onGenerateReport,
                        enabled = !isGeneratingReport
                    ) {
                        if (isGeneratingReport) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generating...")
                        } else {
                            Icon(Icons.Default.Assessment, "Generate Report")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Generate Report")
                        }
                    }
                }
            }
            
            if (testResults.isEmpty()) {
                Text(
                    text = "No test results yet. Run file transcriptions to generate results.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(testResults) { result ->
                        TestResultCard(result)
                    }
                }
            }
        }
    }
}

@Composable
fun TestResultCard(result: com.aikeyboard.asrtesting.benchmark.TestResult) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = result.testName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            result.expectedText?.let { expected ->
                Text(
                    text = "Expected: $expected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "Transcribed: ${result.transcribedText}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                result.wordErrorRate?.let { wer ->
                    Text(
                        text = "WER: ${String.format("%.2f%%", wer * 100)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (wer < 0.1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
                
                Text(
                    text = "Latency: ${result.latencyMs} ms",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

