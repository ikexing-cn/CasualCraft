package me.ikexing.casualcraft.core.hook.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class HookEntityItem {

    public static NBTTagCompound handleWriteEntityToNBT(Map<String, Pair<Integer, Integer>> potions) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        if (Objects.isNull(potions) || potions.isEmpty()) return nbtTagCompound;
        for (Map.Entry<String, Pair<Integer, Integer>> entry : potions.entrySet()) {
            int[] ints = {entry.getValue().getLeft(), entry.getValue().getRight()};
            nbtTagCompound.setIntArray(entry.getKey(), ints);
        }
        return nbtTagCompound;
    }

    public static void handleReadEntityFromNBT(Map<String, Pair<Integer, Integer>> potions, NBTTagCompound nbt) {
        for (String key : nbt.getKeySet()) {
            int[] ints = nbt.getIntArray(key);
            potions.put(key, Pair.of(ints[0], ints[1]));
        }
    }

}
