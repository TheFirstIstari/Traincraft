/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.payload.FriendlyByteBufPayload;
import traincraft.Traincraft;

public class TCNetwork {

    public static final ResourceLocation CHANNEL = new ResourceLocation(Traincraft.MOD_ID, "main");

    public static void register() {
        Traincraft.LOGGER.info("Registering network channels");
        
        // Register packet handlers using NeoForge's payload system
    }

    public static void sendToServer(Object message) {
        PacketDistributor.sendToServer();
    }

    public static void sendToPlayer(ServerPlayer player, Object message) {
        PacketDistributor.sendToPlayer(player, () -> message);
    }

    public static void sendToAllPlayers(Object message) {
        PacketDistributor.sendToAllPlayers(() -> message);
    }

    public static void sendToTracking(Entity entity, Object message) {
        PacketDistributor.sendToTrackingEntity(entity, () -> message);
    }
}