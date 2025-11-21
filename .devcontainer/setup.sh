#!/bin/bash
set -e

echo "🚀 Setting up Android development environment..."

# Set up Android SDK
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Create Android SDK directory
mkdir -p $ANDROID_HOME

# Download command line tools if not present
if [ ! -d "$ANDROID_HOME/cmdline-tools" ]; then
    echo "📥 Downloading Android command line tools..."
    cd /tmp
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
    unzip -q commandlinetools-linux-*.zip -d $ANDROID_HOME/cmdline-tools
    mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest
    rm commandlinetools-linux-*.zip
fi

# Accept licenses
echo "📝 Accepting Android licenses..."
yes | sdkmanager --licenses || true

# Install required SDK components
echo "📦 Installing Android SDK components..."
sdkmanager --install "platform-tools" "platforms;android-34" "build-tools;34.0.0" || true

# Create local.properties if it doesn't exist
if [ ! -f "local.properties" ]; then
    echo "📝 Creating local.properties..."
    echo "sdk.dir=$ANDROID_HOME" > local.properties
fi

# Set up Gradle
echo "⚙️ Configuring Gradle..."
mkdir -p ~/.gradle
cat >> ~/.gradle/gradle.properties << EOF
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
EOF

# Build the project
echo "🔨 Building project..."
./gradlew assembleDebug --no-daemon || echo "⚠️ Build failed, but environment is set up"

echo "✅ Setup complete!"
echo ""
echo "📱 To connect a device:"
echo "   1. Enable USB debugging on your device"
echo "   2. Connect via USB or ADB over network"
echo "   3. Run: adb devices"
echo ""
echo "🔨 To build:"
echo "   ./gradlew assembleDebug"
echo ""
echo "📥 APK location:"
echo "   app/build/outputs/apk/debug/app-debug.apk"

