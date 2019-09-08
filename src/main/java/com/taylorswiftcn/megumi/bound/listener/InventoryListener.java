package com.taylorswiftcn.megumi.bound.listener;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import com.taylorswiftcn.megumi.bound.util.ItemUtil;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class InventoryListener implements Listener {

    private MegumiBound plugin = MegumiBound.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        if (e.getInventory().getTitle().equals(Config.GUI.Title + " ")) {
            e.setCancelled(true);
            return;
        }

        if (!e.getInventory().getTitle().equals(Config.GUI.Title)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();

        if (e.getClick() == ClickType.NUMBER_KEY) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (WeiUtil.isEmpty(item)) return;

        e.setCancelled(true);

        ClickType type = e.getClick();

        if (type == ClickType.RIGHT) {
            List<RecallItem> items = plugin.getBoxMap().get(player.getUniqueId());

            for (int i = 0; i < items.size(); i++) {
                RecallItem recallItem = items.get(i);
                if (!recallItem.getItem().equals(item)) continue;

                if (ItemUtil.isFull(player)) {
                    player.sendMessage(Config.Prefix + Message.InventoryFull);
                    return;
                }

                player.getInventory().addItem(item);

                plugin.getSqlManager().remove(recallItem.getId());

                items.remove(recallItem);
                plugin.getBoxMap().put(player.getUniqueId(), items);

                plugin.getGui().open(player);
            }
        }
    }
}
