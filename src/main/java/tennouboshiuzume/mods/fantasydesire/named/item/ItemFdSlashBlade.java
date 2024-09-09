package tennouboshiuzume.mods.fantasydesire.named.item;

import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.TagPropertyAccessor;
import mods.flammpfeil.slashblade.ability.SoulEater;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.ability.Taunt;
import mods.flammpfeil.slashblade.ability.UpthrustBlast;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.util.InventoryUtility;
import mods.flammpfeil.slashblade.util.SilentUpdateItem;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

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
    public static TagPropertyAccessor.TagPropertyFloat lockRange = new TagPropertyAccessor.TagPropertyFloat("lockRange");

    /** ��ʾ�� */
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
                    par3List.add(String.format("��6%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                } else {
                    par3List.add(String.format("��5%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                }
            }else{
                par3List.add(String.format("��3%s", I18n.format("flammpfeil.swaepon.info.magic")));
            }
        }else{
            par3List.add(String.format("��8%s", I18n.format("flammpfeil.swaepon.info.noname")));
        }
    }

    /** ��ֹ�ε����ظ�ע�� */
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
