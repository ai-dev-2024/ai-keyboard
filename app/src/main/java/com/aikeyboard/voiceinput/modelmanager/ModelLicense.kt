package com.aikeyboard.voiceinput.modelmanager

import kotlinx.serialization.Serializable

/**
 * Represents license information for an ASR model.
 */
@Serializable
data class ModelLicense(
    val licenseType: String, // "MIT", "Apache-2.0", "CC-BY", "Custom", "Unknown"
    val redistributionAllowed: Boolean,
    val commercialUseAllowed: Boolean,
    val attributionRequired: Boolean,
    val licenseUrl: String? = null,
    val copyrightHolder: String? = null,
    val licenseText: String? = null
)

/**
 * Registry of known model licenses.
 * This helps identify licenses for common models.
 */
object ModelLicenseRegistry {
    
    private val knownLicenses = mapOf(
        // NVIDIA Parakeet 0.6B
        "parakeet-0.6b" to ModelLicense(
            licenseType = "Apache-2.0",
            redistributionAllowed = true,
            commercialUseAllowed = true,
            attributionRequired = true,
            licenseUrl = "https://github.com/NVIDIA/NeMo/blob/main/LICENSE",
            copyrightHolder = "NVIDIA Corporation",
            licenseText = "Apache License 2.0"
        ),
        
        // Distil-Whisper
        "distil-whisper" to ModelLicense(
            licenseType = "Apache-2.0",
            redistributionAllowed = true,
            commercialUseAllowed = true,
            attributionRequired = true,
            licenseUrl = "https://huggingface.co/distil-whisper",
            copyrightHolder = "Hugging Face / OpenAI",
            licenseText = "Apache License 2.0"
        ),
        
        // Whisper (OpenAI)
        "whisper" to ModelLicense(
            licenseType = "MIT",
            redistributionAllowed = true,
            commercialUseAllowed = true,
            attributionRequired = true,
            licenseUrl = "https://github.com/openai/whisper/blob/main/LICENSE",
            copyrightHolder = "OpenAI",
            licenseText = "MIT License"
        ),
        
        // Vosk models (generic)
        "vosk" to ModelLicense(
            licenseType = "Apache-2.0",
            redistributionAllowed = true,
            commercialUseAllowed = true,
            attributionRequired = true,
            licenseUrl = "https://github.com/alphacep/vosk-api/blob/master/LICENSE",
            copyrightHolder = "Alpha Cephei Inc.",
            licenseText = "Apache License 2.0"
        )
    )
    
    /**
     * Get license information for a model by ID.
     * Returns null if model is not in registry (e.g., user-imported).
     */
    fun getLicense(modelId: String): ModelLicense? {
        // Try exact match first
        knownLicenses[modelId]?.let { return it }
        
        // Try prefix matching for variants
        knownLicenses.keys.firstOrNull { modelId.startsWith(it) }?.let {
            return knownLicenses[it]
        }
        
        // Check if it's a vosk model
        if (modelId.contains("vosk", ignoreCase = true)) {
            return knownLicenses["vosk"]
        }
        
        // Check if it's a whisper variant
        if (modelId.contains("whisper", ignoreCase = true)) {
            return knownLicenses["whisper"]
        }
        
        return null
    }
    
    /**
     * Get license for user-imported models (unknown license).
     */
    fun getUnknownLicense(): ModelLicense {
        return ModelLicense(
            licenseType = "Unknown",
            redistributionAllowed = false, // Unknown - assume no
            commercialUseAllowed = false, // Unknown - assume no
            attributionRequired = true, // Always require attribution when unknown
            licenseUrl = null,
            copyrightHolder = null,
            licenseText = "License information not available. User is responsible for verifying license compliance."
        )
    }
    
    /**
     * Check if a model ID matches a known model pattern.
     */
    fun isKnownModel(modelId: String): Boolean {
        return getLicense(modelId) != null
    }
}

