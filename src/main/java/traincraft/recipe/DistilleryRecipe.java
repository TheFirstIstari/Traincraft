/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class DistilleryRecipe implements Recipe<RecipeInput> {

    private final Ingredient input;
    private final int inputAmount;
    private final ItemStack output;
    @Nullable
    private final FluidStack fluidOutput;
    private final int burnTime;

    public DistilleryRecipe(Ingredient input, int inputAmount, ItemStack output, @Nullable FluidStack fluidOutput, int burnTime) {
        this.input = input;
        this.inputAmount = inputAmount;
        this.output = output;
        this.fluidOutput = fluidOutput;
        this.burnTime = burnTime;
    }

    public boolean matches(ItemStack stack, Level level) {
        return this.input.test(stack) && stack.getCount() >= this.inputAmount;
    }

    @Nullable
    public FluidStack getFluidOutput() {
        return this.fluidOutput;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(this.input);
    }

    public static class Type implements RecipeType<DistilleryRecipe> {
        public static final Type INSTANCE = new Type();
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<DistilleryRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<DistilleryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter((DistilleryRecipe r) -> r.input),
            com.mojang.serialization.Codec.INT.fieldOf("input_amount").orElse(1).forGetter((DistilleryRecipe r) -> r.inputAmount),
            ItemStack.CODEC.fieldOf("output").forGetter((DistilleryRecipe r) -> r.output),
            FluidStack.CODEC.optionalFieldOf("fluid_output").forGetter((DistilleryRecipe r) -> java.util.Optional.ofNullable(r.fluidOutput)),
            com.mojang.serialization.Codec.INT.fieldOf("burn_time").orElse(200).forGetter((DistilleryRecipe r) -> r.burnTime)
        ).apply(instance, (input, inputAmount, output, fluidOutput, burnTime) ->
            new DistilleryRecipe(input, inputAmount, output, fluidOutput.orElse(null), burnTime)));

        private static final StreamCodec<RegistryFriendlyByteBuf, DistilleryRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, (DistilleryRecipe r) -> r.input,
            ByteBufCodecs.INT, (DistilleryRecipe r) -> r.inputAmount,
            ItemStack.STREAM_CODEC, (DistilleryRecipe r) -> r.output,
            FluidStack.OPTIONAL_STREAM_CODEC, (DistilleryRecipe r) -> r.fluidOutput,
            ByteBufCodecs.INT, (DistilleryRecipe r) -> r.burnTime,
            DistilleryRecipe::new
        );

        private Serializer() {}

        @Override
        public MapCodec<DistilleryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DistilleryRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
