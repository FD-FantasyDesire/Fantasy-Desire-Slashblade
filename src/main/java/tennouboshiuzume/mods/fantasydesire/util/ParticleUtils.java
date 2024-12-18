package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ParticleUtils {
    public static void spawnParticle(World world, EnumParticleTypes types, boolean force, double posX, double posY, double posZ, int count, double xd, double yd, double zd, float speed) {
        if (!world.isRemote) {
            ((WorldServer) world).spawnParticle(types,
                    force,
                    posX,
                    posY,
                    posZ,
                    count, xd, yd, zd, speed);
        }
    }

    public static void spawnParticleLine(World world, EnumParticleTypes types, double startX, double startY, double startZ, double endX, double endY, double endZ, double dX, double dY, double dZ, int count, float speed) {
        if (!world.isRemote) {
            // 粒子数量（决定线的平滑度）

            double range = MathUtils.getDistancePos(startX, startY, startZ, endX, endY, endZ);
//            System.out.println("range"+range);
            // 计算向量
            double x1 = (endX - startX);
            double y1 = (endY - startY);
            double z1 = (endZ - startZ);

            Vec3d spawnPos = new Vec3d(x1, y1, z1).normalize();

            // 循环生成粒子
            for (int i = 0; i <= range * 2; i++) {
                Vec3d particlePos = spawnPos.scale(0.5f * i);
                // 使用 spawnParticle 方法生成粒子
                ((WorldServer) world).spawnParticle(types, true,
                        particlePos.x + startX,
                        particlePos.y + startY,
                        particlePos.z + startZ,
                        count, dX, dY, dZ, speed);
            }
        }
    }

    public static void spawnParticleLightningLine(World world, EnumParticleTypes types,
                                                  double startX, double startY, double startZ,
                                                  double endX, double endY, double endZ,
                                                  double dX, double dY, double dZ, int count, float speed) {
        if (!world.isRemote) {
            double range = MathUtils.getDistancePos(startX, startY, startZ, endX, endY, endZ);

            // 递归分割生成粒子线段
            spawnParticleSegment(world, types, startX, startY, startZ, endX, endY, endZ, dX, dY, dZ, count, speed, range * 0.5);
        }
    }

    private static void spawnParticleSegment(World world, EnumParticleTypes types,
                                             double x1, double y1, double z1,
                                             double x2, double y2, double z2,
                                             double dX, double dY, double dZ, int count, float speed, double randomness) {
        // 计算线段长度并判断是否需要继续分割
        double distance = MathUtils.getDistancePos(x1, y1, z1, x2, y2, z2);
        if (distance < 0.5) {
            return; // 当距离小于阈值时停止递归
        }

        // 在中点添加随机偏移
        double midX = (x1 + x2) / 2 + (Math.random() - 0.5) * randomness;
        double midY = (y1 + y2) / 2 + (Math.random() - 0.5) * randomness;
        double midZ = (z1 + z2) / 2 + (Math.random() - 0.5) * randomness;

        // 在起始点生成粒子
        ((WorldServer) world).spawnParticle(types, true,
                x1, y1, z1,
                count, dX, dY, dZ, speed);

        // 递归调用生成分段
        spawnParticleSegment(world, types, x1, y1, z1, midX, midY, midZ, dX, dY, dZ, count, speed, randomness * 0.5);
        spawnParticleSegment(world, types, midX, midY, midZ, x2, y2, z2, dX, dY, dZ, count, speed, randomness * 0.5);

        // 在终点生成粒子
        ((WorldServer) world).spawnParticle(types, true,
                x2, y2, z2,
                count, dX, dY, dZ, speed);
    }


}
