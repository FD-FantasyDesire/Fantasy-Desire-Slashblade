package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

/**
 * Created by Furia on 15/06/19.
 */
public class FlameDrain implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "FlameDrain";

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event) {
//        if(!useBlade(event.sequence)) return;
        if (!(event.blade.getItem() instanceof ItemFdSlashBlade)) return;
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ArdorBlossomStar", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        ItemStack stack = event.blade;
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        if (event.target.isBurning() || event.target.isImmuneToFire()) {
            ItemFdSlashBlade.SpecialCharge.set(tag, Math.min(ItemFdSlashBlade.SpecialCharge.get(tag) + 20, 10000));
            DamageSource OverHeatFlame = new EntityDamageSource("OverHeatFlame", player).setDamageIsAbsolute();
            event.target.attackEntityFrom(OverHeatFlame,magicDamage);
            event.target.extinguish();
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