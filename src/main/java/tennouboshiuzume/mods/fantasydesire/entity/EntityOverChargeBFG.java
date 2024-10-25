package tennouboshiuzume.mods.fantasydesire.entity;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import tennouboshiuzume.mods.fantasydesire.specialattack.FreezeZero;
import tennouboshiuzume.mods.fantasydesire.util.ColorUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;

import java.util.List;
import java.util.Random;

public class EntityOverChargeBFG extends EntityOverCharge {

    private static final DataParameter<Boolean> IS_BLAST = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Float> EXP_RADIUS = EntityDataManager.<Float>createKey(EntityOverChargeBFG.class, DataSerializers.FLOAT);

    private static final DataParameter<Boolean> IS_BFG = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> IS_BLACKHOLE = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> IS_COLD = EntityDataManager.<Boolean>createKey(EntityOverChargeBFG.class, DataSerializers.BOOLEAN);

    @Override
    protected void entityInit() {
        super.entityInit();
        EntityDataManager manager = getDataManager();
        manager.register(IS_BLAST, false);
        manager.register(EXP_RADIUS, 5f);
        manager.register(IS_BFG, false);
        manager.register(IS_BLACKHOLE, false);
        manager.register(IS_COLD, false);
    }

    public boolean getIsBFG() {
        return this.getDataManager().get(IS_BFG);
    }

    public void setBFG(boolean isBFG) {
        this.getDataManager().set(IS_BFG, isBFG);
    }

    public boolean getIsBlackHole() {
        return this.getDataManager().get(IS_BLACKHOLE);
    }

    public void setIsBlackhole(boolean isBlackhole) {
        this.getDataManager().set(IS_BLACKHOLE, isBlackhole);
    }

    public boolean getIsCold() {
        return this.getDataManager().get(IS_COLD);
    }

    public void setIsCold(boolean isCold) {
        this.getDataManager().set(IS_COLD, isCold);
    }

    public boolean getIsBlast() {
        return this.getDataManager().get(IS_BLAST);
    }

    public void setIsBlast(boolean isOverWall) {
        this.getDataManager().set(IS_BLAST, isOverWall);
    }

    public final float getExpRadius() {
        return getDataManager().get(EXP_RADIUS);
    }

    public final void setExpRadius(float hitscale) {
        this.getDataManager().set(EXP_RADIUS, hitscale);
    }

    public EntityOverChargeBFG(World worldIn) {
        super(worldIn);
    }

    public EntityOverChargeBFG(World worldIn, EntityLivingBase entityLiving, float AttackLevel) {
        super(worldIn, entityLiving, AttackLevel);
    }

    private static final double OriginAMBIT = 1.5;

    @Override
    public void onUpdate() {
        if (Rainbow){
            setColor(ColorUtils.getSmoothTransitionColor(ticksExisted*colorStepScale + colorStep, colorTotalStep,true));
        }
        if (!world.isRemote)
            detectCollision(getScale(), getHitScale());
        if (getInterval() < this.ticksExisted)
            move();
        playParticle();
        if (sound != null && this.ticksExisted == getInterval() + 1) {
            this.playSound(sound, volume, rate);
        }

        if (getIsCold()) {
            FreezeZero(Math.max(1.0f, attackLevel_));
        }

        if (this.ticksExisted >= getLifeTime()) {
            if (getIsBlast()) {
                Blast();
            }
            setDead();
        }
    }

    private void detectCollision(float scale, float hitScale) {
        double AMBIT = OriginAMBIT * scale * hitScale;
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
                    thrower_,
                    isMultiHit ?
                            StylishRankManager.AttackTypes.QuickDrive :
                            StylishRankManager.AttackTypes.Drive);

            for (Entity target : list) {
                if (!getIsBFG() && !getIsBlackHole() && !getIsCold()) {
                    onImpact(target, damage);
                    ((WorldServer) this.world).spawnParticle(EnumParticleTypes.TOTEM,
                            true,
                            this.posX,
                            this.posY + this.height / 2,
                            this.posZ,
                            (int) (20), 0, 0, 0, 0.5);

                }
                if (getIsBFG() && this.ticksExisted % 4 == 0) {
                    this.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 3, 2f);
                    BFG(blade_,world,thrower_,this,target,damage/10);
                }
                if (this.ticksExisted % 10 == 0 && getIsBlackHole()) {
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 3, 0.5f);
                    pullEntityTowards(target, this.posX, this.posY, this.posZ, 1);
                }
            }
        }
        if (getIsCold()) {
            List<Entity> list = world.getEntitiesInAABBexcluding(thrower_, bb, EntitySelectorAttackable.getInstance());
            list.removeAll(alreadyHitEntity_);
            if (!isMultiHit)
                alreadyHitEntity_.addAll(list);
            for (Entity target : list) {
                target.motionX = 0;
                target.motionY = 0;
                target.motionZ = 0;
                target.setPositionAndRotationDirect(target.lastTickPosX,target.lastTickPosY,target.lastTickPosZ,target.prevRotationYaw,target.prevRotationPitch,0,true);
                target.extinguish();
                if (this.ticksExisted%20==0&&!world.isRemote){
                    ParticleUtils.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, true, target.posX, target.posY + target.height / 2, target.posZ, 20, 0, 0, 0, 0.2f);
                }
                if (this.ticksExisted % 20 == 0) {
                    this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 0.5f);
                    target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 0.5f);
                }
            }
        }

        // ----- 地面等の障害物との衝突
        if (!world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !isOverWall()) {
            if (getIsBlast()) {
                Blast();
            }
            setDead();
        }
    }

    private void move() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;

        this.motionX *= 1.05f;
        this.motionY *= 1.05f;
        this.motionZ *= 1.05f;

        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

