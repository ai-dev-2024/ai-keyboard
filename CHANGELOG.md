# Changelog

All notable changes to AI Keyboard will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-11-XX

### Added
- **Initial Release** - First public release of AI Keyboard
- **Offline AI Voice Typing** - Support for ONNX Runtime and Vosk engines
- **Model Manager** - Install, manage, and switch between ASR models
- **Full Keyboard Customization** - Themes, appearance, height adjustment
- **Clipboard Manager** - History, pinning, and management
- **Premium Features** - Pro unlock with in-app purchase
- **Onboarding Flow** - Guided setup for new users
- **Privacy Policy** - Complete privacy policy page
- **Splash Screen** - Branded launch screen

### Features

#### Core Keyboard
- Multi-language typing support
- Smart auto-correct and next-word predictions
- Personal dictionary (Room database)
- Emoji picker with categories
- Gesture typing (swipe)
- Cursor glide
- Long-press symbols
- Haptics and sound settings
- Full dark/light themes (Material 3)

#### Voice Input
- 100% offline speech recognition
- Support for ONNX Runtime models
- Support for Vosk models
- Model installation from file (SAF)
- Model download from URL
- Partial transcription display
- Real-time voice input

#### Customization
- Theme engine with presets:
  - Default (Blue)
  - Dark Pro
  - Midnight Neon
  - Mint Glow
  - Minimal White
- Custom theme creation
- Keyboard height adjustment
- Key appearance customization

#### Productivity
- Clipboard manager with history
- Clipboard pinning
- Personal dictionary
- Settings sync (local)

#### Premium Features (Pro Unlock)
- Premium themes
- Unlimited clipboard history
- Custom theme export/import
- Advanced customization options

### Privacy
- 100% offline voice processing
- No analytics or tracking
- No data collection
- No network calls (except user-initiated model downloads)
- Complete privacy policy
- Microphone permission clearly documented

### Technical
- Built with Jetpack Compose
- Material 3 design system
- Room database for local storage
- DataStore for preferences
- Hilt for dependency injection
- WorkManager for background tasks
- ProGuard/R8 optimization
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 14)

### Dependencies
- ONNX Runtime Mobile 1.16.3
- Vosk Android 0.3.45
- Google Play Billing 6.1.0
- Jetpack Compose BOM 2023.10.01
- Room 2.6.1
- Hilt 2.48

### Legal
- Licensed under Apache-2.0
- Copyright © 2024 AI Keyboard
- Model licensing audit completed
- Privacy policy included

---

## [Unreleased]

### Planned
- Additional language support
- More AI model options
- Cloud sync (optional)
- Additional premium features
- Performance optimizations
- Accessibility improvements

---

## Version History

- **1.0.0** - Initial release (2024-11-XX)

---

## Notes

- All voice recognition is processed 100% offline
- No audio data is sent to external servers
- Models are not bundled with the app (user-installed)
- Premium features are optional (app is fully functional without them)

