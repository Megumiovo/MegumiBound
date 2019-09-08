package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import org.bukkit.command.CommandSender;

public class HelpCommand extends WeiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Message.Help.forEach(CommandSender::sendMessage);
        if (CommandSender.hasPermission("megumibound.admin"))
            Message.AdminHelp.forEach(CommandSender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
