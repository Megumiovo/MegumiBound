package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FixCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 2) return;

        Player player = getPlayer();

        String v1 = Strings[1];

        UUID uuid = UUID.nameUUIDFromBytes(String.format("OfflinePlayer:%s", v1).getBytes());

        List<RecallItem> items = plugin.getSqlManager().getRecallItems(uuid);

        for (RecallItem item : items) {
            player.getInventory().addItem(item.getItem());
            plugin.getSqlManager().remove(item.getId());
        }

        CommandSender.sendMessage(Config.Prefix + String.format("§a%s 玩家的绑定箱子物品已全部拿出,共 %s 件", v1, items.size()));
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "megumibound.admin";
    }
}
