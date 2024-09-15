package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class CheatRumble implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "CheatRumble";

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence){
        if(sequence.useScabbard) return false;
        if(sequence == ItemSlashBlade.ComboSequence.None) return false;
        if(sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }
//    该效果仅为还原原作角色一击必杀的效果，一般情况下玩家不可能获得
    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event){

        if(!useBlade(event.sequence)) return;
        if(!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;
        switch (SpecialEffects.isEffective(player, event.blade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        DamageSource CheatRumble = new DamageSource("CheatDamage").setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
        event.target.attackEntityFrom(CheatRumble,Float.MAX_VALUE);
        event.target.setHealth(0f);
//        event.target.setDead();
        System.out.println(String.format(player.getName()+"use ChikaFlare special ability killed"+ event.target.getName()));
        player.onEnchantmentCritical(event.target);
        UntouchableTime.setUntouchableTime(player, 200);

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

        UntouchableTime.setUntouchableTime(player, 200);

    }

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 800000;
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