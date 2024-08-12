package tennouboshiuzume.mods.fantasydesire.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Moflop
 * @updateDate 2020/02/12
 */
public class FdTabs {
    public static final CreativeTabs Fd_Item= new CreativeTabs("FantasyDesire")
    {
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Items.ARROW);
        }
    };
}
