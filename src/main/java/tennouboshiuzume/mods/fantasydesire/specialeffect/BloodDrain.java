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

import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class BloodDrain implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "BloodDrain";

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence){
        if(sequence.useScabbard) return false;
        if(sequence == ItemSlashBlade.ComboSequence.None) return false;
        if(sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event){

//        if(!useBlade(event.sequence)) return;
        if (!(event.blade.getItem() instanceof ItemFdSlashBlade))return;
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.CrimsonScythe", 1).getUnlocalizedName()))return;
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
        World world = player.world;
        ItemStack stack = event.blade;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade.ProudSoul.tryAdd(tag,2,false);
        Integer proudSoul = ItemSlashBlade.ProudSoul.get(tag);
        float addDamage = (float)  proudSoul/2000;
        NBTTagCompound example = ItemSlashBlade.getItemTagCompound(BladeUtils.getCustomBlade("tennouboshiuzume.slashblade.CrimsonScythe"));
        ItemSlashBlade.setBaseAttackModifier(tag,ItemSlashBlade.BaseAttackModifier.get(example) + addDamage);
//        状态栏提示
        String formattedDamage = String.format("%.2f", addDamage);
        player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.BloodDrainEv", formattedDamage)), true);
//        吸血
        player.setHealth(player.getHealth()+1);
        player.addPotionEffect(new PotionEffect(MobEffects.SATURATION,1,1));
        event.target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,20*3,3));
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