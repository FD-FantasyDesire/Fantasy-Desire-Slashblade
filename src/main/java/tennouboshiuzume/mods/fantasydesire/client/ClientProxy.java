package tennouboshiuzume.mods.fantasydesire.client;

import mods.flammpfeil.slashblade.client.renderer.entity.InvisibleRender;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tennouboshiuzume.mods.fantasydesire.client.renderer.entity.*;
import tennouboshiuzume.mods.fantasydesire.common.CommonProxy;
import tennouboshiuzume.mods.fantasydesire.entity.*;
import tennouboshiuzume.mods.fantasydesire.util.ItemUtils;

/**
 * @author Moflop
 * @updateDate 2020/02/12
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        new ItemUtils();

        RenderingRegistry.registerEntityRenderingHandler(
                EntityDriveEx.class,
                new IRenderFactory<EntityDriveEx>() {
                    @Override
                    public Render<? super EntityDriveEx> createRenderFor(RenderManager manager) {
                        return new RenderDriveEx(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityPhantomSwordEx.class,
                new IRenderFactory<EntityPhantomSwordEx>() {
                    @Override
                    public Render<? super EntityPhantomSwordEx> createRenderFor(RenderManager manager) {
                        return new RenderPhantomSwordEx(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityPhantomSwordExBase.class,
                new IRenderFactory<EntityPhantomSwordExBase>() {
                    @Override
                    public Render<? super EntityPhantomSwordExBase> createRenderFor(RenderManager manager) {
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
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityOverCharge.class,
                new IRenderFactory<EntityOverCharge>() {
                    @Override
                    public Render<? super EntityOverCharge> createRenderFor(RenderManager manager) {
                        return new RenderOverCharge(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityOverChargeBFG.class,
                new IRenderFactory<EntityOverChargeBFG>() {
                    @Override
                    public Render<? super EntityOverChargeBFG> createRenderFor(RenderManager manager) {
                        return new RenderOverCharge(manager);
                    }
                });

        RenderingRegistry.registerEntityRenderingHandler(
                EntitySeekSoulPhantomSword.class,
                new IRenderFactory<EntitySeekSoulPhantomSword>() {
                    @Override
                    public Render<? super EntitySeekSoulPhantomSword> createRenderFor(RenderManager manager) {
                        return new RenderPhantomSwordEx(manager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityTwinSlashManager.class,
                new IRenderFactory<EntityTwinSlashManager>() {
                    @Override
                    public Render<? super EntityTwinSlashManager> createRenderFor(RenderManager renderManager) {
                        return new InvisibleRender(renderManager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityIceFallManager.class,
                new IRenderFactory<EntityIceFallManager>() {
                    @Override
                    public Render<? super EntityIceFallManager> createRenderFor(RenderManager renderManager) {
                        return new InvisibleRender(renderManager);
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(
                EntityBeam.class,
                new IRenderFactory<EntityBeam>() {
                    @Override
                    public Render<? super EntityBeam> createRenderFor(RenderManager renderManager) {
                        return new RenderBeamEntity(renderManager);
                    }
                }
        );
    }


    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
