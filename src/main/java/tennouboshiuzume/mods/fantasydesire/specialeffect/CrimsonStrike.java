package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class CrimsonStrike implements ISpecialEffect
{
    private static final String EffectKey = "CrimsonStrike";

    /**
     * 使用コスト
     */
    private static final int COST = 0;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 0;

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence)
    {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event)
    {
        if (!SpecialEffects.isPlayer(event.entity))
            return;
        EntityPlayer player = (EntityPlayer)event.entity;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ComboSequence seq = ItemSlashBlade.getComboSequence(tag);
        if (!useBlade(seq))
            return;
        if (ItemSlashBlade.IsBroken.get(tag).booleanValue())
            return;

        if (SpecialEffects.isEffective(player, event.blade, this) != SpecialEffects.State.Effective) {
            return;
        }

        PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
        int check = haste != null ? haste.getAmplifier() != 1 ? 1 : 3 : 2;
//        System.out.println(player.swingProgressInt);

        if (player.swingProgressInt != check)
            return;

        doAddAttack(event.blade, player, seq);
    }

    public void doAddAttack(ItemStack stack, EntityPlayer player, ItemSlashBlade.ComboSequence setCombo)
    {
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        World world = player.world;

        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -COST, false)) {
            ItemSlashBlade.damageItem(stack, NO_COST_DAMAGE, player);
            return;
        }

        if (world.isRemote)
            return;

        float baseModif = ((ItemSlashBlade)stack.getItem()).getBaseAttackModifiers(tag);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        float magicDamage = baseModif / 10f;
        int rank = StylishRankManager.getStylishRank(player);
        if (rank >= 5) {
            magicDamage += (float) (ItemSlashBlade.AttackAmplifier.get(tag)*(level/5.0 + 0.5f));
        }
        for (int i=0;i<2;i++){
            EntityDriveEx entityDrive = new EntityDriveEx(world,player,magicDamage);
            entityDrive.setInitialPosition(player.posX,player.posY+player.height/2,player.posZ, player.rotationYaw, player.rotationPitch,setCombo.swingDirection+(i%2==0? 0:90f),0.3f);
            entityDrive.setColor(0xFF0000);
            entityDrive.setLifeTime(60);
            entityDrive.setScale((i%2==0? 0.5f:1.5f));
            entityDrive.setIsOverWall(true);
            world.spawnEntity(entityDrive);
        }

        UntouchableTime.setUntouchableTime(player, 40);

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