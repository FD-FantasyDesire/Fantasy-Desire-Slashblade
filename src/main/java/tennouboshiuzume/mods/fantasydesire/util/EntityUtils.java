package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityUtils {
    public static IBlockState getBlockUnderEntity(Entity entity) {
        // 获取实体所在世界
        World world = entity.world;

        // 获取实体的坐标，并向下移动 1 格
        BlockPos posUnder = new BlockPos(entity.posX, entity.posY - 1, entity.posZ);

        // 获取该位置的方块状态
        return world.getBlockState(posUnder);
    }
}
