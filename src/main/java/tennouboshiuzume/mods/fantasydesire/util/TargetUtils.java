package tennouboshiuzume.mods.fantasydesire.util;

import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TargetUtils {
    public static List<EntityLivingBase> findAllHostileEntities(Entity player, float range, boolean overWallCheck) {
        World world = player.world;
        List<EntityLivingBase> hostileEntities = new ArrayList<>();

        // 获取玩家附近的所有符合条件的生物实体
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), EntitySelectorAttackable.getInstance());

        for (EntityLivingBase entity : entities) {
            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
                hostileEntities.add(entity);
            }
        }
        return hostileEntities;
    }
    public static List<Entity> findAllHostileEntitiesE(Entity player, float range, boolean overWallCheck) {
        World world = player.world;
        List<Entity> hostileEntities = new ArrayList<>();

        // 获取玩家附近的所有符合条件的生物实体
        List<Entity> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), EntitySelectorAttackable.getInstance());

        for (Entity entity : entities) {
            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
                hostileEntities.add(entity);
            }
        }
        return hostileEntities;
    }

    public static List<EntityLivingBase> findAllHostileEntities(Entity player, float range, Entity nonTarget, boolean overWallCheck) {
        World world = player.world;
        List<EntityLivingBase> hostileEntities = new ArrayList<>();

        // 获取玩家附近的所有符合条件的生物实体
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), entity -> EntitySelectorAttackable.getInstance().apply(entity) && !entity.equals(nonTarget));

        for (EntityLivingBase entity : entities) {

            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
                hostileEntities.add(entity);
            }
        }
        return hostileEntities;
    }
    public static List<Entity> findAllHostileEntitiesE(Entity player, float range, Entity nonTarget, boolean overWallCheck) {
        World world = player.world;
        List<Entity> hostileEntities = new ArrayList<>();

        // 获取玩家附近的所有符合条件的生物实体
        List<Entity> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), entity -> EntitySelectorAttackable.getInstance().apply(entity) && !entity.equals(nonTarget));

        for (Entity entity : entities) {

            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
                hostileEntities.add(entity);
            }
        }
        return hostileEntities;
    }


    public static List<EntityLivingBase> findHostileEntitiesInFOV(Entity player, float fovAngle, float range, boolean overWallCheck) {
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
            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
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

    public static List<EntityLivingBase> findHostileEntitiesInFOV(Entity player, float fovAngle, float range, Entity nonTarget, boolean overWallCheck) {
        World world = player.world;
        List<EntityLivingBase> hostileEntities = new ArrayList<>();

        // 获取玩家的视线方向向量
        Vec3d lookVec = player.getLookVec().normalize();

        // 定义玩家视线范围的最大角度的余弦值
        double maxCosAngle = Math.cos(Math.toRadians(fovAngle / 2));

        // 获取玩家附近的所有生物实体
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range), entity -> EntitySelectorAttackable.getInstance().apply(entity) && !entity.equals(nonTarget));

        for (EntityLivingBase entity : entities) {
            // 判断是否是敌对生物，通常可以通过检测是否属于特定生物类型或标签来实现
            if (entity.isEntityAlive() && (player.getDistance(entity) < range)) {
                if (overWallCheck && isObstructed(world, player, entity)) {
                    continue;
                }
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
        return currentEnemy;
    }


    public static int setTargetEntityFromList(int i, List<EntityLivingBase> enemies) {
        // 通过取模操作获取当前要操作的敌人
        int index = i % enemies.size();
        EntityLivingBase currentEnemy = enemies.get(index);
        // 设置当前敌人为目标
        return currentEnemy.getEntityId();
    }

//    更精确的视线检测
    public static boolean isObstructed(World world, Entity player, Entity target) {
        // 玩家和实体的眼睛位置
        Vec3d playerPos = player.getPositionEyes(1.0F);

        // 目标实体的顶部、中间和底部位置
        Vec3d targetTopPos = new Vec3d(target.posX, target.posY + target.height - 0.1f, target.posZ);
        Vec3d targetMidPos = new Vec3d(target.posX, target.posY + target.height / 2.0, target.posZ);
        Vec3d targetBottomPos = new Vec3d(target.posX, target.posY + 0.1f, target.posZ);

        // 对三次位置进行 rayTraceBlocks 检查，任何一次不通过（有遮挡）则返回 true
        RayTraceResult resultTop = world.rayTraceBlocks(playerPos, targetTopPos);
        RayTraceResult resultMid = world.rayTraceBlocks(playerPos, targetMidPos);
        RayTraceResult resultBottom = world.rayTraceBlocks(playerPos, targetBottomPos);

        // 如果任意一个位置检测结果为非空，说明存在遮挡
        return resultTop != null || resultMid != null || resultBottom != null;
    }

}
