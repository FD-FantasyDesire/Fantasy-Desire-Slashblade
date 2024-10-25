package tennouboshiuzume.mods.fantasydesire.entity;

import mods.flammpfeil.slashblade.ability.UntouchableTime;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorDestructable;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/08.
 */
public class EntityTwinSlashManager extends Entity implements IThrowableEntity {
    /**
     * ★撃った人
     */
    protected Entity thrower;

    protected ItemStack blade = ItemStack.EMPTY;

    /**
     * ★多段Hit防止用List
     */
    protected List<Entity> alreadyHitEntity = new ArrayList<Entity>();

    /**
     * ■コンストラクタ
     * @param par1World
     */
    public EntityTwinSlashManager(World par1World)
    {
        super(par1World);
        ticksExisted = 0;
    }


    private static final DataParameter<Integer> ThrowerEntityID = EntityDataManager.<Integer>createKey(EntityTwinSlashManager.class, DataSerializers.VARINT);

    @Override
    protected void entityInit() {
        //entityid
        this.getDataManager().register(ThrowerEntityID, 0);
    }



    int getThrowerEntityID(){
        return this.getDataManager().get(ThrowerEntityID);
    }

    void setThrowerEntityID(int id){
        this.getDataManager().set(ThrowerEntityID,id);
    }

    public EntityTwinSlashManager(World par1World, EntityLivingBase entityLiving)
    {
        this(par1World);

        //■撃った人
        thrower = entityLiving;

        setThrowerEntityID(thrower.getEntityId());

        blade = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
        if(!blade.isEmpty() && !(blade.getItem() instanceof ItemSlashBlade)){
            blade = ItemStack.EMPTY;
        }

        //■撃った人と、撃った人が（に）乗ってるEntityも除外
        alreadyHitEntity.clear();
        alreadyHitEntity.add(thrower);
        alreadyHitEntity.add(thrower.getRidingEntity());
        alreadyHitEntity.addAll(thrower.getPassengers());

        //■生存タイマーリセット
        ticksExisted = 0;

        //■サイズ変更
        setSize(64.0F, 32.0F);

        //■初期位置・初期角度等の設定
        setLocationAndAngles(thrower.posX,
                thrower.posY,
                thrower.posZ,
                thrower.rotationYaw,
                thrower.rotationPitch);
    }

