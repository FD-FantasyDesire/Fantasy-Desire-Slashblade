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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.EnchantmentTransfer;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
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
        ItemFdSlashBlade.CustomMaxDamage.set(tag, 512 );
        ItemFdSlashBlade.IsDefaultBewitched.set(tag, true);
        ItemFdSlashBlade.isFdBlade.set(tag, true);
        ItemFdSlashBlade.bladeType.set(tag, "CrimsonScythe");
        ItemSlashBlade.TextureName.set(tag, "named/CrimsonScythe");
        ItemSlashBlade.ModelName.set(tag, "named/CrimsonScythe");
        ItemSlashBlade.SpecialAttackType.set(tag, 207);
        ItemSlashBlade.StandbyRenderType.set(tag, 2);
        ItemSlashBlade.BaseAttackModifier.set(tag, 12.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xFF0000);
        ItemFdSlashBlade.unlockLevel.set(tag,75);
        SpecialEffects.addEffect(customblade,FdSEs.CrimsonStrike);
        SpecialEffects.addEffect(customblade,FdSEs.BloodDrain);
        tag.setTag("BladeLore",  new NBTTagInt(3));
        tag.setTag("SpecialEffectLore", new NBTTagInt(6));
        tag.setTag("SpecialAttackLore", new NBTTagInt(5));
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

        if(!event.entityBladeStand.hasBlade()) return;

        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;

        if (!event.entityBladeStand.isInLava()) return;

        if (!(event.entityBladeStand.world.provider.getDimension() == -1)) return;

        ItemStack targetBlade = SlashBlade.findItemStack(SlashBlade.modid,"slashbladeNamed",1);

        NBTTagCompound tag = event.blade.getTagCompound();

        if (!(ItemSlashBlade.ProudSoul.get(tag)>=1000)) return;

        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.playSound(SoundEvents.ITEM_FIRECHARGE_USE,1,0.5f);

        ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.LAVA,
                false,
                event.entityBladeStand.posX,
                event.entityBladeStand.posY + event.entityBladeStand.height / 2,
                event.entityBladeStand.posZ,
                200, 1, 1, 1, 0.5f);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
