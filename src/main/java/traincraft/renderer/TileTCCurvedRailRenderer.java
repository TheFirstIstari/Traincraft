/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import traincraft.curves.CurveData;
import traincraft.blocks.rail.TileTCCurvedRail;

/**
 * Renders {@link TileTCCurvedRail} as a stretched textured strip following the cubic-Bezier sub-arc
 * the block is responsible for. Each block subdivides its [tStart, tEnd] range into a handful of
 * micro-segments and emits a flat textured quad for each, so a chain of curved-rail blocks
 * collectively traces a smooth visual curve through the world.
 */
public class TileTCCurvedRailRenderer implements BlockEntityRenderer<TileTCCurvedRail> {

    /** How many sub-segments to draw per block. Higher = smoother visual curve at higher cost. */
    private static final int SEGMENTS_PER_BLOCK = 6;
    /** Half-width of the rendered rail strip in world units. */
    private static final double HALF_WIDTH = 0.45;
    /** Y offset above the curve's evaluated y so the strip clears the floor block. */
    private static final float Y_OFFSET = 0.0625f;

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("traincraft", "textures/block/rail_steel.png");

    public TileTCCurvedRailRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(TileTCCurvedRail tile, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        CurveData curve = tile.getCurve();
        if (curve == null) return;

        double tStart = tile.getTStart();
        double tEnd = tile.getTEnd();
        if (tEnd - tStart < 1e-6) return;

        // The PoseStack is already positioned at the block's lower corner. Compute curve points
        // in world space and subtract the block origin so we render in block-local coordinates.
        Vec3 origin = Vec3.atLowerCornerOf(tile.getBlockPos());

        VertexConsumer buf = buffers.getBuffer(RenderType.entityCutout(TEXTURE));
        Matrix4f m = pose.last().pose();
        var n = pose.last();

        for (int i = 0; i < SEGMENTS_PER_BLOCK; i++) {
            double tA = lerp((double) i / SEGMENTS_PER_BLOCK, tStart, tEnd);
            double tB = lerp((double) (i + 1) / SEGMENTS_PER_BLOCK, tStart, tEnd);

            Vec3 pA = curve.evaluate(tA).subtract(origin);
            Vec3 pB = curve.evaluate(tB).subtract(origin);

            // Right vector in the horizontal plane, perpendicular to the local tangent.
            Vec3 dir = pB.subtract(pA);
            double dirLen = dir.length();
            if (dirLen < 1e-6) continue;
            Vec3 dirN = dir.scale(1.0 / dirLen);
            Vec3 right = new Vec3(-dirN.z, 0, dirN.x);
            double rl = right.length();
            if (rl < 1e-6) {
                // Tangent points straight up/down — fall back to world X to keep the strip wide.
                right = new Vec3(1, 0, 0);
            } else {
                right = right.scale(1.0 / rl);
            }
            right = right.scale(HALF_WIDTH);

            // Quad corners (counter-clockwise viewed from above so the front face points up).
            float ay = (float) pA.y + Y_OFFSET;
            float by = (float) pB.y + Y_OFFSET;
            Vec3 c1 = pA.add(right);   // start, right
            Vec3 c2 = pB.add(right);   // end,   right
            Vec3 c3 = pB.subtract(right); // end,   left
            Vec3 c4 = pA.subtract(right); // start, left

            float v0 = (float) i / SEGMENTS_PER_BLOCK;
            float v1 = (float) (i + 1) / SEGMENTS_PER_BLOCK;

            // Tile the rail texture along the strip's length so connected segments line up.
            buf.addVertex(m, (float) c1.x, ay, (float) c1.z)
                .setColor(0xFFFFFFFF).setUv(0f, v0)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(n, 0f, 1f, 0f);
            buf.addVertex(m, (float) c2.x, by, (float) c2.z)
                .setColor(0xFFFFFFFF).setUv(0f, v1)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(n, 0f, 1f, 0f);
            buf.addVertex(m, (float) c3.x, by, (float) c3.z)
                .setColor(0xFFFFFFFF).setUv(1f, v1)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(n, 0f, 1f, 0f);
            buf.addVertex(m, (float) c4.x, ay, (float) c4.z)
                .setColor(0xFFFFFFFF).setUv(1f, v0)
                .setOverlay(packedOverlay).setLight(packedLight)
                .setNormal(n, 0f, 1f, 0f);
        }
    }

    private static double lerp(double t, double a, double b) {
        return a + (b - a) * t;
    }

    @Override
    public int getViewDistance() {
        return 96;
    }
}
