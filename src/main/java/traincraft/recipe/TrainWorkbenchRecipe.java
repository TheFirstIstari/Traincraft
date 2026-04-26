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

/**
 * A fixed-shape 3x3 recipe used by the Train Workbench.
 * <p>
 * The 9 slot ingredients are positional (slot index 0..8); each slot in the
 * grid must match the corresponding ingredient (or both must be empty).
 * Each consumed slot decrements by one when the result is taken.
 */
public class TrainWorkbenchRecipe implements Recipe<RecipeInput> {

    public static final int SLOT_COUNT = 9;

    private final List<Ingredient> slots; // exactly SLOT_COUNT entries; Ingredient.EMPTY for blank slots
    private final ItemStack result;

    public TrainWorkbenchRecipe(List<Ingredient> slots, ItemStack result) {
        if (slots.size() != SLOT_COUNT) {
            throw new IllegalArgumentException("Train workbench recipe requires exactly " + SLOT_COUNT + " slots");
        }
        this.slots = List.copyOf(slots);
        this.result = result;
    }

    public Ingredient ingredient(int slot) {
        return slots.get(slot);
    }

    public boolean matches(net.neoforged.neoforge.items.IItemHandler handler) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            Ingredient ing = slots.get(i);
            if (ing.isEmpty()) {
                if (!stack.isEmpty()) return false;
            } else if (!ing.test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        if (input.size() < SLOT_COUNT) return false;
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = input.getItem(i);
            Ingredient ing = slots.get(i);
            if (ing.isEmpty()) {
                if (!stack.isEmpty()) return false;
            } else if (!ing.test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
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
        NonNullList<Ingredient> list = NonNullList.createWithCapacity(SLOT_COUNT);
        list.addAll(slots);
        return list;
    }

    public static class Type implements RecipeType<TrainWorkbenchRecipe> {
        public static final Type INSTANCE = new Type();
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<TrainWorkbenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        // We expect a fixed list of 9 entries; use Ingredient codec which already handles "empty" via empty arrays.
        private static final Codec<List<Ingredient>> SLOTS_CODEC = Ingredient.CODEC_NONEMPTY.listOf().xmap(
            list -> {
                if (list.size() != SLOT_COUNT) {
                    throw new IllegalArgumentException("Train workbench recipe must declare exactly " + SLOT_COUNT + " slots");
                }
                return list;
            },
            list -> list
        );

        // Allow individual empty slots by accepting a list of "ingredient or null". Codec keeps it simple by
        // requiring users to specify Ingredient.EMPTY-equivalent (e.g. {"item":"minecraft:air"}) for blank
        // slots; in practice the JSON simply lists the 9 ingredients in row-major order.
        private static final MapCodec<TrainWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("slots").forGetter(r -> r.slots),
            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(inst, TrainWorkbenchRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, TrainWorkbenchRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC),
                r -> r.slots,
            ItemStack.STREAM_CODEC, r -> r.result,
            TrainWorkbenchRecipe::new
        );

        private Serializer() {}

        @Override
        public MapCodec<TrainWorkbenchRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TrainWorkbenchRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
