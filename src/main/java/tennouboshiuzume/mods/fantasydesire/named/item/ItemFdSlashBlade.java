package tennouboshiuzume.mods.fantasydesire.named.item;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.TagPropertyAccessor;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;

import tennouboshiuzume.mods.fantasydesire.util.BladeUtils.*;


import static tennouboshiuzume.mods.fantasydesire.util.BladeUtils.*;
import static tennouboshiuzume.mods.fantasydesire.util.ItemUtils.FD_BLADE;

/**
 * @author Cat,AbbyQAQ,Moflop,520
 * @updateDate 2020/02/13
 */
public class ItemFdSlashBlade extends ItemSlashBladeNamed {
    public ItemFdSlashBlade(ToolMaterial par2EnumToolMaterial, float baseAttackModifiers,String name) {
        super(par2EnumToolMaterial, baseAttackModifiers);
        this.setUnlocalizedName("tennouboshiuzume.Fantasydesire." + name);
        this.setRegistryName(name);
        ForgeRegistries.ITEMS.register(this);
        FD_BLADE.add(this);
    }

    public static TagPropertyAccessor.TagPropertyBoolean isInCreativeTab = new TagPropertyAccessor.TagPropertyBoolean("isInCreativeTab");
    public static TagPropertyAccessor.TagPropertyBoolean isFdBlade = new TagPropertyAccessor.TagPropertyBoolean("isFdBlade");
    public static TagPropertyAccessor.TagPropertyString bladeType = new TagPropertyAccessor.TagPropertyString("bladeType");

    /** œ‘ æ…Òµ∂ */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationSwordClass(ItemStack par1ItemStack,
                                         EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

        EnumSet<SwordType> swordType = getSwordType(par1ItemStack);
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);

        if(swordType.contains(SwordType.Enchanted)){
            if(swordType.contains(SwordType.Bewitched)){
                if (isFdBlade.get(tag)){
                    par3List.add(I18n.format("tennouboshiuzume.info."+bladeType.get(tag)));
                } else if(tag.hasUniqueId("Owner")) {
                    par3List.add(String.format("°Ï6%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                } else {
                    par3List.add(String.format("°Ï5%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                }
            }else{
                par3List.add(String.format("°Ï3%s", I18n.format("flammpfeil.swaepon.info.magic")));
            }
        }else{
            par3List.add(String.format("°Ï8%s", I18n.format("flammpfeil.swaepon.info.noname")));
        }
    }

    /** ∑¿÷π∞Œµ∂Ω£÷ÿ∏¥◊¢≤· */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for(String bladename : FdNamedBlades){
                ItemStack blade = getCustomBlade(bladename);
                NBTTagCompound tag = getItemTagCompound(blade);
//                BaseAttackModifier.set(tag,0.0F);
                isInCreativeTab.set(tag,true);
//                if(blade.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//                    blade.setItemDamage(0);
//                }
                if(!blade.isEmpty()) {
                    subItems.add(blade);
                }
            }
        }
    }
}
