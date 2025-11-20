package com.aikeyboard.asrtesting.benchmark

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ASRBenchmarkMetrics(
    val modelName: String = "",
    val modelSize: Long = 0,
    val loadTimeMs: Long = 0,
    val warmupTimeMs: Long = 0,
    val averageLatencyMs: Double = 0.0,
    val peakMemoryUsageMB: Double = 0.0,
    val averageMemoryUsageMB: Double = 0.0,
    val wordsPerSecond: Double = 0.0,
    val wordErrorRate: Double = 0.0,
    val partialResultCount: Int = 0,
    val totalChunksProcessed: Int = 0,
    val totalTranscriptionTimeMs: Long = 0,
    val confidenceScore: Double? = null
) {
    companion object {
        fun empty() = ASRBenchmarkMetrics()
    }
}

class MemoryMonitor(private val context: Context) {
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    
    private var baselineMemoryMB = 0.0
    private val memorySamples = mutableListOf<Double>()
    
    fun startMonitoring() {
        baselineMemoryMB = getCurrentMemoryUsageMB()
        memorySamples.clear()
    }
    
    fun sampleMemory() {
        memorySamples.add(getCurrentMemoryUsageMB())
    }
    
    fun getCurrentMemoryUsageMB(): Double {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val nativeHeap = Debug.getNativeHeapAllocatedSize()
        return (usedMemory + nativeHeap) / (1024.0 * 1024.0)
    }
    
    fun getPeakMemoryMB(): Double {
        return memorySamples.maxOrNull() ?: baselineMemoryMB
    }
    
    fun getAverageMemoryMB(): Double {
        return if (memorySamples.isEmpty()) {
            baselineMemoryMB
        } else {
            memorySamples.average()
        }
    }
    
    fun getMemoryIncreaseMB(): Double {
        val current = getCurrentMemoryUsageMB()
        return current - baselineMemoryMB
    }
}

class LatencyTracker {
    private val latencies = mutableListOf<Long>()
    private var chunkStartTime = 0L
    
    fun startChunk() {
        chunkStartTime = System.currentTimeMillis()
    }
    
    fun endChunk() {
        val latency = System.currentTimeMillis() - chunkStartTime
        latencies.add(latency)
    }
    
    fun getAverageLatencyMs(): Double {
        return if (latencies.isEmpty()) 0.0 else latencies.average()
    }
    
    fun getMinLatencyMs(): Long {
        return latencies.minOrNull() ?: 0L
    }
    
    fun getMaxLatencyMs(): Long {
        return latencies.maxOrNull() ?: 0L
    }
    
    fun getMedianLatencyMs(): Double {
        if (latencies.isEmpty()) return 0.0
        val sorted = latencies.sorted()
        val mid = sorted.size / 2
        return if (sorted.size % 2 == 0) {
            (sorted[mid - 1] + sorted[mid]) / 2.0
        } else {
            sorted[mid].toDouble()
        }
    }
    
    fun clear() {
        latencies.clear()
    }
    
    fun getCount(): Int = latencies.size
}

fun calculateWER(reference: String, hypothesis: String): Double {
    val refWords = reference.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
    val hypWords = hypothesis.trim().lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
    
    if (refWords.isEmpty()) {
        return if (hypWords.isEmpty()) 0.0 else 1.0
    }
    
    val n = refWords.size
    val m = hypWords.size
    
    // Use Levenshtein distance algorithm for word-level
    val dp = Array(n + 1) { IntArray(m + 1) }
    
    for (i in 0..n) dp[i][0] = i
    for (j in 0..m) dp[0][j] = j
    
    for (i in 1..n) {
        for (j in 1..m) {
            if (refWords[i - 1] == hypWords[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + 1   // substitution
                )
            }
        }
    }
    
    val errors = dp[n][m]
    return errors.toDouble() / n.toDouble()
}

fun calculateWordsPerSecond(text: String, durationSeconds: Double): Double {
    if (durationSeconds <= 0.0) return 0.0
    val wordCount = text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size
    return wordCount / durationSeconds
}

