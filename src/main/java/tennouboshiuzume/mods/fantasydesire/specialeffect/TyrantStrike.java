package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;

import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class TyrantStrike implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "TyrantStrike";

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence){
        if(sequence.useScabbard) return false;
        if(sequence == ItemSlashBlade.ComboSequence.None) return false;
        if(sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event){

//        if(!useBlade(event.sequence)) return;

        if(!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;

        switch (SpecialEffects.isEffective(player, event.blade, this)){
            case None:
                return;
            case Effective:
                if (event.target.getRNG().nextInt(10) != 0) return;
                break;
            case NonEffective:
                return;
        }
        World world = player.world;
        ItemStack stack = event.blade;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
        Entity target = event.target;

        if(target instanceof EntityLivingBase){
            blade.setDaunting((EntityLivingBase)target);
            ((EntityLivingBase) target).hurtTime = 0;
            ((EntityLivingBase) target).hurtResistantTime = 0;
        }
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
        magicDamage += event.target.getMaxHealth()/4;
//        System.out.println(magicDamage);
//        生成尖碑幻影剑
        if (!world.isRemote){
            if (target != null && !target.isDead){
                EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world,player,magicDamage);
                entityDrive.setInterval(20);
                entityDrive.setScale(2.5f);
                entityDrive.setLifeTime(120);
                entityDrive.setColor(0xFF0000);
                entityDrive.setParticle(EnumParticleTypes.EXPLOSION_LARGE);
                entityDrive.setIsOverWall(true);
                Random random = player.getRNG();
                entityDrive.setInitialPosition(
                        target.posX+random.nextGaussian()*2,
                        target.posY+30f+random.nextGaussian()*2,
                        target.posZ+random.nextGaussian()*2,
                        0f,
                        90,
                        0,
                        5f
                );
                entityDrive.setDriveVector(5.0f);
                entityDrive.setTargetEntityId(target.getEntityId());
                world.spawnEntity(entityDrive);
            }
        }
        event.target.playSound(SoundEvents.ITEM_FIRECHARGE_USE,32.0f,2.0f);

        player.onEnchantmentCritical(event.target);
        event.target.addPotionEffect(new PotionEffect(MobEffects.GLOWING,20*5,0));
        event.target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,20*5,3));

    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event){

        if(!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        if(!useBlade(ItemSlashBlade.getComboSequence(tag))) return;

        switch (SpecialEffects.isEffective(player,event.blade,this)){
            case None:
                return;
            case NonEffective:
                return;
            case Effective:
                break;
        }

        PotionEffect haste = player.getActivePotionEffect(MobEffects.MINING_FATIGUE);
        int check = haste != null ? haste.getAmplifier() != 1 ? 3 : 4 : 2;

        if (player.swingProgressInt != check) return;

        player.addPotionEffect(new PotionEffect(MobEffects.HASTE,20 * 15,1));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,20 * 5,2));

    }

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 60;
    }

    @Override
    public String getEffectKey() {
        return EffectKey;
    }

    @Override
    public boolean canCopy(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return false;
    }
}