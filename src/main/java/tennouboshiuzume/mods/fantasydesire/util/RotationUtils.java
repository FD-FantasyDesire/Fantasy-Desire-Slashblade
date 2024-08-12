package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.util.math.Vec3d;

public class RotationUtils{

    public static Vec3d rotateX(Vec3d vec, double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double newY = vec.y * cos - vec.z * sin;
        double newZ = vec.y * sin + vec.z * cos;
        return new Vec3d(vec.x, newY, newZ);
    }

    public static Vec3d rotateY(Vec3d vec, double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double newX = vec.x * cos + vec.z * sin;
        double newZ = -vec.x * sin + vec.z * cos;
        return new Vec3d(newX, vec.y, newZ);
    }

    public static Vec3d rotateZ(Vec3d vec, double angle) {
        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        double newX = vec.x * cos - vec.y * sin;
        double newY = vec.x * sin + vec.y * cos;
        return new Vec3d(newX, newY, vec.z);
    }

    public static Vec3d rotate(Vec3d vec, double yaw, double pitch, double roll) {
        Vec3d result = rotateY(vec, yaw);
        result = rotateX(result, pitch);
        result = rotateZ(result, roll);
        return result;
    }
}
