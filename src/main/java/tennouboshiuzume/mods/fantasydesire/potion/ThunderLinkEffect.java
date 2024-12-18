package tennouboshiuzume.mods.fantasydesire.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdPotions;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;

public class ThunderLinkEffect extends FdPotionBase {

    public ThunderLinkEffect() {
        // 传入构造参数：是否为负面效果、颜色、注册名和贴图路径
        super(true, 0xFFFFFF, "thunder_link", "textures/gui/thunder_link.png");
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(entity, 10f, false);
        for (EntityLivingBase targetEntity : target) {
            if (targetEntity.isPotionActive(FdPotions.THUNDER_LINK)) {
                targetEntity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, (amplifier + 1) * 5);
                if (MathUtils.randomCheck(10)) {
                    targetEntity.addPotionEffect(new PotionEffect(FdPotions.THUNDER_LINK, 40, 0));
                }
                ParticleUtils.spawnParticleLightningLine(targetEntity.world, EnumParticleTypes.FIREWORKS_SPARK, entity.posX, entity.posY+entity.height/2, entity.posZ,
                        targetEntity.posX, targetEntity.posY+targetEntity.height/2, targetEntity.posZ,
                        0, 0, 0, 1, 0);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 20 == 0;// 始终有效
    }
}
