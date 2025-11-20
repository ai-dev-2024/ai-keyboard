package com.aikeyboard.voiceinput.modelmanager

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelManager @Inject constructor(
    private val context: Context
) {
    private val modelsDir = File(context.filesDir, "models")
    private val json = Json { ignoreUnknownKeys = true }

    init {
        modelsDir.mkdirs()
    }

    fun getModelsDir(): File = modelsDir

    fun listInstalledModels(): Flow<List<InstalledModel>> = flow {
        val models = mutableListOf<InstalledModel>()
        
        modelsDir.listFiles()?.forEach { modelDir ->
            if (modelDir.isDirectory) {
                val manifestFile = File(modelDir, "manifest.json")
                if (manifestFile.exists()) {
                    try {
                        val manifest = json.decodeFromString<ModelManifest>(
                            manifestFile.readText()
                        )
                        val modelFile = File(modelDir, manifest.file)
                        models.add(
                            InstalledModel(
                                id = manifest.id,
                                displayName = manifest.display_name,
                                engine = manifest.engine,
                                modelDir = modelDir,
                                manifest = manifest,
                                isInstalled = modelFile.exists()
                            )
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error reading manifest for ${modelDir.name}", e)
                    }
                }
            }
        }
        
        emit(models)
    }

    suspend fun installModelFromFile(sourceFile: File, modelId: String): Result<Unit> {
        return try {
            val modelDir = File(modelsDir, modelId)
            modelDir.mkdirs()
            
            val destFile = File(modelDir, sourceFile.name)
            sourceFile.copyTo(destFile, overwrite = true)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error installing model", e)
            Result.failure(e)
        }
    }

    suspend fun validateModel(modelDir: File): Boolean {
        val manifestFile = File(modelDir, "manifest.json")
        if (!manifestFile.exists()) return false

        try {
            val manifest = json.decodeFromString<ModelManifest>(
                manifestFile.readText()
            )
            val modelFile = File(modelDir, manifest.file)
            if (!modelFile.exists()) return false

            // Validate checksum if provided
            manifest.checksum_sha256?.let { expectedChecksum ->
                val actualChecksum = calculateSHA256(modelFile)
                if (actualChecksum != expectedChecksum.removePrefix("sha256:")) {
                    Log.w(TAG, "Checksum mismatch for ${manifest.id}")
                    return false
                }
            }

            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error validating model", e)
            return false
        }
    }

    suspend fun deleteModel(modelId: String): Result<Unit> {
        return try {
            val modelDir = File(modelsDir, modelId)
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting model", e)
            Result.failure(e)
        }
    }

    private fun calculateSHA256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val TAG = "ModelManager"
    }
}

data class InstalledModel(
    val id: String,
    val displayName: String,
    val engine: String,
    val modelDir: File,
    val manifest: ModelManifest,
    val isInstalled: Boolean
)

