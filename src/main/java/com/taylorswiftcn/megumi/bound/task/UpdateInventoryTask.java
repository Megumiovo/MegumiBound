package com.taylorswiftcn.megumi.bound.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateInventoryTask extends BukkitRunnable {

    private Player player;

    public UpdateInventoryTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isValid()) return;
        player.updateInventory();
    }
}
