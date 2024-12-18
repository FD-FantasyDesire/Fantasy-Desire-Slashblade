package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class ABMS extends SpecialAttackBase implements ISuperSpecialAttack {
    @Override
    public String toString() {
        return "ABMS";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ArdorBlossomStar", 1).getUnlocalizedName()))
            return;
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if (ItemFdSlashBlade.SpecialCharge.get(tag) < 9500) return;

        final int cost = -100;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 10, player);
        }
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        magicDamage *= Math.max(rank, 1);
        float range = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack)) * Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
//        清除感应值
        ItemFdSlashBlade.SpecialCharge.set(tag, 0);
        if (!world.isRemote) {
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player, 20, false));
            for (EntityLivingBase entity : target) {
                DamageSource OverHeatFlame = new EntityDamageSource("OverHeatFlame", player).setDamageIsAbsolute();
                entity.attackEntityFrom(OverHeatFlame,magicDamage);
                if (entity.isBurning()||entity.isImmuneToFire()){
                    world.createExplosion(player, entity.posX, entity.posY + entity.height / 2f, entity.posZ, range , false);
                }
            }

            UntouchableTime.setUntouchableTime(player, 20);
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Iai);
    }
//      绝对会对你家造成毁灭性后果的超SA
    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ArdorBlossomStar", 1).getUnlocalizedName()))
            return;
        World world = player.world;
        Random random = new Random();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if (ItemFdSlashBlade.SpecialCharge.get(tag) < 9500) return;
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();
        float range = 5 * Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack)) * Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack));
        ItemFdSlashBlade.SpecialCharge.set(tag, 0);

        final int cost = -2000;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            return;
        }
        stack.setItemDamage(stack.getMaxDamage() / 2);

        if (!world.isRemote) {
            world.createExplosion(player, player.posX, player.posY + player.height / 2f, player.posZ, range, true);
            UntouchableTime.setUntouchableTime(player, 20);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Iai);
    }
}