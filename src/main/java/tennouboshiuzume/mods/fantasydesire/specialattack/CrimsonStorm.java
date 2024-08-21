package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;

/**
 * Created by Furia on 14/05/27.
 */
public class CrimsonStorm extends SpecialAttackBase {
    @Override
    public String toString() {
        return "CrimsonStorm";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){
            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            float baseModif = blade.getBaseAttackModifiers(tag);
            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float magicDamage = (baseModif/2.0f);

            int rank = StylishRankManager.getStylishRank(player);
            float scale = Math.min(Math.max(player.experienceLevel/10,3),10);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
            EntityOverCharge entityDrive = new EntityOverCharge(world,player,magicDamage);
            entityDrive.setColor(0xFF0000);
            entityDrive.setScale(scale);
            entityDrive.setInterval(40);
            entityDrive.setLifeTime(40);
            entityDrive.setIsOverWall(true);
            entityDrive.setInitialPosition(player.posX,player.posY+player.height/2,player.posZ,player.rotationYaw,player.rotationPitch,0,0f);
            entityDrive.setMultiHit(true);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
    }
}