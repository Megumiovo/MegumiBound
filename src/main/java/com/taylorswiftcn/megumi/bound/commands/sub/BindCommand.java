package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.task.UpdateInventoryTask;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BindCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length == 1) {
            Player player = getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();
            if (WeiUtil.isEmpty(item)) {
                CommandSender.sendMessage(Config.Prefix + Message.NoItemOnHand);
                return;
            }

            BindHandler.bind(player, item, true);

            (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
        }

        if (Strings.length == 2) {
            if (!CommandSender.hasPermission("megumibound.admin")) {
                CommandSender.sendMessage(Config.Prefix + Message.NoPermission.replace("%s%", "megumibound.admin"));
                return;
            }

            String v1 = Strings[1];

            Player player = getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();
            if (WeiUtil.isEmpty(item)) {
                CommandSender.sendMessage(Config.Prefix + Message.NoItemOnHand);
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(v1);

            BindHandler.bind(offlinePlayer, item);

            (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
        }
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "megumibound.bind";
    }
}
