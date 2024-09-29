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

public class TwinBlade {
    String name = "tennouboshiuzume.slashblade.TwinBlade";
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
        ItemFdSlashBlade.bladeType.set(tag, "TwinBladeL");
        ItemSlashBlade.TextureName.set(tag, "named/TwinBladeLeft");
        ItemSlashBlade.ModelName.set(tag, "named/TwinBlade");
        ItemSlashBlade.SpecialAttackType.set(tag, 208);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.BaseAttackModifier.set(tag, 7.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0x00C8FF);
        SpecialEffects.addEffect(customblade,FdSEs.TwinSet);
        customblade.addEnchantment(Enchantments.INFINITY,1);
        customblade.addEnchantment(Enchantments.FLAME,5);
        customblade.addEnchantment(Enchantments.BLAST_PROTECTION,3);
        customblade.addEnchantment(Enchantments.FIRE_PROTECTION,3);

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
        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
}
