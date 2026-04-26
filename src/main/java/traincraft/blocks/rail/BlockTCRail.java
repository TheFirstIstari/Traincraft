/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.rail;

import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Custom Traincraft rail block.
 * <p>
 * Behaves like a vanilla rail (uses {@link net.minecraft.world.level.block.state.properties.RailShape}
 * with curve and ascending variants) but is recognised by Traincraft rolling stock for an
 * optional speed multiplier and as a building block for future curved-track expansions.
 * <p>
 * The vanilla {@link RailBlock#codec()} is inherited as-is. The codec is only consulted by
 * datagen / advanced block-state serialization, neither of which materialise our subclass
 * fields directly, so a non-overridden codec is safe.
 */
public class BlockTCRail extends RailBlock {

    private final double speedMultiplier;

    public BlockTCRail(BlockBehaviour.Properties properties, double speedMultiplier) {
        super(properties);
        this.speedMultiplier = speedMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
