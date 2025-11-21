# Script to install APK to Android emulator
# This script waits for the emulator to come online and then installs the APK

$apkPath = "app\build\outputs\apk\debug\app-debug.apk"

if (-not (Test-Path $apkPath)) {
    Write-Host "Error: APK not found at $apkPath" -ForegroundColor Red
    exit 1
}

Write-Host "Waiting for emulator to come online..." -ForegroundColor Yellow
$maxAttempts = 20
$attempt = 0
$deviceFound = $false

while ($attempt -lt $maxAttempts) {
    $attempt++
    $devices = adb devices
    Write-Host "Attempt $attempt/$maxAttempts - Checking for devices..."
    
    if ($devices -match "device\s+device") {
        $deviceFound = $true
        Write-Host "Emulator is online!" -ForegroundColor Green
        break
    }
    
    Start-Sleep -Seconds 3
}

if (-not $deviceFound) {
    Write-Host "`nError: No emulator found after waiting." -ForegroundColor Red
    Write-Host "Please make sure:" -ForegroundColor Yellow
    Write-Host "  1. An emulator is running in Android Studio" -ForegroundColor Yellow
    Write-Host "  2. The emulator has finished booting (wait for the home screen)" -ForegroundColor Yellow
    Write-Host "  3. Try restarting the emulator from Android Studio" -ForegroundColor Yellow
    exit 1
}

Write-Host "`nInstalling APK..." -ForegroundColor Yellow
$installResult = adb install -r $apkPath

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nAPK installed successfully!" -ForegroundColor Green
    
    # Launch the app
    Write-Host "`nLaunching the app..." -ForegroundColor Yellow
    $launchResult = adb shell am start -n com.aikeyboard/.ui.onboarding.OnboardingActivity
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "App launched successfully!" -ForegroundColor Green
    } else {
        Write-Host "App installed but could not be launched automatically." -ForegroundColor Yellow
        Write-Host "You can launch it manually from the emulator's app drawer." -ForegroundColor Yellow
    }
} else {
    Write-Host "`nError installing APK. Exit code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host $installResult
    exit 1
}

