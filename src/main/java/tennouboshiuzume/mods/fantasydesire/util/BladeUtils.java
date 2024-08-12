package tennouboshiuzume.mods.fantasydesire.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.util.ResourceLocationRaw;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tennouboshiuzume.mods.fantasydesire.FantasyDesire;


import java.util.List;
import java.util.Map;

public class BladeUtils {
    public static Map<ResourceLocationRaw, ItemStack> fdBladeRegistry = Maps.newHashMap();
    public static List<String> FdNamedBlades = Lists.newArrayList();

    public static void registerCustomItemStack(String name, ItemStack stack){
        fdBladeRegistry.put(new ResourceLocationRaw(FantasyDesire.MODID, name),stack);
    }

    static public ItemStack findItemStack(String modid, String name, int count){
        ResourceLocationRaw key = new ResourceLocationRaw(modid, name);
        ItemStack stack = ItemStack.EMPTY;

        if(fdBladeRegistry.containsKey(key)) {
            stack = fdBladeRegistry.get(key).copy();

        }else if(SlashBlade.BladeRegistry.containsKey(key)){
            stack = SlashBlade.BladeRegistry.get(key).copy();
        }else{
            Item item = Item.REGISTRY.getObject(key);
            if (item != null){
                stack = new ItemStack(item);
            }
        }

        if(!stack.isEmpty()) {
            stack.setCount(count);
        }

        return stack;
    }

    public static ItemStack getCustomBlade(String modid,String name){
        return BladeUtils.findItemStack(modid, name, 1);
    }
    public static ItemStack getCustomBlade(String key){
        String modid;
        String name;
        {
            String str[] = key.split(":",2);
            if(str.length == 2){
                modid = str[0];
                name = str[1];
            }else{
                modid = FantasyDesire.MODID;
                name = key;
            }
        }

        return getCustomBlade(modid,name);
    }

    public static ItemStack getMcItemStack(String name){
        ResourceLocationRaw key = new ResourceLocationRaw("minecraft", name);
        Item item = Item.REGISTRY.getObject(key);
        ItemStack stack = ItemStack.EMPTY;
        if (item != null){
            stack = new ItemStack(item);
        }
        return stack;
    }

}
