package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.entity.EntitySummonedSwordBase;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class testSA02 extends SpecialAttackBase {
    @Override
    public String toString() {
        return "testSA02";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int count = 1;
        float magicDamage = 5f;
        if(!world.isRemote){
            for (int j=1;j<=16;j++){
                    count++;
                    boolean front = (count % 2 == 0);

                    int currentValue = count/2;

                    EntitySummonedSwordBase entityDrive = new EntitySummonedSwordBase(world, player, magicDamage);
                    if (entityDrive != null) {
                        Vec3d playerPos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ); // 考虑玩家的眼睛位置
                        Vec3d lookVec = player.getLookVec();

                        // 计算视线向量的旋转角度（考虑 yaw 和 pitch）
                        double rotationYawRad = Math.toRadians(front ? -120 : 120);
                        double rotationPitchRad = Math.toRadians(player.rotationPitch);

                        // 计算旋转后的视线向量
                        double cosYaw = Math.cos(rotationYawRad);
                        double sinYaw = Math.sin(rotationYawRad);
                        double cosPitch = Math.cos(rotationPitchRad);
                        double sinPitch = Math.sin(rotationPitchRad);

                        double newX = lookVec.x * cosYaw - lookVec.z * sinYaw;
                        double newY = lookVec.y * cosPitch + Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z) * sinPitch;
                        double newZ = lookVec.x * sinYaw + lookVec.z * cosYaw;

                        Vec3d rotatedLookVec = new Vec3d(newX, newY, newZ).normalize(); // 归一化旋转后的向量

                        // 计算目标位置
                        double distance = 1 + currentValue * 0.2f;
                        Vec3d targetPos = playerPos.add(rotatedLookVec.scale(distance));

                        // 设置实体的位置和其他属性
                        entityDrive.setPosition(targetPos.x, targetPos.y, targetPos.z);
                        entityDrive.setInterval(20+currentValue*5);
                        entityDrive.setLifeTime(200);
                        entityDrive.setDriveVector(0.3f+0.05f*currentValue);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);

                        // 生成实体
                        world.spawnEntity(entityDrive);
                    }

            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}