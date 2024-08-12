package tennouboshiuzume.mods.fantasydesire.util;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WorldBladeStandCrafting {
    public static ItemStack crafting(ItemStack targetBlade,String name){
        ItemStack resultBlade = BladeUtils.getCustomBlade(name);
//      获取参与合成的刀的参数
        NBTTagCompound req = ItemSlashBlade.getItemTagCompound(targetBlade);
        int killCount = ItemSlashBlade.KillCount.get(req);
        int refine = ItemSlashBlade.RepairCount.get(req);
        int proudSoul = ItemSlashBlade.ProudSoul.get(req);
//      构造成品刀参数
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(resultBlade);
        ItemSlashBlade.KillCount.set(tag,killCount);
        ItemSlashBlade.ProudSoul.set(tag,proudSoul);
        ItemSlashBlade.RepairCount.set(tag,refine);
//      合并附魔
        EnchantmentTransfer.mergeEnchantments(targetBlade,resultBlade);
        return resultBlade;
    }
}
