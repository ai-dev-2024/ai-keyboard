# Connect to LD Player via ADB
# LD Player typically runs on port 5555

Write-Host "Connecting to LD Player..." -ForegroundColor Cyan

# Try to connect to LD Player
$ldPlayerPort = 5555
$ldPlayerIP = "127.0.0.1"

# Get ADB path
$adbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

if (-not (Test-Path $adbPath)) {
    Write-Host "ERROR: ADB not found at $adbPath" -ForegroundColor Red
    Write-Host "Please ensure Android SDK is installed" -ForegroundColor Yellow
    exit 1
}

# Kill existing ADB server to start fresh
Write-Host "Restarting ADB server..." -ForegroundColor Yellow
& $adbPath kill-server
Start-Sleep -Seconds 1
& $adbPath start-server
Start-Sleep -Seconds 2

# Connect to LD Player
Write-Host "Connecting to LD Player at ${ldPlayerIP}:${ldPlayerPort}..." -ForegroundColor Yellow
& $adbPath connect "${ldPlayerIP}:${ldPlayerPort}"

Start-Sleep -Seconds 2

# Check connection
$devices = & $adbPath devices
Write-Host "`nConnected devices:" -ForegroundColor Green
Write-Host $devices

if ($devices -match "127.0.0.1:5555") {
    Write-Host "`n✓ Successfully connected to LD Player!" -ForegroundColor Green
    Write-Host "You can now use: adb logcat, adb install, etc." -ForegroundColor Cyan
} else {
    Write-Host "`n⚠ Could not connect to LD Player" -ForegroundColor Yellow
    Write-Host "Make sure LD Player is running and has ADB enabled" -ForegroundColor Yellow
    Write-Host "In LD Player: Settings > Advanced > Enable ADB Debugging" -ForegroundColor Yellow
}

