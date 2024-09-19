package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShinMagnetStormSword extends SpecialAttackBase {

    @Override
    public String toString() {
        return "ShinMagnetStormSword";
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
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findHostileEntitiesInFOV(player,60,45f,true));
            Collections.shuffle(target);
            int swordcount = 0;
            for (int j=0;j<ringcount;j++){
                int rings = j+1;
                double radius = 0.5+(0.25*j);
                int points = 3*rings;
                Boolean isBurst =(rings%2==0);
                Vec3d playerPos = player.getPositionVector();
                float yaw = player.rotationYaw;
                Vec3d lookVec = new Vec3d(
                        -Math.sin(Math.toRadians(yaw)),
                        0,
                        Math.cos(Math.toRadians(yaw))
                ).normalize();
                Vec3d upVec = new Vec3d(0, 1, 0);
                Vec3d rightVec = lookVec.crossProduct(upVec).normalize();
                Vec3d forwardVec = rightVec.crossProduct(lookVec).normalize();

                for (int i = 0; i < points; i++) {
                    swordcount++;
                    double angle = 2 * Math.PI * i / points;
                    double xOffset = radius * Math.cos(angle);
                    double yOffset = radius * Math.sin(angle);
                    Vec3d circlePoint = playerPos.add(rightVec.scale(xOffset)).add(forwardVec.scale(yOffset));
                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    entityDrive.setPosition(circlePoint.x, circlePoint.y + player.getEyeHeight(), circlePoint.z);
                    entityDrive.setLifeTime(160);
                    entityDrive.setColor(isBurst ? 0x00FFFF : 0xFFFFFF);
//                    entityDrive.setBurst(isBurst);
//                    entityDrive.setExpRadius(2);
                    entityDrive.setIsOverWall(false);
                    entityDrive.setInterval(2+2*rings);
                    entityDrive.setRoll(i * -(360f/points) +90f);
                    entityDrive.setIsNonPlayer(true);
                    if (!target.isEmpty()){
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));;
                    }
                    world.spawnEntity(entityDrive);
                }
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
