package tennouboshiuzume.mods.fantasydesire.specialeffect;

import ibxm.Player;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;


public class SoulShield implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "SoulShield";
//      原始效果，已取消
//    private boolean useBlade(ItemSlashBlade.ComboSequence sequence){
//        if(sequence.useScabbard) return false;
//        if(sequence == ItemSlashBlade.ComboSequence.None) return false;
//        if(sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
//        return true;
//    }
//
//    @SubscribeEvent
//    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event){
//
//        if(!SpecialEffects.isPlayer(event.entity)) return;
//        EntityPlayer player = (EntityPlayer) event.entity;
//
//        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
//        if(!useBlade(ItemSlashBlade.getComboSequence(tag))) return;
//
//        switch (SpecialEffects.isEffective(player,event.blade,this)){
//            case None:
//                return;
//            case NonEffective:
//                return;
//            case Effective:
//                if (player.getRNG().nextInt(4) != 0) return;
//                break;
//        }
//
//        PotionEffect haste = player.getActivePotionEffect(MobEffects.MINING_FATIGUE);
//        int check = haste != null ? haste.getAmplifier() != 1 ? 3 : 4 : 2;
//
//        if (player.swingProgressInt != check) return;
//
//        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,20 * 30, 0));
//        player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING,20 * 30,0));
//    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSlashBlade)) return;
        ItemStack blade = player.getHeldItem(EnumHand.MAIN_HAND);
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
        if (!blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))
            return;
        switch (SpecialEffects.isEffective(player, blade, FdSEs.SoulShield)) {
            /** 任何时候可触发 */
            case None:
                return;
            /** 未达到所需等级 */
            case NonEffective:
                return;
            /** 达到所需等级 */
            case Effective:
                break;
        }
        if (MathUtils.randomCheck(25)) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1.0f, 2.0f);
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.END_ROD, false, player.posX, player.posY + player.height / 2, player.posZ, 2, 0, 0, 0, 0.2f);
            event.setCanceled(true);
            blade.damageItem(1, player);
            player.setAbsorptionAmount(20);
//            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION,20 * 5,4));
        } else {
            float originalDamage = event.getAmount();
            float reducedDamage = Math.min(originalDamage, 5f);
            event.setAmount(reducedDamage);
        }
    }


    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 5;
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