package com.aikeyboard.voiceinput.modelmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

@HiltWorker
class ModelDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val modelUrl = inputData.getString(KEY_MODEL_URL) ?: return Result.failure()
            val modelId = inputData.getString(KEY_MODEL_ID) ?: return Result.failure()
            val modelDir = File(applicationContext.filesDir, "models/$modelId")
            modelDir.mkdirs()

            val modelFile = File(modelDir, "model.onnx")
            URL(modelUrl).openStream().use { input ->
                modelFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val KEY_MODEL_URL = "model_url"
        const val KEY_MODEL_ID = "model_id"
    }
}

