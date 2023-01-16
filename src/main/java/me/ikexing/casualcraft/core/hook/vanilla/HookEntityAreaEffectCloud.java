package me.ikexing.casualcraft.core.hook.vanilla;

import me.ikexing.casualcraft.core.utils.reflect.ReflectEntityItem;
import me.ikexing.casualcraft.recipes.RecipeBaseTransform;
import me.ikexing.casualcraft.recipes.RecipeEntityCloudRecipe;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class HookEntityAreaEffectCloud {

    public static final int DURATION_TICK = 5;
    public static final int CRAFTING_TICK = 10;

    public static void handleOnUpdate(EntityAreaEffectCloud entity, List<PotionEffect> potions) {
        World world = entity.getEntityWorld();

        List<EntityItem> entityItemList = world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox());
        for (EntityItem entityItem : entityItemList) {
            for (PotionEffect potion : potions) {
                putPotionEffect(entityItem, potion);
            }
        }

        if (entity.ticksExisted % CRAFTING_TICK == 0) {
            RecipeEntityCloudRecipe foundRecipe = RecipeBaseTransform.Companion.matchesEntityCloudRecipe(entityItemList, false);
            if (Objects.nonNull(foundRecipe)) {
                foundRecipe.spawnOutput(entity.getPosition(), entityItemList, world, true);
            }
        }
    }

    private static void putPotionEffect(EntityItem entityItem, PotionEffect potionEffect) {
        int amplifier = potionEffect.getAmplifier();
        String effectName = potionEffect.getEffectName();
        Map<String, Pair<Integer, Integer>> entityItemPotions = ReflectEntityItem.getPotions(entityItem);

        if (Objects.isNull(entityItemPotions)) return;

        if (entityItemPotions.containsKey(effectName)) {
            Pair<Integer, Integer> pair = entityItemPotions.get(effectName);
            entityItemPotions.put(effectName, Pair.of(amplifier, pair.getRight() + DURATION_TICK));
        } else {
            entityItemPotions.put(effectName, Pair.of(amplifier, DURATION_TICK));
        }
    }

}
