package tennouboshiuzume.mods.fantasydesire.potion;

import net.minecraft.entity.EntityLivingBase;

public class ImmortalSoulEffect extends FdPotionBase {

    public ImmortalSoulEffect() {
        // 传入构造参数：是否为负面效果、颜色、注册名和贴图路径
        super(false, 0xFFFF00, "immortal_soul", "textures/gui/immortal_soul.png");
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        // 药水效果逻辑
        // 不需要效果，已通过拔刀剑效果实现，该药水效果仅用做用户可见标识
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;  // 始终有效
    }
}
