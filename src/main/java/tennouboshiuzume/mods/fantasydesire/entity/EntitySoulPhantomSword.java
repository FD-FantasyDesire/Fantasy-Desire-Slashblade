package tennouboshiuzume.mods.fantasydesire.entity;


import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
//他妈的霓虹金，两个类弄一样名字干什么了
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntitySoulPhantomSword extends EntityPhantomSwordExBase {

    PotionEffect addeffect = null;

    public EntitySoulPhantomSword(World par1World) {
        super(par1World);
    }

    public EntitySoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel, float roll) {
        super(par1World, entityLiving, AttackLevel, roll);
    }

    public EntitySoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel) {
        super(par1World, entityLiving, AttackLevel);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    public PotionEffect setPotionEffect(PotionEffect value){
        return value;
    }


    @Override
    protected void attackEntity(Entity target) {
        if (getBurst()) {
            target.hurtResistantTime = 0;
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, getExpRadius(), false, false);
        }
        if (!this.world.isRemote) {
            if (getTrueDamage()){
                if (target instanceof EntityLivingBase){
                    EntityLivingBase entityTarget =(EntityLivingBase) target;
                    entityTarget.setHealth(entityTarget.getHealth() - (entityTarget.getMaxHealth() * TrueDamageLevel / 100) );
                }
            }
            float magicDamage = Math.max(1.0f, AttackLevel);
            target.hurtResistantTime = 0;
            DamageSource ds = new EntityDamageSource("directMagic", this.getThrower()).setDamageIsAbsolute().setDamageAllowedInCreativeMode();
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
