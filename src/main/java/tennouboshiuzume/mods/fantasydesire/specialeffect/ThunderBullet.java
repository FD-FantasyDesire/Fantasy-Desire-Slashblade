package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ability.FireResistance;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.*;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class ThunderBullet implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "ThunderBullet";

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.ImpactEffectEvent event) {

        if (!SpecialEffects.isPlayer(event.user))
            return;
        EntityPlayer player = (EntityPlayer) event.user;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade itemBlade = (ItemSlashBlade) event.blade.getItem();
        if (ItemSlashBlade.IsBroken.get(tag).booleanValue())
            return;
        switch (SpecialEffects.isEffective(player, event.blade, this)) {
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
        if (!event.blade.getUnlocalizedName().equals(BladeUtils.findItemStack(FantasyDesire.MODID, "tennouboshiuzume.slashblade.ModernGunblade", 1).getUnlocalizedName())
        ) {
            player.sendStatusMessage(new TextComponentString(I18n.format("tennouboshiuzume.tip.GunbladeFail")), true);
            return;
        }

        if (SpecialEffects.isEffective(player,event.blade,"ExplosionBullet") == SpecialEffects.State.Effective) return;
        Entity target = event.target;

        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, event.blade);
            float magicDamage = 1.0f + ItemSlashBlade.BaseAttackModifier.get(tag) / 2.0f;
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank){
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
        }
        if ( player.getDistance(target)>6 && MathUtils.randomCheck(10)){
            DamageSource HyperLightning = new EntityDamageSource("HyperLightning",player).setDamageBypassesArmor();
            Vec3d pos2 = new Vec3d(0, 0, 1);
            for (int i = 0; i < 72; i++) {
                Vec3d pos3 = pos2.rotateYaw((float) Math.toRadians(i*5f)).scale(10f);
                ParticleUtils.spawnParticle(player.world, EnumParticleTypes.FIREWORKS_SPARK, true,
                        pos3.x + target.posX, pos3.y + target.posY, pos3.z + target.posZ,
                        1, 0, 0, 0, 0);
            }
            List<EntityLivingBase> targetList = TargetUtils.findAllHostileEntities(target,10,player,false);
            targetList.remove(target);
            target.attackEntityFrom(HyperLightning,magicDamage * 5f);
            if (!targetList.isEmpty()){
                for (EntityLivingBase targetAOE:targetList){
                    targetAOE.attackEntityFrom(HyperLightning,magicDamage * 5f);
                    itemBlade.hitEntity(event.blade, targetAOE, player);
                }
            }
            target.world.addWeatherEffect(new EntityLightningBolt(target.world,target.posX,target.posY,target.posZ,true));
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
        return true;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return true;
    }
}