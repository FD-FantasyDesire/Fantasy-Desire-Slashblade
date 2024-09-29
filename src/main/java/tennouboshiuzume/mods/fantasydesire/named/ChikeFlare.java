package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

public class ChikeFlare {
    String name = "tennouboshiuzume.slashblade.ChikeFlare";
    String materialName = "flammpfeil.slashblade.named.yamato";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 66);
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "fantasy");
        ItemSlashBlade.TextureName.set(tag, "named/Chike");
        ItemSlashBlade.ModelName.set(tag, "named/Chike");
        ItemSlashBlade.SpecialAttackType.set(tag, 200);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.RepairCount.set(tag,67);
        ItemSlashBlade.BaseAttackModifier.set(tag, 2.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFFFF00);
        SpecialEffects.addEffect(customblade, FdSEs.CheatRumble);
        SpecialEffects.addEffect(customblade, FdSEs.TyrantStrike);
        SpecialEffects.addEffect(customblade, FdSEs.SoulShield);
        SpecialEffects.addEffect(customblade, FdSEs.ImmortalSoul);
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
        customblade.addEnchantment(Enchantments.UNBREAKING, 10);
        customblade.addEnchantment(Enchantments.SHARPNESS,7);
        customblade.addEnchantment(Enchantments.POWER, 7);
        customblade.addEnchantment(Enchantments.LOOTING, 3);
        customblade.addEnchantment(Enchantments.MENDING,1);
        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);

    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
//      通过将阎魔刀放置于刀架上并且雷击来制造
    @SubscribeEvent
    public void onBladeStandAttack(SlashBladeEvent.BladeStandAttack event){
//      是否有刀
        if(!event.entityBladeStand.hasBlade()) return;
//      是否为单刀挂架
        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//      是否为雷击造成伤害
        if(!event.damageSource.damageType.equals("lightningBolt")) return;

        ItemStack targetBlade = BladeUtils.findItemStack(SlashBlade.modid,materialName,1);
        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
