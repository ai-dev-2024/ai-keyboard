# AI Keyboard — Model Licensing Analysis

## Executive Summary

This document provides a comprehensive legal analysis of ASR (Automatic Speech Recognition) models used or referenced in the AI Keyboard application. **Critical finding: No models should be bundled in the APK for commercial distribution.** All models must be user-installed to ensure legal compliance.

---

## Legal Analysis Table

| Model | License Type | Redistribution Allowed? | Commercial Use Allowed? | Attribution Required? | Bundling in APK Allowed? | Notes |
|-------|-------------|------------------------|------------------------|----------------------|-------------------------|-------|
| **NVIDIA Parakeet 0.6B ONNX** | Apache-2.0 | ✅ Yes | ✅ Yes | ✅ Yes (Copyright notice) | ❌ **NO** (Size/Policy) | Apache-2.0 allows redistribution, but bundling 240MB+ violates Play Store policies and increases APK size. |
| **Distil-Whisper ONNX** | Apache-2.0 | ✅ Yes | ✅ Yes | ✅ Yes (Copyright notice) | ❌ **NO** (Size/Policy) | Apache-2.0 compliant, but model size makes bundling impractical. |
| **Whisper (OpenAI)** | MIT | ✅ Yes | ✅ Yes | ✅ Yes (License text) | ❌ **NO** (Size/Policy) | MIT license is permissive, but original Whisper models are large. |
| **Whisper (Various Ports)** | MIT / Apache-2.0 | ✅ Yes (varies) | ✅ Yes | ✅ Yes (varies) | ❌ **NO** (Size/Policy) | Port-specific licenses apply. Check each port's license. |
| **Vosk Models (Small/Medium/Large)** | Apache-2.0 | ✅ Yes | ✅ Yes | ✅ Yes (Copyright notice) | ❌ **NO** (Size/Policy) | Vosk models are Apache-2.0, but bundling multiple models increases APK size significantly. |
| **User-Imported Models** | **Unknown/Varies** | ⚠️ **Unknown** | ⚠️ **Unknown** | ⚠️ **Unknown** | ❌ **NO** | Users are responsible for verifying licenses of custom models. |

---

## Detailed Model Analysis

### 1. NVIDIA Parakeet 0.6B ONNX

- **License**: Apache-2.0
- **Source**: NVIDIA NeMo
- **License URL**: https://github.com/NVIDIA/NeMo/blob/main/LICENSE
- **Key Terms**:
  - ✅ Commercial use allowed
  - ✅ Redistribution allowed (with license)
  - ✅ Modification allowed
  - ✅ Patent grant included
  - ✅ Must include copyright notice and license text
- **Bundling Decision**: ❌ **DO NOT BUNDLE**
  - Model size: ~240 MB
  - Play Store APK size limits and policies discourage large binaries
  - User installation is safer and more flexible

### 2. Distil-Whisper ONNX

- **License**: Apache-2.0
- **Source**: Hugging Face / OpenAI Distil-Whisper
- **License URL**: https://huggingface.co/distil-whisper
- **Key Terms**:
  - ✅ Commercial use allowed
  - ✅ Redistribution allowed (with license)
  - ✅ Must include Apache-2.0 license text
- **Bundling Decision**: ❌ **DO NOT BUNDLE**
  - Model size varies (typically 50-150 MB)
  - User installation preferred for flexibility

### 3. Whisper Family Models

#### OpenAI Whisper (Original)
- **License**: MIT
- **Source**: OpenAI
- **License URL**: https://github.com/openai/whisper/blob/main/LICENSE
- **Key Terms**:
  - ✅ Commercial use allowed
  - ✅ Redistribution allowed (with license)
  - ✅ Must include MIT license text
- **Bundling Decision**: ❌ **DO NOT BUNDLE**
  - Original models are very large (1.5GB+)
  - ONNX ports may be smaller but still substantial

#### Whisper ONNX Ports
- **License**: MIT or Apache-2.0 (varies by port)
- **Source**: Community ports (e.g., onnxruntime-extensions)
- **Key Terms**: Varies by port
- **Bundling Decision**: ❌ **DO NOT BUNDLE**
  - Verify license for each specific port
  - Size considerations apply

