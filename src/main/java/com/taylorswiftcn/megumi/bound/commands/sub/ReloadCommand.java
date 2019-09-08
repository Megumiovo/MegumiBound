package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        plugin.closeInventory();

        plugin.getBoxMap().clear();

        plugin.getFileManager().init();
        Config.init();
        Message.init();

        if (Config.SQL.Enable) {
            plugin.getSqlManager().init();
        }
        else {
            plugin.getLogger().info("[!] 未启用数据库连接!");
        }
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
