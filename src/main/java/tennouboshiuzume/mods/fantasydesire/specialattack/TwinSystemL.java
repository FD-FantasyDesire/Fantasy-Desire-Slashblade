package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;

/**
 * Created by Furia on 14/05/27.
 */
public class TwinSystemL extends SpecialAttackBase {
    @Override
    public String toString() {
        return "TwinSystemL";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        ItemStack offBlade = player.getHeldItemOffhand();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if ((ItemSlashBlade.SpecialAttackType.get(tag)==208)){
            ItemSlashBlade.SpecialAttackType.set(tag,209);
            ItemFdSlashBlade.bladeType.set(tag,"TwinBladeR");
            ItemSlashBlade.TextureName.set(tag,"named/TwinBladeRight");
            ItemSlashBlade.SummonedSwordColor.set(tag, 0xFF5555);
        }
        if (!(player.getHeldItemOffhand().getItem() instanceof ItemSlashBlade))return;
        NBTTagCompound offTag = ItemSlashBlade.getItemTagCompound(offBlade);

        if ((ItemSlashBlade.SpecialAttackType.get(offTag)==209)){
            ItemSlashBlade.SpecialAttackType.set(offTag,208);
            ItemFdSlashBlade.bladeType.set(offTag,"TwinBladeL");
            ItemSlashBlade.TextureName.set(offTag,"named/TwinBladeLeft");
            ItemSlashBlade.SummonedSwordColor.set(offTag, 0x5555FF);
        }

        switch (SpecialEffects.isEffective(player, offBlade, FdSEs.TwinSet)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        switch (SpecialEffects.isEffective(player, stack, FdSEs.TwinSet)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }

        double playerDist = 9;
        if (!player.onGround)
            playerDist *= 0.33f;
        ReflectionAccessHelper.setVelocity(player,
                -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        int count = Math.min(Math.max(player.experienceLevel/10,1),5);

        if(!world.isRemote){

            final int cost = -20;
            if(!ItemSlashBlade.ProudSoul.tryAdd(tag,cost,false)){
                ItemSlashBlade.damageItem(stack, 10, player);
            }
            for (int i = 0;i<count;i++){
                {
                    EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                    entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                    entityDrive.setScale(3f);
                    entityDrive.setInterval(i*2);
                    entityDrive.setLifeTime(100+i*2);
                    entityDrive.setColor(0xFF5555);
                    entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                    entityDrive.setParticle(EnumParticleTypes.END_ROD);
                    world.spawnEntity(entityDrive);
                }
                {
                    EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                    entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                    entityDrive.setScale(3f);
                    entityDrive.setInterval(i*2);
                    entityDrive.setLifeTime(100+i*2);
                    entityDrive.setColor(0x5555FF);
                    entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                    entityDrive.setParticle(EnumParticleTypes.END_ROD);
                    world.spawnEntity(entityDrive);
                }
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);


    }
}