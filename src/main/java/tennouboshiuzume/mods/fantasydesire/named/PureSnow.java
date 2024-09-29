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
import tennouboshiuzume.mods.fantasydesire.specialeffect.RainbowFlux;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

public class PureSnow {
    String name = "tennouboshiuzume.slashblade.PureSnow";
    String materialName = "flammpfeil.slashblade.named.yamato";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 50);
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "PureSnow");
        ItemSlashBlade.TextureName.set(tag,"named/PureSnow");
        ItemSlashBlade.ModelName.set(tag,"named/PureSnow");
        ItemSlashBlade.SpecialAttackType.set(tag, 211);
        ItemSlashBlade.StandbyRenderType.set(tag, 1);
        ItemSlashBlade.BaseAttackModifier.set(tag, 7.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFFFFFF);
        SpecialEffects.addEffect(customblade,FdSEs.RainbowFlux);
        SpecialEffects.addEffect(customblade,FdSEs.ColorFlux);
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
        tag.setTag("EffectLore", seLoreList);
        customblade.addEnchantment(Enchantments.SILK_TOUCH,1);

        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
}
