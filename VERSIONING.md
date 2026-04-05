# Traincraft Versioning Plan

## Version Scheme
Using **SemVer** with pre-release versioning: `MAJOR.MINOR.PATCH[-PRERELEASE]`

## Versioning Line

| Version | Minecraft | NeoForge | Status | Notes |
|---------|-----------|----------|--------|-------|
| 4.4.1 | 1.7.10 | Forge | Legacy | Last 1.7.10 release |
| 4.4.1_022 | 1.12.2 | Forge | Legacy | Last 1.12.2 release |
| **5.0.0-alpha1** | **1.21.1** | **21.1.2** | **In Development** | NeoForge rewrite |

## Current Status (5.0.0-alpha1)

### Completed (~95% feature parity)
- ✅ 17 blocks with full functionality
- ✅ 78+ items including all materials, train parts, and tools
- ✅ 14 armor pieces across 4 sets
- ✅ 3 fluids with source and flowing variants
- ✅ 10 tile entities (6 with full GUI/behavior)
- ✅ Rolling stock entity framework with linking, skins, seats
- ✅ LocomotiveSteamSmall with renderer and spawn egg
- ✅ World generation (copper ore, petrol ore, oil sand)
- ✅ 4 recipe types (distillery, assembly table, train workbench, smelting)
- ✅ All legacy GUI textures (40) converted and in use
- ✅ All legacy block/item textures (400+) converted and in use
- ✅ Full English localization
- ✅ GitHub Actions CI workflow

### Exceeds Legacy
- Open Hearth Furnace (was empty placeholder, now full smelting)
- Energy blocks (all 4 generate/store RF, were empty in legacy)
- LocomotiveSteamSmall (was commented out, now active with renderer)
- Multi-face block textures (proper top/front/side variants)
- Modern NeoForge APIs (capabilities, DeferredRegister, data generation)

### Remaining for 5.0.0-alpha1
- [ ] Guide book system
- [ ] Sounds
- [ ] Train workbench recipe JSONs
- [ ] Armor dyeing
- [ ] JEI compatibility

## Release Plan

### Phase 1: Alpha (5.0.0-alpha1)
- Target: 1.21.1
- Focus: Core functionality at feature parity with legacy
- All blocks, items, fluids, tile entities, entities
- Basic rolling stock with renderer
- World generation
- Recipe system

### Phase 2: Alpha (5.0.0-alpha2+)
- Guide book system
- Sounds
- Armor dyeing
- More rolling stock types
- JEI compatibility

### Phase 3: Beta (5.0.0-beta1+)
- Feature freeze
- Focus on stability and bug fixes
- Performance optimization
- Multiplayer testing

### Phase 4: Stable (5.0.0)
- Full feature parity with 4.4.1_022
- Production ready
- CurseForge/Modrinth release

## Dependencies
- NeoForge 21.1.2 (for 1.21.1)
- Java 21+

## Distribution
- GitHub Releases
- CurseForge (via API or manual upload)
- Modrinth (optional)

## Build Artifacts
```
Traincraft-5.0.0-alpha1-1.21.1.jar
```
