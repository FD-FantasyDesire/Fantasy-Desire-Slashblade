package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityPhantomSwordBase;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordEx;
import tennouboshiuzume.mods.fantasydesire.entity.EntityPhantomSwordExBase;
import tennouboshiuzume.mods.fantasydesire.entity.EntitySoulPhantomSword;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpikeOfAbyss extends SpecialAttackBase {

    @Override
    public String toString() {
        return "SpikeOfAbyss";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int rains = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-2, 1),8);

        ItemSlashBlade blade = (ItemSlashBlade)stack.getItem();

        int level = Math.max(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack));
        float baseModif = blade.getBaseAttackModifiers(tag);
        float magicDamage = 1.0f + (baseModif/2.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        Random random = player.getRNG();
        if (!world.isRemote) {
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player,60,true));
            if (!target.isEmpty()){
                for (int i=0;i<target.size()*rains;i++){

                    EntityLivingBase targetEntity = TargetUtils.setTargetEntityFromListByEntity(i,target);
                    EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world,player,magicDamage);
                    entityDrive.setInitialPosition(
                            targetEntity.posX+random.nextGaussian()*2,
                            targetEntity.posY-20f+random.nextGaussian()*2,
                            targetEntity.posZ+random.nextGaussian()*2,
                            0f,
                            -89,
                            0,
                            1.75f
                    );
                    entityDrive.setRoll((float) (random.nextGaussian()*30));
                    entityDrive.setInterval(40);
                    entityDrive.setIsOverWall(true);
                    entityDrive.setColor(-0x010101);
                    if (!target.isEmpty()){
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));
                        TargetUtils.setTargetEntityFromListByEntity(i,target).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,200,45));
                    }
                    entityDrive.setScale(2.5f);
                    entityDrive.setLifeTime(200);
                    world.spawnEntity(entityDrive);
                }
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
