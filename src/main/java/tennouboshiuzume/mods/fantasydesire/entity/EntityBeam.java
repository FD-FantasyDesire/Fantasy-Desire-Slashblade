package tennouboshiuzume.mods.fantasydesire.entity;

import com.google.common.base.Predicate;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorDestructable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.tools.nsc.doc.model.Public;
import tennouboshiuzume.mods.fantasydesire.util.ColorUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/08.
 */
public class EntityBeam extends Entity implements IProjectile, IThrowableEntity {

    /**
     * ★撃った人
     */
    protected Entity thrower;

    protected ItemStack blade = ItemStack.EMPTY;
    private static final double OriginAMBIT = 1.5;
    protected SoundEvent sound = null;
    protected float volume = 1;
    protected float rate = 1;

    protected Boolean Rainbow = false;
    protected float colorStep = 1;
    protected int colorTotalStep = 1;
    protected float colorStepScale = 1;
    /**
     * ★多段Hit防止用List
     */
    protected List<Entity> alreadyHitEntity = new ArrayList<Entity>();

    protected float AttackLevel = 0.0f;

    protected float TrueDamageLevel = 0.0f;

    protected EnumParticleTypes updateParticle = null;

    protected float updateParticleVec = 0.0f;

    protected EnumParticleTypes onHitParticle = null;

    protected float onHitParticleVec = 0.0f;

    protected Entity targetingCenter;

    protected float range = 0;
    /**
     * ■コンストラクタ
     *
     * @param worldIn
     */
    public EntityBeam(World worldIn) {
        super(worldIn);

        this.noClip = true;

        //■生存タイマーリセット
        ticksExisted = 0;

        setSize(0.5F, 0.5F);
    }

    public EntityBeam(World worldIn, EntityLivingBase entityLiving, float AttackLevel, float roll) {
        this(worldIn, entityLiving, AttackLevel);
        this.setRoll(roll);
    }

    public EntityBeam(World worldIn, EntityLivingBase entityLiving, float AttackLevel) {
        this(worldIn);

        this.AttackLevel = AttackLevel;

        //■撃った人
        setThrower(entityLiving);

        blade = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
        if (!blade.isEmpty() && !(blade.getItem() instanceof ItemSlashBlade)) {
            blade = ItemStack.EMPTY;
        }

        //■撃った人と、撃った人が（に）乗ってるEntityも除外
        alreadyHitEntity.clear();
        alreadyHitEntity.add(thrower);
        alreadyHitEntity.add(thrower.getRidingEntity());
        alreadyHitEntity.addAll(thrower.getPassengers());


        {
            float dist = 2.0f;

            double ran = (rand.nextFloat() - 0.5) * 2.0;

            double yaw = Math.toRadians(-thrower.rotationYaw + 90);

            double x = ran * Math.sin(yaw);
            double y = 1.0 - Math.abs(ran);
            double z = ran * Math.cos(yaw);

            x *= dist;
            y *= dist;
            z *= dist;


//            //■初期位置・初期角度等の設定
//            setLocationAndAngles(thrower.posX + x,
//                    thrower.posY + y,
//                    thrower.posZ + z,
//                   thrower.rotationYaw,
//                    thrower.rotationPitch
//            );
//
//            iniYaw = thrower.rotationYaw;
//
//            iniPitch = thrower.rotationPitch;
//
//            setDriveVector(1.75f);
            //■初期位置・初期角度等の設定
            setLocationAndAngles(thrower.posX + x,
                    thrower.posY + y,
                    thrower.posZ + z,
                    thrower.rotationYaw,
                    thrower.rotationPitch
            );

            iniYaw = thrower.rotationYaw;

            iniPitch = thrower.rotationPitch;

            setDriveVector(1.75f);
        }
    }

