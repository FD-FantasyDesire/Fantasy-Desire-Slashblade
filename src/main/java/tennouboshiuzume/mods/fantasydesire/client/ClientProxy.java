package tennouboshiuzume.mods.fantasydesire.client;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tennouboshiuzume.mods.fantasydesire.client.renderer.entity.RenderDriveEx;
import tennouboshiuzume.mods.fantasydesire.client.renderer.entity.RenderPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.client.renderer.entity.RenderPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.common.CommonProxy;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.ItemUtils;

/**
 * @author Moflop
 * @updateDate 2020/02/12
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        new ItemUtils();

        RenderingRegistry.registerEntityRenderingHandler(
                EntityDriveEx.class,
                new IRenderFactory<EntityDriveEx>() {
                    @Override
                    public Render<? super EntityDriveEx> createRenderFor(RenderManager manager)
                    {
                        return new RenderDriveEx(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityPhantomSwordEx.class,
                new IRenderFactory<EntityPhantomSwordEx>() {
                    @Override
                    public Render<? super EntityPhantomSwordEx> createRenderFor(RenderManager manager)
                    {
                        return new RenderPhantomSwordEx(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityPhantomSwordExBase.class,
                new IRenderFactory<EntityPhantomSwordExBase>() {
                    @Override
                    public Render<? super EntityPhantomSwordExBase> createRenderFor(RenderManager manager)
                    {
                        return new RenderPhantomSwordExBase(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntitySoulPhantomSword.class,
                new IRenderFactory<EntitySoulPhantomSword>() {
                    @Override
                    public Render<? super EntitySoulPhantomSword> createRenderFor(RenderManager manager) {
                        return new RenderPhantomSwordExBase(manager);
                    }
                }
        );


    }


    @Override
    public void init(FMLInitializationEvent event){
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
    }

}
