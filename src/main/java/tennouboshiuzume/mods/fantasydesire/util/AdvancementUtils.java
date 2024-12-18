package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class AdvancementUtils {

    public static void grantAdvancementToPlayersInRange(World world, double x, double y, double z, double range, String advancementID) {
        // 获取服务器实例
        MinecraftServer server = world.getMinecraftServer();
        if (server == null) {
            System.out.println("Server instance not found!");
            return;
        }

        // 1. 获取进度
        Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(advancementID));
        if (advancement == null) {
            System.out.println("Advancement not found: " + advancementID);
            return;
        }

        // 2. 定义范围并找到范围内的所有玩家
        AxisAlignedBB boundingBox = new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
        List<EntityPlayerMP> playersInRange = world.getEntitiesWithinAABB(EntityPlayerMP.class, boundingBox);

        // 3. 授予进度
        for (EntityPlayerMP player : playersInRange) {
            AdvancementProgress progress = player.getAdvancements().getProgress(advancement);
            if (!progress.isDone()) { // 检查玩家是否已获得该进度
                for (String criterion : progress.getRemaningCriteria()) {
                    player.getAdvancements().grantCriterion(advancement, criterion); // 授予进度
                }
            }
        }
    }
    public static void grantAdvancementToPlayer(EntityPlayerMP player, String advancementID) {
        // 获取服务器实例
        MinecraftServer server = player.getServer();
        if (server == null) {
            System.out.println("Server instance not found!");
            return;
        }

        // 获取指定的成就
        Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(advancementID));
        if (advancement == null) {
            System.out.println("Advancement not found: " + advancementID);
            return;
        }

        // 获取玩家的进度数据并检查成就完成情况
        AdvancementProgress progress = player.getAdvancements().getProgress(advancement);
        if (!progress.isDone()) { // 如果成就未完成
            for (String criterion : progress.getRemaningCriteria()) {
                player.getAdvancements().grantCriterion(advancement, criterion); // 授予成就
            }
        }
    }
}
