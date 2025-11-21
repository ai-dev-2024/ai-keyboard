# Development Tools

Helper scripts for Android development workflow.

## Prerequisites

- Android SDK installed (should be at `%LOCALAPPDATA%\Android\Sdk`)
- ADB available in platform-tools
- LD Player installed (for emulator testing) OR physical device connected

## Scripts

### `connect-ldplayer.ps1`
Connects ADB to LD Player emulator.

```powershell
.\dev-tools\connect-ldplayer.ps1
```

**Note**: Make sure LD Player has ADB debugging enabled:
- LD Player Settings > Advanced > Enable ADB Debugging

### `capture-logs.ps1`
Captures Android logs for debugging.

```powershell
# Capture all errors and AI Keyboard logs
.\dev-tools\capture-logs.ps1

# Custom filter
.\dev-tools\capture-logs.ps1 "*:E AIKeyboard*"
```

Logs are saved to `logs/keyboard_log_YYYYMMDD_HHMMSS.txt`

### `install-apk.ps1`
Installs the debug APK to connected device.

```powershell
# Install default debug APK
.\dev-tools\install-apk.ps1

# Install custom APK
.\dev-tools\install-apk.ps1 "path\to\app.apk"
```

### `quick-build-and-install.ps1`
One-command build and install workflow.

```powershell
.\dev-tools\quick-build-and-install.ps1
```

This will:
1. Build the debug APK
2. Install it to connected device
3. Ready to test!

## Workflow Examples

### Testing on LD Player

```powershell
# 1. Connect to LD Player
.\dev-tools\connect-ldplayer.ps1

# 2. Build and install
.\dev-tools\quick-build-and-install.ps1

# 3. Capture logs in another terminal
.\dev-tools\capture-logs.ps1
```

### Testing on Physical Device (S23 Ultra)

```powershell
# 1. Enable USB debugging on phone
# 2. Connect via USB
# 3. Verify connection
adb devices

# 4. Build and install
.\dev-tools\quick-build-and-install.ps1

# 5. Capture crash logs
.\dev-tools\capture-logs.ps1 "*:E AndroidRuntime:E"
```

## Useful ADB Commands

```powershell
# List connected devices
adb devices

# View real-time logs
adb logcat

# Clear log buffer
adb logcat -c

# Install APK
adb install -r app-debug.apk

# Uninstall app
adb uninstall com.aikeyboard

# Take screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png

# View app info
adb shell dumpsys package com.aikeyboard
```

## Troubleshooting

### ADB not found
- Ensure Android SDK is installed
- Check `local.properties` has correct `sdk.dir` path

### LD Player not connecting
- Make sure LD Player is running
- Enable ADB in LD Player settings
- Try restarting ADB: `adb kill-server && adb start-server`

### Device not recognized
- Enable USB debugging on device
- Install device drivers if needed
- Try different USB cable/port

