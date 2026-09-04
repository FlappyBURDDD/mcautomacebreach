#!/bin/bash

# McAutomaceBreach - Build Mod JAR Script
# This script builds the mod JAR and allows saving it to a custom location
# Usage: ./build_mod_jar.sh (or bash build_mod_jar.sh)

set -e

echo ""
echo "========================================"
echo "   McAutomaceBreach - Build Mod JAR"
echo "========================================"
echo ""

# Check if gradle wrapper exists
if [ ! -f "gradlew" ]; then
    echo "ERROR: gradlew not found!"
    echo "This script must be run from the project root directory."
    read -p "Press Enter to exit..."
    exit 1
fi

echo "[1/3] Building mod JAR..."
echo ""

# Build the mod
if ! ./gradlew build; then
    echo ""
    echo "ERROR: Build failed!"
    read -p "Press Enter to exit..."
    exit 1
fi

echo ""
echo "[2/3] Build successful!"
echo ""

# Find the JAR file
if [ ! -f "build/libs/mcautomacebreach-1.0.0.jar" ]; then
    echo "ERROR: JAR file not found at build/libs/mcautomacebreach-1.0.0.jar"
    read -p "Press Enter to exit..."
    exit 1
fi

echo "[3/3] Select save location..."
echo ""

# Platform detection for file dialog
if command -v zenity &> /dev/null; then
    # Linux with zenity
    savePath=$(zenity --file-selection --save --filename="mcautomacebreach-1.0.0.jar" --file-filter="JAR Files (*.jar)" 2>/dev/null)
elif command -v osascript &> /dev/null; then
    # macOS
    savePath=$(osascript -e 'tell application "System Events" to choose file name with prompt "Save Mod JAR:" default name "mcautomacebreach-1.0.0.jar"' 2>/dev/null)
else
    # Fallback: manual input
    echo "Enter the full path where you want to save the JAR file:"
    echo "(Example: /home/username/Downloads/mcautomacebreach-1.0.0.jar)"
    echo ""
    read -p "Save path: " savePath
fi

# Check if path is empty or cancelled
if [ -z "$savePath" ] || [ "$savePath" = "" ]; then
    echo ""
    echo "Build cancelled."
    exit 0
fi

# Ensure .jar extension
if [[ ! "$savePath" == *.jar ]]; then
    savePath="${savePath}/mcautomacebreach-1.0.0.jar"
fi

# Create directory if it doesn't exist
dir=$(dirname "$savePath")
if [ ! -d "$dir" ]; then
    mkdir -p "$dir"
fi

echo ""
echo "Saving JAR to: $savePath"
echo ""

# Copy the JAR file
if ! cp "build/libs/mcautomacebreach-1.0.0.jar" "$savePath"; then
    echo "ERROR: Failed to save JAR file!"
    echo "Make sure you have write permissions to the selected directory."
    read -p "Press Enter to exit..."
    exit 1
fi

echo ""
echo "========================================"
echo "   SUCCESS! Mod JAR created and saved!"
echo "========================================"
echo ""
echo "Location: $savePath"
echo ""
echo "The JAR is ready to be installed in your mods folder."
echo ""
read -p "Press Enter to exit..."
