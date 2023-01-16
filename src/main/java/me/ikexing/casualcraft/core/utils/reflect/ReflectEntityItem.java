package me.ikexing.casualcraft.core.utils.reflect;

import me.ikexing.casualcraft.core.utils.CoreUtils;
import net.minecraft.entity.item.EntityItem;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectEntityItem {

    public static final String CLASS_NAME = "net.minecraft.entity.item.EntityItem";
    public static final String FIELD_POTIONS = "potions";

    @SuppressWarnings("all")
    public static Map<String, Pair<Integer, Integer>> getPotions(EntityItem entityItem) {
        Map<String, Pair<Integer, Integer>> result = null;
        try {
            Class<?> clazz = Class.forName(CLASS_NAME);
            Field fieldPotions = clazz.getDeclaredField(FIELD_POTIONS);
            result = ((Map<String, Pair<Integer, Integer>>) fieldPotions.get(entityItem));
        } catch (Exception e) {
            CoreUtils.getLogger().error(e);
        }
        return result;
    }

}
