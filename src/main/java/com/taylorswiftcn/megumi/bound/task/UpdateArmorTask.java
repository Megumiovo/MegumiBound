package com.taylorswiftcn.megumi.bound.task;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateArmorTask extends BukkitRunnable {

    private Player player;

    public UpdateArmorTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (!WeiUtil.isEmpty(item) && BindHandler.isBindOnEquip(item)) {
                BindHandler.bind(player, item, false);
            }
        }

        player.getInventory().setArmorContents(player.getInventory().getArmorContents());
    }
}
