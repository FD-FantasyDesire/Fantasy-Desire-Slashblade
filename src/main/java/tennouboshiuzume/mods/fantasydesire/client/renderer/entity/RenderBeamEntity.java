package tennouboshiuzume.mods.fantasydesire.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tennouboshiuzume.mods.fantasydesire.entity.EntityBeam;

import java.util.Random;

public class RenderBeamEntity extends Render<EntityBeam> {

    public RenderBeamEntity(RenderManager renderManager) {
        super(renderManager);
    }
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");
    @Override
    public void doRender(EntityBeam entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();

        // 启用深度测试
        GlStateManager.enableDepth();

        // 设置混合模式
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 其他渲染设置
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.disableFog();
        GlStateManager.disableLighting();

        // 设置光照纹理坐标
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        // 光束位置调整
        GlStateManager.translate(-0.5F, 0, -0.5F);

        // 绑定光束纹理
        this.bindTexture(BEAM_TEXTURE);

        //设置获取预设颜色
        int color = entity.getColor();
        float alpha = 255.0f;
        float red = ((color >> 16) & 0xff)/255.0f;
        float green = ((color >>  8) & 0xff)/255.0f;
        float blue = ((color      ) & 0xff)/255.0f;

        float defAddon = 2f;

        // 渲染光束
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z,
                0,
                0,
                -entity.ticksExisted, // 旋转增量
                0,//y轴增量
                256, // 光束高度
                new float[]{red, green, blue}, // 颜色
                0.2d+Math.max(0, defAddon - defAddon/entity.getInterval()*entity.ticksExisted), // 内光束
                0.25d+Math.max(0, 1.25*defAddon - 1.25*defAddon/entity.getInterval()*entity.ticksExisted ) // 外光束
        );
        // 恢复其他状态
        GlStateManager.enableLighting();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
    }


    @Override
    protected ResourceLocation getEntityTexture(EntityBeam entity) {
        return BEAM_TEXTURE;// 这个实体没有特定的纹理
    }
}