### 4. Vosk Models

- **License**: Apache-2.0
- **Source**: Alpha Cephei
- **License URL**: https://github.com/alphacep/vosk-api/blob/master/LICENSE
- **Key Terms**:
  - ✅ Commercial use allowed
  - ✅ Redistribution allowed (with license)
  - ✅ Must include Apache-2.0 license text
- **Model Variants**:
  - Small: ~40 MB
  - Medium: ~1.4 GB
  - Large: ~1.8 GB
- **Bundling Decision**: ❌ **DO NOT BUNDLE**
  - Multiple model sizes available
  - User choice preferred
  - Size considerations

### 5. User-Imported Models

- **License**: **UNKNOWN** (User's responsibility)
- **Source**: User-provided
- **Key Terms**: ⚠️ **Must be verified by user**
- **Bundling Decision**: ❌ **NOT APPLICABLE**
  - Users install models manually
  - App must disclaim responsibility for user-installed model licenses

---

## Recommended Safe Strategy

### ✅ DO:

1. **Do NOT bundle any model files in the APK**
   - Keep APK size small
   - Avoid Play Store policy violations
   - Allow user flexibility

2. **Provide metadata links only**
   - Link to official model repositories
   - Provide installation instructions
   - Include license information in metadata

3. **Allow user to import models manually**
   - Support SAF (Storage Access Framework) for file selection
   - Support URL downloads with user consent
   - Validate model format and manifest

4. **Add clear disclaimers**
   - "Users are responsible for model licensing when installing custom models"
   - Display license information for installed models
   - Show attribution requirements

5. **Display model licenses in Settings**
   - Show all installed models
   - Display license type and attribution requirements
   - Link to full license texts

### ❌ DO NOT:

1. ❌ Bundle model binaries in APK
2. ❌ Distribute model files from unofficial sources
3. ❌ Assume all user-imported models are licensed for commercial use
4. ❌ Hide license information from users
5. ❌ Redistribute models without proper attribution

---

## Attribution Requirements

### Apache-2.0 Models
Must include:
```
Copyright [YEAR] [COPYRIGHT HOLDER]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### MIT Models
Must include:
```
Copyright (c) [YEAR] [COPYRIGHT HOLDER]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## Play Store Compliance

### APK Size Considerations
- Play Store recommends APK size < 100 MB
- Large model files would exceed this limit
- User installation avoids size restrictions

### Content Policy
- Models must be legally distributable
- User-installed models are user's responsibility
- App must not facilitate piracy or license violations

### Privacy Policy
- Must disclose offline processing
- Must explain model storage location
- Must clarify no data transmission

---

## Implementation Checklist

- [x] No models bundled in APK
- [x] User installation flow implemented
- [x] Model manifest includes license metadata
- [x] Settings display shows model licenses
- [x] Disclaimer added for user-imported models
- [x] Attribution displayed in About section
- [x] License texts accessible to users
- [x] Privacy notice explains offline processing

---

## Risk Assessment

### Low Risk ✅
- Using Apache-2.0 and MIT licensed models
- Not bundling models in APK
- Providing proper attribution
- User-installed models (user's responsibility)

### Medium Risk ⚠️
- User may import unlicensed models
  - **Mitigation**: Clear disclaimer in UI
- Model ports may have different licenses
  - **Mitigation**: Verify each port's license

### High Risk ❌
- Bundling large models in APK
  - **Mitigation**: Not implemented
- Redistributing models without attribution
  - **Mitigation**: Attribution displayed in Settings

---

## Conclusion

The AI Keyboard app is **legally safe for commercial Play Store distribution** when:
1. ✅ No models are bundled in the APK
2. ✅ Users install models manually
3. ✅ License information is displayed
4. ✅ Proper disclaimers are shown
5. ✅ Attribution requirements are met

**Status**: ✅ **COMPLIANT** (with current implementation)

---

*Last Updated: 2024*
*Legal Review Recommended Before Commercial Release*

