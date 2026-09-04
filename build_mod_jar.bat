@echo off
REM McAutomaceBreach - Build Mod JAR Script
REM This script builds the mod JAR and allows saving it to a custom location

setlocal enabledelayedexpansion

echo.
echo ========================================
echo    McAutomaceBreach - Build Mod JAR
echo ========================================
echo.

REM Check if gradle wrapper exists
if not exist "gradlew.bat" (
    echo ERROR: gradlew.bat not found!
    echo This script must be run from the project root directory.
    pause
    exit /b 1
)

echo [1/3] Building mod JAR...
echo.

REM Build the mod
call gradlew.bat build

if errorlevel 1 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo [2/3] Build successful!
echo.

REM Find the JAR file
if not exist "build\libs\mcautomacebreach-1.0.0.jar" (
    echo ERROR: JAR file not found at build\libs\mcautomacebreach-1.0.0.jar
    pause
    exit /b 1
)

echo [3/3] Select save location...
echo.

REM Use PowerShell to open file save dialog
for /f "delims=" %%A in ('powershell -Command "Add-Type -AssemblyName System.Windows.Forms; $dialog = New-Object System.Windows.Forms.SaveFileDialog; $dialog.FileName = 'mcautomacebreach-1.0.0.jar'; $dialog.DefaultExt = 'jar'; $dialog.Filter = 'JAR Files (*.jar^|*.jar^|All Files (*.*^|*.*'; $dialog.InitialDirectory = [Environment]::GetFolderPath('Desktop'); $result = $dialog.ShowDialog(); if ($result -eq 'OK') { Write-Host $dialog.FileName } else { Write-Host 'CANCEL' }"') do (
    set "savePath=%%A"
)

REM Check if user cancelled
if "!savePath!"=="CANCEL" (
    echo.
    echo Build cancelled.
    pause
    exit /b 0
)

REM Copy the JAR file
echo.
echo Saving JAR to: !savePath!
echo.

copy "build\libs\mcautomacebreach-1.0.0.jar" "!savePath!" >nul 2>&1

if errorlevel 1 (
    echo ERROR: Failed to save JAR file!
    echo Make sure you have write permissions to the selected directory.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    SUCCESS! Mod JAR created and saved!
echo ========================================
echo.
echo Location: !savePath!
echo.
echo The JAR is ready to be installed in your mods folder.
echo.
pause
