package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.ColorUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class CrimsonStorm extends SpecialAttackBase implements IJustSpecialAttack, ISuperSpecialAttack {
    @Override
    public String toString() {
        return "CrimsonStorm";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        final int cost = -20;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }

        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif / 2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        float scale = Math.min(Math.max(player.experienceLevel / 10, 3), 6);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));


        if (!world.isRemote) {
            Random random = player.getRNG();
            {
                EntityOverCharge entityDrive = new EntityOverCharge(world, player, magicDamage);
                entityDrive.setColor(0xFF0000);
                entityDrive.setScale(scale);
                entityDrive.setInterval(40);
                entityDrive.setLifeTime(40);
                entityDrive.setIsOverWall(true);
                entityDrive.setInitialPosition(player.posX, player.posY + player.height / 2, player.posZ, player.rotationYaw, player.rotationPitch, 0, 0f);
                entityDrive.setMultiHit(true);
                world.spawnEntity(entityDrive);
            }
            {
                int count = 0;
                float multiple = scale * 3f;
                for (int z = 0; z < multiple; z++) {
                    float yaw = (360f / multiple) * count;
                    float pitch = (float) (random.nextGaussian() * 30f);
                    float roll = (float) (random.nextInt(360) - 180);
                    Vec3d basePos = new Vec3d(0, 0, 1);
                    Vec3d spawnPos = new Vec3d(player.posX, player.posY + player.height / 2, player.posZ)
                            .add(basePos
                                    .rotatePitch((float) Math.toRadians(pitch))
                                    .rotateYaw((float) Math.toRadians(yaw))
                                    .scale(5f));

                    EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                    entityDrive.setInitialPosition(
                            spawnPos.x,
                            spawnPos.y,
                            spawnPos.z,
                            -yaw,
                            -pitch,
                            roll,
                            1.75f
                    );
                    entityDrive.setInterval(40);
                    entityDrive.setColor(0xFF0000);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setScale(2f);
                    entityDrive.setLifeTime(40);
                    world.spawnEntity(entityDrive);
                    count++;
                }

            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
    }

    @Override
    public void doJustSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        final int cost = -2000;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            return;
        }
        stack.setItemDamage(stack.getMaxDamage() / 2);

        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif / 2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        float scale = Math.min(Math.max(player.experienceLevel / 10, 6), 8f);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        Entity target = getEntityToWatch(player);

        if (target == null) {
            target = player;
        }

        if (!world.isRemote) {
            Random random = player.getRNG();
            {
                EntityOverCharge entityDrive = new EntityOverCharge(world, player, magicDamage);
                entityDrive.setColor(0xFF0000);
                entityDrive.setScale(scale);
                entityDrive.setInterval(60);
                entityDrive.setLifeTime(60);
                entityDrive.setIsOverWall(true);
                entityDrive.setInitialPosition(target.posX, target.posY + target.height / 2, target.posZ, target.rotationYaw, target.rotationPitch, 0, 0f);
                entityDrive.setMultiHit(true);
                world.spawnEntity(entityDrive);
            }
            {
                int count = 0;
                float multiple = scale * 3f;
                for (int z = 0; z < multiple; z++) {
                    float yaw = (360f / multiple) * count;
                    float pitch = (float) (random.nextGaussian() * 30f);
                    float roll = (float) (random.nextInt(360) - 180);
                    Vec3d basePos = new Vec3d(0, 0, 1);
                    Vec3d spawnPos = new Vec3d(target.posX, target.posY + target.height / 2, target.posZ)
                            .add(basePos
                                    .rotatePitch((float) Math.toRadians(pitch))
                                    .rotateYaw((float) Math.toRadians(yaw))
                                    .scale(5f));
                    EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                    entityDrive.setInitialPosition(
                            spawnPos.x,
                            spawnPos.y,
                            spawnPos.z,
                            -yaw,
                            -pitch+180f,
                            roll,
                            1.75f
                    );
                    entityDrive.setInterval(10);
                    entityDrive.setColor(0xFF0000);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setScale(2f);
                    entityDrive.setParticle(EnumParticleTypes.FLAME);
                    entityDrive.setLifeTime(20);
                    world.spawnEntity(entityDrive);
                    count++;
                }

            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);

    }

    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif / 2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));

        magicDamage*=Math.max(rank,1);
        List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player, 30, player, true));
        if (!target.isEmpty()){
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,30 * 20,target.size()));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,30 * 20,target.size()));
            player.setAbsorptionAmount(player.getAbsorptionAmount()+target.size()*5);
            if (!world.isRemote) {
                Random random = player.getRNG();
                if (!target.isEmpty()) {
                    int count = 0;
                    for (EntityLivingBase targetEntity : target) {
                        float yaw = 360f / target.size()* count;
                        float pitch = 90f+(float)(random.nextGaussian() * 10f);
                        float roll = (float) (random.nextInt(360) - 180);
                        Vec3d basePos = new Vec3d(0, 0, 1);
                        Vec3d spawnPos = new Vec3d(targetEntity.posX, targetEntity.posY + targetEntity.height/2, targetEntity.posZ)
                                .add(basePos
                                        .rotatePitch((float) Math.toRadians(pitch))
                                        .rotateYaw((float) Math.toRadians(yaw))
                                        .scale(10f));

                        EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world, player, magicDamage);
                        entityDrive.setInitialPosition(
                                spawnPos.x,
                                spawnPos.y,
                                spawnPos.z,
                                -yaw,
                                -pitch-180f,
                                roll,
                                1.75f
                        );
                        entityDrive.setInterval(0);
                        entityDrive.setColor(0xFF0000);
                        entityDrive.setTargetEntityId(targetEntity.getEntityId());
                        entityDrive.setIsOverWall(true);
                        entityDrive.setScale(5f);
                        entityDrive.setLifeTime(60);
                        world.spawnEntity(entityDrive);
                        count++;
                    }
                }

            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriorosi);
    }

    private Entity getEntityToWatch(EntityPlayer player) {
        World world = player.world;
        Entity target = null;
        for (int dist = 2; dist < 60; dist += 2) {
            AxisAlignedBB bb = player.getEntityBoundingBox();
            Vec3d vec = player.getLookVec();
            vec = vec.normalize();
            bb = bb.grow(2.0f, 2.0f, 2.0f);
            bb = bb.offset(vec.x * (float) dist, vec.y * (float) dist, vec.z * (float) dist);

            List<Entity> list = world.getEntitiesInAABBexcluding(player, bb, EntitySelectorAttackable.getInstance());
            float distance = 60.0f;
            for (Entity curEntity : list) {
                float curDist = curEntity.getDistance(player);
                if (curDist < distance) {
                    target = curEntity;
                    distance = curDist;
                }
            }
            if (target != null)
                break;
        }
        return target;
    }

}