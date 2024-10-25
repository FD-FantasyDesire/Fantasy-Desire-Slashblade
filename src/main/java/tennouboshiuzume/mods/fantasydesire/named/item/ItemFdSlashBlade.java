package tennouboshiuzume.mods.fantasydesire.named.item;

import com.mojang.authlib.GameProfile;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.TagPropertyAccessor;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import org.lwjgl.input.Keyboard;

import static tennouboshiuzume.mods.fantasydesire.util.BladeUtils.*;
import static tennouboshiuzume.mods.fantasydesire.util.ItemUtils.FD_BLADE;

public class ItemFdSlashBlade extends ItemSlashBladeNamed {
    private static ResourceLocationRaw texture = new ResourceLocationRaw("flammpfeil.slashblade", "model/blade.png");
    private ResourceLocationRaw model = new ResourceLocationRaw("flammpfeil.slashblade", "model/blade.obj");

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
            this.addInformationBladeBound(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationKillCount(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationProudSoul(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationSpecialAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationRepairCount(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationRangeAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationSpecialEffec(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationMaxAttack(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationBladeLore(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationEffectLore(par1ItemStack, par2EntityPlayer, par3List, par4);
            this.addInformationAttackLore(par1ItemStack, par2EntityPlayer, par3List, par4);
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

    //    显示自定刀铭
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
                    par3List.add(String.format("§6%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                } else {
                    par3List.add(String.format("§5%s", I18n.format("flammpfeil.swaepon.info.bewitched")));
                }
            } else {
                par3List.add(String.format("§3%s", I18n.format("flammpfeil.swaepon.info.magic")));
            }
        } else {
            par3List.add(String.format("§8%s", I18n.format("flammpfeil.swaepon.info.noname")));
        }
    }

    //    注册创造物品栏
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

    //   特殊文本
    @SideOnly(Side.CLIENT)
    public void addInformationBladeLore(ItemStack par1ItemStack,
                                        EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (isFdBlade.get(tag) && !isInCreativeTab.get(tag) && tag.hasKey("BladeLore")) {
            int count = tag.getInteger("BladeLore");
            String name = getUnlocalizedName(par1ItemStack);
            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(29) && !Keyboard.isKeyDown(157)) {
                for (int i = 0; i < count; i++) {
                    par3List.add(I18n.format(name + ".desc" + i));
                }
            }
        }
    }

    //    SE效果介绍
    @SideOnly(Side.CLIENT)
    public void addInformationEffectLore(ItemStack par1ItemStack,
                                         EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (isFdBlade.get(tag) && !isInCreativeTab.get(tag) && tag.hasKey("SpecialEffectLore")) {
            int count = tag.getInteger("SpecialEffectLore");
            String name = getUnlocalizedName(par1ItemStack);
            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
                par3List.add("§7" + I18n.format("tennouboshiuzume.slashblade.info.hold", new Object[0]) + " §e§o" + I18n.format("tennouboshiuzume.slashblade.info.shift", new Object[0]) + " §r§7" + I18n.format("tennouboshiuzume.slashblade.info.forEffectDetails", new Object[0]) + "§r");
            } else {
                for (int i = 0; i < count; i++) {
                    par3List.add(I18n.format(name + ".SEdesc" + i));
                }
            }
        }
    }

    public void addInformationAttackLore(ItemStack par1ItemStack,
                                         EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (isFdBlade.get(tag) && !isInCreativeTab.get(tag) && tag.hasKey("SpecialAttackLore")) {
            int count = tag.getInteger("SpecialAttackLore");
            String name = getUnlocalizedName(par1ItemStack);
            if (!Keyboard.isKeyDown(29) && !Keyboard.isKeyDown(157)) {
                par3List.add("§7" + I18n.format("tennouboshiuzume.slashblade.info.hold", new Object[0]) + " §e§o" + I18n.format("tennouboshiuzume.slashblade.info.ctrl", new Object[0]) + " §r§7" + I18n.format("tennouboshiuzume.slashblade.info.forAttackDetails", new Object[0]) + "§r");
            } else {
                for (int i = 0; i < count; i++) {
                    par3List.add(I18n.format(name + ".SAdesc" + i));
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

    @SideOnly(Side.CLIENT)
    public void addInformationBladeBound(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        NBTTagCompound tag = getItemTagCompound(par1ItemStack);
        if (tag.hasUniqueId("Owner")){
            UUID ownerid = tag.getUniqueId("Owner");
            if (ownerid.equals(par2EntityPlayer.getUniqueID())){
                par3List.add("§6"+I18n.format("tennouboshiuzume.slashblade.info.BoundOnYou"));
            }else {
                par3List.add("§4"+I18n.format("tennouboshiuzume.slashblade.info.Bound")+":"+ownerid);
            }
        }else {
            par3List.add("§7"+I18n.format("tennouboshiuzume.slashblade.info.noBound"));
        }
    }
}
