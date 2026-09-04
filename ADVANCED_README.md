# McAutomaceBreach - Advanced Documentation

## 📋 Table of Contents

1. [Feature Details](#feature-details)
2. [Technical Architecture](#technical-architecture)
3. [Configuration Guide](#configuration-guide)
4. [Debugging & Troubleshooting](#debugging--troubleshooting)
5. [Development Guide](#development-guide)

---

## Feature Details

### 1. AutoBreachSwap ⚔️

**How it works:**
- Detects when player swings a **sword** in main hand
- Automatically finds the best **Breach Mace** in hotbar (highest enchantment level)
- Swaps to the Breach Mace and performs an attack
- Returns to the original sword after attack completes
- Implements 17-tick cooldown (Minecraft standard Mace attack speed)

**Requirements:**
- Player must be holding a sword
- At least one Breach Mace must be in hotbar (slots 0-8)
- Breach Mace identified by:
  - Custom name containing "breach" (case-insensitive)
  - OR NBT enchantment tag containing "breach"

**Mechanics:**
- Uses client-side swing detection via Mixin
- Non-blocking thread handles item swap delays
- Cooldown prevents spam and respects Mace attack speed

---

### 2. Auto Stunslam ⚡

**How it works:**
- Monitors player blocking status (holding shield) + falling state
- Detects when player falls while blocking (fall distance > 0.5 blocks)
- Automatically switches to **Density Mace** when landing detected
- Executes mace swing at precise moment before ground impact
- Returns to previous held item after attack

**Requirements:**
- Player must be blocking with shield
- Player must be falling (fallDistance > 0 && increasing)
- At least one Density Mace in hotbar
- Density Mace identified by:
  - Custom name containing "density" (case-insensitive)
  - OR NBT enchantment tag containing "density"

**Mechanics:**
- Fall distance tracking detects landing moment
- Smart mace selection picks highest Density level
- 300ms delay between item swap and previous item restoration
- Can be toggled on/off with keybind (default: U)

**State Machine:**
```
Not Blocking → Blocking → Blocking + Falling → Landing Detected → Execute Stunslam → Reset
```

---

### 3. Shield Drain 🛡️

**How it works:**
- Automatically attacks shields with **Density Mace**
- Repeats attack based on configurable tick delay
- Each hit drains 1 durability from target's shield
- Requires 337 hits to fully break a shield

**Requirements:**
- Player must hold Density Mace in main hand
- Feature must be enabled in settings
- Target must have shield (detected via attack feedback)

**Mechanics:**
- Tick counter increments every client tick
- Attack executed every N ticks (configurable: 1-20 ticks)
- Delay of 3 ticks = ~15 hits per second (respects Minecraft's 20 ticks/second)
- Scales with attack speed enchantments

**Durability Calculator:**
```
Shield Durability: 337 points
Damage per hit: 1 point (melee attacks)
Hits to break: 337 / 1 = 337 hits
With 15 hits/sec: 337 / 15 ≈ 22.5 seconds to break shield
```

---

## Technical Architecture

### Project Structure

```
mcautomacebreach/
├── src/main/java/com/flappyburdddd/mcautomacebreach/
│   ├── McAutomaceBreachMod.java          (Main mod entry point)
│   ├── client/
│   │   ├── McAutomaceBreachClient.java   (Client initialization)
│   │   ├── config/
│   │   │   └── ModConfig.java            (Config persistence)
│   │   ├── handler/
│   │   │   ├── EventHandler.java         (Core game logic)
│   │   │   └── CommandHandler.java       (Chat commands)
│   │   ├── util/
│   │   │   └── InventoryUtil.java        (Inventory & NBT parsing)
│   │   └── screen/
│   │       └── SettingsScreen.java       (GUI settings)
│   └── mixin/
│       ├── PlayerEntityMixin.java        (Server-side hooks)
│       └── ClientPlayerEntityMixin.java  (Client-side swing detection)
├── src/main/resources/
│   ├── fabric.mod.json                   (Mod metadata)
│   ├── mcautomacebreach.mixins.json      (Mixin config)
│   ├── mcautomacebreach.accesswidener    (Access widening)
│   └── lang/en_us.json                   (Language strings)
├── build.gradle                          (Build configuration)
├── gradle.properties                     (Version & dependencies)
└── settings.gradle                       (Gradle settings)
```

### Event Flow

#### AutoBreachSwap Flow
```
PlayerEntity.swingHand(Hand.MAIN_HAND)
    ↓
[Mixin Hook] ClientPlayerEntityMixin.onClientSwingHand()
    ↓
EventHandler.onPlayerAttack(client)
    ↓
Check if holding sword → Find best Breach Mace → Perform swap
    ↓
Inventory.selectedSlot = maceSlot
player.swingHand(Hand.MAIN_HAND)
    ↓
[Async] Thread.sleep(500ms) → Return to original slot
```

#### Stunslam Flow
```
ClientTickEvents.END_CLIENT_TICK
    ↓
EventHandler.onClientTick(client)
    ↓
handleStunSlamDetection(player)
    ↓
Check: wasBlocking && isCurrentlyBlocking && fallDistance detected
    ↓
executeStunslam(player)
    ↓
Find Density Mace → Switch → Swing → Async return
```

#### Shield Drain Flow
```
ClientTickEvents.END_CLIENT_TICK
    ↓
EventHandler.onClientTick(client)
    ↓
handleShieldDrain(player)
    ↓
Check if holding Density Mace → Increment shieldDrainTickCounter
    ↓
If counter >= configuredDelay: player.swingHand() → Reset counter
```

### NBT Enchantment Detection

**NBT Structure Example:**
```json
{
  "Enchantments": [
    {
      "id": "namespace:breach",
      "lvl": 3
    }
  ],
  "display": {
    "Name": "{\"text\":\"Breach Mace\"}"  (Optional)
  }
}
```

**Detection Priority:**
1. Custom name contains keyword (case-insensitive)
2. NBT Enchantments list contains enchantment ID
3. Level extracted from NBT enchantment lvl value

---

## Configuration Guide

### Config File Location

**Windows:**
```
%APPDATA%/.minecraft/config/mcautomacebreach/config.json
```

**Linux:**
```
~/.minecraft/config/mcautomacebreach/config.json
```

**macOS:**
```
~/Library/Application Support/minecraft/config/mcautomacebreach/config.json
```

### Config File Format

```json
{
  "autoBreachSwapEnabled": false,
  "autoStunSlamEnabled": false,
  "shieldDrainEnabled": false,
  "shieldDrainDelayTicks": 3
}
```

### Configuration Options

| Option | Type | Default | Range | Description |
|--------|------|---------|-------|-------------|
| `autoBreachSwapEnabled` | boolean | false | true/false | Enable AutoBreachSwap feature |
| `autoStunSlamEnabled` | boolean | false | true/false | Enable Auto Stunslam feature |
| `shieldDrainEnabled` | boolean | false | true/false | Enable Shield Drain feature |
| `shieldDrainDelayTicks` | int | 3 | 1-20 | Ticks between Shield Drain hits |

**Delay to HPS (Hits Per Second) Conversion:**
- 1 tick delay = 20 hits/sec
- 3 ticks delay = ~6.67 hits/sec
- 5 ticks delay = 4 hits/sec
- 10 ticks delay = 2 hits/sec
- 17 ticks delay = ~1.18 hits/sec (matches Mace attack speed)

---

## Debugging & Troubleshooting

### Enable Debug Logging

**Edit `log4j2.xml`:**
```xml
<Logger name="com.flappyburdddd.mcautomacebreach" level="debug" />
<Logger name="mcautomacebreach" level="debug" />
```

**Then check logs at:**
```
.minecraft/logs/latest.log
```

### Common Issues

#### 1. AutoBreachSwap not working

**Checklist:**
- [ ] Mod is enabled and loaded
- [ ] AutoBreachSwap toggle is ON in settings (`/helb`)
- [ ] Holding a sword (check item name)
- [ ] Breach Mace in hotbar with "breach" in name/enchantments
- [ ] Swing the sword (don't just hold it)

**Debugging:**
```
# Check mod initialization
Grep logs for: "[McAutomaceBreach] Client initialized successfully!"

# Check mace detection
Grep logs for: "[McAutomaceBreach] BreachSwap executed"
```

#### 2. Stunslam not triggering

**Checklist:**
- [ ] Stunslam toggle ON (U key or settings)
- [ ] Shield in off-hand
- [ ] Density Mace in hotbar
- [ ] Actually falling (not just jumping)
- [ ] Fall distance > 0.5 blocks

**Debugging:**
```
# Check stunslam detection
Grep logs for: "[McAutomaceBreach] Stunslam executed!"

# Check density mace detection
Grep logs for: "No Density Mace found for Stunslam"
```

#### 3. Shield Drain too fast/slow

**Solution:** Adjust `shieldDrainDelayTicks` in `/helb` settings
- Lower = faster (1-3 ticks recommended)
- Higher = slower

---

## Development Guide

### Building the Mod

```bash
# Clone repository
git clone https://github.com/FlappyBURDDD/mcautomacebreach
cd mcautomacebreach

# Build JAR
./gradlew build

# Output: build/libs/mcautomacebreach-1.0.0.jar
```

### Adding New Features

#### Step 1: Extend EventHandler
```java
public static void onClientTick(MinecraftClient client) {
    // Add your tick logic here
    handleNewFeature(client.player);
}

private static void handleNewFeature(ClientPlayerEntity player) {
    // Feature implementation
}
```

#### Step 2: Add Config Options
```java
public static class ConfigData {
    public boolean newFeatureEnabled = false;  // Add new config
}
```

#### Step 3: Add GUI Toggle
```java
// In SettingsScreen.init()
this.newFeatureButton = this.addDrawableChild(
    ButtonWidget.builder(...)
        .build()
);
```

### Testing

**Local Development:**
```bash
# Run Minecraft dev client
./gradlew runClient

# Mod will be loaded automatically
```

**Testing Checklist:**
- [ ] Feature activates when enabled
- [ ] Feature deactivates when disabled
- [ ] Settings persist after restart
- [ ] No console errors or exceptions
- [ ] Performance impact minimal

---

## Performance Notes

### Client-Side Only
- **Pro:** Works on vanilla servers without mods
- **Con:** Server-side anti-cheat may flag detection

### Tick Rate
- Each feature runs every client tick (20 ticks/second)
- Negligible performance impact
- Optimized NBT parsing with early returns

### Thread Safety
- Item swaps use non-blocking threads
- Main game loop not blocked
- Config file I/O async

---

## Version History

**v1.0.0** (Current)
- AutoBreachSwap with Breach Mace detection
- Auto Stunslam with falling detection
- Shield Drain with configurable tick delay
- GUI settings via `/helb` command
- NBT-based enchantment parsing
- JSON config persistence

---

## License

MIT License - See LICENSE file

## Contributing

Contributions welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
