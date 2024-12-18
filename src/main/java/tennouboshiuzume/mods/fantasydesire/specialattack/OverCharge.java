package tennouboshiuzume.mods.fantasydesire.specialattack;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityBeam;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Furia on 14/05/27.
 */
public class OverCharge extends SpecialAttackBase implements ISuperSpecialAttack {
    @Override
    public String toString() {
        return "OverCharge";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ModernGunblade", 1).getUnlocalizedName())
        ){
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")),true);
            return;
        }
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        final int cost = -20;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        int color = 0x99FF00;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") == SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") != SpecialEffects.State.Effective)
            color = 0xFFFF00;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") != SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") == SpecialEffects.State.Effective)
            color = 0xFF0000;
        int count = Math.max(Math.min(player.experienceLevel/10,7),2);

        if(!world.isRemote){
            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
            ItemSlashBlade.TextureName.set(tag,"named/SmartPistol");
            ItemSlashBlade.SummonedSwordColor.set(tag,0x00FFFF);
            ItemSlashBlade.SpecialAttackType.set(tag,201);

            float baseModif = blade.getBaseAttackModifiers(tag);
            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float magicDamage = 1.0f+(baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
            magicDamage*=Math.max(rank,1);

            EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world,player,magicDamage);
            entityDrive.setColor(color);
            entityDrive.setBFG(true);
            entityDrive.setScale(3f);
            entityDrive.setHitScale(count);
            entityDrive.setExpRadius(10f);
            entityDrive.setLifeTime(40);
            entityDrive.setIsOverWall(false);
            entityDrive.setIsBlast(true);
            entityDrive.setInitialPosition(player.posX,player.posY,player.posZ,player.rotationYaw,player.rotationPitch,0,0.5f);
            entityDrive.setMultiHit(true);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.HiraTuki);
    }
    @Override
    public void doSuperSpecialAttack(ItemStack stack,EntityPlayer player){
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){
            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(player,60f, player , false);
//            target.remove(player);
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
                System.out.println(target);
                for (int i = 0;i< count; i++){
                    EntityLivingBase CTarget = target.get(i % target.size());
                    EntityBeam entityBeam = new EntityBeam(world,player,magicDamage);
                    entityBeam.setInitialPosition(CTarget.posX,CTarget.posY,CTarget.posZ, CTarget.rotationYaw, CTarget.rotationPitch,0,0.3f);
                    entityBeam.setLifeTime(60 * 20);
                    entityBeam.setInterval(100);
                    entityBeam.setIsOverWall(true);
                    entityBeam.setMultiHit(true);
                    entityBeam.setTargetingCenter(player.getEntityId());
                    entityBeam.setRange(30f);
                    entityBeam.setColor(0xFFFFFF);
                    entityBeam.setOnHitParticle(EnumParticleTypes.EXPLOSION_LARGE,0);
                    entityBeam.setTargetEntityId(CTarget.getEntityId());
                    entityBeam.setUpdateParticle(EnumParticleTypes.END_ROD,0.3f);
                    world.spawnEntity(entityBeam);
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.HiraTuki);
    }
}