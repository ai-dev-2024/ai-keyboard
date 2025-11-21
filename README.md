# AI Keyboard

<div align="center">

**The first Android keyboard with fully offline AI voice typing powered by user-selectable models**

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![Minimum SDK](https://img.shields.io/badge/min%20SDK-26-orange.svg)](https://developer.android.com/about/versions/oreo)

[Features](#-features) • [Installation](#-installation) • [Models](#-supported-models) • [Privacy](#-privacy) • [Contributing](#-contributing) • [Cloud Build](#-cloud-build--testing)

</div>

---

## 🌟 Overview

**AI Keyboard** is a modern, privacy-first Android keyboard that combines intelligent typing with AI-powered offline speech recognition. Unlike other keyboards that send your voice to the cloud, AI Keyboard processes everything **100% on-device** using user-selectable ONNX or Vosk models.

### Why AI Keyboard?

- 🔒 **100% Offline**: Your voice never leaves your device
- 🤖 **User-Selectable Models**: Choose the AI model that works best for your device
- 🎨 **Fully Customizable**: Themes, layouts, and appearance
- 🚀 **Modern & Fast**: Built with Jetpack Compose and Material 3
- 📦 **Open Source**: Licensed under Apache-2.0

---

## ✨ Key Features

### Core Keyboard Features

- ✅ Multi-language typing support
- ✅ Smart auto-correct and next-word predictions
- ✅ Personal dictionary (Room database)
- ✅ Clipboard manager with history & pinning
- ✅ Emoji picker with categories
- ✅ Theme engine with presets + custom themes
- ✅ Keyboard height adjustment
- ✅ Long-press symbols
- ✅ Gesture typing (swipe)
- ✅ Cursor glide
- ✅ Haptics / sound settings
- ✅ Full dark/light themes (Material 3)

### 🎤 Offline AI Voice Input

- **100% Offline Processing**: All speech recognition happens on-device
- **Pluggable ASR Engines**: Support for ONNX Runtime and Vosk
- **Model Manager**: Install, manage, and switch between ASR models
- **Real-time Transcription**: See your words appear as you speak
- **Privacy-First**: No audio data ever leaves your device

### Supported Models

- **NVIDIA Parakeet 0.6B (ONNX)** — Best accuracy, multi-language
- **Distil-Whisper ONNX** — Fast, English-only
- **Vosk Small Models** — Low-end devices
- **User-Installed Models** — Install any compatible ONNX or Vosk model

---

## 📦 Installation

### For End Users

AI Keyboard is available on the Google Play Store. [Download Now →](https://play.google.com/store/apps/details?id=com.aikeyboard) *(Link will be available after release)*

### Building from Source

#### Requirements

- **Android Studio**: Hedgehog (2023.1.1) or later
- **Android SDK**: 26+ (Android 8.0+)
- **Kotlin**: 1.9+
- **Gradle**: 8.2+
- **JDK**: 17+

#### Build Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/ai-dev-2024/ai-keyboard.git
   cd ai-keyboard
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Gradle Dependencies**
   - Android Studio will automatically sync Gradle
   - Wait for sync to complete (may take a few minutes)

4. **Build and Run**
   - Click "Run" (▶️) or press `Shift+F10`
   - Select a device or emulator
   - The app will build and install

#### Debug Build

```bash
./gradlew assembleDebug
```

#### Release Build

```bash
./gradlew assembleRelease
```

The APK will be located at: `app/build/outputs/apk/release/app-release.apk`

---

## 🤖 Model Manager

### Installing ASR Models

#### Method 1: Install from File (SAF)

1. Open AI Keyboard Settings
2. Navigate to **"AI Voice Input"** → **"Models"**
3. Tap **"Install Model"**
4. Select your model directory using the file picker
5. Ensure the directory contains:
   - `manifest.json` (model metadata)
   - Model file (`.onnx` or Vosk model files)

#### Method 2: Download from URL

1. Open AI Keyboard Settings
2. Navigate to **"AI Voice Input"** → **"Models"**
3. Tap **"Download Model"**
4. Enter the model URL
5. The model will download in the background

### Model Directory Structure

```
filesDir/models/<modelId>/
├── manifest.json
└── model.onnx (or vosk model files)
```

### Manifest Format

Create a `manifest.json` file in your model directory:

```json
{
  "id": "parakeet-0.6b",
  "display_name": "NVIDIA Parakeet 0.6B",
  "engine": "onnx",
  "file": "parakeet-0.6b.onnx",
  "languages": ["en", "bn"],
  "size_bytes": 620000000,
  "checksum_sha256": "sha256:...",
  "quantization": "fp16",
  "recommended_min_ram_mb": 2500,
  "description": "High accuracy offline ASR model"
}
```

See [docs/MODEL_GUIDE.md](docs/MODEL_GUIDE.md) for detailed model installation instructions.

### Recommended Models

| Model | Size | RAM | Languages | Best For |
|-------|------|-----|-----------|----------|
| **NVIDIA Parakeet 0.6B** | ~620 MB | 2.5 GB | Multi | Best accuracy |
| **Distil-Whisper ONNX** | ~150 MB | 1 GB | English | Fast & efficient |
| **Vosk Small** | ~40 MB | 512 MB | Multi | Low-end devices |

---

## 🚀 Usage

### Enabling Voice Input

1. Open **AI Keyboard Settings**
2. Navigate to **"AI Voice Input"**
3. Toggle **"Enable Voice Input"**
4. Select an installed model as **"Active Model"**
5. Grant microphone permission when prompted

### Using Voice Input

1. Open any text field
2. Tap the **microphone button** (🎤) on the keyboard
3. Speak your text
4. The transcription will appear in the suggestions bar
5. Tap to insert the transcription

---

## 📸 Screenshots

> Screenshots will be added after release. Placeholder images can be used for now.

<!-- Add screenshots here after release -->

---

## 🛣️ Roadmap

### Version 1.0.0 (Current)
- ✅ Offline AI voice typing with ONNX/Vosk engines
- ✅ Model Manager with file/URL installation
- ✅ Full keyboard customization (themes, appearance, height)
- ✅ Clipboard manager with history & pinning
- ✅ Premium features (Pro unlock)
- ✅ Onboarding flow

### Planned Features
- [ ] Additional language support
- [ ] More AI model options
- [ ] Cloud sync (optional, user-controlled)
- [ ] Additional premium features
- [ ] Performance optimizations
- [ ] Accessibility improvements
- [ ] Tablet and foldable optimizations

---

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guidelines](docs/CONTRIBUTING.md) before submitting PRs.

### Quick Start for Contributors

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

See [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) for detailed guidelines.

---

## 📚 Documentation

- **[User Guide](docs/USER_GUIDE.md)** - How to use AI Keyboard
- **[Model Guide](docs/MODEL_GUIDE.md)** - Installing and managing ASR models
- **[Privacy Policy](docs/PRIVACY.md)** - Privacy and data handling
- **[Security](docs/SECURITY.md)** - Security practices and reporting
- **[Theming Guide](docs/THEMING_GUIDE.md)** - Customizing themes and appearance
- **[Contributing](docs/CONTRIBUTING.md)** - Contributing guidelines

---

## 🔒 Privacy

**AI Keyboard is built with privacy as a core principle.**

- ✅ **100% Offline Processing**: All voice recognition happens on-device
- ✅ **No Data Collection**: We don't collect analytics or personal data
- ✅ **No Cloud Servers**: Your voice never leaves your device
- ✅ **User Control**: You choose which models to install
- ✅ **Local Storage Only**: All data (dictionary, clipboard, settings) stays on your device

See [docs/PRIVACY.md](docs/PRIVACY.md) for complete privacy information.

---

## 🔐 Security

AI Keyboard follows security best practices:

- Model verification using SHA-256 checksums
- Secure local storage (app-private directory)
- No network calls except user-initiated model downloads
- Minimal permissions (microphone only when using voice input)

See [docs/SECURITY.md](docs/SECURITY.md) for security details and vulnerability reporting.

---

## 📄 License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.

### Attribution Requirements

When using AI Keyboard:
- Include the Apache-2.0 license notice
- Preserve copyright notices
- State changes (if any)

### Third-Party Licenses

AI Keyboard uses the following open-source libraries:

- **ONNX Runtime Mobile** (MIT) - AI model inference
- **Vosk Android** (Apache-2.0) - Alternative ASR engine
- **Jetpack Compose** (Apache-2.0) - UI toolkit
- **Room** (Apache-2.0) - Local database
- **Hilt** (Apache-2.0) - Dependency injection

See individual library licenses for details.

---

## ☕ Support

Love AI Keyboard? Support development on Ko-fi:

[![Ko-fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/ai_dev_2024)

### Links

- **GitHub**: [https://github.com/ai-dev-2024](https://github.com/ai-dev-2024)
- **X/Twitter**: [@MjYoke1111](https://x.com/MjYoke1111)
- **Ko-fi**: [https://ko-fi.com/ai_dev_2024](https://ko-fi.com/ai_dev_2024)

---

## 🙏 Acknowledgments

- Built with inspiration from modern keyboard apps
- Uses Apache-2.0 compatible libraries only
- No GPL code included
- Special thanks to all contributors and the open-source community

---

## 🐛 Troubleshooting

### Voice Input Not Working

1. Check microphone permission is granted
2. Verify a model is installed and set as active
3. Ensure the model file exists and is valid
4. Check device has sufficient RAM for the model

### Model Installation Fails

1. Verify `manifest.json` is valid JSON
2. Check model file exists and matches manifest
3. Verify checksum if provided
4. Ensure sufficient storage space

### Keyboard Not Appearing

1. Enable AI Keyboard in **Android Settings** → **System** → **Languages & Input**
2. Select AI Keyboard as default input method
3. Restart the app if needed

---

## 📞 Contact

- **Issues**: [GitHub Issues](https://github.com/ai-dev-2024/ai-keyboard/issues)
- **Email**: *(Contact email will be added)*
- **X/Twitter**: [@MjYoke1111](https://x.com/MjYoke1111)

---

<div align="center">

**Made with ❤️ by the AI Keyboard team**

[⭐ Star us on GitHub](https://github.com/ai-dev-2024/ai-keyboard) • [📖 Read the Docs](docs/) • [💬 Discuss](https://github.com/ai-dev-2024/ai-keyboard/discussions)

</div>
