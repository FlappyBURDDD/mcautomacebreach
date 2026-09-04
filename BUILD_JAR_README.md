# 🛠️ Build Mod JAR - Quick Start Guide

## Windows

### Option 1: Double-Click (Easiest)
1. Navigate to the project folder
2. **Double-click** `build_mod_jar.bat`
3. A save dialog will appear
4. Select where to save the JAR (e.g., Desktop, Downloads, Mods folder)
5. The JAR will be created and saved automatically ✅

### Option 2: Command Line
```bash
build_mod_jar.bat
```

---

## Linux / macOS

### Option 1: Terminal (Recommended)
```bash
# Make script executable (first time only)
chmod +x build_mod_jar.sh

# Run it
./build_mod_jar.sh
```

### Option 2: Open Terminal in Folder
1. Open Terminal in the project directory
2. Run: `./build_mod_jar.sh`
3. A file dialog will appear (Zenity on Linux, Finder on macOS)
4. Select save location
5. Done! ✅

---

## What the Script Does

```
[1/3] Building mod JAR...
    └─ Runs: gradlew build
    └─ Compiles all Java code
    └─ Applies Mixins
    └─ Packages JAR

[2/3] Build successful!
    └─ Verifies build completed
    └─ Checks for JAR file

[3/3] Select save location...
    └─ Opens file save dialog
    └─ Copies JAR to selected location
    └─ Shows success message
```

---

## Where to Save

### For Testing (Recommended)
```
Your Minecraft Folder/mods/
├─ mcautomacebreach-1.0.0.jar  ← Save here
```

### Locate Your Minecraft Folder

**Windows:**
```
%APPDATA%\.minecraft\mods\
```

**Linux:**
```
~/.minecraft/mods/
```

**macOS:**
```
~/Library/Application Support/minecraft/mods/
```

---

## Common Issues

### ❌ "ERROR: Build failed!"
**Solution:**
- Make sure you have Java 21+ installed
- Check internet connection (needs to download dependencies first time)
- Try running again

### ❌ Windows: "PowerShell not found"
**Solution:**
- Use Windows 10/11 (PowerShell is built-in)
- Or manually copy the JAR from `build\libs\`

### ❌ Linux/macOS: "Permission denied"
**Solution:**
```bash
chmod +x build_mod_jar.sh
./build_mod_jar.sh
```

### ❌ Save dialog doesn't appear
**Solution:**
- Manually type the path when prompted
- Example: `/home/user/Downloads/mcautomacebreach-1.0.0.jar`

---

## Output

After successful build, you'll see:
```
========================================
   SUCCESS! Mod JAR created and saved!
========================================

Location: C:\Users\Username\Downloads\mcautomacebreach-1.0.0.jar

The JAR is ready to be installed in your mods folder.
```

---

## Next Steps

1. **Install the JAR:**
   - Place in `mods/` folder
   - Also need: Fabric Loader + Fabric API

2. **Launch Minecraft:**
   - Start with Fabric profile
   - Mod should load automatically

3. **Test in-game:**
   - Open settings: `/helb`
   - Toggle features
   - Test with Mace + Sword

---

## Manual Build (If Scripts Don't Work)

```bash
# Windows
gradlew build

# Linux/macOS
./gradlew build

# JAR output
build/libs/mcautomacebreach-1.0.0.jar
```

Then copy manually to your mods folder.

---

💡 **Pro Tip:** Create a shortcut to the script for even faster builds!

**Windows:** Right-click `build_mod_jar.bat` → Send to → Desktop (create shortcut)

**Linux/macOS:** Create alias in `.bashrc`:
```bash
alias buildmace="~/mcautomacebreach/build_mod_jar.sh"
```
