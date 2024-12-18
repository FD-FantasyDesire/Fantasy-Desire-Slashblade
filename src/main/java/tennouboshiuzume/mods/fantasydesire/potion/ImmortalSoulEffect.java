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

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;  // 始终有效
    }
}
