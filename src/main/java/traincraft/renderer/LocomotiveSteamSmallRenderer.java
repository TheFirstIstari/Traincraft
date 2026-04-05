/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.resources.ResourceLocation;
import traincraft.Traincraft;
import traincraft.entity.LocomotiveSteamSmall;

public class LocomotiveSteamSmallRenderer extends EntityRenderer<LocomotiveSteamSmall> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "textures/trains/locomotive_steam_small.png");

    public LocomotiveSteamSmallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LocomotiveSteamSmall entity) {
        return TEXTURE;
    }

    @Override
    public void render(LocomotiveSteamSmall entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
