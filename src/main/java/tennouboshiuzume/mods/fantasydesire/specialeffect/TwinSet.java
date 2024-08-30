package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.enchantment.EnchantmentHelper;
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
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class TwinSet implements ISpecialEffect
{
    private static final String EffectKey = "TwinSet";

    /**
     * 使用コスト
     */
    private static final int COST = 2;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 1;

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence)
    {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.OnUpdateEvent event){
        if(!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        if (!(player.getHeldItemOffhand().getItem() instanceof ItemSlashBlade))return;
        ItemStack offBlade = player.getHeldItemOffhand();
        switch (SpecialEffects.isEffective(player, offBlade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        switch (SpecialEffects.isEffective(player, event.blade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ComboSequence seq = ItemSlashBlade.getComboSequence(tag);
        if (!useBlade(seq))
            return;
        if (ItemSlashBlade.IsBroken.get(tag).booleanValue())
            return;

        PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
        int check = haste != null ? haste.getAmplifier() != 1 ? 1 : 3 : 2;

        if (player.swingProgressInt != check)
            return;

        ItemSlashBlade blade = (ItemSlashBlade)event.blade.getItem();

        int rank = StylishRankManager.getStylishRank(player);
        if (player.getRNG().nextInt(9-rank) != 0) return;


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
}