/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import traincraft.Traincraft;
import traincraft.entity.bogie.EntityBogie;

public class EntityBogieRenderer extends EntityRenderer<EntityBogie> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/item/bogie_iron.png");

    public EntityBogieRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBogie entity) {
        return TEXTURE;
    }
}