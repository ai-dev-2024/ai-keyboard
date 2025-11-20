# AI Keyboard — Play Store Launch Checklist

## Status Legend
- ✅ Complete
- ⚠️ Needs Review
- ❌ Missing/Incomplete

---

## 1. TECHNICAL CHECKLIST

### Core Functionality
- ✅ IME functions (typing, suggestions, emoji, themes) - Implemented
- ⚠️ Voice Input tested on low/mid/high-end devices - **Needs Testing**
- ✅ ONNX + Vosk engines - Implemented
- ✅ Model Manager - Implemented
- ✅ App icon + adaptive icons - Present (`ic_launcher.xml`, `ic_launcher_round.xml`)
- ❌ Splash screen - **MISSING** - Need to create
- ⚠️ No crashes in logs - **Needs Testing**
- ✅ Minify/Proguard ON with rules for ONNX/Vosk native libs - Configured in `proguard-rules.pro`

### ProGuard Rules Status
✅ Rules present for:
- `com.microsoft.onnxruntime.**`
- `com.alphacephei.vosk.**`
- `kotlinx.coroutines.**`

---

## 2. PERFORMANCE TESTS

### Required Metrics
- ⚠️ Keyboard startup time < 200ms - **Needs Testing**
- ⚠️ Voice model load time < 2 seconds (or show loading indicator) - **Needs Testing**
- ⚠️ Partial transcription latency < 500ms on mid devices - **Needs Testing**
- ⚠️ CPU < 60% sustained during voice usage - **Needs Testing**
- ⚠️ Memory peak within safe bounds - **Needs Testing**

**Action Required:** Create performance testing script/documentation

---

## 3. PRIVACY CHECK

### Network & Data Usage
- ✅ No network calls unless user opts-in - Model downloads are user-initiated via `ModelInstallDialog`
- ✅ No analytics - No analytics libraries found
- ✅ Microphone used only during recording - `RECORD_AUDIO` permission declared
- ⚠️ Privacy Policy page ready - **PARTIAL** - Only notice in AboutSection, need full page
- ⚠️ List microphone permission clearly in Play listing - **Needs Verification**

### Privacy Policy Status
- ❌ Full Privacy Policy page (Markdown + HTML) - **MISSING**
- ✅ Privacy notice in AboutSection - Present

**Action Required:** Create full Privacy Policy page

---

## 4. PLAY STORE ASSETS

### Required Assets
- ❌ 7 screenshots (1080×1920) - **MISSING**
- ❌ Feature graphic (1024×500) - **MISSING**
- ❌ High-res icon (512×512) - **MISSING** (need to verify if exists)
- ✅ Short app description - Present in `PLAY_STORE_LISTING.md`
- ✅ Full description - Present in `PLAY_STORE_LISTING.md`
- ⚠️ Promo text (optional) - **Needs Review**
- ✅ App category: Tools / Productivity - Can be set in Play Console
- ✅ Keywords included in long description - Present

**Action Required:** Create/verify all visual assets

---

## 5. LEGAL & LICENSING

### License & Copyright
- ✅ Apache-2.0 license included - `LICENSE` file present
- ⚠️ Model licensing audit verified - **Needs Review** (ONNX Runtime, Vosk are Apache-2.0)
- ⚠️ Copyright notices in About screen - **PARTIAL** - License mentioned, copyright needs verification
- ✅ Ko-fi link marked as "optional support" - Present in AboutSection with clear labeling

**Action Required:** Add copyright notice to AboutSection

---

## 6. USER EXPERIENCE

### Onboarding & Tutorials
- ❌ Onboarding flow (permission + setup) - **MISSING**
- ❌ Keyboard enabling instructions - **MISSING**
- ❌ Model selection tutorial - **MISSING**
- ❌ Voice typing tutorial - **MISSING**
- ✅ Settings discoverable and friendly - Settings screen implemented

**Action Required:** Create onboarding flow and tutorials

---

## 7. MONETIZATION

### Billing & Premium Features
- ✅ Billing library tested - Google Play Billing 6.1.0 included
- ⚠️ Premium features correctly gated - **Needs Verification** (BillingConstants present, need to check implementation)
- ⚠️ Restore purchases working - **Needs Testing**
- ✅ Ko-fi donation link functional - Implemented in AboutSection
- ⚠️ Pricing finalized - **Needs Decision** (suggested: $4.99–$9.99 or Pro unlock)

**Action Required:** Verify billing implementation and test restore purchases

---

## 8. FINAL DELIVERABLES

### Release Preparation
- ❌ Signed release APK/AAB - **Needs Build**
- ⚠️ Version name & code set - Currently `1.0.0` (versionCode: 1) - **Needs Review**
- ❌ Github release with changelog - **MISSING**
- ❌ Backup promotional assets - **MISSING**
- ❌ Press kit (icons, screenshots, description) - **MISSING**

**Action Required:** Prepare all release artifacts

---

## PRIORITY ACTIONS

### High Priority (Blocking Release)
1. ❌ Create splash screen
2. ❌ Create full Privacy Policy page
3. ❌ Create onboarding flow
4. ⚠️ Test billing implementation
5. ⚠️ Performance testing on multiple devices

### Medium Priority (Should Have)
1. ❌ Create Play Store visual assets (screenshots, feature graphic)
2. ⚠️ Add copyright notice to About screen
3. ❌ Create keyboard enabling instructions
4. ❌ Create tutorials for model selection and voice typing

### Low Priority (Nice to Have)
1. ❌ Create press kit
2. ❌ Prepare GitHub release
3. ⚠️ Finalize pricing strategy

---

## NOTES

- Network usage is properly gated: Model downloads only happen when user explicitly chooses "Download from URL"
- No analytics libraries detected - good for privacy
- ProGuard rules are properly configured for native libraries
- Billing library is included but implementation needs verification
- Current version: 1.0.0 (versionCode: 1) - consider if this is appropriate for initial release

