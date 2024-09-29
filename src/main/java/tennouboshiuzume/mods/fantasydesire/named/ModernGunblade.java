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
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.EnchantmentTransfer;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

import java.util.Map;

public class ModernGunblade {
    String name = "tennouboshiuzume.slashblade.MordernGunblade";
    String materialName = "flammpfeil.slashblade.named.Koseki";
    @SubscribeEvent
    public void init(LoadEvent.InitEvent event){
        ItemStack customblade = new ItemStack(FdBlades.Fd_BLADE,1,0);
        NBTTagCompound tag = new NBTTagCompound();
        customblade.setTagCompound(tag);
        ItemFdSlashBlade.CurrentItemName.set(tag, name);
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 50);
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "Gunblade");
        ItemSlashBlade.TextureName.set(tag, "named/SmartPistol");
        ItemSlashBlade.ModelName.set(tag, "named/SmartPistol");
        ItemSlashBlade.SpecialAttackType.set(tag, 201);
        ItemSlashBlade.StandbyRenderType.set(tag, 3);
        ItemSlashBlade.BaseAttackModifier.set(tag, 8.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0x00FFFF);
        SpecialEffects.addEffect(customblade,FdSEs.TripleBullet);
        SpecialEffects.addEffect(customblade,FdSEs.EnergyBullet);
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
        customblade.addEnchantment(Enchantments.UNBREAKING, 5);
        customblade.addEnchantment(Enchantments.POWER, 1);
        customblade.addEnchantment(Enchantments.FEATHER_FALLING,5);
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
//      是否为单刀挂架
        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//      是否为爆炸造成伤害
        if(!(event.damageSource.getTrueSource() instanceof EntityPlayer)) return;
        if (!event.damageSource.isExplosion()) return;
        if(!event.damageSource.getDamageType().equals("explosion.player")) return;

        ItemStack targetBlade = BladeUtils.findItemStack(SlashBlade.modid,materialName,1);
        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(targetBlade,name);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
