# AI Keyboard — Model Disclaimers & Safe Redistribution Guidelines

## User-Facing Disclaimers

### Disclaimer for User-Imported Models

**IMPORTANT: Model Licensing Responsibility**

When you install custom ASR (Automatic Speech Recognition) models in AI Keyboard, you are responsible for:

1. **Verifying License Compliance**: Ensure the model you install is licensed for your intended use (personal or commercial).

2. **Attribution Requirements**: Some models require attribution. Check the model's license terms.

3. **Commercial Use**: If you plan to use AI Keyboard commercially, verify that the installed model permits commercial use.

4. **Redistribution Rights**: The app does not redistribute models. You must obtain models from legitimate sources.

**The app developer is not responsible for licensing violations resulting from user-installed models.**

---

### Disclaimer for Offline Processing

**Privacy & Offline Processing Notice**

AI Keyboard processes all voice input **100% offline** on your device:

- ✅ No audio data is transmitted to external servers
- ✅ All ASR processing happens locally
- ✅ Models are stored on your device
- ✅ No internet connection required for voice recognition

**Model Storage Location**: Models are stored in the app's private directory and are not accessible to other apps.

---

## Safe Redistribution Guidelines

### For App Developers

#### ✅ DO:

1. **Do NOT bundle model files in APK**
   - Keep APK size under 100 MB (Play Store recommendation)
   - Avoid policy violations
   - Allow user flexibility

2. **Provide metadata only**
   - Link to official model repositories
   - Include installation instructions
   - Display license information

3. **Implement user installation flow**
   - Support file picker (SAF)
   - Support URL downloads (with user consent)
   - Validate model format and manifest

4. **Display license information**
   - Show license type for installed models
   - Display attribution requirements
   - Link to full license texts

5. **Add clear disclaimers**
   - User responsibility for custom models
   - Offline processing explanation
   - Privacy notice

6. **Include attribution in app**
   - Display copyright holders
   - Link to license texts
   - Credit model creators

#### ❌ DO NOT:

1. ❌ Bundle model binaries in APK
2. ❌ Distribute models from unofficial sources
3. ❌ Assume all models are commercially usable
4. ❌ Hide license information
5. ❌ Redistribute without attribution
6. ❌ Modify model licenses
7. ❌ Remove copyright notices

---

## Model Installation Best Practices

### For End Users

1. **Download from Official Sources**
   - Use official model repositories (Hugging Face, GitHub, etc.)
   - Verify checksums when provided
   - Check license before downloading

2. **Verify License Compatibility**
   - Read the license file
   - Check commercial use permissions
   - Understand attribution requirements

3. **Keep License Information**
   - Save license files with models
   - Note copyright holders
   - Document license type

4. **Report Issues**
   - Report licensing concerns to model creators
   - Contact app developer for app-related issues

---

## Attribution Requirements by License

### Apache-2.0 Models

**Required Attribution:**
- Include copyright notice
- Include Apache-2.0 license text
- State changes (if any)

**Example:**
```
This product includes software developed by [Copyright Holder].
Licensed under the Apache License, Version 2.0.
```

### MIT Models

**Required Attribution:**
- Include copyright notice
- Include MIT license text

**Example:**
```
Copyright (c) [YEAR] [Copyright Holder]
Licensed under the MIT License.
```

### CC-BY Models

**Required Attribution:**
- Credit the creator
- Link to original work
- Indicate if changes were made

---

## Legal Compliance Checklist

### Pre-Release Checklist

- [ ] No model files bundled in APK
- [ ] User installation flow implemented
- [ ] License information displayed in Settings
- [ ] Disclaimers shown in About section
- [ ] Attribution displayed for known models
- [ ] Privacy notice explains offline processing
- [ ] Model manifest supports license metadata
- [ ] License registry includes common models
- [ ] Unknown license handling implemented
- [ ] License URLs are accessible

### Ongoing Compliance

- [ ] Monitor for new model licenses
- [ ] Update license registry as needed
- [ ] Review user feedback on licensing
- [ ] Keep disclaimers up to date
- [ ] Verify attribution requirements

---

## Model-Specific Guidelines

### NVIDIA Parakeet 0.6B

- **License**: Apache-2.0
- **Attribution**: Required
- **Commercial Use**: ✅ Allowed
- **Redistribution**: ✅ Allowed (with license)
- **Bundling**: ❌ Not recommended (size)

### Distil-Whisper

- **License**: Apache-2.0
- **Attribution**: Required
- **Commercial Use**: ✅ Allowed
- **Redistribution**: ✅ Allowed (with license)
- **Bundling**: ❌ Not recommended (size)

### Vosk Models

- **License**: Apache-2.0
- **Attribution**: Required
- **Commercial Use**: ✅ Allowed
- **Redistribution**: ✅ Allowed (with license)
- **Bundling**: ❌ Not recommended (size)

### User-Imported Models

- **License**: Unknown (user's responsibility)
- **Attribution**: Unknown
- **Commercial Use**: ⚠️ Verify with user
- **Redistribution**: ❌ Not applicable
- **Bundling**: ❌ Not applicable

---

## Contact & Support

For licensing questions:
- Model licenses: Contact model creators
- App licensing: See app's LICENSE file
- Legal concerns: Consult legal counsel

---

*Last Updated: 2024*
*This document is for informational purposes only and does not constitute legal advice.*

