package tennouboshiuzume.mods.fantasydesire.named;

import com.sun.java.accessibility.util.java.awt.ListTranslator;
import jdk.nashorn.internal.ir.Block;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
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
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.EntityUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.UUID;

public class BladeStandEvents {

    public BladeStandEvents() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    //  灵魂锻造
    @SubscribeEvent
    public void OnAnvilFall(SlashBladeEvent.BladeStandAttack event) {
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
            int cost = Math.min(1000 + 50 * ItemSlashBlade.RepairCount.get(req), 2500);
            ItemSlashBlade.ProudSoul.tryAdd(req, -cost, false);
            ItemSlashBlade.RepairCount.tryAdd(req, 1, false);
            System.out.println(ItemSlashBlade.RepairCount.get(req) + "Repair," + cost + "ProundSoul, Remain:" + proundSoul);
            ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.SPELL_MOB, false, event.entityBladeStand.posX, event.entityBladeStand.posY + event.entityBladeStand.height / 2, event.entityBladeStand.posZ, 3, 0.5, 0, 0.5, 0.5f);
            proundSoul = ItemSlashBlade.ProudSoul.get(req);
            isDone = true;
        }
        if (isDone) event.entityBladeStand.playSound(SoundEvents.BLOCK_ANVIL_USE, 1, 0.5f);
    }

    //  希望之羽
    @SubscribeEvent
    public void FeatherOfHope(SlashBladeEvent.OnEntityBladeStandUpdateEvent event) {
//        2s循环计时器
        if (event.entityBladeStand.ticksExisted % 40 == 0) {
//            检测刀的类型
            if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikaFlare", 1).getUnlocalizedName()))
                return;
            World world = event.entityBladeStand.world;
//            必须是丢出来的刀造成的刀挂架
            if (!(event.entityBladeStand.getStandType() == -1)) return;
            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
//            获取刀的主人UUID
            if (tag.hasUniqueId("Owner")) {
                UUID ownerid = tag.getUniqueId("Owner");
                EntityPlayer player = world.getPlayerEntityByUUID(ownerid);
//                搜索附近可攻击目标
                List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(event.entityBladeStand, 15);
                System.out.println(target);
                if (!target.isEmpty()) {
//                    消耗2耀魂来施放
                    if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -2, false)) return;
                }
//                  生成羽翼幻影剑
                if (!target.isEmpty() && player != null && !player.world.isRemote) {
                    for (int i = 0; i < 2; i++) {
                        Vec3d position = new Vec3d(0, 0, 0.75f).rotateYaw((i % 2 == 0 ? -90f : 90f));
                        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(player.world, player, 5f);
                        entityDrive.setInitialPosition(event.entityBladeStand.posX + position.x, event.entityBladeStand.posY + 1 + position.y, event.entityBladeStand.posZ + position.z, (i % 2 == 0 ? 90 : -90), -30f, 90f, 1.5f);
                        entityDrive.setLifeTime(300);
                        entityDrive.setColor(i % 2 == 0 ? 0xFFFF00 : 0x00FFFF);
                        entityDrive.setSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2, 0.5f);
                        entityDrive.setInterval(10);
//                        标记目标
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i, target));
                        System.out.println("Spawn");
                        player.world.spawnEntity(entityDrive);
                    }
                }
            }
        }
    }

    //  千宙深寒
    @SubscribeEvent
    public void OverCold(SlashBladeEvent.OnEntityBladeStandUpdateEvent event) {
//        检测刀的类型
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.OverCold", 1).getUnlocalizedName()))
            return;
        World world = event.entityBladeStand.world;
//        必须是丢出来的刀造成的刀挂架
        if (!(event.entityBladeStand.getStandType() == -1)) return;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
//        获取刀主人UUID
        if (tag.hasUniqueId("Owner")) {
            UUID ownerid = tag.getUniqueId("Owner");
            EntityPlayer player = world.getPlayerEntityByUUID(ownerid);
            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(event.entityBladeStand, 15);
//            冻结敌人移动
            Boolean isDone = false;
            if (!target.isEmpty() && player != null && !player.world.isRemote) {
                if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -1, false)) return;
                for (EntityLivingBase targetEntity : target) {
                    targetEntity.motionX = 0;
                    targetEntity.motionY = 0;
                    targetEntity.motionZ = 0;
                    targetEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 5, 50));
                    if (event.entityBladeStand.ticksExisted%20==0){
                        ParticleUtils.spawnParticle(world,EnumParticleTypes.SNOW_SHOVEL,true,targetEntity.posX,targetEntity.posY+targetEntity.height/2,targetEntity.posZ,20,0,0,0,0.2f);
                    }
                }
                if (event.entityBladeStand.ticksExisted%20==0){
                    event.entityBladeStand.playSound(SoundEvents.BLOCK_GLASS_BREAK,1,0.5f);
                }
                ParticleUtils.spawnParticle(world,EnumParticleTypes.CLOUD,true,event.entityBladeStand.posX,event.entityBladeStand.posY+event.entityBladeStand.height,event.entityBladeStand.posZ,20,10,0,10,0f);
            }
        }
    }

}
