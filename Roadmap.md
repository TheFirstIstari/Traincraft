# Traincraft Development Roadmap

This file tracks the development progress of the NeoForge 1.21.1 rewrite.

## Current Status: 5.0.0-alpha1 (Released)

### Completed Features (~95% parity with legacy 1.12.2)

- [x] NeoForge 1.21.1 project setup with ModDevGradle
- [x] GitHub Actions CI/CD workflow with automatic releases
- [x] All 17 blocks implemented with full functionality
- [x] All 78+ items registered
- [x] All 14 armor pieces with custom materials
- [x] All 3 fluids with FluidType + source/flowing variants
- [x] All 10 tile entities with tickers and NBT
- [x] Rolling stock entity framework (AbstractRollingStock)
- [x] LocomotiveSteamSmall with renderer and spawn egg
- [x] World generation (copper ore, petrol ore, oil sand)
- [x] 4 recipe types (distillery, assembly table, train workbench, smelting)
- [x] All legacy GUI textures (40) converted and in use
- [x] All legacy block/item textures (400+) converted and in use
- [x] Full English localization
- [x] Energy system (RF/FE) for all power blocks

### Remaining for 5.0.0-alpha1

- [ ] Guide book system
- [ ] Sounds
- [ ] Train workbench recipe JSONs
- [ ] Armor dyeing (ticketman/driver sets)
- [ ] JEI compatibility

---

## Future Feature Plan

### Priority 1: Core Gameplay (High Impact, Low-Medium Effort)

| Feature | Description | Source | Effort |
|---------|-------------|--------|--------|
| **Custom Train Physics** | Apply acceleration, braking, max speed to AbstractRollingStock movement | 1.7 SpeedHandler | ⭐ |
| **Bogie/Trailer Entities** | Extend AbstractRollingStock for bogies and trailers | 1.7 EntityBogie | ⭐ |
| **Signal Blocks** | Train signals with redstone output and state | 1.7 BlockSignal | ⭐ |
| **Switch Stand Blocks** | Track switches with direction state | 1.7 BlockSwitchStand | ⭐ |
| **More Rolling Stock Types** | Diesel locomotives, freight carts, passenger carts, tenders | Legacy + 1.7 | ⭐⭐ |
| **Train Workbench Recipes** | JSON recipes for train component crafting | Legacy | ⭐ |
| **Assembly Table Recipes** | JSON recipes for assembly table crafting | Legacy | ⭐ |
| **Distillery Recipes** | JSON recipes for fluid processing | Legacy | ⭐ |

### Priority 2: Quality of Life (Medium Impact, Medium Effort)

| Feature | Description | Source | Effort |
|---------|-------------|--------|--------|
| **Guide Book System** | In-game documentation with pages | Legacy | ⭐⭐ |
| **Sounds** | Train, block, and entity sound effects | Legacy | ⭐⭐ |
| **Armor Dyeing** | Color customization for ticketman/driver armor | Legacy | ⭐⭐ |
| **Recipe Book Item** | In-game recipe reference book | 1.7 ItemRecipeBook | ⭐⭐ |
| **EntityAIFearHorn** | Mobs flee from train horns | 1.7 EntityAIFearHorn | ⭐⭐ |
| **JEI Compatibility** | Recipe viewing for custom recipe types | Modern | ⭐⭐ |

### Priority 3: Advanced Features (High Impact, High Effort)

| Feature | Description | Source | Effort |
|---------|-------------|--------|--------|
| **Overhead Electric Lines** | Power lines for electric trains | 1.7 ItemOverheadLines | ⭐⭐⭐ |
| **Electric Trains** | Trains powered by overhead lines | 1.7 ElectricTrain | ⭐⭐⭐ |
| **Diesel/Steam Train Bases** | Specialized locomotive base classes | 1.7 DieselTrain, SteamTrain | ⭐⭐⭐ |
| **LiquidManager** | Advanced fluid management system | 1.7 LiquidManager | ⭐⭐⭐ |
| **MTC System** | Multi-Train Control networking | Legacy mtc/ | ⭐⭐⭐ |
| **Advancements** | Achievement system for train progression | Legacy | ⭐⭐⭐ |

### Priority 4: Major Rewrites (Very High Effort)

| Feature | Description | Source | Effort |
|---------|-------------|--------|--------|
| **Custom Track System** | Curved and sloped rails (BlockTCRail) | 1.7 BlockTCRail | ⭐⭐⭐⭐ |
| **Zeppelins** | Flying airship entities | 1.7 AbstractZeppelin | ⭐⭐⭐⭐ |
| **Rotative Digger** | Mining/excavation vehicle | 1.7 EntityRotativeDigger | ⭐⭐⭐⭐ |
| **PDM System** | Passenger Display Module | Legacy | ⭐⭐⭐⭐ |
| **Config System** | Runtime configuration | Modern | ⭐⭐⭐ |

---

## Version Plan

### 5.0.0-alpha2
- Custom train physics (acceleration, braking, max speed)
- Bogie/trailer entities
- Signal blocks
- Switch stand blocks
- Train workbench recipe JSONs
- Assembly table recipe JSONs
- Distillery recipe JSONs

### 5.0.0-alpha3
- More rolling stock types (diesel, freight, passenger)
- Guide book system
- Sounds
- Armor dyeing
- Recipe book item

### 5.0.0-beta1
- Feature freeze
- Overhead electric lines
- Electric trains
- JEI compatibility
- Stability and bug fixes
- Multiplayer testing

### 5.0.0 (Stable)
- Full feature parity with legacy 4.4.1_022
- Production ready
- CurseForge release
- Modrinth release

### Post 5.0.0
- Custom track system
- Zeppelins
- Rotative digger
- MTC system
- Advancements
- Config system

---

## Legacy Roadmap (1.12.2)

The original roadmap for the 1.12.2 version is preserved below for reference. Many items from it have been completed in this rewrite.

<details>
<summary>Original 1.12.2 Roadmap</summary>

- [x] Compilable for 1.12.2
- [x] New registry for Items, Blocks and more
- [x] Begin to develop a new API
- [ ] Implement all the items from 1.7.10
- [ ] Implement all the blocks from 1.7.10
- [ ] Implement driving physics for TrackAPI compatible rails
- [ ] Implement linked rolling stock
- [ ] Implementing GUIs for trains
- [ ] Implement train logic
- [ ] Adding more rolling stock from different types
- [ ] Adding chunk loading option for trains
- [ ] Implement Advancements and Sounds
- [ ] Implement new TAPI-Compatible tracks
- [ ] Implement the recipe book
- [ ] Implement all the rolling stock from 1.6.4

</details>
