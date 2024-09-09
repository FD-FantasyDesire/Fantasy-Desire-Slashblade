package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;

/**
 * Created by Furia on 14/05/27.
 */
public class TwinSystemR extends SpecialAttackBase {
    @Override
    public String toString() {
        return "TwinSystemR";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        ItemStack offBlade = player.getHeldItemOffhand();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if ((ItemSlashBlade.SpecialAttackType.get(tag)==209)){
            ItemSlashBlade.SpecialAttackType.set(tag,208);
            ItemFdSlashBlade.bladeType.set(tag,"TwinBladeL");
            ItemSlashBlade.TextureName.set(tag,"named/TwinBladeLeft");
            ItemSlashBlade.SummonedSwordColor.set(tag, 0x5555FF);
        }
        if (!(player.getHeldItemOffhand().getItem() instanceof ItemSlashBlade))return;
        NBTTagCompound offTag = ItemSlashBlade.getItemTagCompound(offBlade);

        if ((ItemSlashBlade.SpecialAttackType.get(offTag)==208)){
            ItemSlashBlade.SpecialAttackType.set(offTag,209);
            ItemFdSlashBlade.bladeType.set(offTag,"TwinBladeR");
            ItemSlashBlade.TextureName.set(offTag,"named/TwinBladeRight");
            ItemSlashBlade.SummonedSwordColor.set(offTag, 0xFF5555);
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
                -Math.sin(Math.toRadians(player.rotationPitch)) * playerDist,
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
            {
                EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world, player, magicDamage);
                entityDrive.setIsBlackhole(true);
                entityDrive.setScale(5f);
                entityDrive.setHitScale(count);
                entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, 0, 0, 0, 0);
                entityDrive.setIsOverWall(true);
                entityDrive.setMultiHit(true);
                entityDrive.setColor(0x5555FF);
                entityDrive.setLifeTime(80);
                world.spawnEntity(entityDrive);
            }
            {
                EntityOverCharge entityDrive = new EntityOverCharge(world, player, magicDamage);
                entityDrive.setScale(3f);
                entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, 90, 90, 0, 0);
                entityDrive.setIsOverWall(true);
                entityDrive.setMultiHit(true);
                entityDrive.setColor(0xFF5555);
                entityDrive.setInterval(0);
                entityDrive.setSound(SoundEvents.ENTITY_WITHER_BREAK_BLOCK,3,0.5f);
                entityDrive.setLifeTime(80);
                world.spawnEntity(entityDrive);
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.ReturnEdge);
    }
}