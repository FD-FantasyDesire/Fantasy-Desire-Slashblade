package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.specialeffect.RainbowFlux;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.EnchantmentTransfer;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

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
        ItemFdSlashBlade.unlockLevel.set(tag,60);
        SpecialEffects.addEffect(customblade,FdSEs.RainbowFlux);
        SpecialEffects.addEffect(customblade,FdSEs.ColorFlux);
        tag.setInteger("BladeLore",  3);
        tag.setInteger("SpecialEffectLore", 5);
        tag.setInteger("SpecialAttackLore", 4);
        customblade.addEnchantment(Enchantments.SILK_TOUCH,1);
        customblade.addEnchantment(Enchantments.INFINITY,1);
        customblade.addEnchantment(Enchantments.FEATHER_FALLING,1);
        customblade.addEnchantment(Enchantments.POWER,3);
        customblade.addEnchantment(Enchantments.UNBREAKING,10);

        BladeUtils.registerCustomItemStack(name, customblade);
        BladeUtils.FdNamedBlades.add(name);
    }
    @SubscribeEvent
    public void postinit(LoadEvent.PostInitEvent event){
        SlashBladeHooks.EventBus.register(this);
    }
    @SubscribeEvent
    public void OnBladeStandUpdate(SlashBladeEvent.OnEntityBladeStandUpdateEvent event){

        if(!event.entityBladeStand.hasBlade()) return;

        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;

        if (!(event.entityBladeStand.world.provider.getDimension() == 0)) return;

        if (event.entityBladeStand.posY<250) return;

        ItemStack targetBlade = SlashBlade.findItemStack(SlashBlade.modid,"slashbladeNamed",1);

        NBTTagCompound tag = event.blade.getTagCompound();

        if (!(ItemSlashBlade.ProudSoul.get(tag)>=1000)) return;

        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        long currentTime = event.entityBladeStand.world.getWorldTime();

        if (!(currentTime>=5960 && currentTime <= 6040)) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,1,1);

        ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.END_ROD,
                false,
                event.entityBladeStand.posX,
                event.entityBladeStand.posY + event.entityBladeStand.height / 2,
                event.entityBladeStand.posZ,
                200, 0, 0, 0, 0.5f);
        event.entityBladeStand.setBlade(resultBlade);
    }
}
