package com.taylorswiftcn.megumi.bound.listener;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.task.UpdateArmorTask;
import com.taylorswiftcn.megumi.bound.task.UpdateInventoryTask;
import com.taylorswiftcn.megumi.bound.util.ItemUtil;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private MegumiBound plugin = MegumiBound.getInstance();

    @EventHandler(priority = EventPriority.LOW)
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem().getItemStack();

        if (player.hasPermission("megumibound.admin")) {
            return;
        }

        if (BindHandler.hasBind(item)) {
            if (!BindHandler.isBindPlayer(player, item)) {
                plugin.getSqlManager().addItem(item);
                e.setCancelled(true);
                e.getItem().remove();
                return;
            }
        }

        if (BindHandler.isBindOnPickup(item)) {
            BindHandler.bind(player, item, false);

            (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (WeiUtil.isEmpty(item)) return;

        Action action = e.getAction();

        if (action != Action.PHYSICAL) {
            if (ItemUtil.isEquipable(item)) {
                UpdateArmorTask task = new UpdateArmorTask(player);
                task.runTaskLater(plugin, 1);
                return;
            }

            if (BindHandler.isBindOnUse(item)) {
                BindHandler.bind(player, item, false);

                (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
            }
        }
    }
}
