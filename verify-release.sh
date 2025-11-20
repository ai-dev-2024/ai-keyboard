#!/bin/bash
# AI Keyboard - Release Verification Script
# Verifies release build before upload

set -e

echo "=========================================="
echo "AI Keyboard - Release Verification"
echo "=========================================="
echo ""

AAB_PATH="app/build/outputs/bundle/release/app-release.aab"

# Check if AAB exists
if [ ! -f "$AAB_PATH" ]; then
    echo "ERROR: AAB not found!"
    echo "Expected location: $AAB_PATH"
    echo ""
    echo "Build the release first: ./build-release.sh"
    exit 1
fi

echo "✓ AAB file exists"
echo ""

# Check size
SIZE=$(du -h "$AAB_PATH" | cut -f1)
SIZE_BYTES=$(stat -f%z "$AAB_PATH" 2>/dev/null || stat -c%s "$AAB_PATH" 2>/dev/null)
SIZE_MB=$((SIZE_BYTES / 1024 / 1024))

echo "AAB size: $SIZE ($SIZE_MB MB)"
if [ $SIZE_MB -gt 150 ]; then
    echo "⚠ WARNING: AAB size is large (>150MB). Consider optimization."
else
    echo "✓ AAB size is acceptable"
fi
echo ""

# Check ProGuard
if grep -q "isMinifyEnabled.*true" app/build.gradle.kts; then
    echo "✓ ProGuard is enabled"
else
    echo "⚠ WARNING: ProGuard may not be enabled"
fi
echo ""

# Check version
VERSION_NAME=$(grep "versionName" app/build.gradle.kts | head -1 | sed 's/.*versionName = "\(.*\)".*/\1/')
VERSION_CODE=$(grep "versionCode" app/build.gradle.kts | head -1 | sed 's/.*versionCode = \(.*\).*/\1/')

echo "Version: $VERSION_NAME (code: $VERSION_CODE)"
echo ""

# Check signing
echo "Checking signing configuration..."
if grep -q "signingConfigs" app/build.gradle.kts; then
    echo "✓ Signing configuration found"
else
    echo "⚠ WARNING: Signing configuration not found"
fi
echo ""

# Summary
echo "=========================================="
echo "Verification Summary"
echo "=========================================="
echo "AAB: $AAB_PATH"
echo "Size: $SIZE"
echo "Version: $VERSION_NAME ($VERSION_CODE)"
echo ""
echo "Next steps:"
echo "1. Test the AAB on a device"
echo "2. Run performance tests"
echo "3. Test billing (if applicable)"
echo "4. Upload to Play Console"
echo ""

