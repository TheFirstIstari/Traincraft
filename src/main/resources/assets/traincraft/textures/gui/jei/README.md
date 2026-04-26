# JEI Textures

This directory contains texture files required for JEI (Just Enough Items) integration in Traincraft.

## Required Texture Files

### 1. assembly_table_jei.png

- **Dimensions**: 166 x 130 pixels
- **Used by**: `AssemblyTableRecipeCategory`
- **Purpose**: Background texture for the assembly table recipe GUI in JEI
- **Recipe Layout**:
  - 18 input slots arranged in a 6x3 grid
  - 1 output slot
  - Slot positions:
    - Input slots: Starting at (8, 8) with 18px spacing
    - Output slot: At (134, 35)

### 2. distillery_jei.png

- **Dimensions**: 166 x 60 pixels
- **Used by**: `DistilleryRecipeCategory`
- **Purpose**: Background texture for the distillery recipe GUI in JEI
- **Recipe Layout**:
  - 1 input slot
  - 1 output slot
  - Slot positions:
    - Input slot: At (30, 20)
    - Output slot: At (90, 20)

### 3. train_workbench_jei.png

- **Dimensions**: 166 x 100 pixels
- **Used by**: `TrainWorkbenchRecipeCategory`
- **Purpose**: Background texture for the train workbench recipe GUI in JEI
- **Recipe Layout**:
  - 3x3 crafting grid (9 input slots)
  - 1 output slot
  - Slot positions:
    - Input slots: Starting at (30, 16) with 18px spacing
    - Output slot: At (124, 34)

## Texture Specifications

### General Guidelines

- Textures should use the standard JEI GUI color palette
- Backgrounds should have semi-transparent areas for slot highlights
- Input slots are typically rendered with a light background
- Output slots should have a more prominent appearance
- Textures should follow a similar visual style to vanilla JEI textures

### Slot Positioning Reference

The texture dimensions determine the drawable area for recipe layouts:

| Category | Width | Height | Input Slots | Output Slots |
|----------|-------|--------|--------------|--------------|
| Assembly Table | 166 | 130 | 18 (6x3) | 1 |
| Distillery | 166 | 60 | 1 | 1 |
| Train Workbench | 166 | 100 | 9 (3x3) | 1 |

## Implementation Notes

The textures are loaded using JEI's `guiHelper.createDrawable()` method:

```java
// From AssemblyTableRecipeCategory.java
this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 130);

// From DistilleryRecipeCategory.java
this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 60);

// From TrainWorkbenchRecipeCategory.java
this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 100);
```

The texture resources are defined as `ResourceLocation`:

- `textures/gui/assembly_table_jei.png`
- `textures/gui/distillery_jei.png`
- `textures/gui/train_workbench_jei.png`

## Adding New JEI Categories

When adding new recipe categories to JEI:

1. Create a new recipe category class implementing `IRecipeCategory`
2. Define a new `TEXTURE` ResourceLocation
3. Specify the dimensions in `guiHelper.createDrawable()`
4. Add the required texture file to this directory
5. Update this README with the new texture specifications