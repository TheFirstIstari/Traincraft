/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.blocks.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import traincraft.curves.CurveData;

import java.util.UUID;

/**
 * Block entity attached to every {@link BlockTCCurvedRail}. Stores the curve definition the
 * block belongs to plus the {@code [tStart, tEnd]} range of the curve that runs through this
 * block. A single curve produces many such tiles, all sharing the same {@code curveId} and
 * {@code curve} but each holding a different t-range.
 */
public class TileTCCurvedRail extends BlockEntity {

    @Nullable
    private CurveData curve;
    private double tStart = 0.0;
    private double tEnd = 1.0;
    @Nullable
    private UUID curveId;

    public TileTCCurvedRail(BlockPos pos, BlockState state) {
        super(traincraft.tile.TCTiles.CURVED_RAIL.get(), pos, state);
    }

    public void setCurve(CurveData curve, double tStart, double tEnd, UUID curveId) {
        this.curve = curve;
        this.tStart = tStart;
        this.tEnd = tEnd;
        this.curveId = curveId;
        setChanged();
    }

    @Nullable
    public CurveData getCurve() {
        return curve;
    }

    public double getTStart() {
        return tStart;
    }

    public double getTEnd() {
        return tEnd;
    }

    @Nullable
    public UUID getCurveId() {
        return curveId;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (curve != null) {
            tag.put("curve", curve.toNbt());
            tag.putDouble("t_start", tStart);
            tag.putDouble("t_end", tEnd);
            if (curveId != null) tag.putUUID("curve_id", curveId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("curve", 10)) {
            this.curve = CurveData.fromNbt(tag.getCompound("curve"));
            this.tStart = tag.getDouble("t_start");
            this.tEnd = tag.getDouble("t_end");
            this.curveId = tag.hasUUID("curve_id") ? tag.getUUID("curve_id") : null;
        }
    }
}
