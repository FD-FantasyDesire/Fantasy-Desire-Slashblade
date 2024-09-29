package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityPhantomSwordBase;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FreezeZero extends SpecialAttackBase {

    @Override
    public String toString() {
        return "FreezeZero";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        //            检查进化等级
        float SAscale = 1;

        int evo_3 = 30000;
        int evo_2 = 3000;
        int evo_1 = 300;

        int proudSoul = ItemSlashBlade.ProudSoul.get(tag);

        if (proudSoul>=evo_3){
//            evo 3
            SAscale = 5;
        } else if (proudSoul>=evo_2) {
//            evo 2
            SAscale = 3;
        }else if (proudSoul>=evo_1){
//            evo 1
            SAscale = 2;
        }else {
//            evo 0
            SAscale = 1;
        }
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.OverCold", 1).getUnlocalizedName())){
            SAscale = 1;
        }

        if (!world.isRemote) {
            EntityOverChargeBFG entitiDrive = new EntityOverChargeBFG(world, player, magicDamage/6);
            entitiDrive.setInitialPosition(player.posX,
                    player.posY+player.height/2,
                    player.posZ,
                    0,
                    0,
                    0,
                    0);
            entitiDrive.setColor(0xAAFFFF);
            entitiDrive.setIsCold(true);
            entitiDrive.setScale(SAscale);
            entitiDrive.setMultiHit(true);
            entitiDrive.setHitScale(5f);
            entitiDrive.setIsOverWall(true);
            entitiDrive.setParticle(EnumParticleTypes.SNOW_SHOVEL);
            entitiDrive.setLifeTime(60);
            entitiDrive.setInterval(0);
            entitiDrive.setSound(SoundEvents.BLOCK_GLASS_BREAK,1,0.5f);
            world.spawnEntity(entitiDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
    }
}
