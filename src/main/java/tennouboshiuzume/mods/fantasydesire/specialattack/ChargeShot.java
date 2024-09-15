package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class ChargeShot extends SpecialAttackBase {
    @Override
    public String toString() {
        return "ChargeShot";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int ringcount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-2, 1),8);
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));

        if (!world.isRemote) {

            ItemSlashBlade.TextureName.set(tag,"named/SmartPistol_OC");
            ItemSlashBlade.SummonedSwordColor.set(tag,0x99FF99);
            ItemSlashBlade.SpecialAttackType.set(tag,202);

            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player,30));

            int swordcount = 0;
            for (int j = 0; j < ringcount; j++) {
                int rings = j + 1;
                double radius = 0.5 + (0.2 * j);
                int points = 3 * rings;
                boolean isBurst = (rings % 2 == 0);
                Vec3d playerPos = player.getPositionVector();
                float yaw = player.rotationYaw;

                // 计算在XZ平面上的方向向量
                Vec3d lookVec = new Vec3d(
                        -Math.sin(Math.toRadians(yaw)),
                        0,
                        Math.cos(Math.toRadians(yaw))
                ).normalize();
                Vec3d rightVec = new Vec3d(
                        Math.cos(Math.toRadians(yaw)),
                        0,
                        Math.sin(Math.toRadians(yaw))
                ).normalize();

                for (int i = 0; i < points; i++) {
                    swordcount++;
                    double angle = 2 * Math.PI * i / points;

                    // 修改为在XZ平面上计算偏移
                    double xOffset = radius * Math.cos(angle);
                    double zOffset = radius * Math.sin(angle);

                    // 根据偏移量在XZ平面上生成圆形分布
                    Vec3d circlePoint = playerPos.add(rightVec.scale(xOffset)).add(lookVec.scale(zOffset));
                    EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, player, magicDamage);

                    entityDrive.setInitialPosition(circlePoint.x, playerPos.y + player.getEyeHeight(), circlePoint.z, i * (360f / points) +player.rotationYaw -90, 0, 0, 3f);
                    entityDrive.setLifeTime(160);
                    entityDrive.setColor(isBurst ? 0x00FFFF : 0xFFFFFF);
                    entityDrive.setSound(SoundEvents.ENTITY_WITHER_BREAK_BLOCK, 2f, 2f);
                    entityDrive.setScale(0.2f);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setInterval(1 + rings);

                    if (!target.isEmpty()) {
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i, target));
                    }
                    world.spawnEntity(entityDrive);
                }
            }

            UntouchableTime.setUntouchableTime(player, 20);

        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}