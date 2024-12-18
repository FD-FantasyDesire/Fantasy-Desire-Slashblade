package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.DefeatTheBoss;
import mods.flammpfeil.slashblade.ability.SoulEater;
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
import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class ColorFlux implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "ColorFlux";

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event) {
        if (!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;
        if (!(event.blade.getItem() instanceof ItemFdSlashBlade))return;
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.PureSnow", 1).getUnlocalizedName()))
            return;

        switch (SpecialEffects.isEffective(player, event.blade, this)) {
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 2, 2));
                return;
        }

        EntityLivingBase target = event.target;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ProudSoul.tryAdd(tag,1,false);

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, event.blade));
        float baseModif = ItemSlashBlade.BaseAttackModifier.get(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));

        if (target.getHealth() < magicDamage){
            ItemSlashBlade.ProudSoul.tryAdd(tag, 5, false);
            ItemSlashBlade.KillCount.tryAdd(tag, 1, false);
        }
        summonRainbowPhantomSword(player.world, player,target,20,magicDamage);
    }

    private void summonRainbowPhantomSword(World world,EntityPlayer player,EntityLivingBase target, int duration,float damage){
        Random random = player.getRNG();
        if (!world.isRemote){
            float yaw = (float) (random.nextInt(360));
            float pitch = (float) (random.nextGaussian() * 30f);
            float roll = (float) (random.nextInt(360)-180);
            Vec3d basePos = new Vec3d(0,0,1);
            Vec3d spawnPos = new Vec3d(target.posX,target.posY+target.height/2,target.posZ)
                    .add(basePos
                            .rotatePitch((float) Math.toRadians(pitch))
                            .rotateYaw((float) Math.toRadians(yaw))
                            .scale(15f));

            EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world, player, damage);
            entityDrive.setInitialPosition(
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    -yaw,
                    -pitch + 180f,
                    roll,
                    1.75f
            );
            entityDrive.setIsOverWall(true);
            entityDrive.setInterval(0);
            entityDrive.setColor(ColorUtils.getSmoothTransitionColor(yaw,120,true));
            entityDrive.setRainbow(true,yaw,120,1f);
            entityDrive.setTargetEntityId(target.getEntityId());
            entityDrive.setIsNonPlayer(true);
            entityDrive.setLifeTime(duration);
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