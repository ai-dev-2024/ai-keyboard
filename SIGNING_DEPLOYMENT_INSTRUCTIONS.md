# Signing & Deployment Instructions — AI Keyboard

## 🔐 Part 1: Generate Keystore

### Step 1: Create the Keystore File

Run this command in your terminal (PowerShell, Command Prompt, or Git Bash):

```bash
keytool -genkey -v -keystore aikeyboard-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias aikeyboard
```

**You will be prompted for:**
1. **Keystore password** - Choose a strong password (remember this!)
2. **Re-enter password** - Confirm the password
3. **First and last name** - Your name or organization name
4. **Organizational unit** - Your department/team (optional)
5. **Organization** - Your company/organization name
6. **City or Locality** - Your city
7. **State or Province** - Your state/province
8. **Two-letter country code** - e.g., US, GB, CA
9. **Confirm information** - Type 'yes' if correct
10. **Key password** - Usually same as keystore password (press Enter to use same)

**Example:**
```
Enter keystore password: ********
Re-enter new password: ********
What is your first and last name?
  [Unknown]:  AI Dev 2024
What is the name of your organizational unit?
  [Unknown]:  Development
What is the name of your organization?
  [Unknown]:  AI Keyboard
What is the name of your City or Locality?
  [Unknown]:  San Francisco
What is the name of your State or Province?
  [Unknown]:  CA
What is the two-letter country code for this unit?
  [Unknown]:  US
Is CN=AI Dev 2024, OU=Development, O=AI Keyboard, L=San Francisco, ST=CA, C=US correct?
  [no]:  yes

Generating 2,048 bit RSA key pair and self-signed certificate (SHA256withRSA) with a validity of 10,000 days
        for: CN=AI Dev 2024, OU=Development, O=AI Keyboard, L=San Francisco, ST=CA, C=US
Enter key password for <aikeyboard>
        (RETURN if same as keystore password):  [Press Enter]
[Storing aikeyboard-release.jks]
```

---

## 📁 Part 2: Secure the Keystore

### ⚠️ CRITICAL SECURITY STEPS

1. **Move keystore to a secure location** (NOT in the project directory):

   ```bash
   # Windows example
   mkdir C:\AndroidKeystores
   move aikeyboard-release.jks C:\AndroidKeystores\
   
   # Linux/Mac example
   mkdir ~/AndroidKeystores
   mv aikeyboard-release.jks ~/AndroidKeystores/
   ```

2. **Backup the keystore** to multiple secure locations:
   - External hard drive
   - Cloud storage (encrypted)
   - Password manager (store password securely)

3. **Add to .gitignore** (if not already there):

   ```gitignore
   # Keystore files
   *.jks
   *.keystore
   keystore.properties
   ```

4. **Never commit keystore files to git!**

---

## ⚙️ Part 3: Configure Build Signing

### Step 1: Create keystore.properties

1. Copy the example file:
   ```bash
   copy keystore.properties.example keystore.properties    # Windows
   cp keystore.properties.example keystore.properties     # Linux/Mac
   ```

2. Edit `keystore.properties` with your actual values:

   ```properties
   # Path to your keystore file (absolute path recommended)
   storeFile=C:/AndroidKeystores/aikeyboard-release.jks
   
   # Keystore password
   storePassword=YOUR_KEYSTORE_PASSWORD_HERE
   
   # Key alias (the alias you used when creating the keystore)
   keyAlias=aikeyboard
   
   # Key password (usually same as keystore password)
   keyPassword=YOUR_KEY_PASSWORD_HERE
   ```

   **Windows Path Examples:**
   - `storeFile=C:/AndroidKeystores/aikeyboard-release.jks`
   - `storeFile=C:\\AndroidKeystores\\aikeyboard-release.jks`

   **Linux/Mac Path Examples:**
   - `storeFile=/home/username/AndroidKeystores/aikeyboard-release.jks`
   - `storeFile=~/AndroidKeystores/aikeyboard-release.jks`

### Step 2: Verify Build Configuration

The `app/build.gradle.kts` file already includes signing configuration that reads from `keystore.properties`. It will automatically:
- Use the keystore for release builds when `keystore.properties` exists
- Build unsigned APKs/AABs when `keystore.properties` is missing

---

## 🏗️ Part 4: Build Signed Release APK

### Clean Build

```bash
# Windows
.\gradlew.bat clean

# Linux/Mac
./gradlew clean
```

### Build Signed Release APK

```bash
# Windows
.\gradlew.bat assembleRelease

# Linux/Mac
./gradlew assembleRelease
```

### Output Location

**Signed APK:** `app/build/outputs/apk/release/app-release.apk`

This APK is now signed and ready for distribution!

---

## 📦 Part 5: Build Signed Play Store AAB

### Build Signed AAB Bundle

```bash
# Windows
.\gradlew.bat bundleRelease

# Linux/Mac
./gradlew bundleRelease
```

### Output Location

**Signed AAB:** `app/build/outputs/bundle/release/app-release.aab`

This AAB is now signed and ready for Play Store upload!

---

## ✅ Part 6: Verify Signing

### Verify APK Signature

```bash
# Using jarsigner
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Expected output should show:
# jar verified.
```

