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
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;

import java.util.logging.Level;

/**
 * Created by Furia on 15/06/19.
 */

public class ImmortalSoul implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "ImmortalSoul";
//    当玩家死亡时，如果耀魂大于1000，免除该次死亡，并且攻击伤害+2
    @SubscribeEvent
    public void onPlayerHurt(LivingDeathEvent event){
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack blade ;
        if ((player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSlashBlade)){
            blade = player.getHeldItem(EnumHand.MAIN_HAND);
        } else if ((player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemSlashBlade)) {
            blade = player.getHeldItem(EnumHand.OFF_HAND);
        }else {
            return;
        }
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
        switch (SpecialEffects.isEffective(player,blade, FdSEs.ImmortalSoul)){
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
        World world = player.world;
        if (!ItemSlashBlade.ProudSoul.tryAdd( tag,-1000,false)) return;
        ItemSlashBlade.setBaseAttackModifier(tag,ItemSlashBlade.BaseAttackModifier.get(tag)+2);
        player.world.playSound(null,player.posX,player.posY,player.posZ,SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS,1.0f,2.0f);
        ParticleUtils.spawnParticle(world,EnumParticleTypes.END_ROD,false,player.posX,player.posY+player.height/2,player.posZ,200,0 ,0 ,0,0.5f);
        player.setHealth(player.getMaxHealth()/10);
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,20*6,5));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,20*6,10));
        event.setCanceled(true);
    }


    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 1;
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