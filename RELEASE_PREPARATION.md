# AI Keyboard — Release Preparation Guide

## Overview

This guide covers the final steps for preparing AI Keyboard for Play Store release.

## Pre-Release Checklist

### 1. Version Information
- [ ] Update `versionName` in `app/build.gradle.kts`
- [ ] Update `versionCode` (increment for each release)
- [ ] Update version in About section
- [ ] Create version history/changelog

**Current Version:**
```kotlin
versionCode = 1
versionName = "1.0.0"
```

**For Release:**
- Consider if 1.0.0 is appropriate for initial release
- Or use 0.9.0 for beta, 1.0.0 for stable

---

### 2. Build Configuration

#### Release Build Settings
```kotlin
// app/build.gradle.kts
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        signingConfig = signingConfigs.getByName("release")
    }
}
```

**Checklist:**
- [ ] ProGuard enabled
- [ ] Resource shrinking enabled
- [ ] Release signing configured
- [ ] Keystore file secured
- [ ] Keystore password stored securely (not in code)

---

### 3. Signing Configuration

#### Create Release Keystore
```bash
keytool -genkey -v -keystore aikeyboard-release.keystore \
  -alias aikeyboard -keyalg RSA -keysize 2048 -validity 10000
```

#### Configure Signing
```kotlin
// app/build.gradle.kts
android {
    signingConfigs {
        create("release") {
            storeFile = file("aikeyboard-release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "aikeyboard"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
}
```

**Security:**
- [ ] Keystore file in secure location
- [ ] Passwords stored as environment variables
- [ ] Keystore backed up securely
- [ ] Never commit keystore to Git

---

### 4. Build Release APK/AAB

#### Build AAB (Recommended)
```bash
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

#### Build APK (Alternative)
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

**Checklist:**
- [ ] Build completes without errors
- [ ] APK/AAB size is reasonable (< 50MB without models)
- [ ] ProGuard rules working (no crashes)
- [ ] All features work in release build
- [ ] Test on multiple devices

---

### 5. Testing Release Build

#### Critical Tests
- [ ] App installs successfully
- [ ] Keyboard enables correctly
- [ ] All IME functions work
- [ ] Voice input works
- [ ] Model manager works
- [ ] Settings accessible
- [ ] Premium features gating works
- [ ] Billing works (test purchases)
- [ ] No crashes in Logcat
- [ ] Performance acceptable

#### Device Testing
- [ ] Test on Android 8.0 (minSdk)
- [ ] Test on Android 14 (targetSdk)
- [ ] Test on different screen sizes
- [ ] Test on different manufacturers
- [ ] Test with different languages

---

### 6. Changelog

#### Create CHANGELOG.md
```markdown
# Changelog

## [1.0.0] - 2024-11-XX

### Added
- Initial release
- Offline AI voice typing with ONNX and Vosk engines
- Model manager for installing ASR models
- Full keyboard customization (themes, appearance)
- Clipboard manager with history
- Premium features unlock
- Onboarding flow
- Privacy policy

### Features
- Multi-language typing support
- Smart auto-correct
- Emoji picker
- Gesture typing
- Voice input with offline processing
- Theme engine with presets
- Keyboard height adjustment

### Privacy
- 100% offline voice processing
- No analytics
- No data collection
- Complete privacy policy