    public EntityBeam(World worldIn, EntityLivingBase entityLiving, float AttackLevel, float iniyaw, float inipitch) {
        this(worldIn);

        this.AttackLevel = AttackLevel;

        //■撃った人
        setThrower(entityLiving);

        blade = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
        if (!blade.isEmpty() && !(blade.getItem() instanceof ItemSlashBlade)) {
            blade = ItemStack.EMPTY;
        }

        //■撃った人と、撃った人が（に）乗ってるEntityも除外
        alreadyHitEntity.clear();
        alreadyHitEntity.add(thrower);
        alreadyHitEntity.add(thrower.getRidingEntity());
        alreadyHitEntity.addAll(thrower.getPassengers());


        {
            float dist = 2.0f;

            double ran = (rand.nextFloat() - 0.5) * 2.0;

            double yaw = Math.toRadians(-thrower.rotationYaw + 90);

            double x = ran * Math.sin(yaw);
            double y = 1.0 - Math.abs(ran);
            double z = ran * Math.cos(yaw);

            x *= dist;
            y *= dist;
            z *= dist;


//            //■初期位置・初期角度等の設定
//            setLocationAndAngles(thrower.posX + x,
//                    thrower.posY + y,
//                    thrower.posZ + z,
//                   thrower.rotationYaw,
//                    thrower.rotationPitch
//            );
//
//            iniYaw = thrower.rotationYaw;
//
//            iniPitch = thrower.rotationPitch;
//
//            setDriveVector(1.75f);
            //■初期位置・初期角度等の設定
            setLocationAndAngles(thrower.posX + x,
                    thrower.posY + y,
                    thrower.posZ + z,
                    iniyaw,
                    inipitch
            );

            iniYaw = iniyaw;

            iniPitch = inipitch;

            setDriveVector(1.75f);
        }
    }