//    private void BFG(Entity target, float magicDamage) {
//        Random random = thrower_.getRNG();
//        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, thrower_, magicDamage);
//        entityDrive.setInitialPosition(this.posX, this.posY, this.posZ, random.nextInt(360), random.nextInt(360), this.getRoll(), 1.5f);
//        entityDrive.setColor(0x99FF00);
//        entityDrive.setTargetEntityId(target.getEntityId());
//        entityDrive.setLifeTime(100);
//        entityDrive.setInterval(40);
//        world.spawnEntity(entityDrive);
//    }

    private void BFG(ItemStack blade, World world, EntityLivingBase player, Entity target, Entity target2, float damage){
        Random random = player.getRNG();

        double xA = target.posX;
        double yA = target.posY-target.height;
        double zA = target.posZ;

        double xB = target2.posX;
        double yB = target2.posY;
        double zB = target2.posZ;

        double deltaX = xB - xA;
        double deltaY = yB - yA;
        double deltaZ = zB - zA;

        // 计算水平距离
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // 计算 yaw 和 pitch
        float yaw = (float)(Math.atan2(deltaX, deltaZ) * (180 / Math.PI));
        float pitch = (float)(-(Math.atan2(deltaY, horizontalDistance) * (180 / Math.PI)));

        if (!world.isRemote){
            float roll = (float) (random.nextInt(360)-180);
            Vec3d basePos = new Vec3d(0,0,1);
            Vec3d spawnPos = new Vec3d(xA,yA,zA)
                    .add(basePos
                            .rotatePitch((float) Math.toRadians(pitch))
                            .rotateYaw((float) Math.toRadians(yaw))
                            .scale(1f));
            EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, damage);
            entityDrive.setInitialPosition(
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    -yaw,
                    pitch,
                    roll,
                    2.5f
            );
            entityDrive.setIsOverWall(true);
            entityDrive.setInterval(0);
            entityDrive.setScale(0.75f);
            entityDrive.setColor(this.getColor());
            entityDrive.setTargetEntityId(target2.getEntityId());
//            entityDrive.setIsNonPlayer(true);
            entityDrive.setLifeTime(30);
            world.spawnEntity(entityDrive);
        }
    }

    private void Blast() {
        world.createExplosion(thrower_, this.posX, this.posY + this.height / 2f, this.posZ, getExpRadius(), false);
        ((WorldServer) this.world).spawnParticle(EnumParticleTypes.END_ROD,
                true,
                this.posX,
                this.posY + this.height / 2,
                this.posZ,
                (int) (20 * getExpRadius()), 0, 0, 0, 0.05 * getExpRadius());
    }

    private void pullEntityTowards(Entity entity, double targetX, double targetY, double targetZ, double strength) {

        double dx = targetX - entity.posX;
        double dy = targetY - entity.posY;
        double dz = targetZ - entity.posZ;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0) {
            double pullFactor = strength / distance;
            entity.motionX += dx * pullFactor;
            entity.motionY += dy * pullFactor;
            entity.motionZ += dz * pullFactor;
            entity.velocityChanged = true;
        }
    }

    private void FreezeZero(float magicDamage) {
        setColor(freezeColor[this.ticksExisted % freezeColor.length]);
        if (this.ticksExisted % 4 == 0) {
            this.playSound(SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, 1f, 0.8f);
            if (!thrower_.world.isRemote) {
                Random random = thrower_.getRNG();
                int rotYaw = random.nextInt(360);
                for (int i = 0; i < 4; i++) {
                    float yaw = (360f / 4 * i + rotYaw);
                    float pitch = (float) (random.nextGaussian() * 30f);
                    float roll = (float) (random.nextInt(360)-180);
                    Vec3d basePos = new Vec3d(0,0,1);
                    Vec3d spawnPos = new Vec3d(this.posX,this.posY,this.posZ)
                            .add(basePos
                                    .rotatePitch((float) Math.toRadians(pitch))
                                    .rotateYaw((float) Math.toRadians(yaw))
                                    .scale(this.getScale()));
                    EntityDriveEx entityDrive = new EntityDriveEx(world, thrower_, magicDamage);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setInitialPosition(spawnPos.x, spawnPos.y, spawnPos.z, -yaw, -pitch, roll , 0.75f);
                    entityDrive.setInterval(1);
                    entityDrive.setParticle(EnumParticleTypes.SNOW_SHOVEL);
                    entityDrive.setColor(freezeColor[i % freezeColor.length]);
                    entityDrive.setScale(this.getScale());
                    entityDrive.setLifeTime(20);
                    world.spawnEntity(entityDrive);
                }
            }
        }
    }

    private final int[] freezeColor = new int[]{
            0xAAFFFF,
            0x00AAFF,
            0x00FFFF,
            0x0000FF
    };
}
