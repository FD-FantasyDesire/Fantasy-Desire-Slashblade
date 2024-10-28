package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ParticleUtils {
    public static void spawnParticle(World world,EnumParticleTypes types,boolean force,double posX,double posY, double posZ,int count,double xd,double yd,double zd ,float speed){
        if (!world.isRemote) {
            ((WorldServer)world).spawnParticle(types,
                    force,
                    posX,
                    posY,
                    posZ,
                    count, xd, yd, zd, speed);
        }
    }

    public static void  spawnParticleLine(World world,EnumParticleTypes types,double startX,double startY,double startZ,double endX,double endY,double endZ,double dX,double dY,double dZ,int count,float speed){
        if (!world.isRemote){
            // 粒子数量（决定线的平滑度）

            double range = MathUtils.getDistancePos(startX,startY,startZ,endX,endY,endZ);
//            System.out.println("range"+range);
            // 计算向量
            double x1 = (endX - startX);
            double y1 = (endY - startY);
            double z1 = (endZ - startZ);

            Vec3d spawnPos = new Vec3d(x1,y1,z1).normalize();

            // 循环生成粒子
            for (int i = 0; i <= range * 2; i++) {
                Vec3d particlePos = spawnPos.scale(0.5f*i);
                // 使用 spawnParticle 方法生成粒子
                ((WorldServer)world).spawnParticle(types,true,
                        particlePos.x+startX,
                        particlePos.y+startY,
                        particlePos.z+startZ,
                        count, dX, dY, dZ,speed);
            }
        }
    }
}
