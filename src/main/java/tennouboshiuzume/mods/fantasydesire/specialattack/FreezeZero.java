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
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
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
            entitiDrive.setScale(5f);
            entitiDrive.setMultiHit(true);
            entitiDrive.setHitScale(10f);
            entitiDrive.setParticle(EnumParticleTypes.SNOW_SHOVEL);
            entitiDrive.setLifeTime(60);
            entitiDrive.setInterval(0);
            entitiDrive.setSound(SoundEvents.BLOCK_GLASS_BREAK,1,0.5f);
            world.spawnEntity(entitiDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
    }
}
