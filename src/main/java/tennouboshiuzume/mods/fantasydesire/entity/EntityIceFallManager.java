package tennouboshiuzume.mods.fantasydesire.entity;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EntityIceFallManager extends EntityTwinSlashManager{
    public EntityIceFallManager(World par1World) {
        super(par1World);
        ticksExisted = 0;
    }

    private float range = 0;

    public EntityIceFallManager(World par1World, EntityLivingBase entityLiving,float range)
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
        this.range = range;
    }

    @Override
    public void onUpdate(){
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
        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));

        List<EntityLivingBase> list1 = TargetUtils.findAllHostileEntities(this,range + 10f,thrower,true);
        for (EntityLivingBase target : list1){
            target.motionX=0;
            target.motionY=0;
            target.motionZ=0;
            if (this.ticksExisted%20==0&&!world.isRemote){
                ParticleUtils.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, true, target.posX, target.posY + target.height / 2, target.posZ, 20, 0, 0, 0, 0.2f);
            }
            if (this.ticksExisted % 20 == 0) {
                target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 0.5f);
            }
        }

        if (ticksExisted % 2==0 && this.getThrower() != null){
            List<EntityLivingBase> targetList = TargetUtils.findAllHostileEntities(this,range,thrower,true);
            Collections.shuffle(targetList);
            if (!targetList.isEmpty() && !world.isRemote){
                for (int i = 0 ;i < 5;i++){
                    EntityLivingBase target = TargetUtils.setTargetEntityFromListByEntity(i,targetList);
                    Random random = player.getRNG();
                    float yaw = (float) random.nextInt(360);
                    float pitch = 90f+(float)(random.nextGaussian() * 10f);
                    float roll = (float) (random.nextInt(360) - 180);
                    Vec3d basePos = new Vec3d(0, 0, 1);
                    Vec3d spawnPos = new Vec3d(target.posX, target.posY + target.height/2, target.posZ)
                            .add(basePos
                                    .rotatePitch((float) Math.toRadians(pitch))
                                    .rotateYaw((float) Math.toRadians(yaw))
                                    .scale( 20f +  random.nextGaussian()*3));
                    EntitySoulPhantomSword entityDrive = new EntitySoulPhantomSword(world, player, magicDamage);
                    entityDrive.setInitialPosition(
                            spawnPos.x,
                            spawnPos.y,
                            spawnPos.z,
                            -yaw,
                            -pitch-180f,
                            roll,
                            1.75f);
                    entityDrive.setInterval(0);
                    entityDrive.setColor(freezeColor[random.nextInt(4)]);
                    entityDrive.setScale(3f);
                    entityDrive.setSound(SoundEvents.BLOCK_GLASS_BREAK,1,0.5f);
                    entityDrive.setTargetEntityId(target.getEntityId());
                    entityDrive.setParticle(EnumParticleTypes.SNOW_SHOVEL);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setLifeTime(30);
                    world.spawnEntity(entityDrive);
                }
            }
        }

        if (ticksExisted>20 * 10){
            this.setDead();
        }
    }
    private final int[] freezeColor = new int[]{
            0xAAFFFF,
            0x00AAFF,
            0x00FFFF,
            0x0000FF
    };
}
