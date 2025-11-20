package com.aikeyboard.asrtesting.benchmark

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class TestResult(
    val testName: String,
    val modelId: String,
    val expectedText: String? = null,
    val transcribedText: String,
    val latencyMs: Long,
    val memoryUsageMB: Double,
    val wordErrorRate: Double? = null,
    val confidenceScore: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class ModelBenchmarkReport(
    val modelId: String,
    val modelName: String,
    val engine: String,
    val modelSizeMB: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val deviceInfo: DeviceInfo,
    val loadTimeMs: Long,
    val warmupTimeMs: Long,
    val metrics: BenchmarkMetrics,
    val testResults: List<TestResult>
) {
    companion object {
        fun generate(
            modelId: String,
            modelName: String,
            engine: String,
            modelSizeMB: Double,
            loadTimeMs: Long,
            warmupTimeMs: Long,
            metrics: ASRBenchmarkMetrics,
            testResults: List<TestResult>,
            deviceInfo: DeviceInfo
        ): ModelBenchmarkReport {
            return ModelBenchmarkReport(
                modelId = modelId,
                modelName = modelName,
                engine = engine,
                modelSizeMB = modelSizeMB,
                timestamp = System.currentTimeMillis(),
                deviceInfo = deviceInfo,
                loadTimeMs = loadTimeMs,
                warmupTimeMs = warmupTimeMs,
                metrics = BenchmarkMetrics(
                    averageLatencyMs = metrics.averageLatencyMs,
                    minLatencyMs = 0.0, // Can be enhanced
                    maxLatencyMs = 0.0, // Can be enhanced
                    medianLatencyMs = 0.0, // Can be enhanced
                    peakMemoryUsageMB = metrics.peakMemoryUsageMB,
                    averageMemoryUsageMB = metrics.averageMemoryUsageMB,
                    wordsPerSecond = metrics.wordsPerSecond,
                    averageWordErrorRate = metrics.wordErrorRate,
                    partialResultCount = metrics.partialResultCount,
                    totalChunksProcessed = metrics.totalChunksProcessed,
                    confidenceScore = metrics.confidenceScore
                ),
                testResults = testResults
            )
        }
    }
}

@Serializable
data class BenchmarkMetrics(
    val averageLatencyMs: Double,
    val minLatencyMs: Double,
    val maxLatencyMs: Double,
    val medianLatencyMs: Double,
    val peakMemoryUsageMB: Double,
    val averageMemoryUsageMB: Double,
    val wordsPerSecond: Double,
    val averageWordErrorRate: Double,
    val partialResultCount: Int,
    val totalChunksProcessed: Int,
    val confidenceScore: Double? = null
)

@Serializable
data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkInt: Int,
    val totalMemoryMB: Long
)

class ASRTestReportGenerator(private val reportsDir: File) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    init {
        reportsDir.mkdirs()
    }
    
    fun saveReport(report: ModelBenchmarkReport): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date(report.timestamp))
        val filename = "benchmark_${report.modelId}_${timestamp}.json"
        val file = File(reportsDir, filename)
        
        file.writeText(json.encodeToString(ModelBenchmarkReport.serializer(), report))
        return file
    }
    
    fun loadLatestReport(modelId: String? = null): ModelBenchmarkReport? {
        val files = reportsDir.listFiles { _, name ->
            name.startsWith("benchmark_") && name.endsWith(".json") &&
                    (modelId == null || name.contains(modelId))
        }?.sortedByDescending { it.lastModified() }
        
        return files?.firstOrNull()?.let { file ->
            try {
                json.decodeFromString(ModelBenchmarkReport.serializer(), file.readText())
            } catch (e: Exception) {
                null
            }
        }
    }
    
    fun listReports(): List<ModelBenchmarkReport> {
        val files = reportsDir.listFiles { _, name ->
            name.startsWith("benchmark_") && name.endsWith(".json")
        }?.sortedByDescending { it.lastModified() } ?: return emptyList()
        
        return files.mapNotNull { file ->
            try {
                json.decodeFromString(ModelBenchmarkReport.serializer(), file.readText())
            } catch (e: Exception) {
                null
            }
        }
    }
    
    fun getDeviceInfo(context: android.content.Context): DeviceInfo {
        val activityManager = context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val memInfo = android.app.ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)
        
        return DeviceInfo(
            manufacturer = android.os.Build.MANUFACTURER,
            model = android.os.Build.MODEL,
            androidVersion = android.os.Build.VERSION.RELEASE,
            sdkInt = android.os.Build.VERSION.SDK_INT,
            totalMemoryMB = (memInfo.totalMem / (1024 * 1024))
        )
    }
}

