package tennouboshiuzume.mods.fantasydesire.specialeffect;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;

import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class TyrantStrike implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "TyrantStrike";

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence){
        if(sequence.useScabbard) return false;
        if(sequence == ItemSlashBlade.ComboSequence.None) return false;
        if(sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event){
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ChikeFlare", 1).getUnlocalizedName()))
            return;
        if(!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;

        switch (SpecialEffects.isEffective(player, event.blade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        if (!MathUtils.randomCheck(10)) return;
        World world = player.world;
        ItemStack stack = event.blade;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
        Entity target = event.target;

        if(target instanceof EntityLivingBase){
            blade.setDaunting((EntityLivingBase)target);
            ((EntityLivingBase) target).hurtTime = 0;
            ((EntityLivingBase) target).hurtResistantTime = 0;
        }

        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        float magicDamage = 1.0f + ItemSlashBlade.BaseAttackModifier.get(tag) * (level / 5.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank){
            magicDamage += ItemSlashBlade.BaseAttackModifier.get(tag);
        }
        magicDamage += event.target.getMaxHealth()/4;
//        System.out.println(magicDamage);
//        生成尖碑幻影剑
        if (!world.isRemote){
            if (target != null && !target.isDead){
                Random random = player.getRNG();
                float yaw = (float) random.nextInt(360);
                float pitch = 90f+(float)(random.nextGaussian() * 10f);
                float roll = (float) (random.nextInt(360) - 180);
                Vec3d basePos = new Vec3d(0, 0, 1);
                Vec3d spawnPos = new Vec3d(target.posX, target.posY + target.height/2, target.posZ)
                        .add(basePos
                                .rotatePitch((float) Math.toRadians(pitch))
                                .rotateYaw((float) Math.toRadians(yaw))
                                .scale(30f));
                EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                entityDrive.setInitialPosition(
                        spawnPos.x,
                        spawnPos.y,
                        spawnPos.z,
                        -yaw,
                        -pitch-180f,
                        roll,
                        2.5f);
                entityDrive.setInterval(0);
                entityDrive.setScale(2.5f);
                entityDrive.setLifeTime(40);
                entityDrive.setColor(0x9900EE);
                entityDrive.setParticle(EnumParticleTypes.EXPLOSION_LARGE);
                entityDrive.setIsOverWall(true);
                entityDrive.setTrueDamage(true,5);
                entityDrive.setPotionEffect(new PotionEffect(MobEffects.LEVITATION,20*5,1));
                entityDrive.setTargetEntityId(target.getEntityId());
                world.spawnEntity(entityDrive);
            }
        }
        event.target.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,2.5f,2.0f);
        player.onEnchantmentCritical(event.target);
        event.target.addPotionEffect(new PotionEffect(MobEffects.GLOWING,20*5,0));
        event.target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,20*5,3));
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