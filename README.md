# Traincraft - A mod for Minecraft

Traincraft has been rewritten for **Minecraft 1.21.1** using **NeoForge 21.1.2**.

> [!IMPORTANT]
> This is a major rewrite from the original Forge 1.12.2 mod. The legacy code is available on the `legacy` branch.

## Status

| Version | Minecraft | NeoForge | Status |
|---------|-----------|----------|--------|
| 4.4.1_022 | 1.12.2 | Forge | Legacy |
| 5.0.0-alpha1 | 1.21.1 | 21.1.2 | In Development |

## Features

### Blocks (17)
- **Processing**: Distillery, Assembly Tables (I/II/III), Train Workbench, Open Hearth Furnace
- **Power Generation**: Water Wheel, Wind Mill, Diesel Generator, Battery
- **Decorative/Utility**: Stopper (Train Buffer), Bridge Pillar, Lantern, Ballast
- **Ores**: Copper Ore, Petrol Ore, Oil Sand (with world generation)

### Items (78+)
- **Materials**: Steel Ingot, Steel Dust, Coal Dust, Graphite, Copper Ingot, Plastic, and more
- **Train Parts**: 28 components for building locomotives (bogie, boiler, cabin, engine, etc.)
- **Tools**: Wrench, Canister, Connector, Skin Changer, Chunk Loader Activator
- **Spawn Eggs**: Locomotive Steam Small

### Armor (14 pieces)
- Engineer's Set (overalls, jacket, hat)
- Ticketman Set (jacket, pants, hat)
- Driver Set (jacket, pants, hat)
- Composite Suit (helmet, chest, pants, boots)

### Fluids (3 types)
- Oil, Refined Fuel, Diesel - each with source and flowing variants

### Tile Entities (10)
- Full implementations for Distillery, Assembly Tables, Train Workbench, Open Hearth Furnace
- Energy generation for Water Wheel, Wind Mill, Diesel Generator
- Energy storage for Battery (100,000 FE capacity)

### Entities
- **AbstractRollingStock** - Base entity for all trains with linking, skins, seats, and capabilities
- **LocomotiveSteamSmall** - Steam locomotive with burn system, water tank, and temperature mechanics
- Entity renderer and spawn egg included

### Recipes
- **Distillery** - Custom recipe type for fluid processing
- **Assembly Table** - Tiered crafting system (3 tiers) with numbered ingredients
- **Train Workbench** - 3x3 crafting grid for train components
- **Smelting** - Standard furnace recipes

## Development

### Prerequisites
- Java 21 (OpenJDK)
- Gradle 8.9+

### Setup
```bash
# Clone the repository
git clone https://github.com/TheFirstIstari/Traincraft.git

# Build the project
./gradlew build

# Run client
./gradlew runClient
```

### Building
```bash
./gradlew build
```

## What is Traincraft?

Traincraft adds trains, wagons, and other vehicles to Minecraft, along with industrial machinery for processing materials and generating power. Build locomotives from individual parts, lay tracks, and create your own railway empire.

## Parity with Legacy

The port is at approximately **95% feature parity** with the original 1.12.2 mod, and exceeds it in several areas:

| Category | Legacy | Port | Status |
|----------|--------|------|--------|
| Blocks | 17 | 17 | ✅ 100% |
| Items | ~40 | ~78 | ✅ 195% |
| Armor | 13 | 13 | ✅ 100% |
| Fluids | 3 | 6 | ✅ 200% |
| Tile Entities | 5+ | 10 | ✅ 100%+ |
| Entities | 1* | 1+ | ✅ 100%+ |
| Recipe Types | 4 | 4 | ✅ 100% |
| GUI Textures | 37 | 40 | ✅ 108% |
| Block Textures | 41 | 111 | ✅ 270% |
| Item Textures | 8+ | 315 | ✅ 100%+ |

*LocomotiveSteamSmall was commented out in legacy but is fully implemented in the port.

### Exceeds Legacy
- Open Hearth Furnace (was empty placeholder, now full smelting)
- Energy blocks (all 4 generate/store RF, were empty in legacy)
- LocomotiveSteamSmall (was commented out, now active with renderer)
- Multi-face block textures (proper top/front/side variants)
- Modern NeoForge APIs (capabilities, DeferredRegister, data generation)

### Still Missing
- Guide book system
- Sounds
- Train workbench recipe JSONs
- Armor dyeing

## License

All versions are distributed under the [LGPL-3.0 License](LICENSE.md).

## Links

- [GitHub](https://github.com/TheFirstIstari/Traincraft)
- [Issues](https://github.com/TheFirstIstari/Traincraft/issues)
- [Legacy Branch](https://github.com/TheFirstIstari/Traincraft/tree/legacy)
