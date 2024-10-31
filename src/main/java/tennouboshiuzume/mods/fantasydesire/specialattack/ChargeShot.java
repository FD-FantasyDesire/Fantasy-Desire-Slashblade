package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.*;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class ChargeShot extends SpecialAttackBase implements ISuperSpecialAttack {
    @Override
    public String toString() {
        return "ChargeShot";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ModernGunblade", 1).getUnlocalizedName())
        ){
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")),true);
            return;
        }
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
        magicDamage*=Math.max(rank,1);
        int color = 0x00FFFF;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") == SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") != SpecialEffects.State.Effective)
            color = 0xFFFF00;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") != SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") == SpecialEffects.State.Effective)
            color = 0xFF0000;

        if (!world.isRemote) {

            ItemSlashBlade.TextureName.set(tag,"named/SmartPistol_OC");
            ItemSlashBlade.SummonedSwordColor.set(tag,0x99FF00);
            ItemSlashBlade.SpecialAttackType.set(tag,202);

            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player,30,player,false));

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

                    entityDrive.setInitialPosition(circlePoint.x, playerPos.y + player.height/2, circlePoint.z, i * (360f / points) +player.rotationYaw -90, 0, 0, 3f);
                    entityDrive.setLifeTime(160);
                    entityDrive.setColor(isBurst ? color : 0xFFFFFF);
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
    @Override
    public void doSuperSpecialAttack(ItemStack stack,EntityPlayer player){
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){
            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(player, 60f, false);
            Collections.shuffle(target);

            int count = Math.max(3,Math.min(12,(int) Math.sqrt(player.experienceLevel)));

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);

            if(!target.isEmpty()){
                for (int i = 0;i< count; i++){
                    EntityLivingBase CTarget = target.get(i % target.size());
                    EntityBeam entityBeam = new EntityBeam(world,player,magicDamage);
                    entityBeam.setInitialPosition(CTarget.posX,CTarget.posY,CTarget.posZ, CTarget.rotationYaw, CTarget.rotationPitch,0,0.3f);
                    entityBeam.setLifeTime(60 * 20);
                    entityBeam.setInterval(100);
                    entityBeam.setIsOverWall(true);
                    entityBeam.setMultiHit(true);
                    entityBeam.setTargetingCenter(player);
                    entityBeam.setRange(30f);
                    entityBeam.setColor(0xFFFFFF);
                    entityBeam.setOnHitParticle(EnumParticleTypes.EXPLOSION_LARGE,0);
                    entityBeam.setTargetEntityId(CTarget.getEntityId());
                    entityBeam.setUpdateParticle(EnumParticleTypes.END_ROD,0.3f);
                    world.spawnEntity(entityBeam);
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriorosi);
    }
}