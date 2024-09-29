package tennouboshiuzume.mods.fantasydesire.named.item;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.TagPropertyAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import org.lwjgl.input.Keyboard;


import static tennouboshiuzume.mods.fantasydesire.util.BladeUtils.*;
import static tennouboshiuzume.mods.fantasydesire.util.ItemUtils.FD_BLADE;

/**
 * @author Cat, AbbyQAQ, Moflop, 520
 * @updateDate 2020/02/13
 */
public class ItemFdSlashBlade extends ItemSlashBladeNamed {
    public ItemFdSlashBlade(ToolMaterial par2EnumToolMaterial, float baseAttackModifiers, String name) {
        super(par2EnumToolMaterial, baseAttackModifiers);
        this.setUnlocalizedName("tennouboshiuzume.Fantasydesire." + name);
        this.setRegistryName(name);
        ForgeRegistries.ITEMS.register(this);
        FD_BLADE.add(this);
    }

    public static TagPropertyAccessor.TagPropertyBoolean isInCreativeTab = new TagPropertyAccessor.TagPropertyBoolean("isInCreativeTab");
    public static TagPropertyAccessor.TagPropertyBoolean isFdBlade = new TagPropertyAccessor.TagPropertyBoolean("isFdBlade");
    public static TagPropertyAccessor.TagPropertyString bladeType = new TagPropertyAccessor.TagPropertyString("bladeType");
    public static TagPropertyAccessor.TagPropertyFloat lockRange = new TagPropertyAccessor.TagPropertyFloat("lockRange");


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, World world, List par3List, ITooltipFlag inFlag) {
        EntityPlayer par2EntityPlayer = Minecraft.getMinecraft().player;
        boolean par4 = inFlag.isAdvanced();
        if (par2EntityPlayer != null) {
            NBTTagCompound tag = getItemTagCompound(par1ItemStack);
            this.addInformationOwner(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationSwordClass(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationKillCount(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationProudSoul(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationSpecialAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationRepairCount(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationRangeAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationSpecialEffec(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationMaxAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationBladeLore(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationEffectLore(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationCrafting(par1ItemStack, par2EntityPlayer, par3List, par4);
            if (tag.hasKey("adjustX")) {
                float ax = tag.getFloat("adjustX");
                float ay = tag.getFloat("adjustY");
                float az = tag.getFloat("adjustZ");
                par3List.add(String.format("adjust x:%.1f y:%.1f z:%.1f", ax, ay, az));
            }
            this.addInformationEnergy(par1ItemStack, par2EntityPlayer, par3List, par4);
        }
    }

    /**
     * œ‘ æ…Òµ∂
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationSwordClass(ItemStack par1ItemStack,
                                         EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

        EnumSet<SwordType> swordType = getSwordType(par1ItemStack);
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);

        if (swordType.contains(SwordType.Enchanted)) {
            if (swordType.contains(SwordType.Bewitched)) {
                if (isFdBlade.get(tag)) {
                    par3List.add(I18n.format("tennouboshiuzume.info." + bladeType.get(tag)));
                } else if (tag.hasUniqueId("Owner")) {
                    par3List.add(String.format("°Ï6%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                } else {
                    par3List.add(String.format("°Ï5%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                }
            } else {
                par3List.add(String.format("°Ï3%s", I18n.format("flammpfeil.swaepon.info.magic")));
            }
        } else {
            par3List.add(String.format("°Ï8%s", I18n.format("flammpfeil.swaepon.info.noname")));
        }
    }


    /**
     * ∑¿÷π∞Œµ∂Ω£÷ÿ∏¥◊¢≤·
     */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for (String bladename : FdNamedBlades) {
                ItemStack blade = getCustomBlade(bladename);
                if (!blade.isEmpty()) {
                    subItems.add(getCustomBlade(bladename));
                }
                NBTTagCompound tag = getItemTagCompound(blade);
//                BaseAttackModifier.set(tag,0.0F);
                isInCreativeTab.set(tag, true);
//                if(blade.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//                    blade.setItemDamage(0);
//                }
                if (!blade.isEmpty()) {
                    subItems.add(blade);
                }
            }
        }
    }

    //   Ãÿ ‚Œƒ±æ
    @SideOnly(Side.CLIENT)
    public void addInformationBladeLore(ItemStack par1ItemStack,
                                        EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (isFdBlade.get(tag) && !isInCreativeTab.get(tag) && tag.hasKey("BladeLore")) {
            NBTTagList BladeLore = tag.getTagList("BladeLore", 8);
            String name = getUnlocalizedName(par1ItemStack);
            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
                for (int i = 0; i < BladeLore.tagCount(); i++) {
                    String loreText = BladeLore.getStringTagAt(i);
                    par3List.add(I18n.format(name + "." + loreText));

                }
            }
        }
    }

    //    SE–ßπ˚ΩÈ…‹
    @SideOnly(Side.CLIENT)
    public void addInformationEffectLore(ItemStack par1ItemStack,
                                         EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (isFdBlade.get(tag) && !isInCreativeTab.get(tag) && tag.hasKey("EffectLore")) {
            NBTTagList EffectLore = tag.getTagList("EffectLore", 8);
            String name = getUnlocalizedName(par1ItemStack);

            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
                par3List.add("°Ï7" + I18n.format("tennouboshiuzume.slashblade.info.hold", new Object[0]) + " °Ïe°Ïo" + I18n.format("tennouboshiuzume.slashblade.info.shift", new Object[0]) + " °Ïr°Ï7" + I18n.format("tennouboshiuzume.slashblade.info.forEffectDetails", new Object[0]) + "°Ïr");
            } else {
                for (int i = 0; i < EffectLore.tagCount(); i++) {
                    String loreText = EffectLore.getStringTagAt(i);
                    par3List.add(I18n.format(name + "." + loreText));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformationCrafting(ItemStack par1ItemStack,
                                       EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        String bladename = getUnlocalizedName(par1ItemStack);
        if (isFdBlade.get(tag) && isInCreativeTab.get(tag)) {
            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
                par3List.add(I18n.format(bladename + ".secret"));
            } else {
                par3List.add(I18n.format(bladename + ".crafting"));
            }

        }
    }
}
