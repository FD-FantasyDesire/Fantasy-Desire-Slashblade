package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntitySummonedSwordBase;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WingToTheFutureSA extends SpecialAttackBase implements IJustSpecialAttack,ISuperSpecialAttack {

    @Override
    public String toString() {
        return "WingToTheFutureSA";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        int wingcount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-5, 1),3);

        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        int countdown = 1;
        if(!world.isRemote){

            List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player,30,45f);
            for (int i=0;i < wingcount;i++){

                int count = 1;
                for (int j=1;j<=32;j++){
                    count++;
                    countdown++;

                    boolean front = (count % 2 == 0);

                    int currentValue = count/2;
                    int countdownValue = countdown/2;

                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY+player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45){
                            playerPitch = 45;
                        }else if (playerPitch <= -45){
                            playerPitch =-45;
                        }
                        // 将朝向转换为弧度制
                        float yawRadians = (playerYaw - (front ? 120.0f: -120.0f)) / 180.0f * (float) Math.PI;
                        float pitchRadians = playerPitch / 180.0f * (float) Math.PI;

                        // 计算单位向量
                        double dirX = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                        double dirY = -Math.sin(pitchRadians);
                        double dirZ = Math.cos(yawRadians) * Math.cos(pitchRadians);
                        double distance = 1 + currentValue * 0.5f;
                        // 计算目标位置的坐标
                        double targetX = playerX + dirX * distance;
                        double targetY = playerY + dirY * distance;
                        double targetZ = playerZ + dirZ * distance;

                        entityDrive.setPosition(targetX, targetY, targetZ);
                        entityDrive.setInterval(20+countdownValue);
                        entityDrive.setLifeTime(200);
                        entityDrive.setScale(1.5f);
                        entityDrive.setBurst(true);
                        entityDrive.setIsOverWall(true);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setParticleVec(3);
                        entityDrive.setRoll(front ? -45.0f : 45.0f);
                        entityDrive.setDriveVector(0.3f+0.05f*currentValue);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        if (!target.isEmpty()){
                            entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));
                        }
                        world.spawnEntity(entityDrive);
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }


    @Override
    public void doJustSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        int wingcount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-5, 1),3);

        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        int countdown = 1;
        if(!world.isRemote){
            for (int i=0;i < wingcount;i++){

                int count = 1;
                for (int j=1;j<=32;j++){
                    count++;
                    countdown++;

                    boolean front = (count % 2 == 0);

                    int currentValue = count/2;
                    int countdownValue = countdown/2;

                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY+player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45){
                            playerPitch = 45;
                        }else if (playerPitch <= -45){
                            playerPitch =-45;
                        }
                        // 将朝向转换为弧度制
                        float yawRadians = (playerYaw - (front ? 120.0f: -120.0f)) / 180.0f * (float) Math.PI;
                        float pitchRadians = playerPitch / 180.0f * (float) Math.PI;

                        // 计算单位向量
                        double dirX = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                        double dirY = -Math.sin(pitchRadians);
                        double dirZ = Math.cos(yawRadians) * Math.cos(pitchRadians);
                        double distance = 1 + currentValue * 0.5f;
                        // 计算目标位置的坐标
                        double targetX = playerX + dirX * distance;
                        double targetY = playerY + dirY * distance;
                        double targetZ = playerZ + dirZ * distance;

                        entityDrive.setPosition(targetX, targetY, targetZ);
                        entityDrive.setInterval(20+20*i);
                        entityDrive.setLifeTime(200);
                        entityDrive.setScale(1.5f);
                        entityDrive.setBurst(false);
                        entityDrive.setIsOverWall(true);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setParticleVec(3);
                        entityDrive.setRoll(front ? -45.0f : 45.0f);
                        entityDrive.setDriveVector(0.3f+0.05f*currentValue);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }

    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {

        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        final int cost = -2000;
        if(!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)){
            return;
        }
        stack.setItemDamage(stack.getMaxDamage()/2);

        int wingcount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-5, 1),5);

        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));

        int countdown = 1;
        if(!world.isRemote){

            List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player,90,45f);
            for (int i=0;i < wingcount;i++){

                int count = 1;
                for (int j=1;j<=32;j++){
                    count++;
                    countdown++;

                    boolean front = (count % 2 == 0);

                    int currentValue = count/2;
                    int countdownValue = countdown/2;

                    EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY+player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45){
                            playerPitch = 45;
                        }else if (playerPitch <= -45){
                            playerPitch =-45;
                        }
                        // 将朝向转换为弧度制
                        float yawRadians = (playerYaw - (front ? 120.0f: -120.0f)) / 180.0f * (float) Math.PI;
                        float pitchRadians = playerPitch / 180.0f * (float) Math.PI;

                        // 计算单位向量
                        double dirX = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                        double dirY = -Math.sin(pitchRadians);
                        double dirZ = Math.cos(yawRadians) * Math.cos(pitchRadians);
                        double distance = 1 + currentValue * 0.75f;
                        // 计算目标位置的坐标
                        double targetX = playerX + dirX * distance;
                        double targetY = playerY + dirY * distance;
                        double targetZ = playerZ + dirZ * distance;
                        entityDrive.setInitialPosition(
                                targetX+(random.nextGaussian()*0.3),
                                targetY+(random.nextGaussian()*0.3),
                                targetZ+(random.nextGaussian()*0.3),
                                (float) random.nextInt(360),
                                (float) (random.nextInt(20)-90),
                                (float) (random.nextGaussian()*30),
                                2f);
                        entityDrive.setInterval(20+countdownValue*2);
                        entityDrive.setLifeTime(200+countdownValue*2);
                        entityDrive.setScale(1.5f);
                        entityDrive.setIsOverWall(true);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        if (!target.isEmpty()){
                            entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(countdownValue,target));
                        }
                        world.spawnEntity(entityDrive);
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);

    }

    private int[] color = new int[] {
            0xFF0000,
            0xFFAA00,
            0xFFFF00,
            0x00FF00,
            0x00CC00,
            0x00CCFF,
            0x0000FF,
            0xFF00FF
    };
}
//Yarimono!