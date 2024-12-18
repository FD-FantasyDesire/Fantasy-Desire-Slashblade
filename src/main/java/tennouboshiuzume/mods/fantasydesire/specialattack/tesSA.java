package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntitySummonedSwordBase;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityBeam;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 15/06/21.
 */
public class tesSA extends SpecialAttackBase {
    @Override
    public String toString() {
        return "tesSA";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){
            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(player, 60f, false);
            Collections.shuffle(target);
            if(!target.isEmpty()){
                for (int i = 0;i<1 ; i++){
                    EntityLivingBase CTarget = target.get(i % target.size());
                    EntityBeam entityBeam = new EntityBeam(world,player,1);
                    entityBeam.setInitialPosition(CTarget.posX,CTarget.posY,CTarget.posZ, CTarget.rotationYaw, CTarget.rotationPitch,0,0.3f);
                    entityBeam.setLifeTime(60 * 20);
                    entityBeam.setInterval(100);
                    entityBeam.setIsOverWall(true);
                    entityBeam.setMultiHit(true);
                    entityBeam.setTargetingCenter(player.getEntityId());
                    entityBeam.setRange(30f);
                    entityBeam.setColor(0xFFFFFF);
                    entityBeam.setOnHitParticle(EnumParticleTypes.EXPLOSION_LARGE,0);
                    entityBeam.setTargetEntityId(CTarget.getEntityId());
                    entityBeam.setUpdateParticle(EnumParticleTypes.END_ROD,0.3f);
                    world.spawnEntity(entityBeam);
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriorosi);
    }

    private Entity getEntityToWatch(EntityPlayer player){
        World world = player.world;
        Entity target = null;
        for(int dist = 2; dist < 20; dist+=2){
            AxisAlignedBB bb = player.getEntityBoundingBox();
            Vec3d vec = player.getLookVec();
            vec = vec.normalize();
            bb = bb.grow(2.0f, 0.25f, 2.0f);
            bb = bb.offset(vec.x*(float)dist,vec.y*(float)dist,vec.z*(float)dist);

            List<Entity> list = world.getEntitiesInAABBexcluding(player, bb, EntitySelectorAttackable.getInstance());
            float distance = 30.0f;
            for(Entity curEntity : list){
                float curDist = curEntity.getDistance(player);
                if(curDist < distance)
                {
                    target = curEntity;
                    distance = curDist;
                }
            }
            if(target != null)
                break;
        }
        return target;
    }
}