package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StatMultipler extends SpecialAttackBase {
    @Override
    public String toString() {
        return "StatMultipler";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
        ItemSlashBlade.ProudSoul.tryAdd(tag, 10000,false);
        ItemSlashBlade.KillCount.tryAdd(tag,10000,false);
//        StylishRankManager.setRankRate(7);
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
