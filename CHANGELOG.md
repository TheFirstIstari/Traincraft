# Changelog

## 5.0.0-alpha3 (in development)

### Added
- Drivable rolling stock: W/S input applies throttle/brake along the cart's heading.
- Fuel gating: locomotives only accelerate when they have the fuel/water/heat they need.
- Steam locomotive item interactions: water bucket -> water tank, canister of water -> water tank, coal/charcoal -> burn slot, sneak+click -> whistle.
- Diesel locomotive (`LocomotiveDieselSmall`): new entity, renderer, spawn egg, recipe, lang, engine sound event.
- `LocomotiveDiesel` base API class with a fuel tank that consumes diesel or refined fuel mB-by-mB while moving.
- `ItemCanister` is now a real fluid container (CUSTOM_DATA component) with right-click pick-up and sneak+right-click empty.
- Distillery `CONTAINER_INPUT_SLOT` -> `CONTAINER_OUTPUT_SLOT` flow: empty canisters/buckets are filled from the internal fluid tank.
- Connector item now actually links rolling stock (handleEntityClick wired into `AbstractRollingStock.handlePlayerClickWithItem`).
- Coupling physics: spring force pulls follower toward `COUPLING_DISTANCE` behind leader; couplings break beyond 6 blocks to avoid teleporting after derailments.
- Persistent coupling: next/previous link UUIDs stored in NBT and resolved on tick once both endpoints load.
- Driver status HUD: action-bar updates every 10 ticks with steam temperature/water/burn or diesel fuel percentage.
- Freight cart chest: 27-slot `SimpleContainer` opened on sneak + empty-hand right-click, contents drop on `KILLED` removal, persisted via NBT.
- Custom rail blocks `traincraft:rail_steel` (1.5x speed) and `traincraft:rail_copper` (1.2x speed); rolling stock checks the rail beneath it each tick and scales `getMaxSpeed`/`getAcceleration` accordingly.
- Custom 3x3 Train Workbench recipe type with output slot, automatic preview, and ingredient consumption on take.
- 73 recipe JSONs spanning vanilla crafting, smelting/blasting, distillery (oil chain), three assembly-table tiers, and the train workbench.
- Track Layer item + curved rail block + curved-rail tile entity for placing cubic Bezier track segments between two existing rails.
  - Two-click placement workflow with control points pushed along each endpoint's rail tangent.
  - 3D Bezier so curves between rails at different y-values arc smoothly.
  - `AbstractRollingStock.moveAlongTrack` overrides vanilla rail-shape constraint with curve-following motion that snaps to the curve, advances along its parameter, and rotates the cart to the curve tangent.
  - `TileTCCurvedRailRenderer` BER draws each block as a textured strip following its `[tStart, tEnd]` sub-arc.
  - Sneak + right-click on a curved rail with the Track Layer removes every segment of that curve.

### Fixed
- `DistilleryRecipe.getIngredients()` returned an empty `NonNullList` (used `.of(default)` instead of `.create()`), causing JEI to crash with `ArrayIndexOutOfBoundsException` on every distillery recipe.
- Many uncommitted NeoForge 1.21 API migration cleanups (datagen, JEI, dyeable armor, guide book, tile entities, sounds) finally landed.

### Documentation
- README, Roadmap, and this changelog refreshed to reflect the alpha3 feature set.
- `SOUNDS.md` documenting the sound system moved out of the runtime resources directory so Minecraft no longer logs an "Invalid path in pack" warning.

## 5.0.0-alpha1

### Added
- Complete rewrite for NeoForge 1.21.1
- 17 blocks with full functionality (distillery, assembly tables, furnace, energy blocks, ores)
- 78+ items including all materials, train parts, and tools
- 14 armor pieces across 4 sets
- 3 fluids (oil, refined fuel, diesel) with source and flowing variants
- 10 tile entities with full behavior
- Rolling stock entity framework with linking, skins, and seats
- LocomotiveSteamSmall entity with renderer and spawn egg
- World generation for copper ore, petrol ore, and oil sand
- 4 recipe types (distillery, assembly table, train workbench, smelting)
- All legacy GUI textures (40) converted and in use
- All legacy block/item textures (400+) converted and in use
- Full English localization
- Energy system (RF/FE) for all power blocks
- GitHub Actions CI/CD pipeline
