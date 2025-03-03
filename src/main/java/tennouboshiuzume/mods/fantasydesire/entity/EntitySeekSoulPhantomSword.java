package tennouboshiuzume.mods.fantasydesire.entity;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntitySeekSoulPhantomSword extends EntityPhantomSwordEx {

    PotionEffect addeffect = null;

    public EntitySeekSoulPhantomSword(World par1World) {
        super(par1World);
    }

    public EntitySeekSoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel, float roll) {
        super(par1World, entityLiving, AttackLevel, roll);
    }

    public EntitySeekSoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel) {
        super(par1World, entityLiving, AttackLevel);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    public PotionEffect setPotionEffect(PotionEffect value) {
        return value;
    }

    @Override
    protected void attackEntity(Entity target) {
        if (getBurst()) {
            target.hurtResistantTime = 0;
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, getExpRadius(), false, false);
        }
        if (!this.world.isRemote) {
            if (getTrueDamage()) {
                if (target instanceof EntityLivingBase) {
                    EntityLivingBase entityTarget = (EntityLivingBase) target;
                    entityTarget.setHealth(Math.max(1, entityTarget.getHealth() - (entityTarget.getMaxHealth() * TrueDamageLevel / 100)));
                    System.out.println(((EntityLivingBase) target).getHealth());
                }
            }
            float magicDamage = Math.max(1.0f, AttackLevel);
            target.hurtResistantTime = 0;
            DamageSource ds = new EntityDamageSource("directMagic", this.getThrower()).setDamageBypassesArmor();
            target.attackEntityFrom(ds, magicDamage);
            if (!blade.isEmpty() && target instanceof EntityLivingBase && thrower != null && thrower instanceof EntityLivingBase) {
                StylishRankManager.setNextAttackType(this.thrower, StylishRankManager.AttackTypes.PhantomSword);
                StylishRankManager.doAttack(thrower);
                if (!getIsNonPlayer()) {
                    ((ItemSlashBlade) blade.getItem()).hitEntity(blade, (EntityLivingBase) target, (EntityLivingBase) thrower);
                }

                if (!target.isEntityAlive())
                    ((EntityLivingBase) thrower).heal(1.0F);

                ReflectionAccessHelper.setVelocity(target, 0, 0, 0);
                target.addVelocity(0.0, 0.1D, 0.0);

                ((EntityLivingBase) target).hurtTime = 1;

                if (addeffect!=null) {
                    ((EntityLivingBase) target).addPotionEffect(addeffect);
                }

                ((ItemSlashBlade) blade.getItem()).setDaunting(((EntityLivingBase) target));
            }
        }
        this.setDead();
    }
}
