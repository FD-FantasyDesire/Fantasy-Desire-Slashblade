package tennouboshiuzume.mods.fantasydesire.named;


import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

public class OverCold {
    String name = "tennouboshiuzume.slashblade.OverCold";
//    String materialName = "flammpfeil.slashblade.named.muramasa";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 512 );
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "OverCold_0");
        ItemSlashBlade.TextureName.set(tag, "named/OverCold");
        ItemSlashBlade.ModelName.set(tag, "named/OverCold_0");
        ItemSlashBlade.SpecialAttackType.set(tag, 210);
        ItemSlashBlade.StandbyRenderType.set(tag, 3);
        ItemSlashBlade.BaseAttackModifier.set(tag, 12.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xAAFFFF);
        SpecialEffects.addEffect(customblade, FdSEs.ColdLeak);
        SpecialEffects.addEffect(customblade, FdSEs.EvolutionIce);
        NBTTagList loreList = new NBTTagList();
        loreList.appendTag(new NBTTagString("desc"));
        loreList.appendTag(new NBTTagString("desc1"));
        loreList.appendTag(new NBTTagString("desc2"));
        tag.setTag("BladeLore", loreList);
        NBTTagList seLoreList = new NBTTagList();
        seLoreList.appendTag(new NBTTagString("SEdesc"));
        seLoreList.appendTag(new NBTTagString("SEdesc1"));
        seLoreList.appendTag(new NBTTagString("SEdesc2"));
        seLoreList.appendTag(new NBTTagString("SEdesc3"));
        seLoreList.appendTag(new NBTTagString("SEdesc4"));
        seLoreList.appendTag(new NBTTagString("SEdesc5"));
        seLoreList.appendTag(new NBTTagString("SEdesc6"));
        tag.setTag("EffectLore", seLoreList);
        customblade.addEnchantment(Enchantments.FROST_WALKER,5);
        customblade.addEnchantment(Enchantments.FIRE_PROTECTION,5);
        customblade.addEnchantment(Enchantments.UNBREAKING,3);
        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
//    @SubscribeEvent
//    public void onBladeStandAttack(SlashBladeEvent.BladeStandAttack event){
////      是否有刀
//        if(!event.entityBladeStand.hasBlade()) return;
//        if(!event.entityBladeStand.isBurning()) return;
////      是否为单刀挂架
//        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
////      是否为爆炸造成伤害
//        if(!(event.damageSource.getTrueSource() instanceof EntityPlayer)) return;
//        if (!event.damageSource.isExplosion()) return;
//        if(!event.damageSource.getDamageType().equals("explosion.player")) return;
//
//        ItemStack targetBlade = BladeUtils.findItemStack(SlashBlade.modid,materialName,1);
//        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;
//
//        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);
//
//        event.entityBladeStand.setBlade(resultBlade);
//    }
}
