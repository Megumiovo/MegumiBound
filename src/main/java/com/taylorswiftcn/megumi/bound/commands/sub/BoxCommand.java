package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BoxCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player player = getPlayer();
        if (Strings.length == 1) {
            List<RecallItem> items = plugin.getSqlManager().getRecallItems(player.getUniqueId());
            plugin.getBoxMap().put(player.getUniqueId(), items);
            plugin.getGui().open(player);
        }

        if (Strings.length == 2) {
            if (!CommandSender.hasPermission("megumibound.admin")) {
                CommandSender.sendMessage(Config.Prefix + Message.NoPermission.replace("%s%", "megumibound.admin"));
                return;
            }

            String v1 = Strings[1];

            UUID uuid = UUID.nameUUIDFromBytes(String.format("OfflinePlayer:%s", v1).getBytes());

            List<RecallItem> items = plugin.getSqlManager().getRecallItems(uuid);
            plugin.getBoxMap().put(uuid, items);
            plugin.getGui().open(player, uuid);
        }
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "megumibound.box";
    }
}
