package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.tools.nsc.doc.model.Public;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;

public class BladeStandEvents {

    public BladeStandEvents(){
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
//        灵魂锻造:-1000魂，+1锻造
    public void OnAnvilFall(SlashBladeEvent.BladeStandAttack event){
//        是否有刀
        if(!event.entityBladeStand.hasBlade()) return;

//        是否为单刀挂架
        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
        System.out.println(event.damageSource.damageType);
        if(!event.damageSource.damageType.equals("anvil")) return;


        NBTTagCompound req = ItemSlashBlade.getItemTagCompound(event.blade);
        int proundSoul = ItemSlashBlade.ProudSoul.get(req);
        int refine = ItemSlashBlade.RepairCount.get(req);
        if (proundSoul > 1000){
            proundSoul-=1000;
            ItemSlashBlade.ProudSoul.set(req,proundSoul);
            ItemSlashBlade.RepairCount.set(req,refine+1);
        }
    }
}
