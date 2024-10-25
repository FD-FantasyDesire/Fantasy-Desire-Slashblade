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
import net.minecraft.client.resources.I18n;
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
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class EnergyBullet implements ISpecialEffect, IRemovable
{
    private static final String EffectKey = "EnergyBullet";

    /**
     * 使用コスト
     */
    private static final int COST = 5;

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
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.MordernGunblade", 1).getUnlocalizedName())
        ) {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")), true);
            return;
        }
        doAddAttack(event.blade, player, seq);
    }

    public void doAddAttack(ItemStack stack, EntityPlayer player, ItemSlashBlade.ComboSequence setCombo)
    {
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        World world = player.world;

        if (ItemSlashBlade.SpecialAttackType.get(tag)!=202) return;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -COST, false)) {
            ItemSlashBlade.damageItem(stack, NO_COST_DAMAGE, player);
            return;
        }

        if (world.isRemote)
            return;

        float baseModif = ((ItemSlashBlade)stack.getItem()).getBaseAttackModifiers(tag);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        float magicDamage = baseModif + level;
        int rank = StylishRankManager.getStylishRank(player);
        if (rank >= 5) {
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag)*(level/5.0f + 0.5f);
        }
        Random random = player.getRNG();
        EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world,player,magicDamage);
        entityDrive.setInitialPosition(player.getLookVec().x+player.posX,player.getLookVec().y+player.posY+1.3f,player.getLookVec().z+player.posZ, player.rotationYaw, player.rotationPitch,0f,2f);
        entityDrive.setInterval(0);
        entityDrive.setIsOverWall(true);
        entityDrive.setMultiHit(true);
        entityDrive.setHitScale(3f);
        entityDrive.setSound(SoundEvents.ENTITY_SHULKER_SHOOT,2,2f);
        entityDrive.setParticle(EnumParticleTypes.VILLAGER_HAPPY);
        entityDrive.setLifeTime(100);
        entityDrive.setColor(0x99FF00);
        world.spawnEntity(entityDrive);
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
        return false;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return false;
    }
}