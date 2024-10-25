package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class SentientHook implements ISpecialEffect, IRemovable
{
    private static final String EffectKey = "SentientHook";

    /**
     * 使用コスト
     */
    private static final int COST = 5;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 1;

//    private boolean useBlade(ItemSlashBlade.ComboSequence sequence)
//    {
//        if (sequence.useScabbard) return false;
//        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
//        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
//        return true;
//    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event)
    {
        if (!SpecialEffects.isPlayer(event.entity))
            return;

        EntityPlayer player = (EntityPlayer)event.entity;

        if (SpecialEffects.isEffective(player, event.blade, this) != SpecialEffects.State.Effective) {
            return;
        }
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ComboSequence seq = ItemSlashBlade.getComboSequence(tag);

        if (!seq.useScabbard) return;
        if (seq == ItemSlashBlade.ComboSequence.None) return;
        if (seq == ItemSlashBlade.ComboSequence.Noutou) return;

        if (ItemSlashBlade.IsBroken.get(tag).booleanValue())
            return;

        System.out.println(seq);
        System.out.println(player.swingProgressInt);
        Hook(player,player.swingProgressInt != 0);
    }
    public void Hook(EntityPlayer player,boolean pull){
        List<EntityLivingBase> list = TargetUtils.findHostileEntitiesInFOV(player,90,30,true);
        if (!list.isEmpty()){
            for (EntityLivingBase target :list){
                if (target.getDistance(player)>5){
                    if (pull){
                        pullEntityTowards(target,player.posX,player.posY+player.eyeHeight,player.posZ,0.2);
                        if (!player.world.isRemote){
                            ParticleUtils.spawnParticleLine(player.world, EnumParticleTypes.WATER_BUBBLE,player.posX,player.posY+player.height/2,player.posZ,target.posX,target.posY+target.height/2,target.posZ,30);
                        }
                    }
                }
            }
        }
    }

    private void pullEntityTowards(Entity entity, double targetX, double targetY, double targetZ, double strength) {

        double dx = targetX - entity.posX;
        double dy = targetY - entity.posY;
        double dz = targetZ - entity.posZ;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0) {
            double pullFactor = strength / distance;
            entity.motionX += dx * pullFactor;
            entity.motionY += dy * pullFactor;
            entity.motionZ += dz * pullFactor;
            entity.velocityChanged = true;
        }
    }


    @Override
    public void register()
    {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel()
    {
        return 10;
    }

    @Override
    public String getEffectKey()
    {
        return EffectKey;
    }

    @Override
    public boolean canCopy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return true;
    }
}