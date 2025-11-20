# AI Keyboard — Performance Testing Guide

## Overview

This document outlines the performance testing requirements and procedures for AI Keyboard before Play Store release.

## Required Performance Metrics

### 1. Keyboard Startup Time
**Target:** < 200ms  
**Measurement:** Time from IME service creation to first key render

**Test Procedure:**
1. Clear app data
2. Enable AI Keyboard as default
3. Open any text input field
4. Measure time using `adb shell am start -W` or Logcat timestamps
5. Repeat 10 times and average

**Logcat Filter:**
```bash
adb logcat | grep -E "AIKeyboardService|onCreate|onStartInput"
```

**Expected Result:** Average startup time < 200ms

---

### 2. Voice Model Load Time
**Target:** < 2 seconds (or show loading indicator)  
**Measurement:** Time from model selection to ready state

**Test Procedure:**
1. Install a test model (e.g., Vosk small)
2. Open Settings > Voice Input > Models
3. Select a model
4. Measure time until "Model loaded" message appears
5. Test with different model sizes:
   - Small model (< 50MB)
   - Medium model (50-200MB)
   - Large model (> 200MB)

**Logcat Filter:**
```bash
adb logcat | grep -E "VoiceEngine|ModelManager|loadModel"
```

**Expected Result:** 
- Small models: < 1 second
- Medium models: < 2 seconds
- Large models: < 3 seconds (with loading indicator)

---

### 3. Partial Transcription Latency
**Target:** < 500ms on mid-range devices  
**Measurement:** Time from audio capture to partial text display

**Test Procedure:**
1. Load a voice model
2. Start voice input
3. Speak a short phrase (2-3 words)
4. Measure time from speech end to first partial transcription
5. Test on:
   - Low-end device (2GB RAM, entry-level CPU)
   - Mid-range device (4-6GB RAM, mid-tier CPU)
   - High-end device (8GB+ RAM, flagship CPU)

**Logcat Filter:**
```bash
adb logcat | grep -E "TranscriptionResult|partialTranscription|VoiceInputService"
```

**Expected Result:**
- Low-end: < 1000ms
- Mid-range: < 500ms
- High-end: < 300ms

---

### 4. CPU Usage During Voice Input
**Target:** < 60% sustained  
**Measurement:** Average CPU usage during active voice recognition

**Test Procedure:**
1. Start voice input
2. Record continuous speech for 30 seconds
3. Monitor CPU usage using:
   ```bash
   adb shell top -d 1 | grep com.aikeyboard
   ```
4. Calculate average CPU percentage

**Expected Result:** 
- Sustained CPU < 60%
- Peak CPU < 80% (acceptable for short bursts)

---

### 5. Memory Usage
**Target:** Within safe bounds (device-dependent)  
**Measurement:** Peak memory usage during normal operation

**Test Procedure:**
1. Monitor memory using:
   ```bash
   adb shell dumpsys meminfo com.aikeyboard
   ```
2. Test scenarios:
   - Keyboard startup
   - Voice model loading
   - Active voice recognition
   - Multiple model switching
3. Check for memory leaks (run for 1 hour)

**Expected Result:**
- Base memory: < 100MB
- With model loaded: < 300MB (model-dependent)
- No memory leaks over extended use

---

## Device Testing Matrix

### Low-End Devices
- **Example:** Samsung Galaxy A10, Xiaomi Redmi 8A
- **Specs:** 2GB RAM, Entry-level CPU (Snapdragon 4xx/MediaTek Helio)
- **Focus:** Startup time, basic functionality

### Mid-Range Devices
- **Example:** Samsung Galaxy A52, Pixel 5a
- **Specs:** 4-6GB RAM, Mid-tier CPU (Snapdragon 7xx/6xx)
- **Focus:** All metrics, primary target

### High-End Devices
- **Example:** Samsung Galaxy S23, Pixel 7
- **Specs:** 8GB+ RAM, Flagship CPU (Snapdragon 8xx)
- **Focus:** Performance optimization, edge cases

---

## Automated Testing Scripts

### Performance Test Script
```bash
#!/bin/bash
# performance_test.sh

echo "=== AI Keyboard Performance Tests ==="

# Test 1: Keyboard Startup
echo "Testing keyboard startup..."
adb shell am force-stop com.aikeyboard
sleep 2
START_TIME=$(date +%s%N)
adb shell am start -W -n com.aikeyboard/.ui.settings.SettingsActivity
END_TIME=$(date +%s%N)
STARTUP_TIME=$((($END_TIME - $START_TIME) / 1000000))
echo "Startup time: ${STARTUP_TIME}ms"

# Test 2: Memory Usage
echo "Testing memory usage..."
adb shell dumpsys meminfo com.aikeyboard | grep -E "TOTAL|Pss"

# Test 3: CPU Usage (requires manual voice input)
echo "Start voice input and run: adb shell top -d 1 | grep com.aikeyboard"
```

---

## Logcat Monitoring

### Enable Performance Logging
Add to `AIKeyboardService.kt`:
```kotlin
private val startTime = System.currentTimeMillis()
Log.d("Performance", "Keyboard startup: ${System.currentTimeMillis() - startTime}ms")
```

### Key Log Tags
- `Performance` - Performance metrics
- `VoiceEngine` - Voice processing timing
- `ModelManager` - Model loading timing
- `AIKeyboardService` - Service lifecycle

### Logcat Filters
```bash
# Performance metrics only
adb logcat -s Performance:D VoiceEngine:D ModelManager:D

# All keyboard logs
adb logcat | grep com.aikeyboard

# Errors and warnings
adb logcat *:E *:W | grep com.aikeyboard
```

---

## Performance Optimization Checklist

- [ ] Keyboard startup < 200ms
- [ ] Model load time < 2 seconds (or loading indicator shown)
- [ ] Partial transcription < 500ms on mid-range devices
- [ ] CPU usage < 60% sustained
- [ ] Memory usage within bounds
- [ ] No memory leaks after 1 hour of use
- [ ] Smooth animations (60 FPS)
- [ ] No ANR (Application Not Responding) errors
- [ ] Battery usage acceptable (< 5% per hour of active use)

---

## Reporting Template

```
Performance Test Report - AI Keyboard v1.0.0
Date: [DATE]
Tester: [NAME]
Device: [MODEL] ([SPECS])

Results:
1. Keyboard Startup: [TIME]ms (Target: <200ms) ✅/❌
2. Model Load Time: [TIME]ms (Target: <2000ms) ✅/❌
3. Transcription Latency: [TIME]ms (Target: <500ms) ✅/❌
4. CPU Usage: [PERCENTAGE]% (Target: <60%) ✅/❌
5. Memory Usage: [SIZE]MB (Target: <300MB) ✅/❌

Issues Found:
- [LIST ANY ISSUES]

Recommendations:
- [LIST RECOMMENDATIONS]
```

---

## Notes

- All tests should be performed on release builds (ProGuard enabled)
- Test with different Android versions (8.0, 10, 12, 14)
- Test with different screen sizes and densities
- Verify performance on both light and dark themes
- Test with multiple languages/keyboard layouts

