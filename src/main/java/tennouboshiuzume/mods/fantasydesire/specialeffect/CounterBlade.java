package tennouboshiuzume.mods.fantasydesire.specialeffect;

import ibxm.Player;
import mods.flammpfeil.slashblade.specialeffect.IRemovable;
import mods.flammpfeil.slashblade.specialeffect.ISpecialEffect;
import mods.flammpfeil.slashblade.specialeffect.SpecialEffects;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.SlashBladeEvent;
import mods.flammpfeil.slashblade.util.SlashBladeHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import tennouboshiuzume.mods.fantasydesire.init.FdSEs;
import tennouboshiuzume.mods.fantasydesire.util.MathUtils;
import tennouboshiuzume.mods.fantasydesire.util.ParticleUtils;
import tennouboshiuzume.mods.fantasydesire.util.TargetUtils;

import java.util.Collections;
import java.util.List;


public class CounterBlade implements ISpecialEffect, IRemovable {
    private static final String EffectKey = "CounterBlade";

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSlashBlade)) return;
        ItemStack blade = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!(blade.getItem() instanceof ItemSlashBlade)) return;
        ItemSlashBlade itemBlade = (ItemSlashBlade) blade.getItem();
        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(blade);
        if (!(event.getSource().getTrueSource() instanceof EntityLivingBase)) return;
        EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
        switch (SpecialEffects.isEffective(player, blade, this)) {
            /** 任何时候可触发 */
            case None:
                return;
            /** 未达到所需等级 */
            case NonEffective:
                return;
            /** 达到所需等级 */
            case Effective:
                break;
        }
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, -2, false)) return;
        List<EntityLivingBase> list = TargetUtils.findAllHostileEntities(player, 20, player, true);
        list.remove(attacker);
        Collections.shuffle(list);

        int color = 0xFFFFFF;

        if (ItemSlashBlade.SummonedSwordColor.get(tag) != null) {
            color = ItemSlashBlade.SummonedSwordColor.get(tag);
        }
        float red = Math.max(0.001f, ((color >> 16) & 0xff) / 255.0f);
        float green = Math.max(0.001f, ((color >> 8) & 0xff) / 255.0f);
        float blue = Math.max(0.001f, ((color) & 0xff) / 255.0f);

        if (!list.isEmpty()) {
            ParticleUtils.spawnParticleLine(list.get(0).world, EnumParticleTypes.SPELL_MOB,
                    player.posX, player.posY + player.height / 2, player.posZ,
                    list.get(0).posX, list.get(0).posY + list.get(0).height / 2, list.get(0).posZ,
                    red, green, blue, 0, 1);

            for (int i = 0; i < list.size(); i++) {
                EntityLivingBase target = list.get(i);
                itemBlade.hitEntity(blade, target, player);
                target.hurtTime = 0;

                // 生成连接至下一个实体的粒子线条
                EntityLivingBase nextTarget = list.get((i + 1) % list.size());
                if (!(nextTarget == list.get(0))) {
                    ParticleUtils.spawnParticleLine(target.world, EnumParticleTypes.REDSTONE,
                            target.posX, target.posY + target.height / 2, target.posZ,
                            nextTarget.posX, nextTarget.posY + nextTarget.height / 2, nextTarget.posZ,
                            red, green, blue, 0, 1);
                }
            }
        }
    }


    @Override
    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getDefaultRequiredLevel() {
        return 35;
    }

    @Override
    public String getEffectKey() {
        return EffectKey;
    }

    @Override
    public boolean canCopy(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRemoval(ItemStack stack) {
        return false;
    }
}