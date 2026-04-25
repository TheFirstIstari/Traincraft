/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.recipe;

import com.mojang.serialization.Codec;
import com.google.gson.JsonObject;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record NumberedIngredient(Ingredient ingredient, int count) {

    public boolean test(ItemStack stack) {
        return !stack.isEmpty() && ingredient.test(stack) && stack.getCount() >= count;
    }

    public static final Codec<NumberedIngredient> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(NumberedIngredient::ingredient),
        Codec.INT.fieldOf("count").orElse(1).forGetter(NumberedIngredient::count)
    ).apply(inst, NumberedIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NumberedIngredient> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, NumberedIngredient::ingredient,
        ByteBufCodecs.INT, NumberedIngredient::count,
        NumberedIngredient::new
    );

    public static final Codec<List<NumberedIngredient>> LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, List<NumberedIngredient>> LIST_STREAM_CODEC =
        ByteBufCodecs.collection(NonNullList::createWithCapacity, STREAM_CODEC);

    public com.google.gson.JsonObject toJsonObject() {
        com.google.gson.JsonObject json = new com.google.gson.JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("count", count);
        return json;
    }
}
