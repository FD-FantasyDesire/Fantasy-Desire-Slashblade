package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityBladeStand;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import scala.Int;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.*;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class PrismFlux implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "PrismFlux";

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event) {
        if (!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;

        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.PureSnow", 1).getUnlocalizedName())) return;

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        EntityLivingBase target = event.target;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ProudSoul.tryAdd(tag,1,false);

        List<EntityLivingBase> PrismTarget = TargetUtils.findAllHostileEntities(target,15f,target,true);
        PrismTarget.remove(player);
        Collections.shuffle(PrismTarget);
        if (!PrismTarget.isEmpty()){
            for(int i=0;i<4;i++){
                int index = i%PrismTarget.size();
                summonPrismPhantomSword(event.blade,player.world, player,target, PrismTarget.get(index),5f);
            }
        }
    }

    private void summonPrismPhantomSword(ItemStack blade,World world,EntityPlayer player,EntityLivingBase target, EntityLivingBase target2,float damage){
        Random random = player.getRNG();

        double xA = target.posX;
        double yA = target.posY+target.height / 2f;
        double zA = target.posZ;

        double xB = target2.posX;
        double yB = target2.posY;
        double zB = target2.posZ;

        double deltaX = xB - xA;
        double deltaY = yB - yA;
        double deltaZ = zB - zA;

        // 计算水平距离
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // 计算 yaw 和 pitch
        float yaw = (float)(Math.atan2(deltaX, deltaZ) * (180 / Math.PI));
        float pitch = (float)(-(Math.atan2(deltaY, horizontalDistance) * (180 / Math.PI)));

        if (!world.isRemote){
            float roll = (float) (random.nextInt(360)-180);
            Vec3d basePos = new Vec3d(0,0,1);
            Vec3d spawnPos = new Vec3d(xA,yA,zA)
                    .add(basePos
                            .rotatePitch((float) Math.toRadians(pitch))
                            .rotateYaw((float) Math.toRadians(yaw))
                            .scale(1f));
            EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world, player, damage);
            entityDrive.setInitialPosition(
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    -yaw,
                    pitch,
                    roll,
                    2.5f
            );
            entityDrive.setIsOverWall(true);
            entityDrive.setInterval(0);
            entityDrive.setScale(0.75f);
            NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
            if (!(ItemSlashBlade.SummonedSwordColor.get(tag)==null)){
                entityDrive.setColor(ItemSlashBlade.SummonedSwordColor.get(tag));
            }
//            entityDrive.setRainbow(true,yaw,40,1f);
            entityDrive.setTargetEntityId(target2.getEntityId());
            entityDrive.setIsNonPlayer(true);
            entityDrive.setLifeTime(30);
            world.spawnEntity(entityDrive);
        }
    }

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 60;
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