package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.UUID;

/**
 * Created by Furia on 15/06/19.
 */
public class ColdLeak implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "ColdLeak";

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event) {

        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.OverCold", 1).getUnlocalizedName()))
            return;
        if (!SpecialEffects.isPlayer(event.user)) return;

        EntityPlayer player = (EntityPlayer) event.user;

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 2, 2));
                return;
        }
        EntityLivingBase targetEntity = event.target;
        targetEntity.motionX = 0;
        targetEntity.motionY = 0;
        targetEntity.motionZ = 0;
        StunManager.setStun(targetEntity,5);
        targetEntity.extinguish();
        targetEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 5, 50));
        targetEntity.playSound(SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, 1f, 0.8f);
        ParticleUtils.spawnParticle(player.world, EnumParticleTypes.SNOW_SHOVEL, true, targetEntity.posX, targetEntity.posY + targetEntity.height / 2, targetEntity.posZ, 20, 0, 0, 0, 0.2f);
    }

    //  冰封气场
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
            if (player == null) {
                return;
            }
            switch (SpecialEffects.isEffective(player, event.blade, FdSEs.ColdLeak)) {
                /** 任何时候可触发 */
                case None:
                    return;
                /** 未达到所需等级 */
                case NonEffective:
                    return;
                /** 达到所需等级 */
                case Effective:
                    break;
            }
//            检查进化等级
            int range = 1;

            int evo_3 = 30000;
            int evo_2 = 3000;
            int evo_1 = 300;

//            勿动，SB拔刀剑作者竟然没给刀挂台做服务端客户端同步
            int proudSoul = ItemSlashBlade.ProudSoul.get(tag);
            ItemSlashBlade.ProudSoul.set(tag,proudSoul);
            System.out.println(ItemSlashBlade.ProudSoul.get(tag));

            if (proudSoul>=evo_3){
//            evo 3
                ItemFdSlashBlade.bladeType.set(tag,"OverCold_3");
                ItemFdSlashBlade.ModelName.set(tag,"named/OverCold_3");
                range = 15;
            } else if (proudSoul>=evo_2) {
//            evo 2
                ItemFdSlashBlade.bladeType.set(tag,"OverCold_2");
                ItemFdSlashBlade.ModelName.set(tag,"named/OverCold_2");
                range = 11;
            }else if (proudSoul>=evo_1){
//            evo 1
                ItemFdSlashBlade.bladeType.set(tag,"OverCold_1");
                ItemFdSlashBlade.ModelName.set(tag,"named/OverCold_1");
                range = 7;
            }else {
//            evo 0
                ItemFdSlashBlade.bladeType.set(tag,"OverCold_0");
                ItemFdSlashBlade.ModelName.set(tag,"named/OverCold_0");
                range = 3;
            }

            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(event.entityBladeStand, range, player,false);

            Boolean isDone = false;

            if (!target.isEmpty()) {
                if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -1, false)) return;
//                  冻结敌人移动
                if (!player.world.isRemote) {
                    for (EntityLivingBase targetEntity : target) {
                        targetEntity.motionX = 0;
                        targetEntity.motionY = 0;
                        targetEntity.motionZ = 0;
                        targetEntity.setPositionAndRotationDirect(targetEntity.lastTickPosX,targetEntity.lastTickPosY,targetEntity.lastTickPosZ,targetEntity.prevRotationYaw,targetEntity.prevRotationPitch,0,true);
                        targetEntity.extinguish();
                        targetEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 5, 50));
                        if (event.entityBladeStand.ticksExisted % 20 == 0) {
                            ParticleUtils.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, true, targetEntity.posX, targetEntity.posY + targetEntity.height / 2, targetEntity.posZ, 20, 0, 0, 0, 0.2f);
                        }
                    }
                    if (event.entityBladeStand.ticksExisted % 20 == 0) {
                        event.entityBladeStand.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 0.5f);
                    }
                }
            }
            if (!player.world.isRemote) {
                ParticleUtils.spawnParticle(world, EnumParticleTypes.CLOUD, true, event.entityBladeStand.posX, event.entityBladeStand.posY + 1f, event.entityBladeStand.posZ, 2, 0, 0, 0, 0.2f);
            }
        }
    }

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 30;
    }

    @Override
    public String getEffectKey() {
        return EffectKey;
    }

    @Override
    public boolean canCopy(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return false;
    }
}