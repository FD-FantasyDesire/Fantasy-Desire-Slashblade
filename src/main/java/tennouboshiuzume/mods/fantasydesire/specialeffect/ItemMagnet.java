package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ItemMagnet implements ISpecialEffect, IRemovable
{
    private static final String EffectKey = "ItemMagnet";

    /**
     * 使用コスト
     */
    private static final int COST = 5;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 1;

//    private boolean useBlade(ItemSlashBlade.ComboSequence sequence)
//    {
//        if (sequence.useScabbard) return false;
//        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
//        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
//        return true;
//    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event)
    {
        if (!SpecialEffects.isPlayer(event.entity))
            return;

        EntityPlayer player = (EntityPlayer)event.entity;

        if (!player.getHeldItemMainhand().equals(event.blade)) return;

        switch (SpecialEffects.isEffective(player, event.blade,this)) {
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
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);

        if (ItemSlashBlade.IsBroken.get(tag).booleanValue()) return;
        float range = 15f;
        List<Entity> list = findLoot(player,range);
        if (ItemSlashBlade.ProudSoul.get(tag)==0)return;
        if (!list.isEmpty()){

            Vec3d pos1 = new Vec3d(0,0,0.75);
            Vec3d pos2 = pos1.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime()%60*((float) 360 /60)));
            Vec3d pos3 = pos2.rotateYaw((float) Math.toRadians(180));
            ParticleUtils.spawnParticle(player.world,EnumParticleTypes.SPELL_MOB,true,pos2.x+player.posX,pos2.y+player.posY+player.height/3,pos2.z+player.posZ,0,0,-1,1,1);
            ParticleUtils.spawnParticle(player.world,EnumParticleTypes.SPELL_MOB,true,pos3.x+player.posX,pos3.y+player.posY+player.height/3,pos3.z+player.posZ,0,1,-1,0,1);

            if (player.world.getWorldTime()%20==0){
                ItemSlashBlade.ProudSoul.tryAdd(tag,-1,false);
            }

            for (Entity item : list){

                if (player.getDistance(item)<=1.5 && item instanceof EntityXPOrb){
                    EntityXPOrb xpOrb = (EntityXPOrb) item;
                    player.xpCooldown = 10;
                    item.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,0.1f,1f);

                    ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING,player);
                    if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                        float ratio = itemstack.getItem().getXpRepairRatio(itemstack);
                        int i = Math.min(roundAverage((float)xpOrb.getXpValue() * ratio), itemstack.getItemDamage());
                        xpOrb.xpValue -= roundAverage((float)i / ratio);
                        itemstack.setItemDamage(itemstack.getItemDamage() - i);
                    }
                    if (xpOrb.getXpValue() > 0) {
                        player.addExperience(xpOrb.getXpValue());
                    }
                    item.setDead();
                }
//                ParticleUtils.spawnParticle(item.world,EnumParticleTypes.END_ROD,true,item.posX,item.posY+item.height/2,item.posZ,1,0 ,0 ,0,0.1f);
                if (player.getDistance(item) <= range &&player.getDistance(item)>1.5){
                    double dx = player.posX - item.posX;
                    double dy = player.posY+player.height/2 - item.posY;
                    double dz = player.posZ - item.posZ;

                    if (item instanceof EntityItem){
                        Vec3d pos4 = new Vec3d(0,0,0.25);
                        Vec3d pos5 = pos4.rotateYaw((float) Math.toRadians(player.world.getTotalWorldTime()%60*((float) 360 /60)));
                        Vec3d pos6 = pos5.rotateYaw((float) Math.toRadians(180));
                        ParticleUtils.spawnParticle(item.world,EnumParticleTypes.REDSTONE,true,pos5.x+item.posX,pos5.y+item.posY+item.height/2,pos5.z+item.posZ,0,0.1 ,0 ,1,1);
                        ParticleUtils.spawnParticle(item.world,EnumParticleTypes.REDSTONE,true,pos6.x+item.posX,pos6.y+item.posY+item.height/2,pos6.z+item.posZ,0,0,0,0,0);
                    }

                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (distance > 1.5) {
                        double pullFactor = 0.3 / distance;
                        item.motionX += dx * pullFactor * (item.isAirBorne ? 0.33 : 1);
                        item.motionY += dy * pullFactor * (item.isAirBorne ? 0.33 : 1);
                        item.motionZ += dz * pullFactor * (item.isAirBorne ? 0.33 : 1);
                        item.velocityChanged = true;
                    }
                }
            }
        }
    }

    private static int roundAverage(float p_roundAverage_0_) {
        double floor = Math.floor((double)p_roundAverage_0_);
        return (int)floor + (Math.random() < (double)p_roundAverage_0_ - floor ? 1 : 0);
    }

    private List<Entity> findLoot(Entity entity,float range){
        List<Entity> list = new ArrayList<>();
        List<EntityItem> itemList = entity.world.getEntitiesWithinAABB(EntityItem.class,entity.getEntityBoundingBox().grow(range));

        for (EntityItem item:itemList){
            if (!item.cannotPickup() && entity.getDistance(item)<=range){
//                System.out.println(entity.getDistance(item)<=range);
                list.add(item);
            }
        }

        List<EntityXPOrb> xpOrbList = entity.world.getEntitiesWithinAABB(EntityXPOrb.class,entity.getEntityBoundingBox().grow(range));
        for (EntityXPOrb xpOrb : xpOrbList){
            if (xpOrb.delayBeforeCanPickup == 0  && entity.getDistance(xpOrb)<=range){
                list.add(xpOrb);
            }
        }
//        System.out.println(list);
        return list;
    }


    @Override
    public void register()
    {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel()
    {
        return 10;
    }

    @Override
    public String getEffectKey()
    {
        return EffectKey;
    }

    @Override
    public boolean canCopy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return true;
    }
}