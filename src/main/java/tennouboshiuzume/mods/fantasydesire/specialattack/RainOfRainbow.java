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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class RainOfRainbow extends SpecialAttackBase {

    @Override
    public String toString() {
        return "RainOfRainbow";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int rains = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel))-2, 1),8);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if(5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level /5.0f));
        Random random =player.getRNG();

        if (!world.isRemote) {
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player,60));
            if (!target.isEmpty()){
                for (int i=0;i<target.size()*rains;i++){
                    EntityLivingBase targetEntity = TargetUtils.setTargetEntityFromListByEntity(i,target);
                    Vec3d targetPos = targetEntity.getPositionVector();
                    Vec3d spawnPos = new Vec3d(
                            targetEntity.posX+random.nextGaussian(),
                            targetEntity.posY+20f+random.nextGaussian()*2,
                            targetEntity.posZ+random.nextGaussian());
//                    Vec3d spawnPos = new Vec3d(player.posX,player.posY+20,player.posZ);

                    EntityPhantomSwordExBase entityDrive = new EntityPhantomSwordExBase(world,player,magicDamage);
                    entityDrive.setInitialPosition(
                            spawnPos.x,
                            spawnPos.y,
                            spawnPos.z,
//                            (float) (random.nextGaussian()*60),
                            random.nextInt(360),
                            (float) (90f+random.nextGaussian()*5),
                            0f,
                            1.75f
                    );
                    entityDrive.setRoll((float) (random.nextGaussian()*30));
                    entityDrive.setInterval(40+5*(i%rains));
                    entityDrive.setColor(random.nextInt(16777215));
                    if (!target.isEmpty()){
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromList(i,target));
                    }
                    entityDrive.setScale(2f);
                    entityDrive.setParticle(EnumParticleTypes.TOTEM);
                    entityDrive.setLifeTime(200);
                    world.spawnEntity(entityDrive);
                }
            }
        }

        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
