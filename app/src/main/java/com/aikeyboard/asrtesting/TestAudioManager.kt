package com.aikeyboard.asrtesting

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Serializable
data class TestAudioClip(
    val name: String,
    val filename: String,
    val description: String,
    val expectedText: String? = null,
    val language: String = "en",
    val sampleRate: Int = 16000,
    val channels: Int = 1
)

class TestAudioManager(private val context: Context) {
    private val testClipsDir = File(context.filesDir, "test_audio_clips")
    private val json = Json { ignoreUnknownKeys = true }
    
    init {
        testClipsDir.mkdirs()
        initializeTestClips()
    }
    
    private fun initializeTestClips() {
        // Create manifest for test clips
        val manifest = listOf(
            TestAudioClip(
                name = "Clean Male Voice (English)",
                filename = "clean_male_en.wav",
                description = "Clear male voice speaking English",
                expectedText = "The quick brown fox jumps over the lazy dog",
                language = "en"
            ),
            TestAudioClip(
                name = "Clean Female Voice (English)",
                filename = "clean_female_en.wav",
                description = "Clear female voice speaking English",
                expectedText = "Hello world this is a test of the voice recognition system",
                language = "en"
            ),
            TestAudioClip(
                name = "Accent Test (South Asian English)",
                filename = "accent_south_asian_en.wav",
                description = "South Asian English accent",
                expectedText = "Please speak slowly and clearly for better recognition",
                language = "en"
            ),
            TestAudioClip(
                name = "Noisy Room Speech",
                filename = "noisy_room_en.wav",
                description = "Speech recorded in a noisy environment",
                expectedText = "Testing speech recognition with background noise",
                language = "en"
            ),
            TestAudioClip(
                name = "Fast Dictation",
                filename = "fast_dictation_en.wav",
                description = "Fast-paced dictation",
                expectedText = "This is a quick test of rapid speech recognition capabilities",
                language = "en"
            ),
            TestAudioClip(
                name = "Bengali Sample",
                filename = "bengali_sample.wav",
                description = "Bengali language sample",
                expectedText = null, // No expected text for multilingual
                language = "bn"
            )
        )
        
        val manifestFile = File(testClipsDir, "clips_manifest.json")
        manifestFile.writeText(json.encodeToString(ListSerializer(TestAudioClip.serializer()), manifest))
        
        // Note: Test audio files should be added manually or via app functionality
        Log.d(TAG, "Test audio clips manifest initialized. Add audio files to ${testClipsDir.absolutePath}")
    }
    
    fun getTestClips(): List<TestAudioClip> {
        val manifestFile = File(testClipsDir, "clips_manifest.json")
        return if (manifestFile.exists()) {
            try {
                json.decodeFromString(ListSerializer(TestAudioClip.serializer()), manifestFile.readText())
            } catch (e: Exception) {
                Log.e(TAG, "Error reading manifest", e)
                emptyList()
            }
        } else {
            emptyList()
        }
    }
    
    fun getClipFile(clip: TestAudioClip): File? {
        val file = File(testClipsDir, clip.filename)
        return if (file.exists()) file else null
    }
    
    fun addTestClip(clip: TestAudioClip, audioFile: File): Boolean {
        return try {
            val destFile = File(testClipsDir, clip.filename)
            audioFile.copyTo(destFile, overwrite = true)
            
            // Update manifest
            val clips = getTestClips().toMutableList()
            clips.removeAll { it.filename == clip.filename }
            clips.add(clip)
            val manifestFile = File(testClipsDir, "clips_manifest.json")
            manifestFile.writeText(json.encodeToString(ListSerializer(TestAudioClip.serializer()), clips))
            
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error adding test clip", e)
            false
        }
    }
    
    fun readWavFileAsPCM16(file: File): ShortArray? {
        return try {
            if (file.extension.lowercase() != "wav") {
                Log.e(TAG, "File is not a WAV file")
                return null
            }
            
            val fis = FileInputStream(file)
            val header = ByteArray(44) // WAV header is typically 44 bytes
            fis.read(header)
            
            // Check WAV header
            val riff = String(header, 0, 4)
            val format = String(header, 8, 4)
            
            if (riff != "RIFF" || format != "WAVE") {
                Log.e(TAG, "Invalid WAV file format")
                fis.close()
                return null
            }
            
            // Extract sample rate and channels from header
            val sampleRate = ByteBuffer.wrap(header, 24, 4)
                .order(ByteOrder.LITTLE_ENDIAN).int
            val channels = ByteBuffer.wrap(header, 22, 2)
                .order(ByteOrder.LITTLE_ENDIAN).short.toInt()
            val bitsPerSample = ByteBuffer.wrap(header, 34, 2)
                .order(ByteOrder.LITTLE_ENDIAN).short.toInt()
            
            // Find data chunk
            var dataChunkSize = 0
            var dataOffset = 36
            while (dataOffset < header.size) {
                val chunkId = String(header, dataOffset, 4)
                val chunkSize = ByteBuffer.wrap(header, dataOffset + 4, 4)
                    .order(ByteOrder.LITTLE_ENDIAN).int
                
                if (chunkId == "data") {
                    dataChunkSize = chunkSize
                    break
                }
                dataOffset += 8 + chunkSize
            }
            
            // Read audio data
            val audioBytes = fis.readBytes()
            fis.close()
            
            // Convert to PCM16
            val pcm16 = if (bitsPerSample == 16) {
                val buffer = ByteBuffer.wrap(audioBytes)
                    .order(ByteOrder.LITTLE_ENDIAN)
                val samples = ShortArray(audioBytes.size / 2)
                for (i in samples.indices) {
                    samples[i] = buffer.short
                }
                
                // Convert stereo to mono if needed
                if (channels == 2) {
                    val mono = ShortArray(samples.size / 2)
                    for (i in mono.indices) {
                        mono[i] = ((samples[i * 2].toInt() + samples[i * 2 + 1].toInt()) / 2).toShort()
                    }
                    mono
                } else {
                    samples
                }
            } else {
                Log.e(TAG, "Unsupported bits per sample: $bitsPerSample")
                return null
            }
            
            // Resample if needed (simple linear interpolation)
            if (sampleRate != 16000) {
                Log.w(TAG, "Resampling from $sampleRate to 16000 Hz")
                val resampled = ShortArray((pcm16.size * 16000 / sampleRate).toInt())
                for (i in resampled.indices) {
                    val srcIndex = (i * sampleRate / 16000.0).toInt()
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
    
    fun getClipDuration(file: File): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            retriever.release()
            duration
        } catch (e: Exception) {
            Log.e(TAG, "Error getting duration", e)
            0L
        }
    }
    
    companion object {
        private const val TAG = "TestAudioManager"
    }
}

