# AI Keyboard — Release Pipeline Quick Start

This guide provides a quick overview of the signing and Play Store upload pipeline. For detailed instructions, see the individual documentation files.

## 📋 Files Created

1. **`signing-config.md`** - Complete keystore generation and signing guide
2. **`playstore-upload-guide.md`** - Play Console setup and upload workflows
3. **`.github/workflows/android-play-upload.yml`** - Automated CI/CD workflow
4. **`fastlane/Fastfile`** - Fastlane configuration for manual uploads
5. **`fastlane/README.md`** - Fastlane usage documentation

## 🚀 Quick Start (5 Steps)

### Step 1: Generate Keystore

```bash
keytool -genkeypair -v -storetype PKCS12 \
  -keystore aikeyboard-release.jks \
  -alias aikeyboard \
  -keyalg RSA -keysize 2048 -validity 10000
```

**Save the passwords securely!** You'll need them forever.

### Step 2: Create keystore.properties

Copy `keystore.properties.example` to `keystore.properties` and fill in your values:

```properties
storeFile=../path/to/aikeyboard-release.jks
storePassword=YOUR_PASSWORD
keyAlias=aikeyboard
keyPassword=YOUR_PASSWORD
```

### Step 3: Configure build.gradle.kts

Add signing configuration to `app/build.gradle.kts` (see `app/build.gradle.kts.example` for reference):

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
}

android {
    // ... existing config ...
    
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
            // ... existing config ...
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### Step 4: Set Up Play Console Service Account

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a service account
3. Download JSON key
4. Link service account in Play Console → **API access**
5. Grant permissions

See `playstore-upload-guide.md` for detailed steps.

### Step 5: Configure GitHub Secrets

Go to your GitHub repo → **Settings** → **Secrets and variables** → **Actions**

Add these secrets:

- `KEYSTORE_BASE64` - Base64-encoded keystore file
- `KEYSTORE_PASSWORD` - Keystore password
- `KEY_ALIAS` - Key alias (e.g., `aikeyboard`)
- `KEY_PASSWORD` - Key password
- `PLAY_SERVICE_ACCOUNT_JSON` - Full JSON content from service account
- `PLAY_PACKAGE_NAME` - `com.aikeyboard` (optional, has default)

## 🎯 Usage

### Automated Upload (GitHub Actions)

**Tag a release:**
```bash
git tag v1.0.0
git push origin v1.0.0
```

The workflow will:
1. Build signed AAB
2. Upload to Play Store (internal track by default)
3. Create GitHub release

**Manual workflow dispatch:**
- Go to **Actions** → **Build and Upload to Play Store** → **Run workflow**
- Select track (internal, alpha, beta, production)
- Set rollout percentage (for production)

### Manual Upload (Fastlane)

**Install Fastlane:**
```bash
sudo gem install fastlane
```

**Upload to internal testing:**
```bash
cd fastlane
fastlane android internal
```

**Upload to production:**
```bash
fastlane android production rollout:10
```

### Local Build

**Build release AAB:**
```bash
./gradlew bundleRelease
```

**Build release APK:**
```bash
./gradlew assembleRelease
```

## 📚 Documentation

- **`signing-config.md`** - Keystore generation, security, build.gradle setup
- **`playstore-upload-guide.md`** - Play Console setup, testing tracks, production releases
- **`fastlane/README.md`** - Fastlane commands and usage

## 🔒 Security Checklist

Before first release:

- [ ] Keystore generated and stored securely
- [ ] Keystore password saved in password manager
- [ ] `keystore.properties` added to `.gitignore` ✅ (already done)
- [ ] Keystore file added to `.gitignore` ✅ (already done)
- [ ] Multiple encrypted backups created
- [ ] `build.gradle.kts` signing config tested
- [ ] Release build creates signed AAB
- [ ] GitHub Secrets configured
- [ ] Service account JSON secured
- [ ] Play Console API access configured

## 🐛 Troubleshooting

### Build fails: "Keystore file does not exist"
- Check path in `keystore.properties`
- Use absolute path or path relative to project root

### Upload fails: "Service account doesn't have permission"
- Verify service account is linked in Play Console
- Check permissions are granted
- Ensure JSON key is correct

### Version code error
- Increment `versionCode` in `app/build.gradle.kts`
- Each release must have unique version code

See individual documentation files for more troubleshooting tips.

## 📖 Next Steps

1. Read `signing-config.md` for detailed keystore setup
2. Read `playstore-upload-guide.md` for Play Console configuration
3. Test the pipeline with internal testing track
4. Gradually roll out to production

## 🎉 You're Ready!

Once you've completed the 5 steps above, you can:
- Build signed releases locally
- Upload to Play Store automatically via GitHub Actions
- Upload manually using Fastlane
- Manage releases across testing tracks

**Remember:** Your keystore is critical - back it up securely and never lose it!

