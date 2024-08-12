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
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 15/06/21.
 */
public class CrossSaberSA extends SpecialAttackBase {
    @Override
    public String toString() {
        return "CrossSaberSA";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        Random random = new Random();

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        if(!world.isRemote){

            ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

            Entity target = null;

            int entityId = ItemSlashBlade.TargetEntityId.get(tag);

            if(entityId != 0){
                Entity tmp = world.getEntityByID(entityId);
                if(tmp != null){
                    if(tmp.getDistance(player) < 30.0f)
                        target = tmp;
                }
            }

            if(target == null){
                target = getEntityToWatch(player);
            }

            if(target != null){
                final int cost = -40;
                if(!ItemSlashBlade.ProudSoul.tryAdd(tag,cost,false)){
                    ItemSlashBlade.damageItem(stack, 10, player);
                }


                StylishRankManager.setNextAttackType(player, StylishRankManager.AttackTypes.PhantomSword);
                blade.attackTargetEntity(stack, target, player, true);
                player.onCriticalHit(target);

                ReflectionAccessHelper.setVelocity(target, 0, 0, 0);
                //target.addVelocity(0.0, 0.55D, 0.0);

                if(target instanceof EntityLivingBase){
                    blade.setDaunting((EntityLivingBase)target);
                    ((EntityLivingBase) target).hurtTime = 0;
                    ((EntityLivingBase) target).hurtResistantTime = 0;
                }

                int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
                int rank = StylishRankManager.getStylishRank(player);
                if(5 <= rank){
                    magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
                }

                int count = 0;
                if(!world.isRemote){
                    for (int i=0;i<=10;i++){
                            count++;
                            EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, player, magicDamage,90.0f);
                            if (entityDrive != null) {
                                Vec3d spawnPostion = new Vec3d(target.posX+random.nextInt(21)-10,
                                        target.posY+15.0f+random.nextInt(11)-5,
                                        target.posZ+random.nextInt(21)-10);
                                entityDrive.setInitialPosition(
                                        spawnPostion.x,
                                        spawnPostion.y,
                                        spawnPostion.z,
                                        +36f*i,
                                        90f,
                                        0f,
                                        1.75f
                                );
                                entityDrive.setLifeTime(400);
                                entityDrive.setInterval(20+3*count);
//                                entityDrive.setDriveVector(2.5f);
                                entityDrive.setScale(0.75f);
                                entityDrive.setParticle(EnumParticleTypes.END_ROD);
                                entityDrive.setParticleVec(1);
                                int color = random.nextInt(16777216);
                                entityDrive.setColor(color);
                                entityDrive.setTargetEntityId(target.getEntityId());
                                world.spawnEntity(entityDrive);
                            }
                    }
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