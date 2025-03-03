package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntitySummonedSwordBase;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySeekSoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WingToTheFutureSA extends SpecialAttackBase implements IJustSpecialAttack, ISuperSpecialAttack {

    String name = "tennouboshiuzume.slashblade.ChikeFlare";

    @Override
    public String toString() {
        return "WingToTheFutureSA";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
//        一切刀转化
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName())) {
            if (player.getHealth() < 30) {
                ItemStack resultBlade = WorldBladeStandCrafting.crafting(stack, name);
                player.setHeldItem(EnumHand.MAIN_HAND, resultBlade);
                player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.WingToTheFutureSuccess")), true);
            } else {
                player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.WingToTheFutureFail")), true);
            }
            return;
        }
        World world = player.world;
        Random random = new Random();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        int wingCount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel)) - 5, 1), 3);
        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        int countdown = 1;
        if (!world.isRemote) {
            List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player, 30, 45f, true);
            for (int i = 0; i < wingCount; i++) {
                int count = 1;
                for (int j = 1; j <= 32; j++) {
                    count++;
                    countdown++;
                    boolean front = (count % 2 == 0);
                    int currentValue = count / 2;
                    int countdownValue = countdown / 2;
                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY + player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45) {
                            playerPitch = 45;
                        } else if (playerPitch <= -45) {
                            playerPitch = -45;
                        }
                        // 将朝向转换为弧度制
                        float yawRadians = (playerYaw - (front ? 120.0f : -120.0f)) / 180.0f * (float) Math.PI;
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
                        entityDrive.setInterval(20 + countdownValue);
                        entityDrive.setLifeTime(200 + countdownValue);
                        entityDrive.setScale(1.5f);
                        entityDrive.setBurst(true);
                        entityDrive.setIsOverWall(true);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setParticleVec(3);
                        entityDrive.setRoll(front ? -45.0f : 45.0f);
                        entityDrive.setDriveVector(0.3f + 0.05f * currentValue);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        if (!target.isEmpty()) {
                            entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i, target));
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
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName())) {
            if (player.getHealth() < 30) {
                ItemStack resultBlade = WorldBladeStandCrafting.crafting(stack, name);
                player.setHeldItem(EnumHand.MAIN_HAND, resultBlade);
                player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.WingToTheFutureSuccess")), true);
            } else {
                player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.WingToTheFutureFail")), true);
            }
            return;
        }
        World world = player.world;
        Random random = new Random();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        int wingCount = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel)) - 5, 1), 3);
        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        int countdown = 1;
        if (!world.isRemote) {
            List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player, 30, 45f, true);
            for (int i = 0; i < wingCount; i++) {
                int count = 1;
                for (int j = 1; j <= 32; j++) {
                    count++;
                    countdown++;
                    boolean front = (count % 2 == 0);
                    int currentValue = count / 2;
                    int countdownValue = countdown / 2;
                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY + player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45) {
                            playerPitch = 45;
                        } else if (playerPitch <= -45) {
                            playerPitch = -45;
                        }
                        // 将朝向转换为弧度制
                        float yawRadians = (playerYaw - (front ? 120.0f : -120.0f)) / 180.0f * (float) Math.PI;
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
                        entityDrive.setInterval(20 + countdownValue * 4);
                        entityDrive.setLifeTime(200+ countdownValue * 4);
                        entityDrive.setScale(1.5f);
                        entityDrive.setBurst(true);
                        entityDrive.setExpRadius(1);
                        entityDrive.setIsOverWall(true);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setParticleVec(3);
                        entityDrive.setTrueDamage(true, 1);
                        entityDrive.setRoll(front ? -45.0f : 45.0f);
                        entityDrive.setDriveVector(0.3f + 0.05f * currentValue);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        if (!target.isEmpty()) {
                            entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(currentValue, target));
                        }
                        world.spawnEntity(entityDrive);
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }

    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName())) {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.WingToTheFutureFail")), true);
            return;
        }
        World world = player.world;
        Random random = new Random();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        final int cost = -2000;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            return;
        }
        stack.setItemDamage(stack.getMaxDamage() / 2);
        StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        magicDamage *= Math.max(rank, 1);
        int countdown = 1;
        if (!world.isRemote) {
            List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player, 120f, 45f, false);
            int wingCount = Math.max(3, target.size() / 8);
            for (int i = 0; i < wingCount; i++) {
                int count = 1;
                for (int j = 1; j <= 32; j++) {
                    count++;
                    countdown++;
                    boolean front = (count % 2 == 0);
                    int currentValue = count / 2;
                    int countdownValue = countdown / 2;
                    EntitySeekSoulPhantomSword entityDrive = new EntitySeekSoulPhantomSword(world, player, magicDamage);
                    if (entityDrive != null) {
                        double playerX = player.posX;
                        double playerY = player.posY + player.eyeHeight;
                        double playerZ = player.posZ;
                        float playerYaw = player.rotationYaw;
                        float playerPitch = -player.rotationPitch;
                        if (playerPitch >= 45) {
                            playerPitch = 45;
                        } else if (playerPitch <= -45) {
                            playerPitch = -45;
                        }
                        float yawRadians = (playerYaw - (front ? 120.0f : -120.0f)) / 180.0f * (float) Math.PI;
                        float pitchRadians = playerPitch / 180.0f * (float) Math.PI;
                        double dirX = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                        double dirY = -Math.sin(pitchRadians);
                        double dirZ = Math.cos(yawRadians) * Math.cos(pitchRadians);
                        double distance = 1 + currentValue * 0.75f;
                        double targetX = playerX + dirX * distance;
                        double targetY = playerY + dirY * distance;
                        double targetZ = playerZ + dirZ * distance;
                        entityDrive.setInitialPosition(
                                targetX + (random.nextGaussian() * 0.5),
                                targetY + (random.nextGaussian() * 0.5),
                                targetZ + (random.nextGaussian() * 0.5),
                                (float) random.nextInt(360),
                                (float) (random.nextInt(20) - 90),
                                (float) (random.nextGaussian() * 30),
                                1f);
                        entityDrive.setInterval(20 + countdownValue * 1);
                        entityDrive.setLifeTime(200 + countdownValue * 1);
                        entityDrive.setScale(1.5f);
                        if (!target.isEmpty()) {
                            entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(countdownValue, target));
                        }
                        entityDrive.setIsOverWall(true);
                        entityDrive.setTrueDamage(true, 20);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setColor(front ? 0xFFFF00 : 0x00FFFF);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
//Yarimono!