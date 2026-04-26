# Traincraft

A Minecraft mod that adds trains, wagons, and industrial machinery to your world. Build locomotives from individual parts, lay tracks, and create your own railway empire.

## Status

| Version | Minecraft | NeoForge | Status |
|---------|-----------|----------|--------|
| 4.4.1_022 | 1.12.2 | Forge 1.12.2 | Legacy |
| **5.0.0** | **1.21.1** | **NeoForge 21.1.2** | **Current** |

> [!IMPORTANT]
> This is a complete rewrite of the original Traincraft mod for Minecraft 1.21.1 using NeoForge. The legacy 1.12.2 code is available on the `legacy` branch.

### Current Features

#### Blocks (17)
- **Processing**: Distillery, Assembly Tables (I/II/III), Train Workbench, Open Hearth Furnace
- **Power Generation**: Water Wheel, Wind Mill, Diesel Generator, Battery
- **Decorative/Utility**: Stopper (Train Buffer), Bridge Pillar, Lantern, Ballast
- **Signals**: Signal, Switch Stand
- **Ores**: Copper Ore, Petrol Ore, Oil Sand (with world generation)

#### Items (78+)
- **Materials**: Steel Ingot, Steel Dust, Coal Dust, Graphite, Copper Ingot, Plastic, and more
- **Train Parts**: 28 components for building locomotives (bogie, boiler, cabin, engine, etc.)
- **Tools**: Wrench, Canister, Connector, Skin Changer, Chunk Loader Activator
- **Special Items**: Guide Book, ATO Card, Wireless Transmitter
- **Spawn Eggs**: Locomotive Steam Small

#### Armor (14 pieces - Dyeable)
- Engineer's Set (overalls, jacket, hat)
- Ticketman Set (jacket, pants, hat)
- Driver Set (jacket, pants, hat)
- Composite Suit (helmet, chest, pants, boots)

#### Fluids (3 types)
- Oil, Refined Fuel, Diesel - each with source and flowing variants

#### Tile Entities (10+)
- Full implementations for Distillery, Assembly Tables, Train Workbench, Open Hearth Furnace
- Energy generation for Water Wheel, Wind Mill, Diesel Generator
- Energy storage for Battery (100,000 FE capacity)

#### Entities
- **AbstractRollingStock**: Base entity for all trains with linking, skins, seats, and capabilities
- **LocomotiveSteamSmall**: Steam locomotive with burn system, water tank, and temperature mechanics
- **EntityBogie**: Wheel set for rolling stock
- **EntityFreightCart**: Generic freight wagon
- **EntityPassengerCart**: Passenger wagon

#### Recipe Types (4)
- **Distillery**: Custom recipe type for fluid processing
- **Assembly Table**: Tiered crafting system (3 tiers) with numbered ingredients
- **Train Workbench**: 3x3 crafting grid for train components
- **Smelting**: Standard furnace recipes

#### Integrations
- **JEI**: Full compatibility with recipe viewing and crafting assistance
- **Curios**: Support for accessory slots

#### Gameplay Systems
- **Energy**: RF/FE-based power generation and storage
- **Sound**: Atmospheric sounds for machinery and vehicles
- **Guide Book**: In-game documentation and crafting recipes
- **Armor Dyeing**: Customizable armor colors

### Feature Parity

The port has achieved approximately **95% feature parity** with the original 1.12.2 mod:

| Category | Legacy | Port | Status |
|----------|--------|------|--------|
| Blocks | 17 | 17 | 100% |
| Items | ~40 | ~78+ | 195% |
| Armor | 13 | 14 | 108% |
| Fluids | 3 | 6 | 200% |
| Tile Entities | 5+ | 10+ | 200% |
| Entities | 1* | 5 | 500% |
| Recipe Types | 4 | 4 | 100% |
| GUI Textures | 37 | 40 | 108% |
| Block Textures | 41 | 111 | 270% |
| Item Textures | 8+ | 315 | 100%+ |

