package com.taylorswiftcn.megumi.bound.task;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CheckBoxTask extends BukkitRunnable {

    private MegumiBound plugin;
    private Player player;

    public CheckBoxTask(MegumiBound plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        List<RecallItem> items = plugin.getSqlManager().getRecallItems(player.getUniqueId());

        if (items.size() <= 0) return;

        player.sendMessage(Config.Prefix + Message.BoxHasItems.replace("%s%", String.valueOf(items.size())));
    }
}
