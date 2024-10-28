package tennouboshiuzume.mods.fantasydesire.named;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.*;

public class BladeEffectEvents {

    public BladeEffectEvents() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void ChikFlare(SlashBladeEvent.OnUpdateEvent event) {
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        player.getHeldEquipment();

        Vec3d pos1 = new Vec3d(0, 0, 1).rotateYaw((float) Math.toRadians(player.rotationYaw));

        if (SpecialEffects.isEffective(player, event.blade, FdSEs.ImmortalSoul) == SpecialEffects.State.Effective && (player.getHeldItemMainhand().equals(event.blade) || player.getHeldItemOffhand().equals(event.blade))) {
            Vec3d pos2 = MathUtils.rotateAroundAxis(pos1.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime() % 60 * ((float) 360 / 60))), pos1, Math.toRadians(-30f));
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos2.x + player.posX, pos2.y + player.posY + player.height / 3, pos2.z + player.posZ,
                    0, 1, 1, 0.0001, 1);
        }
        if (!player.getHeldItemMainhand().equals(event.blade)) return;
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.SoulShield) == SpecialEffects.State.Effective) {
            Vec3d pos3 = MathUtils.rotateAroundAxis(pos1.rotateYaw((float) Math.toRadians(-player.world.getTotalWorldTime() % 60 * ((float) 360 / 60))), pos1.rotateYaw((float) Math.toRadians(120f)), Math.toRadians(-30f));
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos3.x + player.posX, pos3.y + player.posY + player.height / 3, pos3.z + player.posZ,
                    0, 0.33, 1, 1, 1);
        }
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.TyrantStrike) == SpecialEffects.State.Effective) {
            Vec3d pos4 = pos1.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime() % 120 * ((float) 360 / 120) + 240f));
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos4.x + player.posX, pos4.y + player.posY + player.height / 2, pos4.z + player.posZ,
                    0, 0.66, 0.001, 1.0, 1);
        }
    }

    @SubscribeEvent
    public void CrimsonScythe(SlashBladeEvent.OnUpdateEvent event) {
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.CrimsonScythe", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (!player.getHeldItemMainhand().equals(event.blade)) return;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);

        Vec3d pos1 = new Vec3d(0, 1, -0.5);
        Vec3d pos4 = new Vec3d(0, 0, 1).rotateYaw((float) Math.toRadians(player.rotationYaw));

        if (SpecialEffects.isEffective(player, event.blade, FdSEs.CrimsonStrike) == SpecialEffects.State.Effective) {
            for (int i = 0; i < 12; i++) {
                Vec3d pos2 = MathUtils.rotateAroundAxis(pos1,
                                new Vec3d(0, 0, 1),
                                Math.toRadians(i * ((float) 360 / 12) + player.world.getTotalWorldTime() % 30 * 2)
                        )
                        .rotateYaw((float) Math.toRadians(-player.rotationYaw));
                ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                        pos2.x + player.posX, pos2.y + player.posY + player.eyeHeight, pos2.z + player.posZ,
                        0, 1, 0, 0, 1);
            }
        }
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.BloodDrain) == SpecialEffects.State.Effective) {
            Vec3d pos3 = pos4.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime() % 120 * ((float) 360 / 120) + 240f));
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.SPELL_MOB, true,
                    pos3.x + player.posX, pos3.y + player.posY, pos3.z + player.posZ,
                    0, 1.0, 0.001, 1.0, 1);
        }
    }
    @SubscribeEvent
    public void PureSnow(SlashBladeEvent.OnUpdateEvent event) {
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.PureSnow", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.entity)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (!player.getHeldItemMainhand().equals(event.blade)) return;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        Vec3d pos1 = new Vec3d(0, 0, 1).rotateYaw((float) Math.toRadians(player.rotationYaw));
//        虹光通量
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.RainbowFlux) == SpecialEffects.State.Effective) {
            Vec3d pos4 = pos1.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime() % 30 * ((float) 360 / 30) + 60f)).scale(0.75);
            int color = ColorUtils.getSmoothTransitionColor(player.world.getTotalWorldTime(),30,true);
            float red = Math.max(0.001f, ((color >> 16) & 0xff) / 255.0f);
            float green = Math.max(0.001f, ((color >> 8) & 0xff) / 255.0f);
            float blue = Math.max(0.001f, ((color) & 0xff) / 255.0f);
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos4.x + player.posX, pos4.y + player.posY + player.height / 2, pos4.z + player.posZ,
                    0, red, green, blue, 1);
        }
//        全色汇流
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.ColorFlux) == SpecialEffects.State.Effective) {
            Vec3d pos4 = pos1.rotateYaw((float) Math.toRadians(Math.abs((player.world.getTotalWorldTime() % 30) - 15) * ((float) 360 / 30) + 120f)).scale(1);
            Vec3d pos5 = pos1.rotateYaw((float) Math.toRadians(-Math.abs((player.world.getTotalWorldTime() % 30) - 15)  * ((float) 360 / 30) + 120f)).scale(1);
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos4.x + player.posX, pos4.y + player.posY + player.height / 2, pos4.z + player.posZ,
                    0, 0.999, 0.999, 0.999, 1);
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos5.x + player.posX, pos5.y + player.posY + player.height / 2, pos5.z + player.posZ,
                    0, 0.001, 0.001, 0.001, 1);

        }
//         棱光通量
        if (SpecialEffects.isEffective(player, event.blade, FdSEs.PrismFlux) == SpecialEffects.State.Effective) {
            int color = ColorUtils.getSmoothTransitionColor(player.world.getTotalWorldTime(),120,true);
            float red = Math.max(0.001f, ((color >> 16) & 0xff) / 255.0f);
            float green = Math.max(0.001f, ((color >> 8) & 0xff) / 255.0f);
            float blue = Math.max(0.001f, ((color) & 0xff) / 255.0f);
            Vec3d pos4 = pos1.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime() % 120 * ((float) 360 / 120) + 240f)).scale(1.25);
            ParticleUtils.spawnParticle(player.world, EnumParticleTypes.REDSTONE, true,
                    pos4.x + player.posX, pos4.y + player.posY + player.height / 2, pos4.z + player.posZ,
                    0, red, green, blue, 1);
        }
    }

}
