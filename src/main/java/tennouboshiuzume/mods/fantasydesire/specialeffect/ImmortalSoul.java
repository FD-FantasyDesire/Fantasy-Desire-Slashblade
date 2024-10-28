package tennouboshiuzume.mods.fantasydesire.specialeffect;

import ibxm.Player;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.rmi.activation.Activatable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Furia on 15/06/19.
 */

public class ImmortalSoul implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "ImmortalSoul";

    //    当玩家死亡时，如果耀魂大于1000，免除该次死亡，并且基础攻击伤害+2
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack blade;
        ItemStack MainBlade = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack OffBlade = player.getHeldItem(EnumHand.OFF_HAND);

        if ((MainBlade.getItem() instanceof ItemSlashBlade && MainBlade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))) {
            blade = MainBlade;
        } else if ((OffBlade.getItem() instanceof ItemSlashBlade && OffBlade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))) {
            blade = OffBlade;
        } else {
            return;
        }
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
        switch (SpecialEffects.isEffective(player, blade, FdSEs.ImmortalSoul)) {
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
        World world = player.world;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -1000, false)) return;
        ItemSlashBlade.setBaseAttackModifier(tag, ItemSlashBlade.BaseAttackModifier.get(tag) + 2);
        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0f, 2.0f);
        ParticleUtils.spawnParticle(world, EnumParticleTypes.END_ROD, false, player.posX, player.posY + player.height / 2, player.posZ, 200, 0, 0, 0, 0.5f);
        player.setHealth(player.getMaxHealth() / 2);
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20 * 6, 5));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20 * 6, 10));
        if (ItemSlashBlade.ProudSoul.get(tag) >= 1000) {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.ImmortalSoulActive")), true);
        } else {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.ImmortalSoulActive1")), true);
        }
        event.setCanceled(true);
    }

    //  希望之羽
    @SubscribeEvent
    public void FeatherOfHope(SlashBladeEvent.OnEntityBladeStandUpdateEvent event) {
//        2s循环计时器
        if (event.entityBladeStand.ticksExisted % 40 == 0) {
//            检测刀的类型
            if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))
                return;
            World world = event.entityBladeStand.world;
//            必须是丢出来的刀造成的刀挂架
            if (!(event.entityBladeStand.getStandType() == -1)) return;
            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
//            获取刀的主人UUID
            if (tag.hasUniqueId("Owner")) {
                UUID ownerid = tag.getUniqueId("Owner");
                EntityPlayer player = world.getPlayerEntityByUUID(ownerid);
                if (player == null) {
                    return;
                }
//                检查是否生效
                switch (SpecialEffects.isEffective(player, event.blade, FdSEs.ImmortalSoul)) {
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
//                搜索附近可攻击目标
                List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(event.entityBladeStand, 15, player, false);
//                System.out.println(target);
                Collections.shuffle(target);
                if (!target.isEmpty()) {
//                    消耗2耀魂来施放
                    if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -2, false)) return;
                }
//                  生成羽翼幻影剑
                if (!target.isEmpty() && player != null && !player.world.isRemote) {
                    for (int i = 0; i < 2; i++) {
                        Vec3d position = new Vec3d(0, 0, 1).rotateYaw((float) Math.toRadians(i % 2 == 0 ? -90f : 90f));
                        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(player.world, player, 5f);
                        entityDrive.setInitialPosition(event.entityBladeStand.posX + position.x, event.entityBladeStand.posY + 1 + position.y, event.entityBladeStand.posZ + position.z, (i % 2 == 0 ? 90 : -90), -30f, 90f, 1.5f);
                        entityDrive.setLifeTime(300);
                        entityDrive.setColor(i % 2 == 0 ? 0xFFFF00 : 0x00FFFF);
                        entityDrive.setSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2, 0.5f);
                        entityDrive.setInterval(10);
//                        标记目标
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i, target));
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        entityDrive.setParticleVec(10);
//                        System.out.println("Spawn");
                        player.world.spawnEntity(entityDrive);
                    }
                }
            }
        }
    }


    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 1;
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