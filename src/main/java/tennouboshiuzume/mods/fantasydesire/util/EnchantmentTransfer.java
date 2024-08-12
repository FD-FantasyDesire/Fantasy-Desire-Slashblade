package tennouboshiuzume.mods.fantasydesire.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentTransfer {

    /**
     * 将 sourceItem 和 targetItem 上的附魔合并，并将最高等级的附魔应用到 targetItem 上
     * @param sourceItem 源物品，附魔将从这个物品上获取并与目标物品的附魔合并
     * @param targetItem 目标物品，附魔将转移到这个物品上
     */
    public static void mergeEnchantments(ItemStack sourceItem, ItemStack targetItem) {
        // 获取源物品和目标物品的附魔
        Map<Enchantment, Integer> sourceEnchantments = EnchantmentHelper.getEnchantments(sourceItem);
        Map<Enchantment, Integer> targetEnchantments = EnchantmentHelper.getEnchantments(targetItem);

        // 用于存储合并后的附魔
        Map<Enchantment, Integer> mergedEnchantments = new HashMap<>(targetEnchantments);

        // 遍历源物品的附魔，将其与目标物品的附魔合并，取最高等级
        for (Map.Entry<Enchantment, Integer> entry : sourceEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int sourceLevel = entry.getValue();
            int targetLevel = mergedEnchantments.getOrDefault(enchantment, 0);

            // 取最高等级
            int maxLevel = Math.max(sourceLevel, targetLevel);

            // 更新合并后的附魔
            mergedEnchantments.put(enchantment, maxLevel);
        }

        // 将合并后的附魔设置到目标物品上
        EnchantmentHelper.setEnchantments(mergedEnchantments, targetItem);
    }
}
