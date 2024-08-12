package tennouboshiuzume.mods.fantasydesire.entity;


import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
//他妈的霓虹金，两个类弄一样名字干什么了
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

public class EntitySoulPhantomSword extends EntityPhantomSwordExBase{

    public EntitySoulPhantomSword(World par1World) {
        super(par1World);
    }

    public EntitySoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel, float roll) {
        super(par1World, entityLiving, AttackLevel, roll);
    }

    public EntitySoulPhantomSword(World par1World, EntityLivingBase entityLiving, float AttackLevel) {
        super(par1World, entityLiving, AttackLevel);
    }
    private static final DataParameter<Boolean> Burst = EntityDataManager.<Boolean>createKey(EntitySoulPhantomSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> Lightning = EntityDataManager.<Boolean>createKey(EntitySoulPhantomSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> ExpRadius = EntityDataManager.<Float>createKey(EntitySoulPhantomSword.class, DataSerializers.FLOAT);

    @Override
    protected void entityInit() {
        super.entityInit();


        //burst
        this.getDataManager().register(Burst, false);
        this.getDataManager().register(ExpRadius, 1.0f);
        this.getDataManager().register(Lightning,false);
    }

    public boolean getBurst(){
        return this.getDataManager().get(Burst);
    }
    public void setBurst(boolean value){
        this.getDataManager().set(Burst,value);
    }
    public float getExpRadius(){
        return this.getDataManager().get(ExpRadius);
    }
    public void setExpRadius(float value){
        this.getDataManager().set(ExpRadius,value);
    }

    @Override
    protected void attackEntity(Entity target) {
        if(getBurst()){
            target.hurtResistantTime = 0;
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, getExpRadius(), false, false);
        }

        if(!this.world.isRemote){
            float magicDamage = Math.max(1.0f, AttackLevel);
            target.hurtResistantTime = 0;
            DamageSource ds = new EntityDamageSource("directMagic",this.getThrower()).setDamageBypassesArmor().setMagicDamage().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
            target.attackEntityFrom(ds, magicDamage);

            if(!blade.isEmpty() && target instanceof EntityLivingBase && thrower != null && thrower instanceof EntityLivingBase){
                StylishRankManager.setNextAttackType(this.thrower, StylishRankManager.AttackTypes.PhantomSword);
                ((ItemSlashBlade)blade.getItem()).hitEntity(blade,(EntityLivingBase)target,(EntityLivingBase)thrower);

                if (!target.isEntityAlive())
                    ((EntityLivingBase)thrower).heal(1.0F);

                ReflectionAccessHelper.setVelocity(target, 0, 0, 0);
                target.addVelocity(0.0, 0.1D, 0.0);

                ((EntityLivingBase) target).hurtTime = 1;

                if(getBurst()){
                    ((EntityLivingBase)target).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20 * 5, 1));
                }

                ((ItemSlashBlade)blade.getItem()).setDaunting(((EntityLivingBase) target));
            }
        }

        this.setDead();
    }
}
