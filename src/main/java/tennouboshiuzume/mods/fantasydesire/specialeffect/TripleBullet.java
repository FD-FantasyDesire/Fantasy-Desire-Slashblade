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
import tennouboshiuzume.mods.fantasydesire.entity.*;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class TripleBullet implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "TripleBullet";

    /**
     * 使用コスト
     */
    private static final int COST = 2;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 1;

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence) {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event) {
        if (!SpecialEffects.isPlayer(event.entity))
            return;
        EntityPlayer player = (EntityPlayer) event.entity;

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

        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ModernGunblade", 1).getUnlocalizedName())
        ) {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")), true);
            return;
        }
        doAddAttack(event.blade, player, seq);
    }

    public void doAddAttack(ItemStack stack, EntityPlayer player, ItemSlashBlade.ComboSequence setCombo) {
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        World world = player.world;

        if (ItemSlashBlade.SpecialAttackType.get(tag) != 201) return;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -COST, false)) {
            ItemSlashBlade.damageItem(stack, NO_COST_DAMAGE, player);
            return;
        }

        if (world.isRemote)
            return;

        float baseModif = ((ItemSlashBlade) stack.getItem()).getBaseAttackModifiers(tag);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        float magicDamage = baseModif + level;
        int rank = StylishRankManager.getStylishRank(player);
        if (rank >= 5) {
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0 + 0.5f);
        }

        int color = 0x00FFFF;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") == SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") != SpecialEffects.State.Effective)
            color = 0xFFFF00;
        if (SpecialEffects.isEffective(player,stack,"ThunderBullet") != SpecialEffects.State.Effective && SpecialEffects.isEffective(player,stack,"ExplosionBullet") == SpecialEffects.State.Effective)
            color = 0xFF0000;

        Random random = player.getRNG();
//            追踪弹
        List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player, 60, 30f, true);

        target.sort(Comparator.comparingDouble(player::getDistance));

        int round = Math.min(Math.max(player.experienceLevel / 10, 1), 5);
        for (int i = 0; i < round; i++) {
            EntitySeekSoulPhantomSword entityDrive = new EntitySeekSoulPhantomSword(world, player, magicDamage);
            entityDrive.setInterval(1 + i);
            entityDrive.setScale(0.2f);
            entityDrive.setSound(SoundEvents.ENTITY_WITHER_SHOOT, 3f, 2f);
            entityDrive.setColor(color);
            entityDrive.setInitialPosition(player.getLookVec().x + player.posX,
                    player.getLookVec().y + player.posY + 1.2f,
                    player.getLookVec().z + player.posZ,
                    (float) (player.rotationYaw + random.nextGaussian()),
                    (float) (player.rotationPitch + random.nextGaussian()),
                    random.nextInt(180),
                    3f);
            entityDrive.setLifeTime(100 + i);
            if (!target.isEmpty()) {
                entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i, target));
                TargetUtils.setTargetEntityFromListByEntity(i, target).addPotionEffect(new PotionEffect(MobEffects.GLOWING, 20 * 6, 0));
            }
            world.spawnEntity(entityDrive);
        }
    }

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 10;
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