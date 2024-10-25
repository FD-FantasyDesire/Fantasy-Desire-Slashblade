package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

/**
 * Created by Furia on 14/05/27.
 */
public class OverCharge extends SpecialAttackBase {
    @Override
    public String toString() {
        return "OverCharge";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        if (!stack.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.MordernGunblade", 1).getUnlocalizedName())
        ){
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")),true);
            return;
        }
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){
            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
            ItemSlashBlade.TextureName.set(tag,"named/SmartPistol");
            ItemSlashBlade.SummonedSwordColor.set(tag,0x00FFFF);
            ItemSlashBlade.SpecialAttackType.set(tag,201);

            float baseModif = blade.getBaseAttackModifiers(tag);
            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float magicDamage = 1.0f+(baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
            magicDamage*=Math.max(rank,1);

            EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world,player,magicDamage);
            entityDrive.setColor(0x99FF00);
            entityDrive.setBFG(true);
            entityDrive.setScale(3f);
            entityDrive.setHitScale(7f);
            entityDrive.setExpRadius(10f);
            entityDrive.setLifeTime(40);
            entityDrive.setIsOverWall(false);
            entityDrive.setIsBlast(true);
            entityDrive.setInitialPosition(player.posX,player.posY,player.posZ,player.rotationYaw,player.rotationPitch,0,0.5f);
            entityDrive.setMultiHit(true);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}