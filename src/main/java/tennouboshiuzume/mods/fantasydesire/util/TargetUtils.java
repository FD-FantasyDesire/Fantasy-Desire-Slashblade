package tennouboshiuzume.mods.fantasydesire.util;

import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TargetUtils {
    public static List<EntityLivingBase> findAllHostileEntities(Entity player, float range) {
        World world = player.world;
        List<EntityLivingBase> hostileEntities = new ArrayList<>();

        // 获取玩家附近的所有符合条件的生物实体
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), EntitySelectorAttackable.getInstance());

        for (EntityLivingBase entity : entities) {
            if (entity.isEntityAlive()) {
                hostileEntities.add(entity);
                }
            }
        return hostileEntities;
    }


    public static List<EntityLivingBase> findHostileEntitiesInFOV(Entity player, float fovAngle,float range) {
        World world = player.world;
        List<EntityLivingBase> hostileEntities = new ArrayList<>();

        // 获取玩家的视线方向向量
        Vec3d lookVec = player.getLookVec().normalize();

        // 定义玩家视线范围的最大角度的余弦值
        double maxCosAngle = Math.cos(Math.toRadians(fovAngle / 2));

        // 获取玩家附近的所有生物实体
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), EntitySelectorAttackable.getInstance());

        for (EntityLivingBase entity : entities) {
            // 判断是否是敌对生物，通常可以通过检测是否属于特定生物类型或标签来实现
            if (entity.isEntityAlive()) {
                // 计算玩家到该实体的方向向量
                Vec3d toEntityVec = entity.getPositionVector().subtract(player.getPositionVector()).normalize();

                // 计算玩家视线方向与该实体方向向量的余弦值
                double cosAngle = lookVec.dotProduct(toEntityVec);

                // 如果余弦值大于或等于最大余弦值，则实体在玩家视线范围内
                if (cosAngle >= maxCosAngle) {
                    hostileEntities.add(entity);
                }
            }
        }

        return hostileEntities;
    }
    public static EntityLivingBase setTargetEntityFromListByEntity(int i, List<EntityLivingBase> enemies) {
        // 通过取模操作获取当前要操作的敌人
        int index = i % enemies.size();
        EntityLivingBase currentEnemy = enemies.get(index);
        // 设置当前敌人为目标
        return  currentEnemy;
    }


    public static int setTargetEntityFromList(int i, List<EntityLivingBase> enemies) {
        // 通过取模操作获取当前要操作的敌人
        int index = i % enemies.size();
        EntityLivingBase currentEnemy = enemies.get(index);
        // 设置当前敌人为目标
        return  currentEnemy.getEntityId();
    }
}
