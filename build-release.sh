#!/bin/bash
# AI Keyboard - Release Build Script
# Builds a signed release AAB for Play Store

set -e  # Exit on error

echo "=========================================="
echo "AI Keyboard - Release Build"
echo "=========================================="
echo ""

# Check if keystore exists
if [ ! -f "app/aikeyboard-release.keystore" ]; then
    echo "ERROR: Release keystore not found!"
    echo "Expected location: app/aikeyboard-release.keystore"
    echo ""
    echo "To create a keystore, run:"
    echo "keytool -genkey -v -keystore app/aikeyboard-release.keystore \\"
    echo "  -alias aikeyboard -keyalg RSA -keysize 2048 -validity 10000"
    exit 1
fi

# Check for keystore passwords
if [ -z "$KEYSTORE_PASSWORD" ] || [ -z "$KEY_PASSWORD" ]; then
    echo "WARNING: Keystore passwords not set in environment variables"
    echo "Set KEYSTORE_PASSWORD and KEY_PASSWORD before running"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "Cleaning previous builds..."
./gradlew clean

echo ""
echo "Building release AAB..."
./gradlew bundleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "Build successful!"
    echo "=========================================="
    echo ""
    AAB_PATH="app/build/outputs/bundle/release/app-release.aab"
    
    if [ -f "$AAB_PATH" ]; then
        SIZE=$(du -h "$AAB_PATH" | cut -f1)
        echo "AAB location: $AAB_PATH"
        echo "AAB size: $SIZE"
        echo ""
        echo "Next steps:"
        echo "1. Test the AAB on a device"
        echo "2. Upload to Google Play Console"
        echo "3. Submit for review"
    else
        echo "ERROR: AAB file not found at expected location"
        exit 1
    fi
else
    echo ""
    echo "=========================================="
    echo "Build failed!"
    echo "=========================================="
    exit 1
fi

