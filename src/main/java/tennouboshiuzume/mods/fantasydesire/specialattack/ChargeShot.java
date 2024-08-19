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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;

/**
 * Created by Furia on 14/05/27.
 */
public class ChargeShot extends SpecialAttackBase {
    @Override
    public String toString() {
        return "ChargeShot";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){


            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
            ItemSlashBlade.TextureName.set(tag,"named/SmartPistol_OC");
            ItemSlashBlade.SummonedSwordColor.set(tag,0x99FF00);
            ItemSlashBlade.SpecialAttackType.set(tag,202);

            float baseModif = blade.getBaseAttackModifiers(tag);
            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float magicDamage = (baseModif/2.0f);

            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage *= ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
//            EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world,player,magicDamage);
//            entityDrive.setColor(0x99FF00);
//            entityDrive.setBFG(true);
//            entityDrive.setScale(3f);
//            entityDrive.setHitScale(7f);
//            entityDrive.setExpRadius(10f);
//            entityDrive.setLifeTime(40);
//            entityDrive.setIsOverWall(false);
//            entityDrive.setIsBlast(true);
//            entityDrive.setInitialPosition(player.posX,player.posY,player.posZ,player.rotationYaw,player.rotationPitch,0,0.5f);
//            entityDrive.setMultiHit(true);
            EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world,player,magicDamage);
            Vec3d pos = player.getLookVec().scale(2.5);
            entityDrive.setIsGrowing(true);
            entityDrive.setIsRolling(true);
            entityDrive.setScale(2.5f);
            entityDrive.setIsOverWall(true);
            entityDrive.setColor(0x00FFFF);
            entityDrive.setLifeTime(100);
            entityDrive.setInterval(40);
            entityDrive.setBurst(true);
            entityDrive.setExpRadius(15);
            entityDrive.setInitialPosition(player.posX+pos.x,player.posY+pos.y+1.35f,player.posZ+pos.z,player.rotationYaw,player.rotationPitch,0f,0.5f);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}