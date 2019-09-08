package com.taylorswiftcn.megumi.bound.gui;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class RecallItem {

    private Integer id;
    private ItemStack item;

    public RecallItem(Integer id, ItemStack item) {
        this.id = id;
        this.item = item;
    }
}
