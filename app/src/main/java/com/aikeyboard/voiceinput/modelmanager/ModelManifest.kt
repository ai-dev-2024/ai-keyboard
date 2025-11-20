package com.aikeyboard.voiceinput.modelmanager

import kotlinx.serialization.Serializable

@Serializable
data class ModelManifest(
    val id: String,
    val display_name: String,
    val engine: String, // "onnx" or "vosk"
    val file: String,
    val languages: List<String> = emptyList(),
    val size_bytes: Long = 0,
    val checksum_sha256: String? = null,
    val quantization: String? = null, // "fp16", "int8", etc.
    val recommended_min_ram_mb: Int = 0,
    val description: String? = null,
    // License information (optional - will be inferred from model ID if not provided)
    val license_type: String? = null, // "MIT", "Apache-2.0", "CC-BY", "Custom", "Unknown"
    val license_url: String? = null,
    val copyright_holder: String? = null
)

