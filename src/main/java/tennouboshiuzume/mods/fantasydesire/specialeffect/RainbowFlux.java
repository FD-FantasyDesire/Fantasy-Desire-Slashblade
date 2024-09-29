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
import tennouboshiuzume.mods.fantasydesire.util.ColorUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;



public class RainbowFlux implements ISpecialEffect {
    private static final String EffectKey = "RainbowFlux";

    private static final int COST = 1;

    private static final int NO_COST_DAMAGE = 0;

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence) {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event) {

        if (!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);

        World world = player.world;
//        System.out.println(world.getWorldTime());

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case NonEffective:
                return;
            case Effective:
                break;
        }

        int color = ColorUtils.getSmoothTransitionColor(((int) world.getWorldTime()%120),120,true);
        ItemSlashBlade.SummonedSwordColor.set(tag, color);

        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.PureSnow", 1).getUnlocalizedName()))return;

        ItemSlashBlade.ComboSequence seq = ItemSlashBlade.getComboSequence(tag);
        if (!useBlade(seq)) return;
//        PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
//        int check = haste != null ? haste.getAmplifier() != 1 ? 1 : 3 : 2;
        int check = 1;
        if (player.swingProgressInt != check) return;
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

        float magicDamage = baseModif / 2;
        int rank = StylishRankManager.getStylishRank(player);
        if (rank >= 5) {
            magicDamage += (float) (ItemSlashBlade.AttackAmplifier.get(tag)*(level/5.0 + 0.5f));
        }
        EntityDriveEx entityDrive = new EntityDriveEx(world,player,magicDamage);
        entityDrive.setInitialPosition(player.getLookVec().scale(2f).x + player.posX,
                player.getLookVec().scale(2f).y + player.posY + player.eyeHeight - entityDrive.height,
                player.getLookVec().scale(2f).z + player.posZ,
                player.rotationYaw,
                player.rotationPitch,
                setCombo.swingDirection+90f,
                3f);
        entityDrive.setColor(ItemSlashBlade.SummonedSwordColor.get(tag));
        entityDrive.setLifeTime(10);
        entityDrive.setScale(3f);
        entityDrive.setIsOverWall(true);
        world.spawnEntity(entityDrive);


        UntouchableTime.setUntouchableTime(player, 40);

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
}