    //■毎回呼ばれる。移動処理とか当り判定とかもろもろ。
    @Override
    public void onUpdate()
    {
        //super.onUpdate();


        if(this.thrower == null && this.getThrowerEntityID() != 0){
            this.thrower = this.world.getEntityByID(this.getThrowerEntityID());
        }

        if(this.blade.isEmpty() && this.getThrower() != null && this.getThrower() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)this.getThrower();
            ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
            if(stack.getItem() instanceof ItemSlashBlade)
                this.blade = stack;
        }
        ItemStack stack = this.blade;
        EntityPlayer player = (EntityPlayer) thrower;
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        if (thrower != null && !world.isRemote) {
            ParticleUtils.spawnParticle(world,EnumParticleTypes.FIREWORKS_SPARK,true,thrower.posX,thrower.posY+thrower.height/4,thrower.posZ,10,0,0,0,0);
        }
        if(this.ticksExisted == 1 && this.getThrower() != null) {

            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw,0);
                {
                    ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
                    int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
                    float baseModif = blade.getBaseAttackModifiers(tag);
                    float magicDamage = 1.0f + (baseModif/2.0f);
                    int rank = StylishRankManager.getStylishRank(player);
                    if(5 <= rank)
                        magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
                    int count = Math.min(Math.max(player.experienceLevel/10,1),5);
                    Vec3d BHpos = player.getLookVec().scale(11).addVector(player.posX,player.posY,player.posZ);

                    if(!world.isRemote){
                        {
                            EntityOverChargeBFG entityDrive = new EntityOverChargeBFG(world, player, magicDamage);
                            entityDrive.setIsBlackhole(true);
                            entityDrive.setScale(5f);
                            entityDrive.setHitScale(count);
                            entityDrive.setInitialPosition(BHpos.x, BHpos.y, BHpos.z, 0, 0, 0, 0);
                            entityDrive.setIsOverWall(true);
                            entityDrive.setMultiHit(true);
                            entityDrive.setColor(0x00C8FF);
                            entityDrive.setLifeTime(80);
                            world.spawnEntity(entityDrive);
                        }
                        {
                            EntityOverCharge entityDrive = new EntityOverCharge(world, player, magicDamage);
                            entityDrive.setScale(3f);
                            entityDrive.setInitialPosition(BHpos.x, BHpos.y, BHpos.z, 90, 90, 0, 0);
                            entityDrive.setIsOverWall(true);
                            entityDrive.setMultiHit(true);
                            entityDrive.setColor(0xFF0089);
                            entityDrive.setInterval(0);
                            entityDrive.setSound(SoundEvents.ENTITY_WITHER_BREAK_BLOCK,3,0.5f);
                            entityDrive.setLifeTime(80);
                            world.spawnEntity(entityDrive);
                        }
                    }
                }


                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw-18,0);
            }

            ReflectionAccessHelper.setVelocity(this.getThrower(), 0, 0, 0);
            double playerDist = 9;
            if (!player.onGround)
                playerDist *= 0.33f;
            ReflectionAccessHelper.setVelocity(player,
                    -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                    -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                    Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);
            int count = Math.min(Math.max(player.experienceLevel/10,1),5);

            if(!world.isRemote){
                for (int i = 0;i<count;i++){
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0xFF0089);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0x00C8FF);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
            UntouchableTime.setUntouchableTime(player, 20);
            ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
        }
        if(this.ticksExisted == 6 && this.getThrower() != null) {
            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw+180-36,0);
            }
            ReflectionAccessHelper.setVelocity(this.getThrower(), 0, 0, 0);
            double playerDist = 9;
            if (!player.onGround)
                playerDist *= 0.33f;
            ReflectionAccessHelper.setVelocity(player,
                    -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                    -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                    Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);
            int count = Math.min(Math.max(player.experienceLevel/10,1),5);

            if(!world.isRemote){
                for (int i = 0;i<count;i++){
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0xFF0089);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0x00C8FF);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
            UntouchableTime.setUntouchableTime(player, 20);
            ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.ReturnEdge);
        }
        if(this.ticksExisted == 11 && this.getThrower() != null) {
            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw+180-36,0);
            }
            ReflectionAccessHelper.setVelocity(this.getThrower(), 0, 0, 0);
            double playerDist = 9;
            if (!player.onGround)
                playerDist *= 0.33f;
            ReflectionAccessHelper.setVelocity(player,
                    -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                    -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                    Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);
            int count = Math.min(Math.max(player.experienceLevel/10,1),5);

            if(!world.isRemote){
                for (int i = 0;i<count;i++){
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0xFF0089);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0x00C8FF);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
            UntouchableTime.setUntouchableTime(player, 20);
            ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
        }
        if(this.ticksExisted == 16 && this.getThrower() != null) {
            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw+180-36,0);
            }
            ReflectionAccessHelper.setVelocity(this.getThrower(), 0, 0, 0);
            double playerDist = 9;
            if (!player.onGround)
                playerDist *= 0.33f;
            ReflectionAccessHelper.setVelocity(player,
                    -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                    -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                    Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);
            int count = Math.min(Math.max(player.experienceLevel/10,1),5);

            if(!world.isRemote){
                for (int i = 0;i<count;i++){
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0xFF0089);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0x00C8FF);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
            UntouchableTime.setUntouchableTime(player, 20);
            ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.ReturnEdge);
        }
        if(this.ticksExisted == 21 && this.getThrower() != null) {
            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw+180-36,0);
            }
            ReflectionAccessHelper.setVelocity(this.getThrower(), 0, 0, 0);
            double playerDist = 9;
            if (!player.onGround)
                playerDist *= 0.33f;
            ReflectionAccessHelper.setVelocity(player,
                    -Math.sin(Math.toRadians(player.rotationYaw)) * playerDist,
                    -Math.sin(Math.toRadians(MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f))) * playerDist,
                    Math.cos(Math.toRadians(player.rotationYaw)) * playerDist);

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
            float baseModif = blade.getBaseAttackModifiers(tag);
            float magicDamage = 1.0f + (baseModif/2.0f);
            int rank = StylishRankManager.getStylishRank(player);
            if(5 <= rank)
                magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
            magicDamage*=Math.max(rank,1);
            int count = Math.min(Math.max(player.experienceLevel/10,1),5);

            if(!world.isRemote){
                for (int i = 0;i<count;i++){
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), 45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0xFF0089);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,0.5f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                    {
                        EntityDriveEx entityDrive = new EntityDriveEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(player.posX, player.posY + player.eyeHeight, player.posZ, player.rotationYaw, MathHelper.clamp(player.rotationPitch, -30.0f, 30.0f), -45, 3f);
                        entityDrive.setScale(3f);
                        entityDrive.setInterval(i*2);
                        entityDrive.setLifeTime(100+i*2);
                        entityDrive.setColor(0x00C8FF);
                        entityDrive.setSound(SoundEvents.ENTITY_BLAZE_HURT,2f,2f);
                        entityDrive.setParticle(EnumParticleTypes.END_ROD);
                        world.spawnEntity(entityDrive);
                    }
                }
            }
            UntouchableTime.setUntouchableTime(player, 20);
            ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
        }

        if (ticksExisted>=35||thrower==null){
            if (player != null) {
                player.setPositionAndRotation(player.posX,player.posY,player.posZ,player.rotationYaw-180-18,0);
            }
            this.setDead();
        }
    }

    /**
     * ■Random
     * @return
     */
    public Random getRand()
    {
        return this.rand;
    }

    /**
     * ■Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     * Liquid = 流体
     */
    @Override
    public boolean isOffsetPositionInLiquid(double par1, double par3, double par5)
    {
        //AxisAlignedBB axisalignedbb = this.boundingBox.getOffsetBoundingBox(par1, par3, par5);
        //List list = this.world.getCollidingBoundingBoxes(this, axisalignedbb);
        //return !list.isEmpty() ? false : !this.world.isAnyLiquid(axisalignedbb);
        return false;
    }

    /**
     * ■Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void move(MoverType moverType, double par1, double par3, double par5) {}

    /**
     * ■Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    @Override
    protected void dealFireDamage(int par1) {}

    /**
     * ■Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    @Override
    public boolean handleWaterMovement()
    {
        return false;
    }

    /**
     * ■Checks if the current block the entity is within of the specified material type
     */
    @Override
    public boolean isInsideOfMaterial(Material par1Material)
    {
        return false;
    }

    /**
     * ■環境光による暗さの描画（？）
     *    EntityXPOrbのぱくり
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender()
    {
        float f1 = 0.5F;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * ■Gets how bright this entity is.
     *    EntityPortalFXのぱくり
     */
    @Override
    public float getBrightness()
    {
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
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    /**
     * ■NBTの書出
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    /**
     * ■Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {}

    /**
     * ■Called by portal blocks when an entity is within it.
     */
    @Override
    public void setPortal(BlockPos p_181015_1_) {
    }

    /**
     * ■Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    @Override
    public boolean isBurning()
    {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    /**
     * ■Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb() {}


    @Override
    public Entity getThrower() {
        return this.thrower;
    }

    @Override
    public void setThrower(Entity entity) {
        this.thrower = entity;
    }
}