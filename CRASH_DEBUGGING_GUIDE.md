# Crash Debugging Guide

## Common Crash Causes and Fixes

### 1. Native Library Loading Issues

**Symptoms:**
- App crashes immediately on startup
- Logcat shows `UnsatisfiedLinkError` or `NoClassDefFoundError`
- Errors related to ONNX or Vosk

**Fix Applied:**
- Added lazy loading of native libraries in `AIKeyboardApplication`
- Libraries are now loaded in background thread to prevent startup crashes
- Added try-catch blocks around native library initialization

**How to Debug:**
```bash
# Check for native library errors
adb logcat | grep -i "onnx\|vosk\|UnsatisfiedLinkError"

# Check if libraries are present
adb shell ls -la /data/app/com.aikeyboard*/lib/
```

### 2. Database Initialization Failures

**Symptoms:**
- App crashes when accessing database
- `Room` initialization errors
- Database corruption errors

**Fix Applied:**
- Added `fallbackToDestructiveMigration()` to prevent crashes on schema changes
- Added error handling with fallback database
- Database errors are now logged but don't crash the app

**How to Debug:**
```bash
# Check database errors
adb logcat | grep -i "DatabaseModule\|Room\|SQLite"

# Clear app data if needed
adb shell pm clear com.aikeyboard
```

### 3. Hilt Dependency Injection Issues

**Symptoms:**
- `HiltAndroidApp` initialization errors
- Missing `@AndroidEntryPoint` annotations
- Dependency injection failures

**Fix Applied:**
- All Activities using Hilt now have `@AndroidEntryPoint`
- Added error handling in Hilt modules
- BillingManager initialization is now lazy

**How to Debug:**
```bash
# Check Hilt errors
adb logcat | grep -i "Hilt\|Dagger\|AndroidEntryPoint"

# Verify annotations
grep -r "@AndroidEntryPoint" app/src/main/
```

### 4. Billing/Google Play Services Issues

**Symptoms:**
- Crashes on devices without Google Play Services
- Billing initialization errors

**Fix Applied:**
- BillingManager initialization is now lazy
- Added error handling for missing Google Play Services
- App continues to work even if billing fails

**How to Debug:**
```bash
# Check billing errors
adb logcat | grep -i "BillingManager\|BillingClient\|GooglePlay"

# Test on device without Google Play
adb install app-debug.apk
```

### 5. Memory Issues

**Symptoms:**
- OutOfMemoryError
- App crashes when loading models
- Slow performance

**How to Debug:**
```bash
# Monitor memory usage
adb shell dumpsys meminfo com.aikeyboard

# Check heap size
adb logcat | grep -i "OutOfMemory\|heap"
```

## Debugging Steps

### Step 1: Capture Crash Logs

```bash
# Capture all errors
adb logcat *:E > crash_log.txt

# Capture AndroidRuntime errors specifically
adb logcat AndroidRuntime:E > crash_log.txt

# Capture with timestamps
adb logcat -v time *:E > crash_log.txt
```

### Step 2: Filter for Your App

```bash
# Filter by package name
adb logcat | grep com.aikeyboard

# Filter by tag
adb logcat | grep AIKeyboardApplication
adb logcat | grep AIKeyboardService
adb logcat | grep OnboardingActivity
```

### Step 3: Check Stack Traces

Look for these patterns in logs:
- `FATAL EXCEPTION` - Critical crash
- `AndroidRuntime` - Runtime exceptions
- `Process: com.aikeyboard` - Your app's process

### Step 4: Reproduce and Capture

1. **Clear app data:**
   ```bash
   adb shell pm clear com.aikeyboard
   ```

2. **Install fresh APK:**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Start logging:**
   ```bash
   adb logcat -c  # Clear logs
   adb logcat > crash_log.txt
   ```

4. **Launch app and reproduce crash**

5. **Stop logging** (Ctrl+C) and analyze `crash_log.txt`

## Common Error Patterns

### Pattern 1: ClassNotFoundException
```
java.lang.ClassNotFoundException: ai.onnxruntime.OrtEnvironment
```
**Solution:** Native library not loaded. Check ProGuard rules and library dependencies.

### Pattern 2: NullPointerException
```
java.lang.NullPointerException: Attempt to invoke virtual method on a null object reference
```
**Solution:** Check for null checks in code. Look at stack trace to find exact line.

### Pattern 3: IllegalStateException
```
java.lang.IllegalStateException: Room cannot access the database
```
**Solution:** Database initialization issue. Check DatabaseModule.

### Pattern 4: UnsatisfiedLinkError
```
java.lang.UnsatisfiedLinkError: dlopen failed: library "onnxruntime" not found
```
**Solution:** Native library missing. Check if library is included in APK.

## Testing Fixes

### Test 1: Fresh Install
```bash
# Uninstall and reinstall
adb uninstall com.aikeyboard
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.aikeyboard/.ui.onboarding.OnboardingActivity
```

### Test 2: Cold Start
```bash
# Force stop and restart
adb shell am force-stop com.aikeyboard
adb shell am start -n com.aikeyboard/.ui.onboarding.OnboardingActivity
```

### Test 3: Background/Foreground
```bash
# Send to background
adb shell input keyevent KEYCODE_HOME

# Bring back to foreground
adb shell am start -n com.aikeyboard/.ui.onboarding.OnboardingActivity
```

## Prevention Checklist

- ✅ Native libraries loaded lazily
- ✅ Database has fallback migration
- ✅ Billing initialization is lazy
- ✅ All Activities have error handling
- ✅ Hilt modules have error handling
- ✅ Application class has try-catch blocks

## Quick Fix Commands

```bash
# View recent crashes
adb logcat -d | grep -A 20 "FATAL EXCEPTION"

# Check app process
adb shell ps | grep aikeyboard

# Check app permissions
adb shell dumpsys package com.aikeyboard | grep permission

# Clear all app data
adb shell pm clear com.aikeyboard

# Reinstall app
adb uninstall com.aikeyboard && adb install app-debug.apk
```

## Getting Help

If crashes persist:

1. **Capture full log:**
   ```bash
   adb logcat -v time > full_log.txt
   ```

2. **Reproduce crash and stop logging**

3. **Share:**
   - Full log file
   - Device model and Android version
   - Steps to reproduce
   - APK version

4. **Check known issues:**
   - Review `BUILD_STATUS.md`
   - Check GitHub issues
   - Review recent changes

---

**Remember:** Most crashes are now handled gracefully. If the app still crashes, check the logs to see which component is failing.

