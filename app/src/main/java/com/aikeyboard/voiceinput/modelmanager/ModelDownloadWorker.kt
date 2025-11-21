package com.aikeyboard.voiceinput.modelmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URL

@HiltWorker
class ModelDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val baseUrl = inputData.getString(KEY_BASE_URL) ?: return@withContext Result.failure()
            val modelId = inputData.getString(KEY_MODEL_ID)
            
            // Ensure base URL ends with /
            val normalizedBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
            
            // Create model directory
            val modelDir = File(applicationContext.filesDir, "models")
            modelDir.mkdirs()
            
            // Step 1: Download manifest.json
            val manifestUrl = "${normalizedBaseUrl}manifest.json"
            val tempModelDir = File(modelDir, "temp_${System.currentTimeMillis()}")
            tempModelDir.mkdirs()
            
            val manifestFile = File(tempModelDir, "manifest.json")
            Log.d(TAG, "Downloading manifest from: $manifestUrl")
            
            URL(manifestUrl).openStream().use { input ->
                manifestFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            // Step 2: Parse manifest to get model info
            val manifest = json.decodeFromString<ModelManifest>(manifestFile.readText())
            val actualModelId = modelId ?: manifest.id
            val finalModelDir = File(modelDir, actualModelId)
            
            // Step 3: Download the model file
            val modelFileUrl = if (manifest.file.startsWith("http://") || manifest.file.startsWith("https://")) {
                manifest.file
            } else {
                "${normalizedBaseUrl}${manifest.file}"
            }
            
            val modelFile = File(finalModelDir, manifest.file)
            finalModelDir.mkdirs()
            
            Log.d(TAG, "Downloading model from: $modelFileUrl")
            
            URL(modelFileUrl).openStream().use { input ->
                modelFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            // Step 4: Move manifest to final location
            val finalManifestFile = File(finalModelDir, "manifest.json")
            manifestFile.copyTo(finalManifestFile, overwrite = true)
            
            // Step 5: Clean up temp directory
            tempModelDir.deleteRecursively()
            
            // Step 6: Validate the model
            val modelManager = ModelManager(applicationContext)
            val isValid = modelManager.validateModel(finalModelDir)
            
            if (!isValid) {
                Log.w(TAG, "Model validation failed, but files are downloaded")
                // Don't fail - let user try to use it anyway
            }
            
            Log.d(TAG, "Model download completed successfully: $actualModelId")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading model", e)
            Result.failure()
        }
    }

    companion object {
        const val TAG = "ModelDownloadWorker"
        const val KEY_BASE_URL = "base_url"
        const val KEY_MODEL_ID = "model_id"
    }
}