\* LocomotiveSteamSmall was commented out in legacy but is fully implemented in the port.

### Exceeds Legacy
- Open Hearth Furnace (was empty placeholder, now fully functional smelting)
- Energy blocks (all 4 generate/store RF, were empty in legacy)
- LocomotiveSteamSmall (was commented out, now active with renderer)
- Multi-face block textures (proper top/front/side variants)
- Armor dyeing system (new)
- JEI integration (new)
- Sound system (new)
- Guide book (new)
- Modern NeoForge APIs (capabilities, DeferredRegister, data generation)

## What's New (Recent Commits)

### 5.0.0-alpha2 / Current Development
- **JEI Compatibility**: Full integration with JEI for recipes and crafting systems
  - TrainWorkbenchRecipeCategory
  - AssemblyTableRecipeCategory  
  - DistilleryRecipeCategory
- **Armor Dyeing**: Implemented DyeableArmorItem interface for customizing armor colors
- **Sound System**: TCSounds registry with atmospheric and gameplay sounds
  - Signal activation sounds
  - Distillery processing sounds
  - Assembly table crafting sounds
  - Locomotive engine and steam whistle sounds
- **Guide Book System**: Complete in-game documentation
  - GuideBookScreen UI
  - ItemGuide book item
  - ContainerGuideBook container
- **Recipe Data Generation**: Automated JSON generation for:
  - Train Workbench recipes
  - Assembly Table recipes
  - Distillery recipes
  - Standard furnace recipes

## Development

### Prerequisites
- Java 21 (OpenJDK 21+)
- Gradle 8.9+
- Minecraft 1.21.1
- NeoForge 21.1.2

### Setup

```bash
# Clone the repository
git clone https://github.com/TheFirstIstari/Traincraft.git
cd Traincraft/Traincraft-source

# Build the project
./gradlew build

# Run development client
./gradlew runClient

# Run development server
./gradlew runServer
```

### Building for Distribution

```bash
# Build the mod JAR
./gradlew build

# The output will be in build/libs/
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with detailed output
./gradlew test --info
```

## Project Structure

```
Traincraft-source/
├── src/main/
│   ├── java/traincraft/
│   │   ├── blocks/           # Block implementations
│   │   ├── compat/          # JEI compatibility
│   │   ├── datagen/         # Recipe data generation
│   │   ├── entity/          # Entity implementations
│   │   ├── gui/             # UI screens
│   │   ├── items/           # Item implementations
│   │   ├── liquids/         # Fluid definitions
│   │   ├── network/        # Network menus
│   │   ├── recipe/         # Recipe types
│   │   ├── renderer/       # Entity/item renders
│   │   ├── tile/           # Tile entities
│   │   ├── world/          # World generation
│   │   ├── Traincraft.java  # Main mod class
│   │   └── TCSounds.java   # Sound registry
│   └── resources/
│       └── assets/traincraft/
│           ├── lang/        # Localization
│           ├── models/     # Item/block models
│           ├── sounds/    # Sound files
│           └── textures/   # Textures
├── build.gradle            # Build configuration
└── gradle.properties       # Gradle properties
```

## License

All versions of Traincraft are distributed under the **GNU Lesser General Public License v3.0 (LGPL-3.0)**.

See [LICENSE.md](LICENSE.md) for the full license text.

### Key License Terms
- You may redistribute this mod
- You may modify it for personal use
- Commercial use is permitted
- Source code must be made available if you distribute modifications
- Attribution is required

## Links

- [GitHub Repository](https://github.com/TheFirstIstari/Traincraft)
- [Issue Tracker](https://github.com/TheFirstIstari/Traincraft/issues)
- [Legacy Branch (1.12.2)](https://github.com/TheFirstIstari/Traincraft/tree/legacy)
- [NeoForge](https://neoforged.net/)
- [JEI Mod](https://jeimod.github.io/)

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.