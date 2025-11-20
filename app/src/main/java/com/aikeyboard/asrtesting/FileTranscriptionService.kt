package com.aikeyboard.asrtesting

import android.util.Log
import com.aikeyboard.voiceinput.engine.TranscriptionResult
import com.aikeyboard.voiceinput.engine.VoiceEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class FileTranscriptionService(
    private val engine: VoiceEngine
) {
    suspend fun transcribeAudioFile(audioFile: File, sampleRate: Int = 16000): TranscriptionResult {
        return withContext(Dispatchers.Default) {
            try {
                if (!engine.isLoaded) {
                    return@withContext TranscriptionResult.Error("Engine not loaded")
                }
                
                // Read audio file as PCM16
                val pcm16 = readWavFileAsPCM16(audioFile, sampleRate)
                if (pcm16 == null) {
                    return@withContext TranscriptionResult.Error("Failed to read audio file")
                }
                
                // Start capture
                engine.startCapture()
                
                // Process audio in chunks
                val chunkSize = sampleRate / 2 // 0.5 seconds
                var audioBuffer = mutableListOf<Short>()
                
                // Read audio data in chunks
                for (i in pcm16.indices step chunkSize) {
                    val end = minOf(i + chunkSize, pcm16.size)
                    val chunk = pcm16.sliceArray(i until end)
                    
                    audioBuffer.addAll(chunk.toList())
                    
                    // Process when we have enough data
                    if (audioBuffer.size >= chunkSize) {
                        val processChunk = audioBuffer.take(chunkSize).toShortArray()
                        audioBuffer = audioBuffer.drop(chunkSize).toMutableList()
                        
                        engine.processAudioChunk(processChunk)
                        
                        // Small delay to simulate real-time processing
                        kotlinx.coroutines.delay(50)
                    }
                }
                
                // Process remaining audio
                if (audioBuffer.isNotEmpty()) {
                    engine.processAudioChunk(audioBuffer.toShortArray())
                    kotlinx.coroutines.delay(50)
                }
                
                // Stop and get final result
                engine.stopCapture()
            } catch (e: Exception) {
                Log.e(TAG, "Error transcribing file", e)
                TranscriptionResult.Error(e.message ?: "Transcription failed")
            }
        }
    }
    
    private suspend fun readWavFileAsPCM16(file: File, targetSampleRate: Int): ShortArray? {
        return withContext(Dispatchers.IO) {
            try {
                if (!file.exists()) {
                    Log.e(TAG, "File does not exist: ${file.absolutePath}")
                    return@withContext null
                }
                
                val fis = FileInputStream(file)
                val header = ByteArray(44) // WAV header
                fis.read(header)
                
                // Check WAV header
                val riff = String(header, 0, 4)
                val format = String(header, 8, 4)
                
                if (riff != "RIFF" || format != "WAVE") {
                    Log.e(TAG, "Invalid WAV file format")
                    fis.close()
                    return@withContext null
                }
                
                // Extract sample rate and channels
                val sampleRate = java.nio.ByteBuffer.wrap(header, 24, 4)
                    .order(java.nio.ByteOrder.LITTLE_ENDIAN).int
                val channels = java.nio.ByteBuffer.wrap(header, 22, 2)
                    .order(java.nio.ByteOrder.LITTLE_ENDIAN).short.toInt()
                val bitsPerSample = java.nio.ByteBuffer.wrap(header, 34, 2)
                    .order(java.nio.ByteOrder.LITTLE_ENDIAN).short.toInt()
                
                // Find data chunk
                var dataOffset = 36
                var dataSize = 0
                var found = false
                
                while (dataOffset + 8 < header.size) {
                    val chunkId = if (dataOffset + 4 < header.size) {
                        String(header, dataOffset, 4)
                    } else {
                        break
                    }
                    
                    if (chunkId == "data") {
                        dataSize = java.nio.ByteBuffer.wrap(header, dataOffset + 4, 4)
                            .order(java.nio.ByteOrder.LITTLE_ENDIAN).int
                        found = true
                        break
                    }
                    
                    val chunkSize = if (dataOffset + 8 < header.size) {
                        java.nio.ByteBuffer.wrap(header, dataOffset + 4, 4)
                            .order(java.nio.ByteOrder.LITTLE_ENDIAN).int
                    } else {
                        break
                    }
                    
                    dataOffset += 8 + chunkSize
                }
                
                // Read audio data
                val audioBytes = fis.readBytes()
                fis.close()
                
                // Convert to PCM16
                val pcm16 = if (bitsPerSample == 16 && audioBytes.isNotEmpty()) {
                    val buffer = java.nio.ByteBuffer.wrap(audioBytes)
                        .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    val samples = ShortArray(audioBytes.size / 2)
                    for (i in samples.indices) {
                        if (buffer.remaining() >= 2) {
                            samples[i] = buffer.short
                        }
                    }
                    
                    // Convert stereo to mono if needed
                    if (channels == 2) {
                        val mono = ShortArray(samples.size / 2)
                        for (i in mono.indices) {
                            val index = i * 2
                            if (index + 1 < samples.size) {
                                mono[i] = ((samples[index].toInt() + samples[index + 1].toInt()) / 2).toShort()
                            }
                        }
                        mono
                    } else {
                        samples
                    }
                } else {
                    Log.e(TAG, "Unsupported audio format: bitsPerSample=$bitsPerSample, channels=$channels")
                    return@withContext null
                }
                
                // Resample if needed (simple linear interpolation)
                if (sampleRate != targetSampleRate) {
                    val resampled = ShortArray((pcm16.size * targetSampleRate / sampleRate).toInt())
                    for (i in resampled.indices) {
                        val srcIndex = (i * sampleRate / targetSampleRate.toDouble()).toInt()
                        if (srcIndex < pcm16.size) {
                            resampled[i] = pcm16[srcIndex]
                        }
                    }
                    resampled
                } else {
                    pcm16
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reading WAV file", e)
                null
            }
        }
    }
    
    companion object {
        private const val TAG = "FileTranscriptionService"
    }
}

