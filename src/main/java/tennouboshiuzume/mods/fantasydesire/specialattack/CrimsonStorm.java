package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.IJustSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.ISuperSpecialAttack;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityOverCharge;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by Furia on 14/05/27.
 */
public class CrimsonStorm extends SpecialAttackBase implements IJustSpecialAttack, ISuperSpecialAttack {
    @Override
    public String toString() {
        return "CrimsonStorm";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif/2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        float scale = Math.min(Math.max(player.experienceLevel/10,3),6);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));

        if(!world.isRemote){
            EntityOverCharge entityDrive = new EntityOverCharge(world,player,magicDamage);
            entityDrive.setColor(0xFF0000);
            entityDrive.setScale(scale);
            entityDrive.setInterval(40);
            entityDrive.setLifeTime(40);
            entityDrive.setIsOverWall(true);
            entityDrive.setInitialPosition(player.posX,player.posY+player.height/2,player.posZ,player.rotationYaw,player.rotationPitch,0,0f);
            entityDrive.setMultiHit(true);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
    }

    @Override
    public void doJustSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);


        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif/2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        float scale = Math.min(Math.max(player.experienceLevel/10,3),8f)*1.2f;
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
        Entity target = getEntityToWatch(player);

        if (target==null){
            target = player;
        }

        if(!world.isRemote){
            EntityOverCharge entityDrive = new EntityOverCharge(world,player,magicDamage);
            entityDrive.setColor(0xFF0000);
            entityDrive.setScale(scale);
            entityDrive.setInterval(60);
            entityDrive.setLifeTime(60);
            entityDrive.setIsOverWall(true);
            entityDrive.setInitialPosition(target.posX,target.posY+target.height/2,target.posZ,target.rotationYaw,target.rotationPitch,0,0f);
            entityDrive.setMultiHit(true);
            world.spawnEntity(entityDrive);
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashEdge);
    }

    @Override
    public void doSuperSpecialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        float baseModif = blade.getBaseAttackModifiers(tag);
        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float magicDamage = (baseModif/2.0f);

        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));

        if(!world.isRemote){
            List<EntityLivingBase> target = TargetUtils.findAllHostileEntities(player,20f,false);
            for (int i=0;i < target.size();i++){
                EntityLivingBase targetEntity = TargetUtils.setTargetEntityFromListByEntity(i,target);
                Random random = targetEntity.getRNG();
                EntityOverCharge entityDrive = new EntityOverCharge(world,player,magicDamage);
                entityDrive.setInitialPosition(targetEntity.posX,targetEntity.posY+targetEntity.height/2,targetEntity.posZ,targetEntity.rotationYaw, random.nextInt(90), random.nextInt(90), 0f);
                entityDrive.setColor(color[i%color.length]);
                entityDrive.setScale(1.5f);
                entityDrive.setLifeTime(40);
                entityDrive.setMultiHit(true);
                world.spawnEntity(entityDrive);
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriorosi);
    }

    private Entity getEntityToWatch(EntityPlayer player){
        World world = player.world;
        Entity target = null;
        for(int dist = 2; dist < 60; dist+=2){
            AxisAlignedBB bb = player.getEntityBoundingBox();
            Vec3d vec = player.getLookVec();
            vec = vec.normalize();
            bb = bb.grow(2.0f, 2.0f, 2.0f);
            bb = bb.offset(vec.x*(float)dist,vec.y*(float)dist,vec.z*(float)dist);

            List<Entity> list = world.getEntitiesInAABBexcluding(player, bb, EntitySelectorAttackable.getInstance());
            float distance = 60.0f;
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

    private final int[] color = new int[] {
            0xFF0000,
            0xEE9900,
            0xFFFF00,
            0x00FF00,
            0x00CC00,
            0x00CCFF,
            0x0000FF,
            0xFF00FF
    };
}