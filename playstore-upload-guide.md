# AI Keyboard — Google Play Store Upload Guide

## Overview

This guide walks you through uploading your AI Keyboard app to Google Play Console, from initial setup to production releases.

---

## Table of Contents

1. [Play Console Setup](#play-console-setup)
2. [Service Account Configuration](#service-account-configuration)
3. [Initial App Creation](#initial-app-creation)
4. [Upload Methods](#upload-methods)
5. [Testing Tracks](#testing-tracks)
6. [Production Release](#production-release)
7. [Expansion Files (OBB)](#expansion-files-obb)
8. [Troubleshooting](#troubleshooting)

---

## Play Console Setup

### Step 1: Create Google Play Developer Account

1. Go to [Google Play Console](https://play.google.com/console)
2. Sign in with your Google account
3. Pay the one-time $25 registration fee
4. Complete developer profile information

### Step 2: Accept Developer Program Policies

- Read and accept Google Play Developer Program Policies
- Complete identity verification (if required)
- Set up payment profile (for paid apps or subscriptions)

---

## Service Account Configuration

For automated uploads via CI/CD, you need a Google Play service account.

### Step 1: Create Service Account

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select or create a project
3. Navigate to **IAM & Admin** → **Service Accounts**
4. Click **Create Service Account**
5. Fill in details:
   - **Name:** `play-store-uploader`
   - **Description:** Service account for Play Store uploads
6. Click **Create and Continue**
7. Skip role assignment (not needed)
8. Click **Done**

### Step 2: Create and Download JSON Key

1. Click on the created service account
2. Go to **Keys** tab
3. Click **Add Key** → **Create new key**
4. Select **JSON** format
5. Click **Create**
6. JSON file downloads automatically — **save it securely!**

### Step 3: Grant Play Console Access

1. Go to [Google Play Console](https://play.google.com/console)
2. Navigate to **Setup** → **API access**
3. Under **Service accounts**, click **Link service account**
4. Enter the service account email (from JSON file: `client_email`)
5. Grant permissions:
   - ✅ **View app information and download bulk reports**
   - ✅ **Manage production releases**
   - ✅ **Manage testing track releases**
   - ✅ **Manage app content and pricing**
6. Click **Invite user**

### Step 4: Store JSON Key Securely

**For GitHub Actions:**
- Copy entire JSON content
- Go to GitHub repo → **Settings** → **Secrets** → **Actions**
- Create secret: `PLAY_SERVICE_ACCOUNT_JSON`
- Paste JSON content as value

**For Local Use:**
- Store JSON file in secure location (not in repo)
- Add to `.gitignore`: `*.json` (or specific filename)
- Use environment variable or secure config file

---

## Initial App Creation

### Step 1: Create New App

1. In Play Console, click **Create app**
2. Fill in details:
   - **App name:** AI Keyboard
   - **Default language:** English (United States)
   - **App or game:** App
   - **Free or paid:** Free (or Paid if applicable)
3. Click **Create app**

### Step 2: Complete Store Listing

Navigate to **Store presence** → **Main store listing**

**Required Information:**
- **App name:** AI Keyboard
- **Short description:** (80 characters max)
  - Example: "Offline AI-powered voice typing keyboard with complete privacy"
- **Full description:** (4000 characters max)
  - Describe features, benefits, use cases
  - Include keywords for discoverability
- **App icon:** 512×512 PNG (see `PLAY_STORE_ASSETS_GUIDE.md`)
- **Feature graphic:** 1024×500 PNG
- **Screenshots:** 7 screenshots, 1080×1920 each
- **Category:** Productivity / Utilities
- **Tags:** AI, Voice, Keyboard, Privacy, Offline

### Step 3: Content Rating

1. Go to **Content rating**
2. Complete questionnaire about app content
3. Submit for rating
4. Wait for rating (usually instant for productivity apps)

### Step 4: Privacy Policy

1. Go to **Policy** → **Privacy policy**
2. Upload or link to privacy policy
3. Required for apps that collect user data
4. See `PRIVACY_POLICY.md` in project root

### Step 5: App Access

1. Go to **App access**
2. Select access level:
   - **All functionality available without restriction** (if free)
   - Or configure restricted features

---

## Upload Methods

### Method 1: Manual Upload (Web UI)

1. Go to **Production** (or testing track)
2. Click **Create new release**
3. Upload AAB file:
   - Drag and drop `app-release.aab`
   - Or click **Browse files**
4. Fill in **Release name:** e.g., "1.0.0"
5. Add **Release notes:**
   ```
   - Initial release
   - Offline AI voice typing
   - Customizable themes
   - Clipboard manager
   ```
6. Click **Review release**
7. Review summary, then click **Start rollout to Production** (or testing track)

### Method 2: GitHub Actions (Automated)

See `.github/workflows/android-play-upload.yml` for automated uploads.

**Workflow triggers:**
- Push tag: `v*` (e.g., `v1.0.0`)
- Manual workflow dispatch
- Pull request (optional, for testing)

**Required secrets:**
- `PLAY_SERVICE_ACCOUNT_JSON`
- `PLAY_PACKAGE_NAME` (optional, defaults to `com.aikeyboard`)

### Method 3: Fastlane (CLI)

See `fastlane/Fastfile` for Fastlane configuration.

**Upload command:**
```bash
fastlane android deploy
```

**Or specific track:**
```bash
fastlane android deploy track:internal
fastlane android deploy track:alpha
fastlane android deploy track:beta
fastlane android deploy track:production
```

### Method 4: Gradle Play Publisher Plugin

Add to `app/build.gradle.kts`:

```kotlin
plugins {
    // ... existing plugins
    id("com.github.triplet.play") version "3.9.0"
}

play {
    serviceAccountCredentials.set(file("play-service-account.json"))
    defaultToAppBundles.set(true)
    track.set("internal") // or "alpha", "beta", "production"
}
```

Upload command:
```bash
./gradlew publishReleaseBundle
```

---

## Testing Tracks

Google Play provides multiple testing tracks before production.

### Internal Testing Track

**Purpose:** Quick testing with small group (up to 100 testers)

**Setup:**
1. Go to **Testing** → **Internal testing**
2. Click **Create new release**
3. Upload AAB
4. Add testers:
   - **Email addresses:** Add up to 100 emails
   - **Or create tester group:** Add emails to group
5. Share testing link with testers

**Use case:** Internal team testing, quick iterations

### Closed Testing Track

**Purpose:** Larger group testing (unlimited testers)

**Setup:**
1. Go to **Testing** → **Closed testing**
2. Create testing track (e.g., "Alpha", "Beta")
3. Click **Create new release**
4. Upload AAB
5. Add testers via:
   - Email list
   - Google Groups
   - Google+ Communities (deprecated)
6. Testers join via opt-in link

**Use case:** Beta testing, feature validation

### Open Testing Track

**Purpose:** Public beta (anyone can join)

**Setup:**
1. Go to **Testing** → **Open testing**
2. Click **Create new release**
3. Upload AAB
4. Testers join via Play Store listing

**Use case:** Public beta, early access program

### Testing Workflow Recommendation

```
Internal Testing (10-50 testers)
    ↓
Closed Testing / Alpha (100-1000 testers)
    ↓
Closed Testing / Beta (1000+ testers)
    ↓
Production (Gradual rollout: 5% → 20% → 50% → 100%)
```

---

## Production Release

### Step 1: Prepare Release

1. **Update version:**
   - Increment `versionCode` in `app/build.gradle.kts`
   - Update `versionName` (semantic versioning)

2. **Build release AAB:**
   ```bash
   ./gradlew bundleRelease
   ```

3. **Test release build:**
   - Install on test device
   - Verify all features work
   - Check for crashes or issues

### Step 2: Create Production Release

1. Go to **Production** in Play Console
2. Click **Create new release**
3. Upload AAB file
4. Fill in **Release name:** e.g., "1.0.0"
5. Add **Release notes:**
   - What's new in this version
   - Bug fixes
   - Improvements

### Step 3: Review and Rollout

1. Click **Review release**
2. Check for warnings or errors:
   - API level requirements
   - Permissions
   - Content rating
   - Store listing completeness
3. Click **Start rollout to Production**

### Step 4: Gradual Rollout (Recommended)

**Initial rollout:**
- Start with 5% or 10% of users
- Monitor crash reports and reviews
- Wait 1-3 days

**Increase rollout:**
- If no issues, increase to 20%
- Then 50%
- Finally 100%

**How to manage rollout:**
1. Go to **Production** → **Releases**
2. Click on active release
3. Click **Manage rollout**
4. Adjust percentage
5. Click **Update**

### Step 5: Monitor Release

**Key metrics to watch:**
- **Crash rate:** Should be < 1%
- **ANR rate:** Should be < 0.1%
- **User reviews:** Monitor feedback
- **Install/uninstall rate**
- **Performance metrics**

**Where to check:**
- **Quality** → **Android Vitals**
- **Statistics** → **User acquisition**
- **Reviews** → **Ratings and reviews**

---

## Expansion Files (OBB)

If your app exceeds 150MB, you need expansion files (OBB).

### When You Need OBB

- App size > 150MB
- Large model files
- Extensive media assets

### Creating OBB Files

**Using Android Studio:**
1. Build → **Generate Signed Bundle / APK**
2. Select **Android App Bundle**
3. Android Studio generates OBB automatically

**Manual creation:**
```bash
# Create OBB file
zip -0 main.1.com.aikeyboard.obb model-files/
```

### Uploading OBB

1. Upload AAB first
2. Play Console automatically handles OBB
3. Or upload manually in **Release** → **App bundles and expansion files**

### Testing OBB

- Download app from Play Store (not direct APK)
- Verify expansion files download correctly
- Test on different devices

---

## Troubleshooting

### Error: "Upload failed: Invalid AAB"

**Solutions:**
- Ensure AAB is built with `bundleRelease`
- Check AAB is signed correctly
- Verify `versionCode` is incremented
- Check AAB size limits (150MB without OBB)

### Error: "Service account doesn't have permission"

**Solutions:**
- Verify service account has correct permissions in Play Console
- Check JSON key is correct
- Ensure service account is linked in Play Console → **API access**

### Error: "Version code already exists"

**Solutions:**
- Increment `versionCode` in `build.gradle.kts`
- Check existing releases in Play Console
- Each release must have unique version code

### Error: "App not eligible for production"

**Common causes:**
- Missing privacy policy
- Content rating not completed
- Store listing incomplete
- App access not configured
- Target API level too low

**Solutions:**
- Complete all required sections in Play Console
- Check **Dashboard** for incomplete items
- Review **Policy status**

### AAB Upload Takes Too Long

**Solutions:**
- Check internet connection
- AAB files can be large (50-150MB)
- Use stable connection or retry
- Consider using API/CLI for faster uploads

### Testers Can't Install App

**Solutions:**
- Verify testers are added to testing track
- Check opt-in link is correct
- Ensure testers have accepted invitation
- Verify app is available in their country
- Check device compatibility (minSdk)

---

## Quick Reference

### Upload AAB Manually
1. Play Console → **Production** (or testing track)
2. **Create new release**
3. Upload `app-release.aab`
4. Add release notes
5. **Review** → **Start rollout**

### Automated Upload (GitHub Actions)
```bash
git tag v1.0.0
git push origin v1.0.0
# Workflow automatically builds and uploads
```

### Fastlane Upload
```bash
fastlane android deploy track:production
```

### Check Release Status
- Play Console → **Production** → **Releases**
- View rollout percentage
- Monitor metrics

---

## Release Checklist

Before uploading to production:

- [ ] Version code incremented
- [ ] Version name updated
- [ ] Release AAB built and tested locally
- [ ] Release notes prepared
- [ ] Store listing complete
- [ ] Privacy policy uploaded
- [ ] Content rating completed
- [ ] App access configured
- [ ] Testing completed (internal/closed tracks)
- [ ] No critical bugs or crashes
- [ ] Performance metrics acceptable
- [ ] Screenshots and assets up to date

---

## Best Practices

1. **Always test in internal/closed tracks first**
   - Catch issues before production
   - Get feedback from testers

2. **Use gradual rollout**
   - Start with 5-10% of users
   - Monitor metrics before full rollout

3. **Keep version codes sequential**
   - Never decrease version code
   - Each release must increment

4. **Write clear release notes**
   - Users read these
   - Highlight new features and fixes

5. **Monitor metrics after release**
   - Watch crash rates
   - Respond to reviews
   - Fix critical issues quickly

6. **Maintain store listing**
   - Update screenshots for new features
   - Keep description current
   - Respond to user reviews

---

## Additional Resources

- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [App Bundle Documentation](https://developer.android.com/guide/app-bundle)
- [Play Console API](https://developers.google.com/android-publisher)
- [Fastlane Documentation](https://docs.fastlane.tools/)

---

**Remember:** Your first production release is a milestone! Take your time, test thoroughly, and use gradual rollout to minimize risk.

