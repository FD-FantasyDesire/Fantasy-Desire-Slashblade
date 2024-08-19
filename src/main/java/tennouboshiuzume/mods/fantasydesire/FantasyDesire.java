package tennouboshiuzume.mods.fantasydesire;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tennouboshiuzume.mods.fantasydesire.common.CommonProxy;


@Mod(
        modid = FantasyDesire.MODID,
        name = FantasyDesire.NAME,
        version = FantasyDesire.VER,
        dependencies = FantasyDesire.DEP,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class FantasyDesire {
    public static final String MODID = "fantasydesire";
    public static final String NAME = "FantasyDesire";
    public static final String VER = "r1";/** 版本号 */
    public static final String DEP = "required-after:flammpfeil.slashblade@[mc1.12-r30,);";/** 依赖拔刀剑版本 */
    public static final String[] AUTHOR = {"TennouboshiUzume,ICE-Coffine"};

    public static Logger logger = LogManager.getLogger(MODID);

    @Instance(FantasyDesire.MODID)
    public static FantasyDesire instance;

    @SidedProxy(
            clientSide = "tennouboshiuzume.mods.fantasydesire.client.ClientProxy",
            serverSide = "tennouboshiuzume.mods.fantasydesire.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
