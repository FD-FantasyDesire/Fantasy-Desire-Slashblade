package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
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
import tennouboshiuzume.mods.fantasydesire.util.EnchantmentTransfer;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

import java.util.Map;

public class ChikaFlare {
    String name = "tennouboshiuzume.slashblade.ChikaFlare";
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
        ItemFdSlashBlade.bladeType.set(tag, "fantasy");
        ItemSlashBlade.TextureName.set(tag, "named/Chika");
        ItemSlashBlade.ModelName.set(tag, "named/Chika");
        ItemSlashBlade.SpecialAttackType.set(tag, 200);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.RepairCount.set(tag,67);
        ItemSlashBlade.BaseAttackModifier.set(tag, 33.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFFFF00);
        SpecialEffects.addEffect(customblade, FdSEs.CheatRumble);
        SpecialEffects.addEffect(customblade, FdSEs.TyrantStrike);
        SpecialEffects.addEffect(customblade, FdSEs.SoulShield);
        NBTTagCompound displayTag = new NBTTagCompound();
        customblade.setTagInfo("display",displayTag);
        NBTTagList loreList = new NBTTagList();
        loreList.appendTag(new NBTTagString(String.format(I18n.format("tennouboshiuzume.slashblade.ChikaFlare.desc"))));
        loreList.appendTag(new NBTTagString(String.format(I18n.format("tennouboshiuzume.slashblade.ChikaFlare.desc1"))));
        loreList.appendTag(new NBTTagString(String.format(I18n.format("tennouboshiuzume.slashblade.ChikaFlare.desc2"))));
        displayTag.setTag("Lore", loreList);
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

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(targetBlade,name);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
