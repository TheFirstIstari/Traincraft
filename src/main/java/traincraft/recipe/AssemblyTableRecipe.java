/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.recipe;

import com.mojang.serialization.Codec;
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

import java.util.ArrayList;
import java.util.List;

public class AssemblyTableRecipe implements Recipe<RecipeInput> {

    private final int tier;
    private final ItemStack output;
    private final List<NumberedIngredient> ingredients;

    public AssemblyTableRecipe(int tier, ItemStack output, List<NumberedIngredient> ingredients) {
        this.tier = tier;
        this.output = output;
        this.ingredients = ingredients;
    }

    public int getTier() {
        return tier;
    }

    public NumberedIngredient getIngredient(int index) {
        return index < ingredients.size() ? ingredients.get(index) : new NumberedIngredient(Ingredient.EMPTY, 0);
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
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
        NonNullList<Ingredient> list = NonNullList.create();
        for (NumberedIngredient ni : ingredients) {
            list.add(ni.ingredient());
        }
        return list;
    }

    public static class Type implements RecipeType<AssemblyTableRecipe> {
        public static final Type INSTANCE = new Type();
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<AssemblyTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<AssemblyTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("tier").forGetter(r -> r.tier),
            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
            NumberedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients)
        ).apply(inst, AssemblyTableRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, AssemblyTableRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, r -> r.tier,
            ItemStack.STREAM_CODEC, r -> r.output,
            ByteBufCodecs.collection(ArrayList::new, NumberedIngredient.STREAM_CODEC), r -> r.ingredients,
            AssemblyTableRecipe::new
        );

        private Serializer() {}

        @Override
        public MapCodec<AssemblyTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AssemblyTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
