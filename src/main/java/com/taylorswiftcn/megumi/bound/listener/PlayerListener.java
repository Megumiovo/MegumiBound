package com.taylorswiftcn.megumi.bound.listener;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.task.CheckBoxTask;
import com.taylorswiftcn.megumi.bound.util.ItemUtil;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

    private MegumiBound plugin = MegumiBound.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CheckBoxTask task = new CheckBoxTask(plugin, e.getPlayer());
        task.runTaskLaterAsynchronously(plugin, 20);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();

        if (player.hasPermission("megumibound.admin")) {
            return;
        }

        if (BindHandler.hasBind(item)) {
            if (!BindHandler.isBindPlayer(player, item)) {
                plugin.getSqlManager().addItem(item);
                entity.remove();
                return;
            }
            else {
                if (Config.Prevent_Item_Drop) {
                    if (ItemUtil.isFull(player)) {
                        plugin.getSqlManager().addItem(item);
                        e.getItemDrop().remove();
                        return;
                    }
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if (Config.Delete_On_Drop) {
            entity.remove();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Inventory gui = e.getInventory();

        if (player.hasPermission("megumibound.admin")) return;

        if (WeiUtil.isEmpty(item)) return;

        if (BindHandler.hasBind(item) && !BindHandler.isBindPlayer(player, item)) {
            plugin.getSqlManager().addItem(item);
            e.setCancelled(true);
            e.setCurrentItem(new ItemStack(Material.AIR));
        }

        if (gui.getType() == InventoryType.CRAFTING) {
            return;
        }

        if (Config.Allow_Item_Storing) {
            if (BindHandler.hasBind(item)) return;
            if (BindHandler.isBindPlayer(player, item)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (WeiUtil.isEmpty(item)) return;

        if (!BindHandler.hasBind(item)) return;
        if (!BindHandler.isBindPlayer(player, item)) {
            plugin.getSqlManager().addItem(item);
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("megumibound.admin")) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (WeiUtil.isEmpty(item)) return;

        if (!BindHandler.hasBind(item)) return;

        String cmd = e.getMessage();

        if (Config.Block_Commands.contains(cmd)) {
            e.setCancelled(true);
            player.sendMessage(Config.Prefix + Message.UnableToExecute);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!Config.Death_Inventory_Drop) return;

        Player player = e.getEntity();
        if (player.getWorld().getGameRuleValue("keepInventory").equals("false")) {
            e.setKeepInventory(true);
            return;
        }

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (WeiUtil.isEmpty(item)) continue;
            if (!BindHandler.hasBind(item)) {
                if (!hasLore(item)) {
                    player.getWorld().dropItem(player.getLocation(), item);
                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
                continue;
            }
            if (!BindHandler.isBindPlayer(player, item)) {
                plugin.getSqlManager().addItem(item);
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }

        player.setTotalExperience(0);
        player.setExp(0);
        player.setLevel(0);
    }

    private boolean hasLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        for (String s : meta.getLore()) {
            if (Config.White_Lore.contains(s)) return true;
        }

        return false;
    }
}
