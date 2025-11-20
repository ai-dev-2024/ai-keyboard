# AI Keyboard — APK Signing Configuration Guide

## Overview

This guide covers everything you need to sign your Android release builds securely and prepare them for Google Play Store distribution.

---

## Table of Contents

1. [Generating a Keystore](#generating-a-keystore)
2. [Storing Credentials Securely](#storing-credentials-securely)
3. [Configuring build.gradle.kts](#configuring-buildgradlekts)
4. [GitHub Secrets Setup](#github-secrets-setup)
5. [Local Release Builds](#local-release-builds)
6. [Key Rotation (Future)](#key-rotation-future)
7. [Troubleshooting](#troubleshooting)

---

## Generating a Keystore

### Step 1: Generate the Keystore File

Use Java's `keytool` command to generate a keystore. **Run this command only once** — you'll use this keystore for all future releases.

```bash
keytool -genkeypair -v -storetype PKCS12 -keystore aikeyboard-release.jks -alias aikeyboard -keyalg RSA -keysize 2048 -validity 10000
```

**What this command does:**
- `-genkeypair`: Creates a new key pair
- `-v`: Verbose output
- `-storetype PKCS12`: Modern keystore format
- `-keystore aikeyboard-release.jks`: Output filename (you can name it anything)
- `-alias aikeyboard`: Key alias (use this in build.gradle)
- `-keyalg RSA`: Encryption algorithm
- `-keysize 2048`: Key size (2048 is standard)
- `-validity 10000`: Valid for ~27 years (10000 days)

### Step 2: Answer the Prompts

You'll be asked for:
- **Keystore password**: Create a strong password (save it securely!)
- **Key password**: Can be same as keystore password (recommended for simplicity)
- **Name, Organization, etc.**: These are optional but recommended:
  - First and last name: Your name or company name
  - Organizational unit: e.g., "Development"
  - Organization: Your company name
  - City/Locality: Your city
  - State/Province: Your state
  - Country code: Two-letter code (e.g., "US", "GB")

**Important:** The passwords you set here are **critical** — you'll need them for every release build.

### Step 3: Verify the Keystore

```bash
keytool -list -v -keystore aikeyboard-release.jks
```

This shows your keystore details. Verify the alias name matches what you'll use in `build.gradle.kts`.

---

## Storing Credentials Securely

### ⚠️ CRITICAL: Never Commit Keystore Files

**DO NOT:**
- Commit `.jks` or `.keystore` files to Git
- Share keystore files via email or cloud storage
- Include passwords in code or documentation

**DO:**
- Store keystore in a secure, encrypted location
- Use password managers for passwords
- Use GitHub Secrets for CI/CD (see below)
- Keep multiple encrypted backups

### Recommended Storage Locations

**Option 1: Encrypted Local Directory (Recommended for Solo Developers)**
```bash
# Create encrypted directory (macOS/Linux)
mkdir -p ~/.android/keystores
# Move keystore there
mv aikeyboard-release.jks ~/.android/keystores/
# Set restrictive permissions
chmod 600 ~/.android/keystores/aikeyboard-release.jks
```

**Option 2: Encrypted Cloud Storage**
- Use encrypted cloud storage (e.g., encrypted Google Drive, Dropbox with encryption)
- Store keystore + passwords separately
- Use strong encryption passwords

**Option 3: Password Manager**
- Store keystore file in encrypted password manager (1Password, Bitwarden, etc.)
- Store passwords as separate secure notes
- Enable 2FA on password manager

### Password Storage Best Practices

1. **Use a Password Manager**
   - Store keystore password
   - Store key password (if different)
   - Store keystore location/path

2. **Create a Secure Backup Document**
   - Document keystore location
   - Document alias name
   - Document validity period
   - Store passwords separately (password manager)

3. **Multiple Backups**
   - Local encrypted backup
   - Cloud encrypted backup
   - Physical encrypted backup (USB drive in safe)

---

## Configuring build.gradle.kts

### Step 1: Create keystore.properties File

Create a file `keystore.properties` in the project root (same level as `build.gradle.kts`):

```properties
storeFile=../path/to/aikeyboard-release.jks
storePassword=YOUR_KEYSTORE_PASSWORD
keyAlias=aikeyboard
keyPassword=YOUR_KEY_PASSWORD
```

**Important:** Add `keystore.properties` to `.gitignore`:

```gitignore
# Keystore files
*.jks
*.keystore
keystore.properties
```

### Step 2: Update app/build.gradle.kts

Add signing configuration to your `app/build.gradle.kts`:

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.aikeyboard"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aikeyboard"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        // ... rest of defaultConfig
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Apply signing config
            signingConfig = signingConfigs.getByName("release")
        }
    }

    // ... rest of android block
}
```

### Step 3: Verify Configuration

Build a release APK to test:

```bash
./gradlew assembleRelease
```

The signed APK will be at: `app/build/outputs/apk/release/app-release.apk`

Verify signing:

```bash
# Check if APK is signed
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

Or use:

```bash
apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
```

---

## GitHub Secrets Setup

For automated CI/CD builds, store credentials as GitHub Secrets.

### Required Secrets

Go to your GitHub repository → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

Create these secrets:

1. **`KEYSTORE_BASE64`**
   - Value: Base64-encoded keystore file
   - How to generate:
     ```bash
     # macOS/Linux
     base64 -i aikeyboard-release.jks | pbcopy
     
     # Windows (PowerShell)
     [Convert]::ToBase64String([IO.File]::ReadAllBytes("aikeyboard-release.jks")) | Set-Clipboard
     ```

2. **`KEYSTORE_PASSWORD`**
   - Value: Your keystore password (plain text)

3. **`KEY_ALIAS`**
   - Value: `aikeyboard` (or your alias name)

4. **`KEY_PASSWORD`**
   - Value: Your key password (usually same as keystore password)

5. **`PLAY_SERVICE_ACCOUNT_JSON`**
   - Value: JSON service account key (see Play Store upload guide)

6. **`PLAY_PACKAGE_NAME`**
   - Value: `com.aikeyboard`

### GitHub Actions Usage

The workflow will:
1. Decode the base64 keystore
2. Create `keystore.properties` from secrets
3. Build signed release AAB
4. Upload to Play Store

See `.github/workflows/android-play-upload.yml` for implementation.

---

## Local Release Builds

### Build Release AAB (for Play Store)

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

### Build Release APK (for direct distribution)

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Build with Version Increment

Update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Increment for each release
    versionName = "1.0.1"  // Semantic versioning
}
```

---

## Key Rotation (Future)

**Important:** Google Play requires you to use the same signing key for all updates. Key rotation is complex and should be planned carefully.

### When You Might Need Key Rotation

- Keystore file is lost or corrupted
- Keystore password is forgotten
- Security breach (key compromised)
- Company/organization change

### Google Play Key Rotation Process

1. **Contact Google Play Support**
   - Explain the situation
   - Request key rotation assistance
   - Provide app package name and details

2. **Google's Process**
   - Google will guide you through the process
   - You'll need to provide proof of ownership
   - Process can take several days

3. **Alternative: App Signing by Google Play**
   - Enable "App Signing by Google Play" in Play Console
   - Google manages the app signing key
   - You upload with an upload key (can be rotated easier)
   - **Recommended for new apps**

### Preventing Key Loss

- **Multiple encrypted backups** (local + cloud)
- **Password manager** for passwords
- **Documentation** of keystore location
- **Enable App Signing by Google Play** (if possible)

---

## Troubleshooting

### Error: "Keystore file does not exist"

**Solution:**
- Check `keystore.properties` path is correct
- Use absolute path or path relative to project root
- Verify file exists at specified location

### Error: "Keystore was tampered with, or password was incorrect"

**Solution:**
- Double-check keystore password
- Verify you're using the correct keystore file
- Check for typos in `keystore.properties`

### Error: "Alias name does not exist"

**Solution:**
- Verify alias name in `keystore.properties` matches keystore
- List keystore aliases: `keytool -list -keystore aikeyboard-release.jks`

### Error: "APK/AAB not signed"

**Solution:**
- Ensure `signingConfig` is applied to `release` build type
- Check `keystore.properties` is loaded correctly
- Verify build completed without errors

### Build Works Locally but Fails in CI/CD

**Solution:**
- Verify all GitHub Secrets are set correctly
- Check base64 encoding of keystore
- Ensure workflow has proper file permissions
- Check workflow logs for specific error messages

---

## Quick Reference

### Keystore Generation
```bash
keytool -genkeypair -v -storetype PKCS12 \
  -keystore aikeyboard-release.jks \
  -alias aikeyboard \
  -keyalg RSA -keysize 2048 -validity 10000
```

### Verify Keystore
```bash
keytool -list -v -keystore aikeyboard-release.jks
```

### Build Release AAB
```bash
./gradlew bundleRelease
```

### Verify Signed APK
```bash
apksigner verify --verbose app/build/outputs/apk/release/app-release.apk
```

---

## Security Checklist

Before your first release:

- [ ] Keystore generated and stored securely
- [ ] Keystore password saved in password manager
- [ ] `keystore.properties` added to `.gitignore`
- [ ] Keystore file added to `.gitignore`
- [ ] Multiple encrypted backups created
- [ ] `build.gradle.kts` signing config tested
- [ ] Release build creates signed AAB/APK
- [ ] GitHub Secrets configured (if using CI/CD)
- [ ] Documentation of keystore location stored securely

---

## Additional Resources

- [Android App Signing Documentation](https://developer.android.com/studio/publish/app-signing)
- [Google Play App Signing](https://support.google.com/googleplay/android-developer/answer/9842756)
- [Keytool Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)

---

**Remember:** Your keystore is the key to your app's identity on Google Play. Protect it like you would protect your bank account password!

