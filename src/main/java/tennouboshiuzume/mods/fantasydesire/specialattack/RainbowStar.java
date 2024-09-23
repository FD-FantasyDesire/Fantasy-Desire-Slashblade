package tennouboshiuzume.mods.fantasydesire.specialattack;

import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityPhantomSwordBase;
import mods.flammpfeil.slashblade.entity.selector.EntitySelectorAttackable;
import mods.flammpfeil.slashblade.event.ScheduleEntitySpawner;
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
import tennouboshiuzume.mods.fantasydesire.util.ColorUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RainbowStar extends SpecialAttackBase {

    @Override
    public String toString() {
        return "RainbowStar";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.world;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        int rains = Math.min(Math.max((int) Math.sqrt(Math.abs(player.experienceLevel)) - 2, 1), 9);
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        float magicDamage = 1.0f + ItemSlashBlade.AttackAmplifier.get(tag) * (level / 5.0f);
        int rank = StylishRankManager.getStylishRank(player);
        if (5 <= rank)
            magicDamage += ItemSlashBlade.AttackAmplifier.get(tag) * (0.25f + (level / 5.0f));
        Random random = player.getRNG();
        magicDamage = 1;
        if (!world.isRemote) {
            List<EntityLivingBase> target = new ArrayList<>(TargetUtils.findAllHostileEntities(player, 30, player, true));
            Collections.shuffle(target);
            if (!target.isEmpty()) {
                int count = 0;
                for (int z = 0; z < Math.min(20, target.size()); z++) {
                    for (int i = 0; i < rains; i++) {
                        float yaw = (360f / (Math.min(20, target.size())*rains)) * count;
                        float pitch = (float) (random.nextGaussian() * 30f);
                        float roll = (float) (random.nextInt(360)-180);
                        Vec3d basePos = new Vec3d(0,0,1);
                        Vec3d spawnPos = new Vec3d(player.posX,player.posY+20f,player.posZ)
                                .add(basePos
                                        .rotatePitch((float) Math.toRadians(pitch))
                                        .rotateYaw((float) Math.toRadians(yaw))
                                        .scale(5f));

                        EntityPhantomSwordEx entityDrive = new EntityPhantomSwordEx(world, player, magicDamage);
                        entityDrive.setInitialPosition(
                                spawnPos.x,
                                spawnPos.y,
                                spawnPos.z,
                                -yaw,
                                -pitch,
                                roll,
                                1.75f
                        );
                        entityDrive.setInterval(40+(count%rains)*2);
                        entityDrive.setColor(ColorUtils.getSmoothTransitionColor(count,Math.min(20, target.size())*rains,true));
                        entityDrive.setTargetEntityId(TargetUtils.setTargetEntityFromListByEntity(count, target).getEntityId());
                        entityDrive.setIsOverWall(true);
                        entityDrive.setScale(2f);
                        entityDrive.setLifeTime(200+(count%rains)*2);
                        world.spawnEntity(entityDrive);
                        count++;
                    }
                }
            }
        }
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.Kiriage);
    }
}
