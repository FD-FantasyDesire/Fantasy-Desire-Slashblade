package tennouboshiuzume.mods.fantasydesire.named;


import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.named.event.LoadEvent;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdBlades;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.WorldBladeStandCrafting;

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
        ItemSlashBlade.BaseAttackModifier.set(tag, 10.0F);
        ItemSlashBlade.SummonedSwordColor.set(tag, 0xAAFFFF);
        ItemFdSlashBlade.unlockLevel.set(tag,45);
        SpecialEffects.addEffect(customblade, FdSEs.ColdLeak);
        SpecialEffects.addEffect(customblade, FdSEs.EvolutionIce);
        tag.setInteger("BladeLore",  2);
        tag.setInteger("SpecialEffectLore", 7);
        tag.setInteger("SpecialAttackLore", 4);
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
    @SubscribeEvent
    public void onBladeStandUpdate(SlashBladeEvent.OnEntityBladeStandUpdateEvent event){

        if(!event.entityBladeStand.hasBlade()) return;

        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;

        Biome biome = event.entityBladeStand.world.getBiome(event.entityBladeStand.getPosition());

        if (!(biome.getRegistryName().equals(new ResourceLocation("minecraft", "mutated_ice_flats")))) return;

        ItemStack targetBlade = SlashBlade.findItemStack(SlashBlade.modid,"slashbladeNamed",1);

        NBTTagCompound tag = event.blade.getTagCompound();

        if (!(ItemSlashBlade.ProudSoul.get(tag)>=1000)) return;

        if(!event.blade.getUnlocalizedName().equals(targetBlade.getUnlocalizedName())) return;

        ItemStack resultBlade = WorldBladeStandCrafting.crafting(event.blade,name);

        event.entityBladeStand.playSound(SoundEvents.BLOCK_GLASS_BREAK,1,0.5f);

        ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.SNOW_SHOVEL,
                false,
                event.entityBladeStand.posX,
                event.entityBladeStand.posY + event.entityBladeStand.height / 2,
                event.entityBladeStand.posZ,
                200, 1, 1, 1, 0.5f);

        event.entityBladeStand.setBlade(resultBlade);
    }
}
