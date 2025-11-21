# Development Setup Guide

## ✅ Environment Status

Your development environment is ready:
- **Java**: 21.0.8 ✓
- **Android SDK**: Installed at `C:\Users\Muhib\AppData\Local\Android\Sdk` ✓
- **ADB**: Available ✓
- **Gradle**: Configured ✓

## 🛠️ Development Tools Created

I've created helper scripts in the `dev-tools/` directory:

### Quick Start

```powershell
# 1. Connect to LD Player (if using emulator)
.\dev-tools\connect-ldplayer.ps1

# 2. Build and install in one command
.\dev-tools\quick-build-and-install.ps1

# 3. Capture logs (in separate terminal)
.\dev-tools\capture-logs.ps1
```

## 📱 Testing Options

### Option 1: LD Player (Current Setup)
1. Enable ADB in LD Player: Settings > Advanced > Enable ADB Debugging
2. Run: `.\dev-tools\connect-ldplayer.ps1`
3. Install APK: `.\dev-tools\install-apk.ps1`

### Option 2: Physical Device (S23 Ultra)
1. Enable USB Debugging on phone
2. Connect via USB
3. Verify: `adb devices`
4. Install: `.\dev-tools\install-apk.ps1`

### Option 3: Android Studio Emulator (Recommended)
1. Install Android Studio
2. Tools > Device Manager > Create Virtual Device
3. Select Pixel 6 or similar, API 34
4. Run app directly from Android Studio

## 🔧 Fixes Applied

### 1. IME Service Improvements
- ✅ Added `onWindowShown()` for proper keyboard display
- ✅ Added `onWindowHidden()` for cleanup
- ✅ Enhanced error handling in `onCreate()` and `onCreateInputView()`
- ✅ Better InputConnection management

### 2. OnboardingActivity
- ✅ Added comprehensive error handling
- ✅ Graceful fallback on initialization errors

### 3. BillingManager
- ✅ Removed immediate initialization (prevents crashes on devices without Google Play)
- ✅ Added error handling for Google Play Services unavailability

### 4. Enhanced Logging
- ✅ Better error messages throughout
- ✅ Log capture scripts for debugging

## 🐛 Debugging Workflow

### Capture Crash Logs

```powershell
# For S23 Ultra crashes
adb logcat *:E AndroidRuntime:E > crash_log.txt

# For keyboard issues
.\dev-tools\capture-logs.ps1 "*:E AIKeyboardService*"
```

### Common Issues & Solutions

#### Keyboard Not Showing
- Check logs: `adb logcat | grep AIKeyboardService`
- Verify keyboard is enabled: Settings > System > Languages & Input
- Check if keyboard is set as default

#### App Crashes on Startup
- Check logs for Hilt initialization errors
- Verify all native libraries are loading
- Check for missing permissions

#### LD Player Connection Issues
- Ensure LD Player is running
- Enable ADB in LD Player settings
- Try: `adb kill-server && adb start-server`

## 📦 Build Commands

```powershell
# Debug build
.\gradlew.bat assembleDebug

# Release build (requires keystore)
.\gradlew.bat assembleRelease

# Clean build
.\gradlew.bat clean assembleDebug
```

## 🚀 Quick Development Cycle

1. **Make code changes**
2. **Build**: `.\gradlew.bat assembleDebug`
3. **Install**: `.\dev-tools\install-apk.ps1`
4. **Test** on device/emulator
5. **Capture logs** if issues: `.\dev-tools\capture-logs.ps1`

## 📝 Next Steps

1. **Test the new APK** on both LD Player and S23 Ultra
2. **Capture logs** if crashes occur
3. **Verify keyboard auto-loads** when typing
4. **Check model download** functionality

## 🔍 Key Files Modified

- `app/src/main/java/com/aikeyboard/ime/AIKeyboardService.kt` - IME service fixes
- `app/src/main/java/com/aikeyboard/ui/onboarding/OnboardingActivity.kt` - Error handling
- `app/src/main/java/com/aikeyboard/shared/di/BillingModule.kt` - Lazy initialization
- `app/src/main/java/com/aikeyboard/billing/BillingManager.kt` - Error handling

## 📚 Additional Resources

- See `dev-tools/README.md` for detailed script documentation
- Check `LD_PLAYER_TESTING_GUIDE.md` for LD Player specific tips
- Review `PERFORMANCE_TESTING_GUIDE.md` for performance testing

---

**Your APK is ready**: `app\build\outputs\apk\debug\app-debug.apk`

Happy coding! 🎉