    private static final DataParameter<Integer> THROWER_ENTITY_ID = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Float> ROLL = EntityDataManager.<Float>createKey(EntityBeam.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TARGET_ENTITY_ID = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> INTERVAL = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntityBeam.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> IS_OVER_WALL = EntityDataManager.<Boolean>createKey(EntityBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> Burst = EntityDataManager.<Boolean>createKey(EntityBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> NonPlayer = EntityDataManager.<Boolean>createKey(EntityBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> ExpRadius = EntityDataManager.<Float>createKey(EntityBeam.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> TrueDamage = EntityDataManager.<Boolean>createKey(EntityBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_MULTI_HIT = EntityDataManager.<Boolean>createKey(EntityBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGETING_CENTER_ENTITY_ID = EntityDataManager.<Integer>createKey(EntityBeam.class, DataSerializers.VARINT);


    /**
     * ■イニシャライズ
     */
    @Override
    protected void entityInit() {

        //EntityId
        this.getDataManager().register(THROWER_ENTITY_ID, 0);

        //lifetime
        this.getDataManager().register(LIFETIME, 20);

        //Roll
        this.getDataManager().register(ROLL, 0.0f);

        //EntityId
        this.getDataManager().register(TARGET_ENTITY_ID, 0);

        this.getDataManager().register(TARGETING_CENTER_ENTITY_ID,0);

        //interval
        this.getDataManager().register(INTERVAL, 7);

        //color
        this.getDataManager().register(COLOR, 0x3333FF);

        this.getDataManager().register(SCALE, 1.0f);

        this.getDataManager().register(IS_OVER_WALL, false);

        this.getDataManager().register(Burst, false);

        this.getDataManager().register(ExpRadius, 1.0f);

        this.getDataManager().register(NonPlayer, false);

        this.getDataManager().register(TrueDamage, false);

        this.getDataManager().register(IS_MULTI_HIT, false);
    }

    /**
     * 多段Hitの有無
     */
    public boolean isMultiHit(){
        return this.getDataManager().get(IS_MULTI_HIT);
    }
    /**
     * 多段Hitの有無
     */
    public void setMultiHit(boolean isMultiHit){
        this.getDataManager().set(IS_MULTI_HIT,isMultiHit);
    }

    public void setRainbow(boolean rainbow, float colorStep, int colorTotalStep, float colorStepScale) {
        this.Rainbow = rainbow;
        this.colorStep = colorStep;
        this.colorTotalStep = colorTotalStep;
        this.colorStepScale = colorStepScale;
    }

    public void setTrueDamage(boolean trueDamage, float trueDamageLevel) {
        this.getDataManager().set(TrueDamage, trueDamage);
        this.TrueDamageLevel = trueDamageLevel;
    }

    public boolean getTrueDamage() {
        return this.getDataManager().get(TrueDamage);
    }

    public boolean getBurst() {
        return this.getDataManager().get(Burst);
    }

    public void setBurst(boolean value) {
        this.getDataManager().set(Burst, value);
    }

    public boolean getIsNonPlayer() {
        return this.getDataManager().get(NonPlayer);
    }

    public void setIsNonPlayer(boolean value) {
        this.getDataManager().set(NonPlayer, value);
    }

    public float getExpRadius() {
        return this.getDataManager().get(ExpRadius);
    }

    public void setExpRadius(float value) {
        this.getDataManager().set(ExpRadius, value);
    }

    public int getThrowerEntityId() {
        return this.getDataManager().get(THROWER_ENTITY_ID);
    }

    public void setThrowerEntityId(int entityid) {
        this.getDataManager().set(THROWER_ENTITY_ID, entityid);
    }

    public int getTargetEntityId() {
        return this.getDataManager().get(TARGET_ENTITY_ID);
    }

    public void setTargetEntityId(int entityid) {
        this.getDataManager().set(TARGET_ENTITY_ID, entityid);
    }

    public float getRoll() {
        return this.getDataManager().get(ROLL);
    }

    public void setRoll(float roll) {
        this.getDataManager().set(ROLL, roll);
    }

    public int getLifeTime() {
        return this.getDataManager().get(LIFETIME);
    }

    public void setLifeTime(int lifetime) {
        this.getDataManager().set(LIFETIME, lifetime);
    }

    public int getInterval() {
        return this.getDataManager().get(INTERVAL);
    }

    public void setInterval(int value) {
        this.getDataManager().set(INTERVAL, value);
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    public final float getScale() {
        return getDataManager().get(SCALE);
    }

    public final void setScale(float scale) {
        this.getDataManager().set(SCALE, scale);
    }

    public final void setSound(SoundEvent sound, float volume, float rate) {
        this.sound = sound;
        this.volume = volume;
        this.rate = rate;
    }

    public boolean isOverWall() {
        return this.getDataManager().get(IS_OVER_WALL);
    }

    public void setIsOverWall(boolean isOverWall) {
        this.getDataManager().set(IS_OVER_WALL, isOverWall);
    }

    public EnumParticleTypes getUpdateParticle() {
        return updateParticle;
    }

    public void setUpdateParticle(EnumParticleTypes updateParticle ,float vec) {
        this.updateParticle = updateParticle;
        this.updateParticleVec = vec;
    }

    public EnumParticleTypes getOnHitParticle() {
        return onHitParticle;
    }

    public void setOnHitParticle(EnumParticleTypes onHitParticle,float vec) {
        this.onHitParticle = onHitParticle;
        this.onHitParticleVec = vec;
    }


    public int getTargetingCenterID() {
        return this.getDataManager().get(TARGETING_CENTER_ENTITY_ID);
    }

    public void setTargetingCenter(int targetingCenter) {
        this.getDataManager().set(TARGETING_CENTER_ENTITY_ID,targetingCenter);
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }


    float speed = 0.0f;
    float iniYaw = Float.NaN;
    float iniPitch = Float.NaN;


    public boolean doTargeting() {
        //boolean result = super.doTargeting();

        int targetid = getTargetEntityId();
        Entity potentialCenter = world.getEntityByID(getTargetingCenterID());

        if (potentialCenter != null) {
            targetingCenter = potentialCenter;
        } else {
            targetingCenter = this;
        }
        world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,targetingCenter.posX,targetingCenter.posY+3,targetingCenter.posZ,0,0,0,10);
        if (targetid == 0) { // 如果当前没有目标实体（targetid == 0）
            List<Entity> list = Collections.emptyList();
            // 在当前实体周围 expandFactor（30）范围内查找所有实体，并排除自身
            if (this.targetingCenter!=null){
                list = TargetUtils.findAllHostileEntitiesE(this.targetingCenter,range,thrower,true);
            }

            // 如果实体不能多次命中（isMultiHit返回false），则移除已命中的实体
            if (!isMultiHit()) {
                list.removeAll(alreadyHitEntity); // 排除已命中的实体
            }

            // 初始化临时的最小距离为15，用于存储找到的最近的目标
            double tmpDistance = range;

            // 初始化一个指向的实体变量，用于存储找到的目标实体
            Entity pointedEntity = null;

            for (Entity entity : list) {
                // 排除无效实体（null或无法碰撞的实体）
                if (entity == null || !entity.canBeCollidedWith())
                    continue;

                // 如果实体不符合攻击条件（不满足EntitySelectorAttackable的要求），跳过
                if (!EntitySelectorAttackable.getInstance().apply(entity))
                    continue;

                // 计算当前实体与发射者之间的距离
                double d3 = this.getDistance(entity);

                // 如果当前实体距离大于之前找到的最小距离，则更新最近目标
                if (d3 < tmpDistance || tmpDistance == 0.0D) {
                    pointedEntity = entity;
                    tmpDistance = d3; // 记录距离
                }

            }

            // 如果找到了符合条件的目标实体，则将其EntityId设置为目标实体ID
            if (pointedEntity != null)
                this.setTargetEntityId(pointedEntity.getEntityId());
        }


        if(targetid != 0 && getInterval() < this.ticksExisted ){
            Entity target = world.getEntityByID(targetid);
//            System.out.println(target);

            if(target != null){

                if(Float.isNaN(iniPitch) && thrower != null){
                    iniYaw = thrower.rotationYaw;
                    iniPitch = thrower.rotationPitch;
                }

                float lastYaw = iniYaw;
                float lastPitch = iniPitch;

                faceEntity(this,target, 10.0f,  10.0f);

                float lastSpeed = (float)(new Vec3d(this.motionX,this.motionY,this.motionZ)).lengthVector();

                float speedFactor = Math.abs(iniYaw - lastYaw) / 10f + Math.abs(iniPitch - lastPitch)/10f;
                speedFactor = 1.0f - Math.min(speedFactor, 0.75f);
                speedFactor = ((0.75f * speedFactor) + lastSpeed * 9f) / 10.0f;

                setDriveVector(speedFactor, false);
            }else if (target == null || !target.isEntityAlive()){
                setTargetEntityId(0);
            }
        }

        return true;
    }

    public static Vec3d getPosition(Entity owner) {
        return new Vec3d(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ);
    }

    public static Vec3d getLook(Entity owner, float rotMax) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (rotMax == 1.0F) {
            f1 = MathHelper.cos(-owner.rotationYaw * 0.017453292F - (float) Math.PI);
            f2 = MathHelper.sin(-owner.rotationYaw * 0.017453292F - (float) Math.PI);
            f3 = -MathHelper.cos(-owner.rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-owner.rotationPitch * 0.017453292F);
            return new Vec3d((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = owner.prevRotationPitch + (owner.rotationPitch - owner.prevRotationPitch) * rotMax;
            f2 = owner.prevRotationYaw + (owner.rotationYaw - owner.prevRotationYaw) * rotMax;
            f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            return new Vec3d((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public void faceEntity(Entity viewer, Entity target, float yawStep, float pitchStep) {
        double d0 = target.posX - viewer.posX;
        double d1 = target.posZ - viewer.posZ;
        double d2;

        if (target instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) target;
            d2 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() - (viewer.posY + (double) viewer.getEyeHeight());
        } else {
            AxisAlignedBB boundingBox = target.getEntityBoundingBox();
            d2 = (boundingBox.minY + boundingBox.maxY) / 2.0D - (viewer.posY + (double) viewer.getEyeHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));


        iniPitch = this.updateRotation(iniPitch, f3, pitchStep);
        iniYaw = this.updateRotation(iniYaw, f2, yawStep);



        /**/

    }

    private float updateRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.wrapDegrees(par2 - par1);

        if (f3 > par3) {
            f3 = par3;
        }

        if (f3 < -par3) {
            f3 = -par3;
        }

        return par1 + f3;
    }

    public void setDriveVector(float fYVecOfset) {
        setDriveVector(fYVecOfset, true);
    }

    /**
     * ■初期ベクトルとかを決めてる。
     * ■移動速度設定
     *
     * @param fYVecOfst
     */
    public void setDriveVector(float fYVecOfst, boolean init) {
//        if(Float.isNaN(iniYaw))
//            iniYaw = getIniYaw();
//        if(Float.isNaN(iniPitch))
//            iniPitch = getIniPitch();

        //■角度 -> ラジアン 変換
        float fYawDtoR = (iniYaw / 180F) * (float) Math.PI;
        float fPitDtoR = (iniPitch / 180F) * (float) Math.PI;

        //■単位ベクトル
        motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
        motionY = -MathHelper.sin(fPitDtoR) * fYVecOfst;
        motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;

        float f3 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
        rotationPitch = (float) ((Math.atan2(motionY, f3) * 180D) / Math.PI);
        if (init) {
            speed = fYVecOfst;
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }
    }

    public void setInitialPosition(double x, double y, double z,
                                   float yaw, float pitch, float roll, float speed) {
        this.prevPosX = this.lastTickPosX = x;
        this.prevPosY = this.lastTickPosY = y;
        this.prevPosZ = this.lastTickPosZ = z;

        this.prevRotationYaw = this.rotationYaw = MathHelper.wrapDegrees(-yaw);
        this.prevRotationPitch = this.rotationPitch = MathHelper.wrapDegrees(-pitch);
        setRoll(roll);
        setPosition(x, y, z);

        iniYaw = yaw;
        iniPitch = pitch;
        setDriveVector(speed);

    }

    /**
     * 向き初期化
     */
    protected void initRotation() {

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }
    }


    public void doRotation() {

        if (doTargeting()) return;

        float f2;
        f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }
    }

    public void normalizeRotation() {

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
    }

    public void spawnParticle() {
        if (this.isInWater()) {
            float trailLength;
            for (int l = 0; l < 4; ++l) {
                trailLength = 0.25F;
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE
                        , this.posX - this.motionX * (double) trailLength
                        , this.posY - this.motionY * (double) trailLength
                        , this.posZ - this.motionZ * (double) trailLength
                        , this.motionX, this.motionY, this.motionZ);
            }
        }
    }

    public void calculateSpeed() {
//        float speedReductionFactor = 1.10F;
//
//        if (this.isInWater())
//            speedReductionFactor = 1.0F;
//
//        this.motionX *= (double) speedReductionFactor;
//        this.motionY *= (double) speedReductionFactor;
//        this.motionZ *= (double) speedReductionFactor;
        //this.motionY -= (double)fallingFactor;

    }

    //■毎回呼ばれる。移動処理とか当り判定とかもろもろ。
    @Override
    public void onUpdate() {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        super.onUpdate();
        if (Rainbow) {
            setColor(ColorUtils.getSmoothTransitionColor(ticksExisted * colorStepScale + colorStep, colorTotalStep, true));
        }
        if (!world.isRemote){
            playParticle();
        }

        initRotation();
        doRotation();
        if (sound != null && this.ticksExisted == getInterval() + 1) {
            this.playSound(sound, volume, rate);
        }
        if (getInterval() < this.ticksExisted){
            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (!world.isRemote)
                detectCollision(getScale());
        }
        normalizeRotation();
        spawnParticle();

        if (this.ticksExisted >= getLifeTime()) {
            this.setDead();
        }
    }

    private void detectCollision(float scale)
    {
        double AMBIT = OriginAMBIT*scale;
        AxisAlignedBB bb = new AxisAlignedBB(this.posX - AMBIT,
                this.posY - AMBIT,
                this.posZ - AMBIT,
                this.posX + AMBIT,
                this.posY + AMBIT,
                this.posZ + AMBIT);
        // ----- 射撃物の迎撃
        double beamSize = 0.2*scale;
        AxisAlignedBB bb2 = new AxisAlignedBB(this.posX - beamSize,
                this.posY - beamSize,
                this.posZ - beamSize,
                this.posX + beamSize,
                this.posY + 256,
                this.posZ + beamSize);

        // ----- 敵エンティティへの攻撃
        final boolean isMultiHit = isMultiHit();
        if (!isMultiHit || this.ticksExisted % 2 == 0) {

            List<Entity> list = world.getEntitiesInAABBexcluding(thrower, bb, EntitySelectorAttackable.getInstance());
            list.addAll(world.getEntitiesInAABBexcluding(thrower, bb2, EntitySelectorAttackable.getInstance()));
//            list.removeAll(alreadyHitEntity);
//            if (!isMultiHit)
//                alreadyHitEntity.addAll(list);

            float damage = Math.max(1.0f, AttackLevel);
            StylishRankManager.setNextAttackType(
                    thrower,
                    isMultiHit ?
                            StylishRankManager.AttackTypes.QuickDrive :
                            StylishRankManager.AttackTypes.Drive);
//            StylishRankManager.doAttack(thrower_);
            for (Entity target : list) {
                onImpact(target, damage);
            }
        }

        // ----- 地面等の障害物との衝突
        if (!world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !isOverWall())
            // 消滅
            setDead();
    }

    protected boolean onImpact(Entity target, float damage)
    {
        return onImpact(target, damage, "directMagic");
    }

    /**
     * 他エンティティに攻撃(?)が通った後の処理.
     *
     * @param target 標的
     * @param damage ダメージ
     * @param source 攻撃方法
     * @return true=刀を持って生体を攻撃した場合
     */
    protected boolean onImpact(Entity target, float damage, String source)
    {
        DamageSource ds = new EntityDamageSource(source, thrower)
                .setDamageBypassesArmor()
                .setMagicDamage();

        return onImpact(target, damage, ds);
    }

    /**
     * 他エンティティに攻撃(?)が通った後の処理.
     *
     * @param target 標的
     * @param damage ダメージ
     * @param ds 攻撃方法
     * @return true=刀を持って生体を攻撃した場合
     */
    protected boolean onImpact(Entity target, float damage, DamageSource ds)
    {
        target.hurtResistantTime = 0;
        target.attackEntityFrom(ds, damage);
        target.motionX=0;
        target.motionY=0;
        target.motionZ=0;
        if (onHitParticle!=null){
            ((WorldServer) this.world).spawnParticle(onHitParticle,
                    true,
                    this.posX,
                    this.posY + this.height / 2,
                    this.posZ,
                    (int) (20), 0, 0, 0, onHitParticleVec);
        }
        if (blade.isEmpty() || !(target instanceof EntityLivingBase))
            return false;
        if (!getIsNonPlayer()) {
            blade.getItem().hitEntity(blade,
                    (EntityLivingBase)target,
                    (EntityLivingBase) thrower);
        }
        if (!isMultiHit() && target.getEntityId() == getTargetEntityId()){
//            System.out.println("HIT");
            alreadyHitEntity.add(target);
            setTargetEntityId(0);
        }
        return true;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    /**
     * ■Random
     *
     * @return
     */
    public Random getRand() {
        return this.rand;
    }

    /**
     * ■Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     * Liquid = 流体
     */
    @Override
    public boolean isOffsetPositionInLiquid(double par1, double par3, double par5) {
        return false;
    }

    /**
     * ■Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void move(MoverType moverType, double x, double y, double z) {
        super.move(moverType, x, y, z);
    }


    /**
     * ■Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    @Override
    protected void dealFireDamage(int par1) {
    }

    /**
     * ■Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    /**
     * ■Checks if the current block the entity is within of the specified material type
     */
    @Override
    public boolean isInsideOfMaterial(Material par1Material) {
        return false;
    }

    /**
     * ■Whether or not the current entity is in lava
     */
    @Override
    public boolean isInLava() {
        return false;
    }

    /**
     * ■環境光による暗さの描画（？）
     * EntityXPOrbのぱくり
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        float f1 = 0.5F;

        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f1 * 15.0F * 16.0F);

        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * ■Gets how bright this entity is.
     * EntityPortalFXのぱくり
     */
    @Override
    public float getBrightness() {
        float f1 = super.getBrightness();
        float f2 = 0.9F;
        f2 = f2 * f2 * f2 * f2;
        return f1 * (1.0F - f2) + f2;
        //return super.getBrightness();
    }

    /**
     * ■NBTの読込
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    /**
     * ■NBTの書出
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    double hitX;
    double hitY;
    double hitZ;
    float hitYaw;
    float hitPitch;


    public Entity ridingEntity2 = null;

    public Entity getRidingEntity() {
        return this.ridingEntity2;
    }

    /**
     * ■Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity) {
        if (par1Entity != null) {
            this.hitYaw = this.rotationYaw - par1Entity.rotationYaw;
            this.hitPitch = this.rotationPitch - par1Entity.rotationPitch;
            this.hitX = this.lastTickPosX - par1Entity.posX;
            this.hitY = this.lastTickPosY - par1Entity.posY;
            this.hitZ = this.lastTickPosZ - par1Entity.posZ;
            this.ridingEntity2 = par1Entity;

            this.ticksExisted = 0;
        }
    }

    /**
     * ■Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    @Override
    public void setPortal(BlockPos p_181015_1_) {
    }

    /**
     * ■Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    /**
     * ■Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb() {
    }


    //IProjectile
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

    }

    //IThrowableEntity
    @Override
    public Entity getThrower() {
        if (this.thrower == null) {
            int id = getThrowerEntityId();
            if (id != 0) {
                this.thrower = this.getEntityWorld().getEntityByID(id);
            }
        }

        return this.thrower;
    }

    @Override
    public void setThrower(Entity entity) {
        if (entity != null)
            setThrowerEntityId(entity.getEntityId());
        this.thrower = entity;
    }

    public void playParticle() {
        if (updateParticle!=null) {
            ((WorldServer) this.world).spawnParticle(updateParticle,
                    true,
                    this.posX,
                    this.posY + this.height / 2,
                    this.posZ,
                    (int) (2), 0, 0, 0, updateParticleVec);
        }
    }
}