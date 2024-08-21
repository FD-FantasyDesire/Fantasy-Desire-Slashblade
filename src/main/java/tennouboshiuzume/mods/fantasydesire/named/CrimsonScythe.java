package tennouboshiuzume.mods.fantasydesire.named;


import ibxm.Player;
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
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
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

public class CrimsonScythe {
    String name = "tennouboshiuzume.slashblade.CrimsonScythe";
    String materialName = "flammpfeil.slashblade.named.muramasa";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 50);
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "CrimsonScythe");
        ItemSlashBlade.TextureName.set(tag, "named/CrimsonScythe");
        ItemSlashBlade.ModelName.set(tag, "named/CrimsonScythe");
        ItemSlashBlade.SpecialAttackType.set(tag, 207);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.BaseAttackModifier.set(tag, 40.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFF0000);
        SpecialEffects.addEffect(customblade,FdSEs.CrimsonStrike);
        SpecialEffects.addEffect(customblade,FdSEs.BloodDrain);
        NBTTagCompound displayTag = new NBTTagCompound();
        customblade.setTagInfo("display",displayTag);
        NBTTagList loreList = new NBTTagList();
        loreList.appendTag(new NBTTagString((I18n.format("tennouboshiuzume.slashblade.CrimsonScythe.desc"))));
        loreList.appendTag(new NBTTagString((I18n.format("tennouboshiuzume.slashblade.CrimsonScythe.desc1"))));
        loreList.appendTag(new NBTTagString((I18n.format("tennouboshiuzume.slashblade.CrimsonScythe.desc2"))));
        loreList.appendTag(new NBTTagString((I18n.format("tennouboshiuzume.slashblade.CrimsonScythe.desc3"))));
        displayTag.setTag("Lore", loreList);
        customblade.addEnchantment(Enchantments.UNBREAKING, 6);
        customblade.addEnchantment(Enchantments.SHARPNESS,6);
        customblade.addEnchantment(Enchantments.LOOTING,6);
        customblade.addEnchantment(Enchantments.FIRE_ASPECT,6);
        customblade.addEnchantment(Enchantments.THORNS,6);
        customblade.addEnchantment(Enchantments.FIRE_PROTECTION,6);
        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
    @SubscribeEvent
    public void onBladeStandAttack(SlashBladeEvent.BladeStandAttack event){
//      是否有刀
        if(!event.entityBladeStand.hasBlade()) return;
        if(!event.entityBladeStand.isBurning()) return;
//      是否为单刀挂架
        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//      是否为爆炸造成伤害
        if(!(event.damageSource.getTrueSource() instanceof EntityPlayer)) return;
        if (!event.damageSource.isExplosion()) return;
        if(!event.damageSource.getDamageType().equals("explosion.player")) return;

        ItemStack targetBlade = BladeUtils.findItemStack(SlashBlade.modid,materialName,1);
        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
