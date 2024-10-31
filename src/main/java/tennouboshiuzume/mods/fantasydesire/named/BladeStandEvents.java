package tennouboshiuzume.mods.fantasydesire.named;

import com.sun.java.accessibility.util.java.awt.ListTranslator;
import jdk.nashorn.internal.ir.Block;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.core.jmx.Server;
import scala.tools.nsc.doc.model.Public;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.*;

import java.util.List;
import java.util.UUID;

public class BladeStandEvents {

    public BladeStandEvents() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    //  灵魂锻造
    @SubscribeEvent
    public void OnAnvilFallIron(SlashBladeEvent.BladeStandAttack event) {
//        是否有刀
        if (!event.entityBladeStand.hasBlade()) return;
//        是否为单刀挂架
        if (EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//        是否为铁砧对刀挂架造成伤害（至少从两格以上落下）
        if (!event.damageSource.damageType.equals("anvil")) return;
//        检测刀挂架是否放在铁块上
        if (!(EntityUtils.getBlockUnderEntity(event.entityBladeStand).getBlock() == Blocks.IRON_BLOCK)) return;
        NBTTagCompound req = ItemSlashBlade.getItemTagCompound(event.blade);
        int proundSoul = ItemSlashBlade.ProudSoul.get(req);
        boolean isDone = false;
        while (proundSoul >= 10000) {
//            计算耀魂消耗
            int ForgeLevel = ItemSlashBlade.getSpecialEffect(event.blade).getInteger("SoulForging");
            System.out.println(ForgeLevel);
            int cost = Math.min(1000 + 50 * ForgeLevel, 2500);
            ItemSlashBlade.ProudSoul.tryAdd(req, -cost, false);
            ItemSlashBlade.RepairCount.tryAdd(req, 1, false);
//            System.out.println(ItemSlashBlade.RepairCount.get(req) + "Repair," + cost + "ProundSoul, Remain:" + proundSoul);
            ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.SPELL_MOB, false, event.entityBladeStand.posX, event.entityBladeStand.posY + event.entityBladeStand.height / 2, event.entityBladeStand.posZ, 3, 0.5, 0, 0.5, 0.5f);
            proundSoul = ItemSlashBlade.ProudSoul.get(req);
            isDone = true;
            SpecialEffects.addEffect(event.blade,"SoulForging",ForgeLevel+1);
        }
        if (isDone){
            event.entityBladeStand.playSound(SoundEvents.BLOCK_ANVIL_USE, 1, 0.5f);
            AdvancementUtils.grantAdvancementToPlayersInRange(event.entityBladeStand.world,event.entityBladeStand.posX, event.entityBladeStand.posY , event.entityBladeStand.posZ,5,"fantasydesire:SoulForging");
        }

    }
//    真实之名
    @SubscribeEvent
    public void OnAnvilFallDiamond(SlashBladeEvent.BladeStandAttack event) {
//        是否有刀
        if (!event.entityBladeStand.hasBlade()) return;
//        是否为单刀挂架
        if (EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//        是否为铁砧对刀挂架造成伤害（至少从两格以上落下）
        if (!event.damageSource.damageType.equals("anvil")) return;
//        检测刀挂架是否放在铁块上
        if (!(EntityUtils.getBlockUnderEntity(event.entityBladeStand).getBlock() == Blocks.DIAMOND_BLOCK)) return;
        NBTTagCompound req = ItemSlashBlade.getItemTagCompound(event.blade);
        if (event.blade.hasDisplayName()){
            event.blade.clearCustomName();
            AdvancementUtils.grantAdvancementToPlayersInRange(event.entityBladeStand.world,event.entityBladeStand.posX,
                    event.entityBladeStand.posY ,
                    event.entityBladeStand.posZ,
                    5,"fantasydesire:TrueName");
        }
    }
}
