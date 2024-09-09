package tennouboshiuzume.mods.fantasydesire.named;

import com.sun.java.accessibility.util.java.awt.ListTranslator;
import jdk.nashorn.internal.ir.Block;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.core.jmx.Server;
import scala.tools.nsc.doc.model.Entity;
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

    public BladeStandEvents(){
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
//        灵魂锻造:每-1000魂，+1锻造
    public void OnAnvilFall(SlashBladeEvent.BladeStandAttack event){
//        是否有刀
        if(!event.entityBladeStand.hasBlade()) return;
//        是否为单刀挂架
        if(EntityBladeStand.getType(event.entityBladeStand) != EntityBladeStand.StandType.Single) return;
//        是否为铁砧对刀挂架造成伤害（至少从两格以上落下）
        if(!event.damageSource.damageType.equals("anvil")) return;
//        检测刀挂架是否放在铁块上
        if (!(EntityUtils.getBlockUnderEntity(event.entityBladeStand).getBlock() == Blocks.IRON_BLOCK)) return;

        NBTTagCompound req = ItemSlashBlade.getItemTagCompound(event.blade);
        int proundSoul = ItemSlashBlade.ProudSoul.get(req);
        if (proundSoul > 11000){
//            计算可锻造次数
            int count = (proundSoul-10000)/1000;
//            扣除耀魂
            ItemSlashBlade.ProudSoul.tryAdd(req,-1000*count,false);
//            增加锻造次数
            ItemSlashBlade.RepairCount.tryAdd(req, count, false);
//            播放音效和粒子
            event.entityBladeStand.playSound(SoundEvents.BLOCK_ANVIL_USE,1,0.5f);
            ParticleUtils.spawnParticle(event.entityBladeStand.world, EnumParticleTypes.LAVA,false,
                    event.entityBladeStand.posX,
                    event.entityBladeStand.posY+event.entityBladeStand.height/2,
                    event.entityBladeStand.posZ,
                    3*count,1,0,1,0.5f
            );
        }
    }

    @SubscribeEvent
    public void WingTurret(SlashBladeEvent.OnEntityBladeStandUpdateEvent event){
//        2s循环计时器
        if(event.entityBladeStand.ticksExisted%40==0){
//            检测刀的类型
            if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID,"tennouboshiuzume.slashblade.ChikaFlare",1).getUnlocalizedName())) return;
            World world =event.entityBladeStand.world;
//            必须是丢出来的刀造成的刀挂架
            if(!(event.entityBladeStand.getStandType()==-1))return;
            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
//            获取刀的主人UUID
            if (tag.hasUniqueId("Owner")){
                UUID ownerid = tag.getUniqueId("Owner");
                EntityPlayer player = world.getPlayerEntityByUUID(ownerid);
//                搜索附近可攻击目标
                List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(event.entityBladeStand,15);
                System.out.println(target);
//                  生成羽翼幻影剑
                if (!target.isEmpty() && player != null && !player.world.isRemote) {
                    for (int i=0;i<2;i++){
                        Vec3d position = new Vec3d(0,0,0.5f).rotateYaw((i%2==0 ? -90f:90f));
                        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(player.world, player, 5f);
                        entityDrive.setInitialPosition(
                                event.entityBladeStand.posX+ position.x,
                                event.entityBladeStand.posY + 2 + position.y,
                                event.entityBladeStand.posZ+ position.z,
                                (i%2==0 ? 90:-90),
                                -60f,
                                90f,
                                1.5f);
                        entityDrive.setLifeTime(300);
                        entityDrive.setColor(i%2==0 ? 0xFFFF00:0x00FFFF);
                        entityDrive.setSound(SoundEvents.ENTITY_WITHER_BREAK_BLOCK,2,2);
                        entityDrive.setInterval(10);
//                        标记目标
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));
                        System.out.println("Spawn");
                        player.world.spawnEntity(entityDrive);
                    }
                }
            }
        }
    }
}
