package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.*;

public class EnchantmentTransfer {

    /**
     * 将 sourceItem 和 targetItem 上的附魔合并，并将最高等级的附魔应用到 targetItem 上
     * @param sourceItem 源物品，附魔将从这个物品上获取并与目标物品的附魔合并
     * @param targetItem 目标物品，附魔将转移到这个物品上
     */
//    public static void mergeEnchantments(ItemStack sourceItem, ItemStack targetItem) {
//        Map<Enchantment, Integer> newItemEnchants = EnchantmentHelper.getEnchantments(targetItem);
//        Map<Enchantment, Integer> oldItemEnchants = EnchantmentHelper.getEnchantments(sourceItem);
//        System.out.println(newItemEnchants);
//        System.out.println(oldItemEnchants);
//        Iterator var9 = oldItemEnchants.keySet().iterator();
//
//        while(var9.hasNext()) {
//            Enchantment enchantIndex = (Enchantment)var9.next();
//            Enchantment enchantment = enchantIndex;
//            int destLevel = newItemEnchants.containsKey(enchantIndex) ? (Integer)newItemEnchants.get(enchantIndex) : 0;
//            int srcLevel = (Integer)oldItemEnchants.get(enchantIndex);
//            srcLevel = Math.max(srcLevel, destLevel);
//            boolean canApplyFlag = enchantIndex.canApply(targetItem);
//            if (canApplyFlag) {
//                Iterator var15 = newItemEnchants.keySet().iterator();
//
//                while(var15.hasNext()) {
//                    Enchantment curEnchantIndex = (Enchantment)var15.next();
//                    if (curEnchantIndex != enchantIndex && !enchantment.isCompatibleWith(curEnchantIndex)) {
//                        canApplyFlag = false;
//                        break;
//                    }
//                }
//
//                if (canApplyFlag) {
//                    newItemEnchants.put(enchantIndex, srcLevel);
//                }
//            }
//        }
//
//        EnchantmentHelper.setEnchantments(newItemEnchants, targetItem);
//    }
    public static void mergeEnchantments(ItemStack sourceItem, ItemStack targetItem){
        // 获取目标物品和源物品的附魔
        Map<Enchantment, Integer> newItemEnchants = EnchantmentHelper.getEnchantments(targetItem);
        Map<Enchantment, Integer> oldItemEnchants = EnchantmentHelper.getEnchantments(sourceItem);

        // 遍历源物品附魔
        for (Enchantment enchantment : oldItemEnchants.keySet()) {
            // 获取源物品和目标物品的附魔等级
            int destLevel = newItemEnchants.getOrDefault(enchantment, 0);
            int srcLevel = oldItemEnchants.get(enchantment);

            // 选择最高等级的附魔
            srcLevel = Math.max(srcLevel, destLevel);

            // 无视附魔冲突，直接将源物品的附魔应用到目标物品
            newItemEnchants.put(enchantment, srcLevel);
        }

        // 更新目标物品的附魔
        EnchantmentHelper.setEnchantments(newItemEnchants, targetItem);
    }
}
