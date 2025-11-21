# Capture Android logs for debugging
# Usage: .\dev-tools\capture-logs.ps1 [filter]

param(
    [string]$Filter = "*:E AndroidRuntime:E AIKeyboard*"
)

$adbPath = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

if (-not (Test-Path $adbPath)) {
    Write-Host "ERROR: ADB not found" -ForegroundColor Red
    exit 1
}

# Check if device is connected
$devices = & $adbPath devices
if (-not ($devices -match "device$")) {
    Write-Host "No device connected. Trying to connect to LD Player..." -ForegroundColor Yellow
    & "$PSScriptRoot\connect-ldplayer.ps1"
    Start-Sleep -Seconds 2
}

$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$logFile = "logs\keyboard_log_$timestamp.txt"

# Create logs directory if it doesn't exist
if (-not (Test-Path "logs")) {
    New-Item -ItemType Directory -Path "logs" | Out-Null
}

Write-Host "Capturing logs to: $logFile" -ForegroundColor Cyan
Write-Host "Filter: $Filter" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop capturing" -ForegroundColor Yellow
Write-Host ""

# Clear logcat buffer
& $adbPath logcat -c

# Start capturing
& $adbPath logcat $Filter | Tee-Object -FilePath $logFile

