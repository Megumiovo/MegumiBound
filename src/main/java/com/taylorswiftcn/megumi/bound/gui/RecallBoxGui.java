package com.taylorswiftcn.megumi.bound.gui;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecallBoxGui {

    private MegumiBound plugin;
    private Inventory gui;

    public RecallBoxGui(MegumiBound plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        gui = Bukkit.createInventory(null, 54, Config.GUI.Title);
        addContent(player.getUniqueId());
        player.openInventory(gui);
    }

    public void open(Player player, UUID target) {
        gui = Bukkit.createInventory(null, 54, Config.GUI.Title + " ");
        addContent(target);
        player.openInventory(gui);
    }

    private void addContent(UUID uuid) {
        for (int i = 45; i < 54; i ++) {
            gui.setItem(i, WeiUtil.createItem("160", 0, 1, null, null, null));
        }

        gui.setItem(49, Config.GUI.Button_Info);

        List<RecallItem> items = plugin.getBoxMap().get(uuid);

        if (items.size() == 0) return;

        List<RecallItem> list = getItem(items);
        for (int i = 0; i < list.size(); i++) {
            gui.setItem(i, list.get(i).getItem());
        }
    }

    private List<RecallItem> getItem(List<RecallItem> items) {
        if (items.size() > 45) {
            return new ArrayList<>(items.subList(0, 45));
        }
        else {
            return items;
        }
    }
}
