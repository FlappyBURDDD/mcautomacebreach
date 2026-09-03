# McAutomaceBreach - Minecraft Fabric Mod

A Fabric mod for Minecraft Java 1.20.4 that adds PVP automation features for Mace combat techniques.

## Features

### 🎯 AutoBreachSwap
- Automatically swaps to Breach Mace when attacking with sword
- Performs the breach swap hit
- Returns to sword for next attack
- Requires Breach Mace in hotbar

### ⚡ Auto Stunslam  
- Automatically executes Stunslam technique when conditions are met
- Toggle with keybind (default: U)
- Requires Density Mace in hotbar
- Works when player is blocking and falling

### 🛡️ Shield Drain
- Automatically hits shields with Density Mace to drain durability
- Configurable hit delay (in ticks)
- Requires Density Mace in hotbar

## Installation

1. Install Fabric Loader (version 0.14.22+)
2. Install Fabric API
3. Download the latest mod JAR from releases
4. Place JAR in your `mods` folder
5. Launch Minecraft

## Configuration

### GUI Settings
- Open settings with `/helb` command
- Toggle AutoBreachSwap, AutoStunslam, and Shield Drain
- Configure Shield Drain delay (1-20 ticks)

### Keybinds
- **U** - Toggle Auto Stunslam (configurable in settings)
- **H** - Open Settings GUI (configurable in settings)

## Config File

Configuration is saved to `config/mcautomacebreach/config.json`

```json
{
  "autoBreachSwapEnabled": false,
  "autoStunSlamEnabled": false,
  "shieldDrainEnabled": false,
  "shieldDrainDelayTicks": 3
}
```

## Requirements

- Minecraft Java 1.20.4
- Fabric Loader 0.14.22+
- Fabric API
- Java 21+

## Building

```bash
./gradlew build
```

Output: `build/libs/mcautomacebreach-1.0.0.jar`

## License

MIT License - See LICENSE file for details

## Author

FlappyBURDDD

## Contributing

Contributions are welcome! Please create a pull request with your improvements.
