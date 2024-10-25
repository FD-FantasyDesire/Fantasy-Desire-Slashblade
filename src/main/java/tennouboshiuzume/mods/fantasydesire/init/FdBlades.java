package tennouboshiuzume.mods.fantasydesire.init;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.creativetab.FdTabs;
import tennouboshiuzume.mods.fantasydesire.named.*;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.specialeffect.PrismFlux;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

/**
 * @author Cat
 * @updateDate 2020/02/14
 */
public class FdBlades {
    public static final Item Fd_BLADE = new ItemFdSlashBlade(Item.ToolMaterial.IRON, 4.0f,"fdSlashBlade")
            .setMaxDamage(40)
            .setCreativeTab(FdTabs.Fd_Item);

    public FdBlades(){
        loadBlade();
        addSoulRecipe();
    }

    public void loadBlade(){
        loadBlade(new ChikeFlare());
        loadBlade(new ModernGunblade());
        loadBlade(new CrimsonScythe());
        loadBlade(new TwinBlade());
        loadBlade(new OverCold());
        loadBlade(new PureSnow());
    }


    public void loadBlade(Object blade) {
        SlashBlade.InitEventBus.register(blade);
    }

    ItemStack sphereSoulOrg = SlashBlade.findItemStack(SlashBlade.modid, SlashBlade.SphereBladeSoulStr, 1);
    ItemStack quadSoulOrg = SlashBlade.findItemStack(SlashBlade.modid, SlashBlade.TrapezohedronBladeSoulStr, 1);
    ItemStack crystalSoulOrg =SlashBlade.findItemStack(SlashBlade.modid,SlashBlade.CrystalBladeSoulStr,1);
    public void addSoulRecipe(){
        {
//            翱向未来之翼 SA球
            NBTTagCompound saTag = new NBTTagCompound();
            ItemStack sphereSoul = sphereSoulOrg;
            sphereSoul.setTagCompound(saTag);
            ItemSlashBlade.SpecialAttackType.set(saTag, 200);
            NBTTagCompound displayTag = new NBTTagCompound();
            sphereSoul.setTagInfo("display",displayTag);
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tennouboshiuzume.info.ChikeFlare.crafting")));
            displayTag.setTag("Lore",lore);

            SlashBlade.addRecipe("WOF_SA",
                    new ShapedOreRecipe(
                            new ResourceLocation(FantasyDesire.MODID, "WOF_Soul"),
                            sphereSoul,
                            "CAC",
                            "BDB",
                            "CAC",
                            'A', new ItemStack(Items.TOTEM_OF_UNDYING),
                            'B', new ItemStack(Items.NETHER_STAR),
                            'C', new ItemStack(Items.GOLDEN_APPLE,1,1),
                            'D', BladeUtils.findItemStack("flammpfeil.slashblade", "sphere_bladesoul", 1)
                    ));
        }
        {
//            真·磁暴幻影剑 SA球
            NBTTagCompound saTag = new NBTTagCompound();
            ItemStack sphereSoul = sphereSoulOrg;
            sphereSoul.setTagCompound(saTag);
            ItemSlashBlade.SpecialAttackType.set(saTag, 204);
            NBTTagCompound displayTag = new NBTTagCompound();
            sphereSoul.setTagInfo("display",displayTag);
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tennouboshiuzume.info.NR.SMS")));
            displayTag.setTag("Lore",lore);

            SlashBlade.addRecipe("MagnetStorm",
                    new ShapedOreRecipe(new ResourceLocation(FantasyDesire.MODID, "SMS_Soul"),
                            sphereSoul,
                            " AC",
                            "BDB",
                            "CA ",
                            'A', new ItemStack(Items.DIAMOND),
                            'B', new ItemStack(Items.NETHER_STAR),
                            'C', new ItemStack(Blocks.STAINED_GLASS,1,3),
                            'D', BladeUtils.findItemStack("flammpfeil.slashblade", "sphere_bladesoul", 1)
                    ));
        }
        {
//            棱光通量 晶态魂
            NBTTagCompound tag = new NBTTagCompound();
            ItemStack crystalSoul = crystalSoulOrg;
            crystalSoul.setTagCompound(tag);
            SpecialEffects.addEffect(crystalSoul, "PrismFlux",50);
            NBTTagCompound displayTag = new NBTTagCompound();
            crystalSoul.setTagInfo("display",displayTag);
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tennouboshiuzume.info.PrismFlux.crafting")));
            displayTag.setTag("Lore",lore);
//            tag.setInteger("RepairCost", -80);


            SlashBlade.addRecipe("PrismFlux",
                    new ShapedOreRecipe(new ResourceLocation(FantasyDesire.MODID, "PrismFlux"),
                            crystalSoul,
                            "ABC",
                            "DEF",
                            "GHI",
                            'A', new ItemStack(Blocks.STAINED_GLASS,1,14),
                            'B', new ItemStack(Blocks.STAINED_GLASS,1,1),
                            'C', new ItemStack(Blocks.STAINED_GLASS,1,5),
                            'D', new ItemStack(Blocks.STAINED_GLASS,1,4),
                            'E', crystalSoulOrg,
                            'F', new ItemStack(Blocks.STAINED_GLASS,1,11),
                            'G', new ItemStack(Blocks.STAINED_GLASS,1,3),
                            'H', new ItemStack(Blocks.STAINED_GLASS,1,10),
                            'I', new ItemStack(Blocks.STAINED_GLASS,1,2)
                    ));
        }
        {
//            棱光通量 晶态魂
            NBTTagCompound tag = new NBTTagCompound();
            ItemStack crystalSoul = crystalSoulOrg;
            crystalSoul.setTagCompound(tag);
            SpecialEffects.addEffect(crystalSoul, "SentientHook",10);
            NBTTagCompound displayTag = new NBTTagCompound();
            crystalSoul.setTagInfo("display",displayTag);
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tennouboshiuzume.info.SentientHook.crafting")));
            displayTag.setTag("Lore",lore);
//            tag.setInteger("RepairCost", -80);

            SlashBlade.addRecipe("SentientHook",
                    new ShapedOreRecipe(new ResourceLocation(FantasyDesire.MODID, "SentientHook"),
                            crystalSoul,
                            "  A",
                            "  B",
                            "CC ",
                            'A', sphereSoulOrg,
                            'B', new ItemStack(Items.LEAD),
                            'C', new ItemStack(Items.STRING)
                    ));
        }

    }

}
