package com.aikeyboard.ui.settings.sections

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aikeyboard.ui.asrtesting.ASRTestingActivity

@Composable
fun BenchmarkSection() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Model Benchmark",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        Text(
            text = "Perform comprehensive testing of ASR engines including latency, memory usage, accuracy, and transcription quality.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Features",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                FeatureItem("Model selection and loading/unloading")
                FeatureItem("Microphone-based real-time testing")
                FeatureItem("File-based transcription testing")
                FeatureItem("Performance metrics (latency, memory, WER)")
                FeatureItem("Diagnostic report generation")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = {
                val intent = Intent(context, ASRTestingActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Assessment, "Open Testing")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open ASR Testing Screen")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
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
                    text = "Test Audio Clips",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "The following test audio clips are available for testing:",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                TestClipItem("Clean Male Voice (English)")
                TestClipItem("Clean Female Voice (English)")
                TestClipItem("Accent Test (South Asian English)")
                TestClipItem("Noisy Room Speech")
                TestClipItem("Fast Dictation")
                TestClipItem("Bengali Sample (Multilingual)")
                
                Text(
                    text = "Note: Test audio files should be placed in the app's test_audio_clips directory.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
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
                    text = "Supported Models",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "ONNX Models:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                ModelItem("Parakeet 0.6B ONNX (fp16)")
                ModelItem("Parakeet 0.6B ONNX (int8)")
                ModelItem("Distil-Whisper ONNX")
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Vosk Models:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                ModelItem("Small English Model")
                ModelItem("Multilingual Model")
            }
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TestClipItem(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ModelItem(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Storage,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

