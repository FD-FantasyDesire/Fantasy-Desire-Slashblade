package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.RecipeAwakeBlade;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.crafting.RecipeBladeSoulUpgrade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

import javax.swing.border.EmptyBorder;

public class ArdorBlossomStar  {
    String name = "tennouboshiuzume.slashblade.ArdorBlossomStar";
//    String materialName = "flammpfeil.slashblade.named.yamato";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 66);
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "ego");
        ItemSlashBlade.TextureName.set(tag, "named/ArdorBlossomStar");
        ItemSlashBlade.ModelName.set(tag, "named/ArdorBlossomStar");
        ItemSlashBlade.SpecialAttackType.set(tag, 212);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.BaseAttackModifier.set(tag, 2.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFFCC00);
        ItemFdSlashBlade.unlockLevel.set(tag,80);
        SpecialEffects.addEffect(customblade, FdSEs.FollowFire);
        tag.setInteger("BladeLore", 3);
        tag.setInteger("SpecialEffectLore",5);
        tag.setInteger("SpecialAttackLore", 7);
        customblade.addEnchantment(Enchantments.FIRE_ASPECT, 10);
        customblade.addEnchantment(Enchantments.FIRE_PROTECTION,10);
        customblade.addEnchantment(Enchantments.FLAME,10);
        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }

}
//    @SubscribeEvent
//    public void postinit(LoadEvent.PostInitEvent event){
//        SlashBladeHooks.EventBus.register(this);
//    }
//    @SubscribeEvent
//    public void onBladeStandAttack(SlashBladeEvent.BladeStandAttack event){
////      是否有刀
//        if(!event.entityBladeStand.hasBlade()) return;
////      是否为单刀挂架
//        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
////      是否为雷击造成伤害
//        if(!event.damageSource.damageType.equals("lightningBolt")) return;
//
//        ItemStack targetBlade = BladeUtils.findItemStack(SlashBlade.modid,materialName,1);
//        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;
//
//        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);
//
//        event.entityBladeStand.setBlade(resultBlade);
//    }


