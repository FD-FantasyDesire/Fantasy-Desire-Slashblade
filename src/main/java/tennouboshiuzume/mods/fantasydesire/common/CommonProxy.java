package tennouboshiuzume.mods.fantasydesire.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tennouboshiuzume.mods.fantasydesire.init.*;
import tennouboshiuzume.mods.fantasydesire.named.BladeEffectEvents;
import tennouboshiuzume.mods.fantasydesire.named.BladeStandEvents;


public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event){
        new FdBlades();
        new FdSAs();
        new FdEntitys();
        new FdSEs();
    }

    public void init(FMLInitializationEvent event){
        new BladeStandEvents();
        new BladeEffectEvents();
//        new SoulDropsEvent();
//        ItemSoul.craft();
    }

    public void postInit(FMLPostInitializationEvent event){

    }
}
