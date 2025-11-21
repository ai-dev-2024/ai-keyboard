#!/bin/bash

# AI Keyboard - BrowserStack Auto-Upload Script
# Automatically uploads the newest debug APK to BrowserStack App Live
# for instant browser-based testing

set -e

echo "🚀 AI Keyboard - BrowserStack Upload Script"
echo "============================================="

# Check if environment variables are set
if [[ -z "$BROWSERSTACK_USERNAME" || -z "$BROWSERSTACK_ACCESS_KEY" ]]; then
    echo "❌ Error: BrowserStack credentials not set!"
    echo "Please set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY"
    exit 1
fi

# Find the newest debug APK automatically
APK_PATH=""
if [[ -n "$APK_PATH" ]]; then
    # Use APK path from environment (set by Codemagic)
    APK_FILE="$APK_PATH"
else
    # Auto-detect the newest debug APK
    echo "🔍 Searching for debug APK..."
    APK_DIR="app/build/outputs/apk/debug"
    
    if [[ ! -d "$APK_DIR" ]]; then
        echo "❌ Error: APK directory not found: $APK_DIR"
        exit 1
    fi
    
    # Find the newest APK file
    APK_FILE=$(find "$APK_DIR" -name "*.apk" -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -d' ' -f2-)
    
    if [[ -z "$APK_FILE" ]]; then
        echo "❌ Error: No APK files found in $APK_DIR"
        exit 1
    fi
fi

echo "📦 Found APK: $APK_FILE"

# Check if file exists and get its size
if [[ ! -f "$APK_FILE" ]]; then
    echo "❌ Error: APK file does not exist: $APK_FILE"
    exit 1
fi

APK_SIZE=$(stat -f%z "$APK_FILE" 2>/dev/null || stat -c%s "$APK_FILE" 2>/dev/null || echo "unknown")
echo "📏 APK size: $APK_SIZE bytes"

# Prepare BrowserStack upload
echo ""
echo "🌍 Uploading to BrowserStack App Live..."
echo "   Username: $BROWSERSTACK_USERNAME"
echo "   APK: $(basename "$APK_FILE")"

# BrowserStack App Live API endpoint
UPLOAD_URL="https://api-cloud.browserstack.com/app-live/upload"

# Upload the APK using curl
RESPONSE=$(curl -X POST \
    -u "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
    -F "file=@$APK_FILE" \
    "$UPLOAD_URL")

echo ""
echo "📡 BrowserStack Response:"
echo "$RESPONSE" | jq '.' 2>/dev/null || echo "$RESPONSE"

# Parse the response to get the app URL
APP_URL=$(echo "$RESPONSE" | jq -r '.app_url // empty' 2>/dev/null)
TESTING_URL=$(echo "$RESPONSE" | jq -r '.share_url // empty' 2>/dev/null)
APP_ID=$(echo "$RESPONSE" | jq -r '.app_id // empty' 2>/dev/null)

if [[ -n "$APP_URL" ]]; then
    echo ""
    echo "✅ Upload successful!"
    echo "🔗 App URL: $APP_URL"
    
    if [[ -n "$TESTING_URL" ]]; then
        echo "🧪 Testing URL: $TESTING_URL"
        echo ""
        echo "🎯 Ready for Testing!"
        echo "==================="
        echo "📱 Click the testing URL above to test the app in your browser"
        echo "🔄 The URL provides instant access to real Android devices"
        echo "⚡ No installation required - test directly in your browser"
        echo ""
        echo "📋 Summary:"
        echo "   • APK uploaded: $(basename "$APK_FILE")"
        echo "   • App ID: $APP_ID"
        echo "   • BrowserStack URL: $TESTING_URL"
        
        # Save URLs to file for easy access
        echo "🔖 Saving URLs to browserstack_urls.txt..."
        cat > browserstack_urls.txt << EOF
AI Keyboard - BrowserStack Testing URLs
=====================================

Upload Time: $(date)
APK File: $APK_FILE
APK Size: $APK_SIZE bytes
App ID: $APP_ID

BrowserStack URLs:
- App URL: $APP_URL
- Testing URL: $TESTING_URL

To test the app:
1. Click: $TESTING_URL
2. Select an Android device
3. Start testing immediately!

Note: This URL is publicly accessible for testing.
EOF
        
        echo "✅ URLs saved to browserstack_urls.txt"
        
    else
        echo "⚠️  Warning: No testing URL received from BrowserStack"
        echo "🔗 App URL available at: $APP_URL"
    fi
    
else
    echo "❌ Upload failed!"
    echo "🔍 Error details: $RESPONSE"
    exit 1
fi

echo ""
echo "🎉 BrowserStack upload completed!"
