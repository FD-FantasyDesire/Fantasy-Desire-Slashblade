package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import net.minecraft.item.ItemStack;

public class SoulForging implements ISpecialEffect, IRemovable {

//    嘿！这只是一个占位计数器SE！
    @Override
    public boolean canCopy(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canRemoval(ItemStack itemStack) {
        return true;
    }

    @Override
    public void register() {

    }

    @Override
    public int getDefaultRequiredLevel() {
        return 0;
    }

    @Override
    public String getEffectKey() {
        return "SoulForging";
    }
}
