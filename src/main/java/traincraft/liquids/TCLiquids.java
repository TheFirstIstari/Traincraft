/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.liquids;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.Registries;
import traincraft.Traincraft;

public class TCLiquids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Traincraft.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Traincraft.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> OIL_TYPE = FLUID_TYPES.register("oil",
        () -> new FluidType(FluidType.Properties.create()));

    public static final DeferredHolder<FluidType, FluidType> REFINED_FUEL_TYPE = FLUID_TYPES.register("refined_fuel",
        () -> new FluidType(FluidType.Properties.create()));

    public static final DeferredHolder<FluidType, FluidType> DIESEL_TYPE = FLUID_TYPES.register("diesel",
        () -> new FluidType(FluidType.Properties.create()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> OIL = FLUIDS.register("oil",
        () -> new BaseFlowingFluid.Source(oilProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> OIL_FLOWING = FLUIDS.register("oil_flowing",
        () -> new BaseFlowingFluid.Flowing(oilFlowingProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> REFINED_FUEL = FLUIDS.register("refined_fuel",
        () -> new BaseFlowingFluid.Source(refinedFuelProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> REFINED_FUEL_FLOWING = FLUIDS.register("refined_fuel_flowing",
        () -> new BaseFlowingFluid.Flowing(refinedFuelFlowingProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> DIESEL = FLUIDS.register("diesel",
        () -> new BaseFlowingFluid.Source(dieselProperties()));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> DIESEL_FLOWING = FLUIDS.register("diesel_flowing",
        () -> new BaseFlowingFluid.Flowing(dieselFlowingProperties()));

    private static BaseFlowingFluid.Properties oilProperties() {
        return new BaseFlowingFluid.Properties(
            OIL_TYPE,
            OIL,
            OIL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties oilFlowingProperties() {
        return new BaseFlowingFluid.Properties(
            OIL_TYPE,
            OIL,
            OIL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties refinedFuelProperties() {
        return new BaseFlowingFluid.Properties(
            REFINED_FUEL_TYPE,
            REFINED_FUEL,
            REFINED_FUEL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties refinedFuelFlowingProperties() {
        return new BaseFlowingFluid.Properties(
            REFINED_FUEL_TYPE,
            REFINED_FUEL,
            REFINED_FUEL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties dieselProperties() {
        return new BaseFlowingFluid.Properties(
            DIESEL_TYPE,
            DIESEL,
            DIESEL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties dieselFlowingProperties() {
        return new BaseFlowingFluid.Properties(
            DIESEL_TYPE,
            DIESEL,
            DIESEL_FLOWING
        ).slopeFindDistance(4).levelDecreasePerBlock(2);
    }
}
