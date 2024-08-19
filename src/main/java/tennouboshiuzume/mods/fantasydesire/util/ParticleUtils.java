package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ParticleUtils {
    public static void spawnParticle(World world,EnumParticleTypes types,boolean force,double posX,double posY, double posZ,int count,double xd,double yd,double zd ,float speed){
        ((WorldServer)world).spawnParticle(types,
                force,
                posX,
                posY,
                posZ,
                count, xd, yd, zd, speed);
    }
}
