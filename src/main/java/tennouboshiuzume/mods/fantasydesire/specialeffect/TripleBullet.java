package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tennouboshiuzume.mods.fantasydesire.entity.EntityDriveEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverChargeBFG;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class TripleBullet implements ISpecialEffect
{
    private static final String EffectKey = "TripleBullet";

    /**
     * 使用コスト
     */
    private static final int COST = 2;

    /**
     * コスト不足時の刀へのダメージ
     */
    private static final int NO_COST_DAMAGE = 1;

    private boolean useBlade(ItemSlashBlade.ComboSequence sequence)
    {
        if (sequence.useScabbard) return false;
        if (sequence == ItemSlashBlade.ComboSequence.None) return false;
        if (sequence == ItemSlashBlade.ComboSequence.Noutou) return false;
        return true;
    }

    @SubscribeEvent
    public void onUpdateItemSlashBlade(SlashBladeEvent.OnUpdateEvent event)
    {
        if (!SpecialEffects.isPlayer(event.entity))
            return;
        EntityPlayer player = (EntityPlayer)event.entity;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        ItemSlashBlade.ComboSequence seq = ItemSlashBlade.getComboSequence(tag);
        if (!useBlade(seq))
            return;
        if (ItemSlashBlade.IsBroken.get(tag).booleanValue())
            return;

        if (SpecialEffects.isEffective(player, event.blade, this) != SpecialEffects.State.Effective) {
            return;
        }

        PotionEffect haste = player.getActivePotionEffect(MobEffects.HASTE);
        int check = haste != null ? haste.getAmplifier() != 1 ? 1 : 3 : 2;
//        System.out.println(player.swingProgressInt);

        if (player.swingProgressInt != check)
            return;

        doAddAttack(event.blade, player, seq);
    }

    public void doAddAttack(ItemStack stack, EntityPlayer player, ItemSlashBlade.ComboSequence setCombo)
    {
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        World world = player.world;

        if (ItemSlashBlade.SpecialAttackType.get(tag)!=201) return;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -COST, false)) {
            ItemSlashBlade.damageItem(stack, NO_COST_DAMAGE, player);
            return;
        }

        if (world.isRemote)
            return;

        float baseModif = ((ItemSlashBlade)stack.getItem()).getBaseAttackModifiers(tag);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        float magicDamage = baseModif + level;
        int rank = StylishRankManager.getStylishRank(player);
        if (rank >= 5) {
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag)*(level/5.0 + 0.5f);
        }
        Random random = player.getRNG();
//            追踪弹
        List<EntityLivingBase> target = TargetUtils.findHostileEntitiesInFOV(player,60,30f,true);
        int round = Math.min(Math.max(player.experienceLevel/10,1),5);
        for (int i=0;i<round;i++){
            EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, player, magicDamage);
            entityDrive.setInterval(1+i);
            entityDrive.setScale(0.2f);
            entityDrive.setSound(SoundEvents.ENTITY_WITHER_SHOOT,3f,2f);
            entityDrive.setColor(0x00CCCC);
            entityDrive.setInitialPosition(player.getLookVec().x+player.posX,
                    player.getLookVec().y+player.posY+1.2f,
                    player.getLookVec().z+player.posZ,
                    (float) (player.rotationYaw+random.nextGaussian()),
                    (float) (player.rotationPitch+random.nextGaussian()),
                    random.nextInt(180),
                    3f);
            entityDrive.setLifeTime(100+i);
            entityDrive.setParticle(EnumParticleTypes.FIREWORKS_SPARK);
            if (!target.isEmpty()){
                entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));
                TargetUtils.setTargetEntityFromListByEntity(i,target).addPotionEffect(new PotionEffect(MobEffects.GLOWING,20*6,0));
            }
            world.spawnEntity(entityDrive);
        }
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
}