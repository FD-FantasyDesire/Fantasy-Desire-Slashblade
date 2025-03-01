package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class FollowFire implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "FollowFire";

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence) {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onUpdateEvent(SlashBladeEvent.OnUpdateEvent event) {
//        if(!useBlade(event.sequence)) return;
        if (!(event.blade.getItem() instanceof ItemFdSlashBlade)) return;
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ArdorBlossomStar", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player.getHeldItemMainhand() != event.blade) return;

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        ItemStack stack = event.blade;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        boolean burning = player.isBurning() || player.isPotionActive(MobEffects.FIRE_RESISTANCE);
        if (burning) {
            ItemFdSlashBlade.SpecialCharge.set(tag, Math.min(ItemFdSlashBlade.SpecialCharge.get(tag) + 5, 10000));
        } else {
            ItemFdSlashBlade.SpecialCharge.set(tag, Math.min(ItemFdSlashBlade.SpecialCharge.get(tag) - 2, 10000));
        }

        float heat = (float) ItemFdSlashBlade.SpecialCharge.get(tag) / 100;
        if (heat >= 95) {
//            劣 化 侵 蚀
            if (ItemSlashBlade.SpecialAttackType.get(tag) == 212) {
                ItemSlashBlade.SpecialAttackType.set(tag, 213);
            }
        }else {
            if (ItemSlashBlade.SpecialAttackType.get(tag) == 213) {
                ItemSlashBlade.SpecialAttackType.set(tag, 212);
            }
        }

        if (heat >= 75) {
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player, 10, player, true));
            for (EntityLivingBase entity : target) {
                entity.setFire(100);
//                entity.attackEntityFrom(DamageSource.IN_FIRE, 2);
            }
        }
//        感应度大于50%时，消耗感应度回复耐久
        if (heat >= 50 && stack.getItemDamage() > 0) {
            stack.setItemDamage(stack.getItemDamage() - 1);
            ItemFdSlashBlade.SpecialCharge.set(tag, Math.min(ItemFdSlashBlade.SpecialCharge.get(tag) - 50, 10000));
        }
//        状态栏提示
        if (heat > 0) {
            String formattedDamage = String.format("%.2f", heat);
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.HeatRate", formattedDamage)), true);
        }
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