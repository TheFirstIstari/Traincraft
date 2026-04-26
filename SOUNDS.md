# Traincraft Sound System

This document covers the sound assets for the Traincraft mod. Actual `.ogg` files
should be dropped into `src/main/resources/assets/traincraft/sounds/<category>/`
matching the paths registered in `assets/traincraft/sounds.json`.

## Sound Categories

1. **Locomotive sounds**
   - `locomotive/steam/whistle.ogg`
   - `locomotive/steam/engine.ogg`
   - `locomotive/diesel/engine.ogg`

2. **Train movement sounds**
   - `train/wheels.ogg`

3. **Block interaction sounds**
   - `assembly_table/craft.ogg`
   - `distillery/process.ogg`
   - `signal/activate.ogg`

All sound files must be in `.ogg` Vorbis format for compatibility with Minecraft.

## Adding new sounds

1. Add the sound event to `TCSounds.java`.
2. Register the sound in `assets/traincraft/sounds.json`.
3. Drop the `.ogg` file into the matching directory under `assets/traincraft/sounds/`.
4. Test in-game.

## Sound event naming convention

Use the pattern `category.subcategory.name`, e.g.:

- `locomotive.steam.whistle`
- `train.wheels`
- `assembly_table.craft`
