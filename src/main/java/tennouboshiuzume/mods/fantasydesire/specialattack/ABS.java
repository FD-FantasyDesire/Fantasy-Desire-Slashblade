package tennouboshiuzume.mods.fantasydesire.specialattack;

import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_zh_TW;
import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class ABS extends SpecialAttackBase {
    @Override
    public String toString() {
        return "ABS";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ArdorBlossomStar", 1).getUnlocalizedName()))
            return;
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if (ItemFdSlashBlade.SpecialCharge.get(tag) > 9500) return;
        final int cost = -10;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 1, player);
        }
//        点燃玩家
//        备忘录：前三个坐标是粒子原点，后三个坐标是粒子运动向量，我要做个以自身为环形施放的效果：（
//        嘿，还真给我做出来了
        player.playSound(SoundEvents.ITEM_FIRECHARGE_USE,1,1);
        Vec3d pos1 = new Vec3d(0,0,5);
        int particles = 60;
        for (int i = 0; i < particles; i++) {
            Vec3d pos = pos1.rotateYaw(i*((float) 360 /particles)).normalize().scale(0.25f);
            world.spawnParticle(EnumParticleTypes.FLAME,player.posX,player.posY+1,player.posZ,pos.x,pos.y,pos.z);
        }
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,60 * 20,20,true,false));
        player.setFire(60 * 20);
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Iai);
    }
}
