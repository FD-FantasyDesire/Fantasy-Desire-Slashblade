package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

public class TwinBlade {
    String name = "tennouboshiuzume.slashblade.TwinBlade";
    String materialName = "flammpfeil.slashblade.named";
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
        ItemFdSlashBlade.unlockLevel.set(tag,30);
        SpecialEffects.addEffect(customblade,FdSEs.TwinSet);
        customblade.addEnchantment(Enchantments.INFINITY,1);
        customblade.addEnchantment(Enchantments.FLAME,5);
        customblade.addEnchantment(Enchantments.BLAST_PROTECTION,3);
        customblade.addEnchantment(Enchantments.FIRE_PROTECTION,3);
        tag.setInteger("BladeLore",  3);
        tag.setInteger("SpecialEffectLore", 4);
        tag.setInteger("SpecialAttackLore", 4);
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

        if (!(event.entityBladeStand.world.provider.getDimension() == 1)) return;

        if (event.entityBladeStand.posY>2) return;

        ItemStack targetBlade = SlashBlade.findItemStack(SlashBlade.modid,"slashbladeNamed",1);
        
        NBTTagCompound tag = event.blade.getTagCompound();

        if (!(ItemSlashBlade.ProudSoul.get(tag)>=1000)) return;

        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        if(!(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, event.blade)>=3)) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
