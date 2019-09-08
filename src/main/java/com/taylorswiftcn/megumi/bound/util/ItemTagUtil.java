package com.taylorswiftcn.megumi.bound.util;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemTagUtil {

    public static final String SOULBOUND_TAG = "SOULBOUND.UUID";

    public static ItemStack setTag(ItemStack item, String value) {
        if (WeiUtil.isEmpty(item)) return item;

        item = getBukkitItemStack(item);
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        if (!nbt.containsKey(SOULBOUND_TAG)) {
            nbt.put(SOULBOUND_TAG, value);
        }

        NbtFactory.setItemTag(item, nbt);

        return item;
    }

    public static ItemStack delTag(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return item;

        item = getBukkitItemStack(item);
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        if (nbt.containsKey(SOULBOUND_TAG)) {
            nbt.remove(SOULBOUND_TAG);
        }

        NbtFactory.setItemTag(item, nbt);

        return item;
    }

    public static String getTag(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return "";

        item = getBukkitItemStack(item);
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        if (!nbt.containsKey(SOULBOUND_TAG)) {
            return "";
        }

        return nbt.getString(SOULBOUND_TAG);
    }

    public static boolean hasTag(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return false;

        ItemStack itemStack = getBukkitItemStack(item.clone());
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(itemStack));

        return nbt.containsKey(SOULBOUND_TAG);
    }

    private static ItemStack getBukkitItemStack(ItemStack item) {
        if (item == null) {
            return new ItemStack(Material.AIR);
        }
        return !item.getClass().getName().endsWith("CraftItemStack") ? MinecraftReflection.getBukkitItemStack(item) : item;
    }
}
