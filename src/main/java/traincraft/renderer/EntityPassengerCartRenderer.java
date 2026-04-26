/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import traincraft.Traincraft;
import traincraft.entity.rollingstock.EntityPassengerCart;

public class EntityPassengerCartRenderer extends EntityRenderer<EntityPassengerCart> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/trains/passenger_blue.png");

    public EntityPassengerCartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCart entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityPassengerCart entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
