# Traincraft Sound System

This directory contains all the sound definitions for the Traincraft mod.

## Sound Categories

1. **Locomotive Sounds**
   - Steam whistle
   - Engine running

2. **Train Movement Sounds**
   - Wheels on tracks

3. **Block Interaction Sounds**
   - Assembly table crafting
   - Distillery processing
   - Signal activation

## Sound File Format

All sound files should be in .ogg format for optimal compatibility with Minecraft.

## Adding New Sounds

1. Add the sound event to TCSounds.java
2. Register the sound in sounds.json
3. Add the actual .ogg file to the appropriate directory
4. Test the sound in-game

## Sound Event Naming Convention

Sounds should follow the pattern: `category.subcategory.name`

Examples:
- locomotive.steam.whistle
- train.wheels
- assembly_table.craft
