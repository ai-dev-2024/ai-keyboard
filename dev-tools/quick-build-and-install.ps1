# Quick build and install workflow
# Builds the APK and installs it to connected device

Write-Host "=== Quick Build & Install ===" -ForegroundColor Cyan
Write-Host ""

# Step 1: Build APK
Write-Host "Step 1: Building APK..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -ne 0) {
    Write-Host "`n✗ Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`n✓ Build successful!" -ForegroundColor Green
Write-Host ""

# Step 2: Install APK
Write-Host "Step 2: Installing APK..." -ForegroundColor Yellow
& "$PSScriptRoot\install-apk.ps1"

Write-Host "`n=== Done ===" -ForegroundColor Green