### Verify AAB Bundle

```bash
# Download bundletool from: https://github.com/google/bundletool/releases
# Then verify:
bundletool verify --bundle=app/build/outputs/bundle/release/app-release.aab
```

### Check APK Info

```bash
# Using aapt2 (from Android SDK)
aapt2 dump badging app/build/outputs/apk/release/app-release.apk | findstr "package"

# Or using apksigner (from Android SDK)
apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk
```

---

## 🚀 Part 7: Deploy to Play Store

### Prerequisites

1. **Google Play Console Account**
   - Sign up at: https://play.google.com/console
   - Pay one-time $25 registration fee
   - Complete developer account setup

2. **App Information Ready**
   - App name, description, screenshots
   - Privacy policy URL
   - App icon and feature graphics
   - See `PLAY_STORE_LISTING.md` for details

### Upload Process

1. **Go to Play Console**
   - Navigate to: https://play.google.com/console
   - Create new app or select existing app

2. **Upload AAB**
   - Go to: Production → Releases → Create new release
   - Upload: `app/build/outputs/bundle/release/app-release.aab`
   - Add release notes

3. **Complete Store Listing**
   - Fill in all required fields
   - Upload screenshots and graphics
   - Set content rating
   - Add privacy policy

4. **Review & Publish**
   - Review all information
   - Submit for review
   - Wait for Google's approval (usually 1-3 days)

### Testing Tracks

Before going to production, test in these tracks:

1. **Internal Testing** - Quick testing with up to 100 testers
2. **Closed Testing** - Beta testing with specific testers
3. **Open Testing** - Public beta testing
4. **Production** - Public release

---

## 🔄 Part 8: Update App Version

Before each release, update version in `app/build.gradle.kts`:

```kotlin
defaultConfig {
    versionCode = 2  // Increment by 1 for each release
    versionName = "1.0.1"  // Semantic versioning
}
```

**Version Code Rules:**
- Must be an integer
- Must increase with each release
- Play Store uses this to determine which version is newer

**Version Name Rules:**
- Human-readable version string
- Follow semantic versioning: MAJOR.MINOR.PATCH
- Example: 1.0.0 → 1.0.1 → 1.1.0 → 2.0.0

---

## 🛡️ Security Best Practices

1. **Keystore Security**
   - Use strong passwords (20+ characters, mixed case, numbers, symbols)
   - Store passwords in a password manager
   - Never share keystore files or passwords
   - Keep multiple backups in secure locations

2. **Build Security**
   - Never commit `keystore.properties` to git
   - Use CI/CD secrets for automated builds
   - Rotate keystores if compromised
   - Use different keystores for different apps

3. **Distribution Security**
   - Only distribute signed APKs/AABs
   - Verify signatures before distribution
   - Use Play App Signing for additional security
   - Enable Play App Signing in Play Console

---

## 📋 Checklist

### Before First Release

- [ ] Keystore generated and secured
- [ ] `keystore.properties` created and configured
- [ ] Keystore backed up to secure locations
- [ ] `.gitignore` updated to exclude keystore files
- [ ] Signed APK built and verified
- [ ] Signed AAB built and verified
- [ ] Version code and name set correctly

### Before Each Release

- [ ] Version code incremented
- [ ] Version name updated
- [ ] Changelog updated
- [ ] All tests passed
- [ ] Signed APK/AAB built
- [ ] Signatures verified
- [ ] Release notes prepared

### Play Store Upload

- [ ] Google Play Console account set up
- [ ] App listing information complete
- [ ] Screenshots and graphics ready
- [ ] Privacy policy published
- [ ] Content rating completed
- [ ] AAB uploaded to testing track
- [ ] Tested on multiple devices
- [ ] Ready for production release

---

## 🆘 Troubleshooting

### "Keystore file not found"

- Check the path in `keystore.properties`
- Use absolute paths (recommended)
- On Windows, use forward slashes or escaped backslashes
- Verify file exists at the specified location

### "Keystore password incorrect"

- Double-check password in `keystore.properties`
- Ensure no extra spaces or characters
- Try typing password manually instead of copy-paste

### "Alias not found"

- Verify alias name matches what you used when creating keystore
- Default alias in instructions: `aikeyboard`
- List aliases: `keytool -list -v -keystore aikeyboard-release.jks`

### "APK/AAB not signed"

- Ensure `keystore.properties` exists and is correct
- Check that signing config is applied in `build.gradle.kts`
- Clean and rebuild: `.\gradlew.bat clean assembleRelease`

### "Play Store rejects AAB"

- Ensure AAB is signed (not unsigned)
- Check that version code is higher than previous upload
- Verify all required metadata is provided
- Check for policy violations

---

## 📚 Additional Resources

- **Android App Signing:** https://developer.android.com/studio/publish/app-signing
- **Play App Signing:** https://support.google.com/googleplay/android-developer/answer/9842756
- **Keytool Documentation:** https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html
- **Bundletool:** https://github.com/google/bundletool
- **Play Console Help:** https://support.google.com/googleplay/android-developer

---

**Last Updated:** 2025-01-XX
**Project:** AI Keyboard
**Version:** 1.0.0

