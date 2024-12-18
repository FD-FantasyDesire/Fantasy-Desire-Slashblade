package tennouboshiuzume.mods.fantasydesire.potion;

import mods.flammpfeil.slashblade.util.ResourceLocationRaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.util.RenderUtils;

public class FdPotionBase extends Potion {

    private final ResourceLocationRaw iconTexture;

    public FdPotionBase(boolean isBadEffect, int liquidColor, String key, String texturePath) {
        super(isBadEffect, liquidColor);
        this.iconTexture = new ResourceLocationRaw(FantasyDesire.MODID, texturePath);
        this.setRegistryName(new ResourceLocation(FantasyDesire.MODID, key));
        this.setPotionName("potion."+FantasyDesire.MODID+"."+key+".name");
        ForgeRegistries.POTIONS.register(this);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        // 药水效果逻辑
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;  // 始终有效
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        Minecraft.getMinecraft().renderEngine.bindTexture(iconTexture);
        RenderUtils.drawModalRectWithCustomSizedTexture(x + 3, y + 3, z, 0, 0, 18, 18, 18, 18);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        Minecraft.getMinecraft().renderEngine.bindTexture(iconTexture);
        RenderUtils.drawModalRectWithCustomSizedTexture(x + 6, y + 7, z, 0, 0, 18, 18, 18, 18);
    }
}
