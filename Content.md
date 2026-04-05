# Traincraft Content

## Blocks

| Block | Registry Name | Notes |
|:--- |:--- |:--- |
| Assembly Table I | `traincraft:assembly_table_i` | 10-slot crafting, tier 1 |
| Assembly Table II | `traincraft:assembly_table_ii` | 10-slot crafting, tier 2 |
| Assembly Table III | `traincraft:assembly_table_iii` | 10-slot crafting, tier 3 |
| Battery | `traincraft:battery` | 100,000 FE energy storage |
| Ballast | `traincraft:ballast` | Decorative block |
| Bridge Pillar | `traincraft:bridge_pillar` | Decorative block |
| Copper Ore | `traincraft:copper_ore` | Generates in stone layers |
| Diesel Generator | `traincraft:generator_diesel` | Burns fuel for RF, has facing |
| Distillery | `traincraft:distillery` | Fluid processing, has active state |
| Lantern | `traincraft:lantern` | Light source |
| Oil Sand | `traincraft:oil_sand` | Generates in sand layers |
| Open Hearth Furnace | `traincraft:open_hearth_furnace` | Smelting, has active state + light |
| Petrol Ore | `traincraft:petrol_ore` | Generates in stone layers |
| Stopper | `traincraft:stopper` | Train buffer |
| Train Workbench | `traincraft:train_workbench` | 3x3 crafting grid |
| Water Wheel | `traincraft:water_wheel` | Generates RF near water |
| Wind Mill | `traincraft:wind_mill` | Generates RF from sky exposure |

## Items

### Materials
- Steel Ingot, Steel Dust, Coal Dust, Graphite
- Copper Ingot, Plastic
- Fine Copper Wire

### Train Parts (28)
- Steel Firebox, Steel Bogie, Steel Frame, Steel Cabin, Steel Chimney
- Balloon, Bogie Iron, Bogie Wood
- Boiler Iron, Boiler Steel, Cab Iron, Cab Wood
- Camshaft, Chimney Iron, Circuit, Controls, Cylinder
- Engine Diesel, Engine Electric, Engine Steam
- Fiberglass Plate, Firebox Iron, Frame Iron, Frame Wood
- Piston, Propeller, Rail Copper, Rail Steel
- Reinforced Plate, Seats, Signal, Transformer, Transmission

### Tools
- **Wrench** - Rotates blocks (durability: 200)
- **Canister** - Collects fluids from source blocks (capacity: 16,000 mB)
- **Connector** - Links rolling stock together (durability: 200)
- **Skin Changer** - Cycles rolling stock skins
- **Chunk Loader Activator** - Force loads chunks
- **ATO Card** - Automatic Train Operation configuration
- **Wireless Transmitter** - Remote train signaling
- **Guide** - In-game documentation
- **Fuel** - Fuel item

### Spawn Eggs
- Locomotive Steam Small Spawn Egg

## Armor

### Engineer's Set
- Overalls (Leggings)
- Jacket (Chestplate)
- Hat (Helmet)

### Ticketman Set
- Ticketman Jacket (Chestplate)
- Ticketman Pants (Leggings)
- Ticketman Hat (Helmet)

### Driver Set
- Driver Jacket (Chestplate)
- Driver Pants (Leggings)
- Driver Hat (Helmet)

### Composite Suit
- Composite Suit Helmet (Helmet)
- Composite Suit Chest (Chestplate)
- Composite Suit Pants (Leggings)
- Composite Suit Boots (Boots)

## Fluids

| Fluid | Registry Name | Notes |
|:--- |:--- |:--- |
| Oil | `traincraft:oil` | Source + flowing |
| Refined Fuel | `traincraft:refined_fuel` | Source + flowing |
| Diesel | `traincraft:diesel` | Source + flowing |

## Tile Entities

| Tile Entity | Ticks | Behavior |
|:--- |:---:|:--- |
| Assembly Table I | No | 10-slot crafting input, 8 storage, 8 output |
| Assembly Table II | No | Same as I, higher tier recipes |
| Assembly Table III | No | Same as I, highest tier recipes |
| Battery | No | Stores up to 100,000 FE |
| Diesel Generator | Yes | Burns fuel, generates 40 FE/tick |
| Distillery | Yes | Processes items into fluids, burn system |
| Open Hearth Furnace | Yes | Smelts items, burn/cook progress |
| Train Workbench | No | 3x3 crafting grid |
| Water Wheel | Yes | Generates 10 FE/tick when near water |
| Wind Mill | Yes | Generates FE based on sky exposure |

## Entities

| Entity | Registry Name | Notes |
|:--- |:--- |:--- |
| Locomotive Steam Small | `traincraft:locomotive_steam_small` | Steam locomotive with renderer |

### Entity Features
- **Linking** - Connect rolling stock using the Connector item
- **Skins** - Multiple visual variants per entity, cycled with Skin Changer
- **Seats** - Passenger seating with controlling seat
- **Capabilities** - Inventory, fluid tank, and energy storage
- **Ownership** - Owner system with public/private restrictions

## World Generation

| Feature | Biome | Vein Size | Height Range |
|:--- |:--- |:---:|:---:|
| Copper Ore | All stone biomes | 6 | 5-50 |
| Petrol Ore | All stone biomes | 14 | 10-50 |
| Oil Sand | All sand biomes | 10 | 25-75 |

## Recipes

### Recipe Types
- **Distillery** - Custom recipe for fluid processing
- **Assembly Table** - Tiered crafting with numbered ingredients
- **Train Workbench** - 3x3 crafting for train components
- **Smelting** - Standard furnace recipes

### Sample Recipes
- Steel Ingot (smelting raw iron)
- Steel Frame (crafting with steel ingots)
- Fine Copper Wire (crafting with copper ingots)
- Controls (crafting with steel ingots + circuit)
- Steel Bogie (crafting with steel ingots)
- Wrench (crafting with steel + iron)
- Locomotive Steam Small (assembly table, tier 1)
