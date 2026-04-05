# Traincraft Development Roadmap

This file tracks the development progress of the NeoForge 1.21.1 rewrite.

## Current Status: 5.0.0-alpha1 (In Development)

### Completed Features (~95% parity with legacy 1.12.2)

- [x] NeoForge 1.21.1 project setup with ModDevGradle
- [x] GitHub Actions CI workflow
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

### Future Versions

#### 5.0.0-alpha2
- [ ] Guide book system
- [ ] Sounds (train, block, entity sounds)
- [ ] Armor dyeing support
- [ ] More rolling stock types (diesel, electric, cargo, passenger)
- [ ] JEI plugin for custom recipes

#### 5.0.0-beta1
- [ ] Feature freeze
- [ ] Stability and bug fixes
- [ ] Multiplayer testing
- [ ] Performance optimization
- [ ] Documentation

#### 5.0.0 (Stable)
- [ ] Full feature parity with legacy 4.4.1_022
- [ ] Production ready
- [ ] CurseForge release
- [ ] Modrinth release

### Far Future (Post 5.0.0)

- [ ] Custom tracks/rails system
- [ ] MTC (Multi-Train Control) system
- [ ] PDM (Passenger Display Module) system
- [ ] Advanced train AI/routing
- [ ] Additional rolling stock from legacy (80+ vehicles)
- [ ] Advancements system
- [ ] Config system

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
