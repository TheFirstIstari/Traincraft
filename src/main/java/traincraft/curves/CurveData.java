/*
 * Traincraft
 * Copyright (c) 2011-2024.
 */

package traincraft.curves;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

/**
 * A cubic Bezier curve in 3D space, used by Traincraft to lay smooth track segments.
 * <p>
 * The curve is defined by four control points {@code p0..p3} with the standard parameterisation
 *
 * <pre>
 *     B(t) = (1-t)^3 * p0 + 3(1-t)^2 t * p1 + 3(1-t) t^2 * p2 + t^3 * p3
 * </pre>
 *
 * Helpers are provided for evaluation, tangent, arc length (Simpson's rule), and finding the
 * parameter of the closest point to an arbitrary world-space position (Newton refinement on a
 * coarse grid scan). All methods are stateless once the curve is constructed.
 */
public record CurveData(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {

    public Vec3 evaluate(double t) {
        double u = 1.0 - t;
        double b0 = u * u * u;
        double b1 = 3.0 * u * u * t;
        double b2 = 3.0 * u * t * t;
        double b3 = t * t * t;
        return new Vec3(
            b0 * p0.x + b1 * p1.x + b2 * p2.x + b3 * p3.x,
            b0 * p0.y + b1 * p1.y + b2 * p2.y + b3 * p3.y,
            b0 * p0.z + b1 * p1.z + b2 * p2.z + b3 * p3.z
        );
    }

    /** Tangent vector (not normalised). */
    public Vec3 tangent(double t) {
        double u = 1.0 - t;
        double d0 = 3.0 * u * u;
        double d1 = 6.0 * u * t;
        double d2 = 3.0 * t * t;
        return new Vec3(
            d0 * (p1.x - p0.x) + d1 * (p2.x - p1.x) + d2 * (p3.x - p2.x),
            d0 * (p1.y - p0.y) + d1 * (p2.y - p1.y) + d2 * (p3.y - p2.y),
            d0 * (p1.z - p0.z) + d1 * (p2.z - p1.z) + d2 * (p3.z - p2.z)
        );
    }

    /**
     * Approximate arc length using Simpson's rule across {@code n} sub-intervals (must be even).
     * 32 samples gives sub-block accuracy for the kinds of curves we lay (typically 4–20 blocks long).
     */
    public double arcLength(int n) {
        if (n < 2 || (n & 1) != 0) n = 32;
        double h = 1.0 / n;
        double sum = tangent(0.0).length() + tangent(1.0).length();
        for (int i = 1; i < n; i++) {
            double t = i * h;
            double w = (i & 1) == 1 ? 4.0 : 2.0;
            sum += w * tangent(t).length();
        }
        return sum * h / 3.0;
    }

    /**
     * Find the parameter {@code t in [0,1]} whose curve point is nearest to {@code target}.
     * Uses a 64-sample coarse grid scan followed by a few Newton iterations.
     */
    public double closestT(Vec3 target) {
        // Coarse scan.
        double bestT = 0.0;
        double bestD = Double.MAX_VALUE;
        int samples = 64;
        for (int i = 0; i <= samples; i++) {
            double t = (double) i / samples;
            double d = evaluate(t).distanceToSqr(target);
            if (d < bestD) {
                bestD = d;
                bestT = t;
            }
        }
        // Newton's method: minimise f(t) = |B(t)-target|^2
        // f'(t) = 2 (B(t)-target) . B'(t); f''(t) ≈ 2 B'(t).B'(t) (drop B'' term for stability).
        for (int iter = 0; iter < 5; iter++) {
            Vec3 b = evaluate(bestT);
            Vec3 d = b.subtract(target);
            Vec3 t = tangent(bestT);
            double num = d.dot(t);
            double den = t.dot(t);
            if (den < 1e-9) break;
            double step = num / den;
            bestT = clamp01(bestT - step);
        }
        return bestT;
    }

    private static double clamp01(double v) {
        return v < 0.0 ? 0.0 : (v > 1.0 ? 1.0 : v);
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        putVec3(tag, "p0", p0);
        putVec3(tag, "p1", p1);
        putVec3(tag, "p2", p2);
        putVec3(tag, "p3", p3);
        return tag;
    }

    public static CurveData fromNbt(CompoundTag tag) {
        return new CurveData(
            getVec3(tag, "p0"),
            getVec3(tag, "p1"),
            getVec3(tag, "p2"),
            getVec3(tag, "p3")
        );
    }

    private static void putVec3(CompoundTag tag, String key, Vec3 v) {
        tag.putDouble(key + "_x", v.x);
        tag.putDouble(key + "_y", v.y);
        tag.putDouble(key + "_z", v.z);
    }

    private static Vec3 getVec3(CompoundTag tag, String key) {
        return new Vec3(tag.getDouble(key + "_x"), tag.getDouble(key + "_y"), tag.getDouble(key + "_z"));
    }
}
