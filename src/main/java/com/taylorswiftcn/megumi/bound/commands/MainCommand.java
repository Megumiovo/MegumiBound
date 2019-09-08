package com.taylorswiftcn.megumi.bound.commands;

import com.taylorswiftcn.megumi.bound.commands.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MainCommand implements CommandExecutor {
    private HelpCommand help;
    private HashMap<String, WeiCommand> commands;

    public MainCommand() {
        this.help = new HelpCommand();
        this.commands = new HashMap<>();
        this.commands.put("bind", new BindCommand());
        this.commands.put("unbind", new UnbindCommand());
        this.commands.put("use", new UseCommand());
        this.commands.put("equip", new EquipCommand());
        this.commands.put("pickup", new PickupCommand());
        this.commands.put("full", new FullCommand());
        this.commands.put("box", new BoxCommand());
        this.commands.put("reload", new ReloadCommand());
        this.commands.put("fix", new FixCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        WeiCommand cmd = help;
        if (strings.length >= 1 && commands.containsKey(strings[0])) {
            cmd = commands.get(strings[0]);
        }
        cmd.execute(commandSender, strings);
        return false;
    }
}
