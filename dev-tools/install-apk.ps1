# Install APK to connected device
# Usage: .\dev-tools\install-apk.ps1 [apk-path]

param(
    [string]$ApkPath = "app\build\outputs\apk\debug\app-debug.apk"
)

$adbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

if (-not (Test-Path $adbPath)) {
    Write-Host "ERROR: ADB not found" -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $ApkPath)) {
    Write-Host "ERROR: APK not found at: $ApkPath" -ForegroundColor Red
    Write-Host "Please build the APK first: .\gradlew.bat assembleDebug" -ForegroundColor Yellow
    exit 1
}

# Check if device is connected
$devices = & $adbPath devices
if (-not ($devices -match "device$")) {
    Write-Host "No device connected. Trying to connect to LD Player..." -ForegroundColor Yellow
    & "$PSScriptRoot\connect-ldplayer.ps1"
    Start-Sleep -Seconds 2
}

Write-Host "Installing APK: $ApkPath" -ForegroundColor Cyan
Write-Host ""

# Uninstall existing app first (optional, comment out if you want to keep data)
# Write-Host "Uninstalling existing app..." -ForegroundColor Yellow
# & $adbPath uninstall com.aikeyboard
# Start-Sleep -Seconds 1

# Install APK
& $adbPath install -r $ApkPath

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✓ APK installed successfully!" -ForegroundColor Green
    Write-Host "You can now launch the app on your device" -ForegroundColor Cyan
} else {
    Write-Host "`n✗ Installation failed" -ForegroundColor Red
    Write-Host "Check the error message above" -ForegroundColor Yellow
}

