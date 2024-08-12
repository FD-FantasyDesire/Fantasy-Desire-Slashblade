package tennouboshiuzume.mods.fantasydesire.init;

import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.Item;
import tennouboshiuzume.mods.fantasydesire.creativetab.FdTabs;
import tennouboshiuzume.mods.fantasydesire.named.*;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;

/**
 * @author Cat
 * @updateDate 2020/02/14
 */
public class FdBlades {
    public static final Item Fd_BLADE = new ItemFdSlashBlade(Item.ToolMaterial.IRON, 4.0f,"fdSlashBlade")
            .setMaxDamage(40)
            .setCreativeTab(FdTabs.Fd_Item);

    public FdBlades(){
        loadBlade();
    }

    public void loadBlade(){
        loadBlade(new ChikaFlare());
    }


    public void loadBlade(Object blade) {
        SlashBlade.InitEventBus.register(blade);
    }

}
