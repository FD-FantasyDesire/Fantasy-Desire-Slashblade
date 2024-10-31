package tennouboshiuzume.mods.fantasydesire.creativetab;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

import java.util.Iterator;

public class FdTabs {
    public static final CreativeTabs Fd_Item= new CreativeTabs("FantasyDesire")
    {
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return (BladeUtils.findItemStack(FantasyDesire.MODID,"tennouboshiuzume.slashblade.ChikeFlare",1));
        }
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);
            items.add(getRecipeOutput("WOF_Soul"));
            items.add(getRecipeOutput("SMS_Soul"));
            items.add(customSoul("RainbowFlux",10));
            items.add(getRecipeOutput("ItemMagnet"));
            items.add(getRecipeOutput("SentientHook"));
            items.add(getRecipeOutput("PrismFlux"));
            items.add(getRecipeOutput("CounterBlade"));
            items.add(customSoul("ExplosionBullet",60));
            items.add(customSoul("ThunderBullet",60));
            items.add(customSoul("SoulForging",0));
        }
        public ItemStack getRecipeOutput(String recipeId) {
            // 使用配方ID查询配方
            IRecipe recipe = CraftingManager.REGISTRY.getObject(new ResourceLocation(FantasyDesire.MODID,recipeId));
//            System.out.println(recipe);

            // 检查配方是否存在，返回产物物品
            if (recipe != null) {
//                System.out.println(recipe.getRecipeOutput());
                return recipe.getRecipeOutput();
            }

            // 若找不到配方，返回空物品
            return ItemStack.EMPTY;
        }
        public ItemStack customSoul (String specialEffects,int level){
            ItemStack crystalSoul =SlashBlade.findItemStack(SlashBlade.modid,SlashBlade.CrystalBladeSoulStr,1);
            NBTTagCompound tag = new NBTTagCompound();
            crystalSoul.setTagCompound(tag);
            SpecialEffects.addEffect(crystalSoul, specialEffects,level);
            NBTTagCompound displayTag = new NBTTagCompound();
            crystalSoul.setTagInfo("display",displayTag);
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tennouboshiuzume.info."+specialEffects+".crafting")));
            displayTag.setTag("Lore",lore);
            return crystalSoul;
        }
    };
}
