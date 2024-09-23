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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.named.item.ItemFdSlashBlade;
import tennouboshiuzume.mods.fantasydesire.util.BladeUtils;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;

import java.time.Duration;
import java.util.Random;

/**
 * Created by Furia on 15/06/19.
 */
public class EvolutionIce implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "EvolutionIce";

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
        EntityLivingBase target = event.target;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(event.blade);
        int proudSoul = ItemSlashBlade.ProudSoul.get(tag);
        int evolutionStage = 0;

        int evo_3 = 30000;
        int evo_2 = 3000;
        int evo_1 = 300;

        DamageSource OverCold = new EntityDamageSource("directMagic", player).setDamageIsAbsolute();
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, event.blade));
        float baseModif = ItemSlashBlade.BaseAttackModifier.get(tag);
        float magicDamage = 1.0f + (baseModif / 2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        target.extinguish();
        World world = player.world;

        if (proudSoul >= evo_3) {
//            evo 3
            ItemFdSlashBlade.bladeType.set(tag, "OverCold_3");
            ItemFdSlashBlade.ModelName.set(tag, "named/OverCold_3");
            if (!MathUtils.randomCheck(35))return;
            summonFrostStar(world,player,target,40,magicDamage*2f,3f,4);
        } else if (proudSoul >= evo_2) {
//            evo 2
            ItemFdSlashBlade.bladeType.set(tag, "OverCold_2");
            ItemFdSlashBlade.ModelName.set(tag, "named/OverCold_2");
            if (!MathUtils.randomCheck(30))return;
            summonFrostStar(world,player,target,20,magicDamage*1.5f,1.5f,2);
        } else if (proudSoul >= evo_1) {
//            evo 1
            ItemFdSlashBlade.bladeType.set(tag, "OverCold_1");
            ItemFdSlashBlade.ModelName.set(tag, "named/OverCold_1");
            if (!MathUtils.randomCheck(25))return;
            target.attackEntityFrom(OverCold, magicDamage);
            target.hurtTime = 0;
        } else {
//            evo 0
            ItemFdSlashBlade.bladeType.set(tag, "OverCold_0");
            ItemFdSlashBlade.ModelName.set(tag, "named/OverCold_0");
            if (!MathUtils.randomCheck(20))return;
            target.attackEntityFrom(OverCold, magicDamage * 0.5f);
            target.hurtTime = 0;
        }
    }

    private void summonFrostStar(World world,EntityPlayer player,EntityLivingBase target, int duration,float damage,float hitScale,int color){
        Random random = player.getRNG();
        if (!world.isRemote){
            EntityOverCharge entityDrive = new EntityOverCharge(world,player,damage);
            entityDrive.setInitialPosition(target.posX,target.posY+target.height/2,target.posZ,0,0,0,0);
            entityDrive.setInterval(duration);
            entityDrive.setColor(freezeColor[random.nextInt(color)]);
            entityDrive.setScale(0.5f);
            entityDrive.setHitScale(hitScale);
            entityDrive.setIsOverWall(true);
            entityDrive.setIsNonPlayer(true);
            entityDrive.setLifeTime(20);
            world.spawnEntity(entityDrive);

        }
    }
    private final int[] freezeColor = new int[]{
            0xAAFFFF,
            0x00AAFF,
            0x00FFFF,
            0x0000FF
    };

    @Override
    public void register() {
        SlashBladeHooks.EventBus.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 5;
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