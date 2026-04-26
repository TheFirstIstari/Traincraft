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

#### Blocks (20)
- **Processing**: Distillery, Assembly Tables (I/II/III), Train Workbench, Open Hearth Furnace
- **Power Generation**: Water Wheel, Wind Mill, Diesel Generator, Battery
- **Decorative/Utility**: Stopper (Train Buffer), Bridge Pillar, Lantern, Ballast
- **Signals**: Signal, Switch Stand
- **Ores**: Copper Ore, Petrol Ore, Oil Sand (with world generation)
- **Rails**: Steel Rail (1.5x speed), Copper Rail (1.2x speed), Curved Rail (multi-block Bezier)

#### Items (80+)
- **Materials**: Steel Ingot, Steel Dust, Coal Dust, Graphite, Copper Ingot, Plastic, and more
- **Train Parts**: 28 components for building locomotives (bogie, boiler, cabin, engine, etc.)
- **Tools**: Wrench, Canister (real fluid storage), Connector, Skin Changer, Chunk Loader Activator, **Track Layer**
- **Special Items**: Guide Book, ATO Card, Wireless Transmitter
- **Spawn Eggs**: Locomotive Steam Small, Locomotive Diesel Small, Freight Cart, Passenger Cart

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
- **AbstractRollingStock**: Base entity for all trains with player-input throttle, fuel-gated acceleration, coupling physics, persistent links, and per-rail speed multipliers
- **LocomotiveSteamSmall**: Steam locomotive with burn system, water tank, temperature mechanics, and whistle on sneak+click
- **LocomotiveDieselSmall**: Diesel locomotive with fuel tank that consumes diesel/refined fuel while moving
- **EntityBogie**: Wheel set for rolling stock
- **EntityFreightCart**: Freight wagon with a 27-slot chest opened on sneak + empty-hand right-click
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

### 5.0.0-alpha3 / Current Development
- **Drivable trains**: W/S throttle on rolling stock with fuel-gated acceleration; sneak+click for steam whistle.
- **Diesel locomotive**: Full entity with fuel tank, recipe, spawn egg, renderer, and engine sound event.
- **Working canister**: Real handheld fluid container (CUSTOM_DATA component); right-click fluid sources to fill, sneak+click to empty.
- **Distillery container slot**: Place an empty canister or bucket to extract fluid from the internal tank.
- **Coupling physics**: Linked rolling stock pull each other via a spring force, with persistent NBT-backed links that survive world reloads.
- **Driver HUD**: Live action-bar status showing steam temperature/water/burn or diesel fuel level.
- **Freight cart chest**: 27-slot inventory opens on sneak + empty-hand right-click, drops on death.
- **Custom rails**: Steel Rail (1.5x speed), Copper Rail (1.2x), with per-tier multipliers applied to TC rolling stock.
- **Bezier curve tracks**: Track Layer item + Curved Rail block. Two-click placement between rails creates a smooth cubic Bezier curve. Trains follow the curve via `moveAlongTrack` override; sneak+click removes the entire curve.
- **Train Workbench recipe type**: Custom 3x3 positional recipe type with output slot and automatic preview.
- **Recipe set**: 73+ recipe JSONs covering vanilla crafting, smelting/blasting, distillery (oil chain), three assembly-table tiers, and the train workbench.

### 5.0.0-alpha2
- **JEI Compatibility**: Full integration with JEI for recipes and crafting systems (TrainWorkbench / AssemblyTable / Distillery categories).
- **Armor Dyeing**: Customizable armor colors via the `DyeableArmorItem` interface.
- **Sound System**: TCSounds registry with whistle/engine/wheels/processing/activation events.
- **Guide Book**: In-game documentation (`GuideBookScreen` + `ItemGuide` + `ContainerGuideBook`).
- **Recipe data generation**: Automated JSON generation for Train Workbench, Assembly Table, Distillery, and standard furnace recipes.

## Playing the Mod (alpha3 controls)

| Action | How |
|--------|-----|
| Mount a cart | Right-click with empty hand |
| Throttle / brake / reverse | `W` (throttle), `S` (brake then reverse) while driving |
| Steam whistle | Sneak + right-click steam locomotive |
| Refill steam water | Right-click loco with water bucket *or* canister of water |
| Add coal | Right-click loco with coal/charcoal |
| Refill diesel fuel | Right-click loco with canister of diesel/refined fuel |
| Open freight cart inventory | Sneak + empty-hand right-click |
| Couple two carts | Right-click each in turn with the Connector |
| Lay a curved track | Right-click two existing rails with the Track Layer |
| Remove a curved track | Sneak + right-click on a curved rail with the Track Layer |
| Cancel a Track Layer selection | Sneak + right-click in air |

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
Traincraft/
├── Traincraft-source/      # Source code directory
│   ├── src/main/
│   │   ├── java/traincraft/
│   │   │   ├── blocks/           # Block implementations
│   │   │   ├── compat/          # JEI compatibility
│   │   │   ├── datagen/         # Recipe data generation
│   │   │   ├── entity/          # Entity implementations
│   │   │   ├── gui/             # UI screens
│   │   │   ├── items/           # Item implementations
│   │   │   ├── liquids/         # Fluid definitions
│   │   │   ├── network/        # Network menus
│   │   │   ├── recipe/         # Recipe types
│   │   │   ├── renderer/       # Entity/item renders
│   │   │   ├── tile/           # Tile entities
│   │   │   ├── world/          # World generation
│   │   │   ├── Traincraft.java # Main mod class
│   │   │   └── TCSounds.java   # Sound registry
│   │   └── resources/
│   │       └── assets/traincraft/
│   │           ├── lang/        # Localization
│   │           ├── models/     # Item/block models
│   │           ├── sounds/     # Sound files
│   │           └── textures/   # Textures
│   ├── build.gradle           # Build configuration
│   └── gradle.properties       # Gradle properties
├── Traincraft-4.4.1_020-CE_7.1.jar  # Legacy mod JAR (1.12.2)
├── LICENSE.md               # LGPL-3.0 License
└── README.md                # This file
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

Contributions are welcome! Please see [CONTRIBUTING.md](Traincraft-source/CONTRIBUTING.md) for guidelines.