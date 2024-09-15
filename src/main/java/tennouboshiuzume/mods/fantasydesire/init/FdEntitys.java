package tennouboshiuzume.mods.fantasydesire.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.*;

import static tennouboshiuzume.mods.fantasydesire.FantasyDesire.MODID;


public class FdEntitys {
    public FdEntitys(){
        loadEntity();
    }

    public void loadEntity(){
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, "entitydriveex"), // 使用正确的命名空间前缀
                EntityDriveEx.class,
                "EntityDriveEx",
                1,
                FantasyDesire.instance, // 确保传入的是你的Mod实例
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, "entityphantomswordex"),
                EntityPhantomSwordEx.class,
                "EntityPhantomSwordEx",
                2,
                FantasyDesire.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, "entityphantomswordexbase"),
                EntityPhantomSwordExBase.class,
                "EntityPhantomSwordExBase",
                3,
                FantasyDesire.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID,"entitysoulphantomsword"),
                EntitySoulPhantomSword.class,
                "EntitySoulPhantomSword",
                4,
                FantasyDesire.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID,"entityovercharge"),
                EntityOverCharge.class,
                "EntityOverCharge",
                5,
                FantasyDesire.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID,"entityoverchargebfg"),
                EntityOverChargeBFG.class,
                "EntityOverChargeBFG",
                5,
                FantasyDesire.instance,
                64,
                1,
                false
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID,"entityseeksoulphantomsword"),
                EntitySeekSoulPhantomSword.class,
                "EntitySeekSoulPhantomSword",
                6,
                FantasyDesire.instance,
                64,
                1,
                false
        );
    }
}
