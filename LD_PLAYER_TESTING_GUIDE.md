# LD Player Testing Guide

## Changes Made to Fix Keyboard Detection in LD Player

### 1. Fixed IME Configuration (`method.xml`)
- Added proper subtype definitions for the keyboard
- Configured multiple locale support (en_US and en)
- This ensures the keyboard appears in the system's input method list

### 2. Enhanced Keyboard Status Detection
- Created `KeyboardUtils` utility class to check keyboard status
- Added real-time status monitoring in settings and onboarding
- Shows clear indicators when keyboard is enabled/disabled/default

### 3. Improved Onboarding Flow
- Added automatic keyboard status checking
- Dynamic step progression based on keyboard state
- Clear instructions for enabling the keyboard
- Visual feedback when steps are completed

### 4. Enhanced Settings Screen
- Added keyboard status card with color-coded indicators
- Quick access buttons to enable keyboard or set as default
- Real-time status updates (checks every second)

### 5. Better Input Connection Handling
- Improved input connection management in `AIKeyboardService`
- Added fallback mechanisms for input connection
- Enhanced logging for debugging

### 6. Model Installation Improvements
- Added file picker support for model installation
- URL download dialog (placeholder for future implementation)
- Better error handling and user feedback

## How to Test in LD Player

### Step 1: Install the APK
1. Build the debug APK: `./gradlew assembleDebug`
2. Install the APK on LD Player using ADB or drag-and-drop

### Step 2: Enable the Keyboard
1. Open the AI Keyboard app (it will show the onboarding screen)
2. Follow the onboarding steps:
   - Step 1: Welcome screen
   - Step 2: Enable Keyboard - Tap "Open Settings"
   - In the system settings, find "AI Keyboard" in the list
   - Toggle it ON
   - Tap "OK" when prompted
   - Return to the app - the step should show a checkmark
   - Step 3: Set as Default - Tap "Set as Default"
   - Select "AI Keyboard" from the list
   - Return to the app

### Alternative: Enable from Settings
1. Open the AI Keyboard app
2. Go to Settings (if you skipped onboarding)
3. In the "General Settings" section, you'll see:
   - **Red card**: Keyboard not enabled - Tap "Enable AI Keyboard"
   - **Yellow card**: Keyboard enabled but not default - Tap "Set as Default Keyboard"
   - **Green card**: Keyboard is default ✓

### Step 3: Test the Keyboard
1. Open any app with a text field (Notes, Messages, etc.)
2. Long-press the input field or tap the keyboard icon in the status bar
3. Select "AI Keyboard" from the keyboard picker
4. The keyboard should appear with the AI Keyboard interface

### Step 4: Test Voice Input (Optional)
1. Open AI Keyboard Settings
2. Go to "Voice Input" section
3. Toggle "Enable Voice Input" ON
4. Tap "Install Model"
5. Choose "From File" to install a model from your device
   - Select a directory containing:
     - `manifest.json` (model metadata)
     - Model file (`.onnx` or Vosk model files)
6. Once installed, set it as "Active"
7. In the keyboard, tap the microphone button (🎤) to test voice input

## Troubleshooting

### Keyboard Not Appearing in Settings
1. **Restart LD Player** - Sometimes emulators need a restart
2. **Force stop and reopen the app** - This re-registers the IME service
3. **Check if the app is installed correctly** - Uninstall and reinstall
4. **Check logs** - Use `adb logcat | grep AIKeyboardService` to see service logs

### Keyboard Not Working After Enabling
1. **Set as default** - Make sure AI Keyboard is set as the default keyboard
2. **Check input connection** - Open any text field and see if keyboard appears
3. **Restart the app** - Sometimes the service needs to restart

### Model Installation Issues
1. **Check file permissions** - Make sure the app has storage permissions
2. **Verify manifest.json** - Ensure it's valid JSON with required fields
3. **Check model file** - Ensure the model file exists and matches the manifest
4. **Storage space** - Ensure there's enough space for the model

## Key Files Modified

- `app/src/main/res/xml/method.xml` - IME configuration
- `app/src/main/java/com/aikeyboard/shared/util/KeyboardUtils.kt` - Keyboard utilities
- `app/src/main/java/com/aikeyboard/ui/onboarding/OnboardingScreen.kt` - Enhanced onboarding
- `app/src/main/java/com/aikeyboard/ui/settings/sections/MainSettingsSection.kt` - Status display
- `app/src/main/java/com/aikeyboard/ime/AIKeyboardService.kt` - Improved input handling
- `app/src/main/AndroidManifest.xml` - Fixed activity configuration

## Testing Checklist

- [ ] APK installs successfully on LD Player
- [ ] App launches and shows onboarding
- [ ] Keyboard appears in system input method settings
- [ ] Keyboard can be enabled from system settings
- [ ] Keyboard can be set as default
- [ ] Keyboard appears when typing in text fields
- [ ] Keyboard input works (typing, backspace, enter)
- [ ] Settings screen shows correct keyboard status
- [ ] Model installation dialog appears
- [ ] Voice input can be enabled (if model installed)

## Notes for LD Player

LD Player is an Android emulator, and sometimes IME services need special handling:
- The keyboard service may take a moment to register after installation
- Restarting the emulator often helps with IME registration
- Make sure LD Player has sufficient resources allocated
- Some features may behave differently than on physical devices




