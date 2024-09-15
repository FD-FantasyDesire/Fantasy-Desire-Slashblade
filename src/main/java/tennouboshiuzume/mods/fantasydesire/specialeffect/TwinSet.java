package tennouboshiuzume.mods.fantasydesire.specialeffect;

import javafx.scene.effect.Effect;
import mods.flammpfeil.slashblade.ItemSlashBladeNamed;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tennouboshiuzume.mods.fantasydesire.entity.*;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;


public class TwinSet implements ISpecialEffect
{
    private static final String EffectKey = "TwinSet";

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
    public void onImpactEffectEvent(SlashBladeEvent.ImpactEffectEvent event){
        if(!SpecialEffects.isPlayer(event.user)) return;
        EntityPlayer player = (EntityPlayer) event.user;
//        检测双持
        if (!(player.getHeldItemOffhand().getItem() instanceof ItemSlashBlade))return;
        ItemStack offBlade = player.getHeldItemOffhand();
        switch (SpecialEffects.isEffective(player, offBlade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        switch (SpecialEffects.isEffective(player, event.blade, this)){
            case None:
                return;
            case Effective:
                break;
            case NonEffective:
                return;
        }
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        NBTTagCompound offtag = ItemSlashBlade.getItemTagCompound(offBlade);


        int mainSoul = ItemSlashBlade.ProudSoul.get(tag);
        int mainKill = ItemSlashBlade.KillCount.get(tag);
        int offSoul = ItemSlashBlade.ProudSoul.get(offtag);
        int offKill = ItemSlashBlade.KillCount.get(offtag);
        //均分资源
        int avgSoul = (mainSoul+offSoul)/2;
        int avgKill = (mainKill+offKill)/2;

        ItemSlashBlade.ProudSoul.set(tag,avgSoul);
        ItemSlashBlade.ProudSoul.set(offtag,avgSoul);

        ItemSlashBlade.KillCount.set(tag,avgKill);
        ItemSlashBlade.KillCount.set(offtag,avgKill);

        ItemSlashBlade blade = (ItemSlashBlade)event.blade.getItem();
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, event.blade));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        EntityLivingBase target = event.target;
        Random random = target.getRNG();

//        B以上回复耐久
        if (rank >= 3){
            event.blade.setItemDamage(event.blade.getItemDamage() - 3);
            offBlade.setItemDamage(event.blade.getItemDamage() - 3);
        }
//        S以上追加攻击
        if (rank >= 5){
            ParticleUtils.spawnParticle((WorldServer)target.world,EnumParticleTypes.SWEEP_ATTACK,true,target.posX,target.posY,target.posZ,10,1,1,1, 0.75f);
            DamageSource ts = new DamageSource("TwinSet").setDamageIsAbsolute().setDamageBypassesArmor();
            target.attackEntityFrom(ts, magicDamage);
            target.playSound(SoundEvents.BLOCK_GLASS_BREAK,2,2f);
            player.onEnchantmentCritical(target);
        }
        target.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,20*3,50));

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