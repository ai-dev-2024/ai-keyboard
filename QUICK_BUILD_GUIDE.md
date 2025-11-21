# Quick Build Guide — AI Keyboard

## 🚀 Quick Start

### 1. Generate Gradle Wrapper (One-time setup)

**If you have Gradle installed:**
```bash
gradle wrapper --gradle-version 8.2
```

**If using Android Studio:**
- Open project in Android Studio
- Wrapper will be generated automatically
- Use Terminal tab or Gradle panel

### 2. Build Debug APK

```bash
# Windows
.\gradlew.bat clean
.\gradlew.bat assembleDebug

# Linux/Mac
./gradlew clean
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/app-debug.apk`

### 3. Install on Device

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell ime enable com.aikeyboard/.AIKeyboardService
adb shell ime set com.aikeyboard/.AIKeyboardService
```

### 4. Build Release APK (Unsigned)

```bash
.\gradlew.bat assembleRelease    # Windows
./gradlew assembleRelease        # Linux/Mac
```

**Output:** `app/build/outputs/apk/release/app-release-unsigned.apk`

### 5. Build AAB Bundle (Unsigned)

```bash
.\gradlew.bat bundleRelease    # Windows
./gradlew bundleRelease        # Linux/Mac
```

**Output:** `app/build/outputs/bundle/release/app-release.aab`

---

## 🔐 Signing Setup (One-time)

### Generate Keystore

```bash
keytool -genkey -v -keystore aikeyboard-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias aikeyboard
```

### Create keystore.properties

Copy `keystore.properties.example` to `keystore.properties`:

```properties
storePassword=YOUR_PASSWORD
keyPassword=YOUR_PASSWORD
keyAlias=aikeyboard
storeFile=C:/path/to/aikeyboard-release.jks
```

### Build Signed Release

```bash
.\gradlew.bat clean
.\gradlew.bat assembleRelease
.\gradlew.bat bundleRelease
```

---

## 📱 Testing on Emulator

1. Start emulator from Android Studio
2. Install APK: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
3. Enable IME: `adb shell ime set com.aikeyboard/.AIKeyboardService`
4. Test keyboard in any app

---

## ⚠️ Troubleshooting

**"gradlew not found"**
- Generate wrapper: `gradle wrapper --gradle-version 8.2`
- Or use Android Studio

**"Device not found"**
- Enable USB debugging
- Check USB drivers
- Try different USB port/cable

**"Installation failed"**
- Enable "Install via USB" in Developer Options
- Uninstall existing version first: `adb uninstall com.aikeyboard`

**"IME not appearing"**
- Restart device
- Clear app data: `adb shell pm clear com.aikeyboard`
- Re-enable: `adb shell ime enable com.aikeyboard/.AIKeyboardService`

---

For detailed instructions, see `POST_REDESIGN_FINALIZATION.md`

