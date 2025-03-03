package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityTwinSlashManager;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

/**
 * Created by Furia on 14/05/27.
 */
public class TwinSystemR extends SpecialAttackBase implements ISuperSpecialAttack {
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
            ItemSlashBlade.SummonedSwordColor.set(tag, 0x00C8FF);
        }
        if (!(offBlade.getItem() instanceof ItemSlashBlade))return;
        NBTTagCompound offTag = ItemSlashBlade.getItemTagCompound(offBlade);

        if (!offBlade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.TwinBlade", 1).getUnlocalizedName())
        ){
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.TwinSetFail")),true);
            return;
        }

        if ((ItemSlashBlade.SpecialAttackType.get(offTag)==208)){
            ItemSlashBlade.SpecialAttackType.set(offTag,209);
            ItemFdSlashBlade.bladeType.set(offTag,"TwinBladeR");
            ItemSlashBlade.TextureName.set(offTag,"named/TwinBladeRight");
            ItemSlashBlade.SummonedSwordColor.set(offTag, 0xFF0089);
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
        final int cost = -20;
        if(!ItemSlashBlade.ProudSoul.tryAdd(tag,cost,false)){
            ItemSlashBlade.damageItem(stack, 10, player);
        }

        if(!world.isRemote){
            {
                EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world, player, magicDamage);
                entityDrive.setIsBlackhole(true);
                entityDrive.setScale(5f);
                entityDrive.setHitScale(count);
                entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, 0, 0, 0, 0);
                entityDrive.setIsOverWall(true);
                entityDrive.setMultiHit(true);
                entityDrive.setColor(0x00C8FF);
                entityDrive.setLifeTime(80);
                world.spawnEntity(entityDrive);
            }
            {
                EntityOverCharge entityDrive = new EntityOverCharge(world, player, magicDamage);
                entityDrive.setScale(3f);
                entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, 90, 90, 0, 0);
                entityDrive.setIsOverWall(true);
                entityDrive.setMultiHit(true);
                entityDrive.setColor(0xFF0089);
                entityDrive.setInterval(0);
                entityDrive.setSound(SoundEvents.ENTITY_WITHER_BREAK_BLOCK,3,0.5f);
                entityDrive.setLifeTime(80);
                world.spawnEntity(entityDrive);
            }
        }

        UntouchableTime.setUntouchableTime(player, 20);

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.ReturnEdge);
    }
    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        ItemStack offBlade = player.getHeldItemOffhand();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
//        检查是否双持双子

        if ((ItemSlashBlade.SpecialAttackType.get(tag)==208)){
            ItemSlashBlade.SpecialAttackType.set(tag,209);
            ItemFdSlashBlade.bladeType.set(tag,"TwinBladeR");
            ItemSlashBlade.TextureName.set(tag,"named/TwinBladeRight");
            ItemSlashBlade.SummonedSwordColor.set(tag, 0xFF0089);
        }
        if (!(offBlade.getItem() instanceof ItemSlashBlade))return;
        NBTTagCompound offTag = ItemSlashBlade.getItemTagCompound(offBlade);

        if (!offBlade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.TwinBlade", 1).getUnlocalizedName())
        ){
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.TwinSetFail")),true);
            return;
        }

        if ((ItemSlashBlade.SpecialAttackType.get(offTag)==209)){
            ItemSlashBlade.SpecialAttackType.set(offTag,208);
            ItemFdSlashBlade.bladeType.set(offTag,"TwinBladeL");
            ItemSlashBlade.TextureName.set(offTag,"named/TwinBladeLeft");
            ItemSlashBlade.SummonedSwordColor.set(offTag, 0x00C8FF);
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
        if (!world.isRemote){
            EntityTwinSlashManager entitydrive = new EntityTwinSlashManager(world,player);
            world.spawnEntity(entitydrive);
        }

    }

}