### Technical
- Built with Jetpack Compose
- Room database for local storage
- Hilt for dependency injection
- Material 3 design
```

**Checklist:**
- [ ] Changelog created
- [ ] Version history documented
- [ ] Features listed
- [ ] Known issues documented (if any)

---

### 7. GitHub Release

#### Create Release
1. Go to GitHub repository
2. Click "Releases" > "Draft a new release"
3. Tag: `v1.0.0`
4. Title: `AI Keyboard v1.0.0 - Initial Release`
5. Description: Copy from CHANGELOG.md
6. Attach release APK/AAB (optional)
7. Publish release

**Checklist:**
- [ ] Release tag created
- [ ] Release notes complete
- [ ] Assets attached (if applicable)
- [ ] Release published

---

### 8. Play Store Listing

#### Store Listing Checklist
- [ ] App title: "AI Keyboard"
- [ ] Short description (80 chars)
- [ ] Full description
- [ ] App icon (512×512)
- [ ] Feature graphic (1024×500)
- [ ] 7 screenshots (1080×1920)
- [ ] Category: Tools / Productivity
- [ ] Content rating completed
- [ ] Privacy policy URL set
- [ ] Target audience set
- [ ] Keywords optimized

#### Content Rating
- [ ] Complete questionnaire
- [ ] Submit for rating
- [ ] Wait for rating approval

#### Pricing & Distribution
- [ ] Set app as Free
- [ ] Set in-app purchase prices
- [ ] Select countries for distribution
- [ ] Set availability

---

### 9. Internal Testing

#### Upload to Internal Track
1. Create release in Play Console
2. Upload AAB file
3. Add testers (email addresses)
4. Publish to internal track
5. Share testing link with testers

**Checklist:**
- [ ] Internal track configured
- [ ] Testers added
- [ ] Release published to internal
- [ ] Testers can install and test
- [ ] Feedback collected

---

### 10. Beta Testing (Optional)

#### Upload to Alpha/Beta Track
1. Create release in Play Console
2. Upload AAB file
3. Create beta testing program
4. Add beta testers
5. Publish to beta track

**Checklist:**
- [ ] Beta track configured
- [ ] Beta testers added
- [ ] Release published
- [ ] Beta feedback collected
- [ ] Issues fixed before production

---

### 11. Production Release

#### Final Checks
- [ ] All tests pass
- [ ] No critical bugs
- [ ] Performance acceptable
- [ ] Privacy policy complete
- [ ] Legal compliance verified
- [ ] Billing tested
- [ ] Store listing complete
- [ ] Screenshots approved
- [ ] Content rating approved

#### Release to Production
1. Create production release
2. Upload AAB file
3. Review store listing
4. Submit for review
5. Wait for approval (1-3 days)

**Checklist:**
- [ ] Production release created
- [ ] AAB uploaded
- [ ] Store listing reviewed
- [ ] Submitted for review
- [ ] Monitoring for issues

---

## Build Scripts

### build-release.sh
```bash
#!/bin/bash
# Build release AAB

echo "Building release AAB..."

./gradlew clean
./gradlew bundleRelease

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "AAB location: app/build/outputs/bundle/release/app-release.aab"
else
    echo "Build failed!"
    exit 1
fi
```

### verify-release.sh
```bash
#!/bin/bash
# Verify release build

echo "Verifying release build..."

# Check AAB exists
if [ ! -f "app/build/outputs/bundle/release/app-release.aab" ]; then
    echo "ERROR: AAB not found!"
    exit 1
fi

# Check size
SIZE=$(du -h app/build/outputs/bundle/release/app-release.aab | cut -f1)
echo "AAB size: $SIZE"

# Check ProGuard
if grep -q "minifyEnabled.*true" app/build.gradle.kts; then
    echo "ProGuard: Enabled ✓"
else
    echo "WARNING: ProGuard not enabled!"
fi

echo "Verification complete!"
```

---

## Release Checklist Summary

### Before Building
- [ ] Version updated
- [ ] Changelog created
- [ ] All tests pass
- [ ] No known critical bugs
- [ ] Performance acceptable

### Building
- [ ] Release keystore configured
- [ ] ProGuard enabled
- [ ] AAB built successfully
- [ ] Build verified

### Testing
- [ ] Release build tested
- [ ] All features work
- [ ] Billing tested
- [ ] Performance verified
- [ ] No crashes

### Store Preparation
- [ ] Store listing complete
- [ ] Screenshots ready
- [ ] Feature graphic ready
- [ ] Privacy policy URL set
- [ ] Content rating complete

### Release
- [ ] Internal testing done
- [ ] Beta testing done (optional)
- [ ] Production release created
- [ ] Submitted for review
- [ ] Monitoring enabled

---

## Post-Release

### Monitoring
- [ ] Monitor crash reports
- [ ] Monitor user reviews
- [ ] Monitor analytics (if enabled)
- [ ] Track download numbers
- [ ] Monitor billing issues

### Updates
- [ ] Prepare hotfix if needed
- [ ] Plan next version
- [ ] Gather user feedback
- [ ] Prioritize feature requests

---

## Emergency Procedures

### Critical Bug Found
1. Stop rollout (if in staged release)
2. Fix bug immediately
3. Test fix thoroughly
4. Build new release
5. Increment version code
6. Upload new AAB
7. Submit update

### Billing Issue
1. Disable affected products
2. Investigate issue
3. Fix if possible
4. Refund affected users (if needed)
5. Update and release fix

---

## Notes

- Always test release builds before uploading
- Keep keystore secure and backed up
- Monitor first 24 hours closely
- Be ready for quick hotfix if needed
- Document all releases in changelog

