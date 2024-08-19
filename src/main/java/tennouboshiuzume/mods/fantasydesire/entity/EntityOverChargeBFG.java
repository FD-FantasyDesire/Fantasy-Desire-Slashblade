package tennouboshiuzume.mods.fantasydesire.entity;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;

public class EntityOverChargeBFG extends EntityOverCharge{

    private static final DataParameter<Boolean> IS_BLAST = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Float> EXP_RADIUS = EntityDataManager.<Float>createKey(EntityOverChargeBFG.class,DataSerializers.FLOAT);

    private static final DataParameter<Boolean> IS_BFG = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);
    @Override
    protected void entityInit()
    {
        super.entityInit();
        EntityDataManager manager = getDataManager();
        manager.register(IS_BLAST,false);
        manager.register(EXP_RADIUS,5f);
        manager.register(IS_BFG, false);
    }

    // 获取 IS_BFG 的值
    public boolean getIsBFG() {
        return this.getDataManager().get(IS_BFG);
    }

    // 设置 IS_BFG 的值
    public void setBFG(boolean isBFG) {
        this.getDataManager().set(IS_BFG, isBFG);
    }

    public boolean getIsBlast(){
        return this.getDataManager().get(IS_BLAST);
    }
    public void setIsBlast(boolean isOverWall){
        this.getDataManager().set(IS_BLAST,isOverWall);
    }

    public final float getExpRadius(){
        return getDataManager().get( EXP_RADIUS);
    }
    public final void setExpRadius(float hitscale){
        this.getDataManager().set( EXP_RADIUS,hitscale);
    }

    public EntityOverChargeBFG(World worldIn) {
        super(worldIn);
    }

    public  EntityOverChargeBFG(World worldIn, EntityLivingBase entityLiving, float AttackLevel) {
        super(worldIn, entityLiving, AttackLevel);
    }
    private static final double OriginAMBIT = 1.5;

    @Override
    public void onUpdate()
    {
        if (!world.isRemote)
            detectCollision(getScale(),getHitScale());
        if(getInterval() < this.ticksExisted){
            move();
            playParticle();
        }

        if (this.ticksExisted >= getLifeTime()){
            if (getIsBlast()){
                Blast();
            }
            setDead();
        }
    }

    private void detectCollision(float scale,float hitScale)
    {
        double AMBIT = OriginAMBIT*scale*hitScale;
        AxisAlignedBB bb = new AxisAlignedBB(this.posX - AMBIT,
                this.posY - AMBIT,
                this.posZ - AMBIT,
                this.posX + AMBIT,
                this.posY + AMBIT,
                this.posZ + AMBIT);
        // ----- 射撃物の迎撃
        intercept(bb, false);

        // ----- 敵エンティティへの攻撃
        final boolean isMultiHit = isMultiHit();
        if (!isMultiHit || this.ticksExisted % 2 == 0) {

            List<Entity> list = world.getEntitiesInAABBexcluding(thrower_, bb, EntitySelectorAttackable.getInstance());
            list.removeAll(alreadyHitEntity_);
            if (!isMultiHit)
                alreadyHitEntity_.addAll(list);

            float damage = Math.max(1.0f, attackLevel_);
            StylishRankManager.setNextAttackType(
                    thrower_ ,
                    isMultiHit ?
                            StylishRankManager.AttackTypes.QuickDrive :
                            StylishRankManager.AttackTypes.Drive);

            for (Entity target : list)
            {
                if (!getIsBFG()){
                    onImpact(target,damage);
                    ((WorldServer)this.world).spawnParticle(EnumParticleTypes.TOTEM,
                            true,
                            this.posX,
                            this.posY + this.height/2,
                            this.posZ,
                            (int) (20), 0, 0, 0, 0.5);

                }
                if (this.ticksExisted%4==0&&getIsBFG()){
                    BFG(target,damage);
                }
            }
        }

        // ----- 地面等の障害物との衝突
        if (!world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !isOverWall()){
            if (getIsBlast()){
                Blast();
            }
            setDead();
        }
    }

    private void move()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;

        this.motionX *= 1.05f;
        this.motionY *= 1.05f;
        this.motionZ *= 1.05f;

        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }
    private void BFG(Entity target,float magicDamage){
        Random random = thrower_.getRNG();
        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world,thrower_,magicDamage);
        entityDrive.setInitialPosition(this.posX,this.posY,this.posZ,random.nextInt(360),random.nextInt(360),this.getRoll(),1.5f);
        entityDrive.setColor(0x99FF00);
        entityDrive.setTargetEntityId(target.getEntityId());
        entityDrive.setLifeTime(100);
        entityDrive.setInterval(40);
        world.spawnEntity(entityDrive);
    }
    private void Blast(){
        world.newExplosion(thrower_,this.posX,this.posY+this.height/2f,this.posZ,getExpRadius(),false,false);
        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.END_ROD,
                true,
                this.posX,
                this.posY + this.height/2,
                this.posZ,
                (int) (20*getExpRadius()), 0, 0, 0, 0.05*getExpRadius());
    }
}
