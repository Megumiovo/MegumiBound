package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.task.UpdateInventoryTask;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FullCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 2) return;
        String v1 = Strings[1];

        if (Bukkit.getPlayerExact(v1) == null) {
            CommandSender.sendMessage(Config.Prefix + Message.PlayerOffline);
            return;
        }

        Player player = Bukkit.getPlayerExact(v1);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (WeiUtil.isEmpty(item)) continue;
            BindHandler.bind(player, item, false);
        }

        CommandSender.sendMessage(Config.Prefix + String.format("§a玩家 %s 背包所有物品已全部绑定", player.getName()));

        (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return "megumibound.admin";
    }
}
