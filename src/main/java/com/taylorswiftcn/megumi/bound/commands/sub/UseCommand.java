package com.taylorswiftcn.megumi.bound.commands.sub;

import com.taylorswiftcn.megumi.bound.BindHandler;
import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.commands.WeiCommand;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.task.UpdateInventoryTask;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UseCommand extends WeiCommand {

    private MegumiBound plugin = MegumiBound.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player player = getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (WeiUtil.isEmpty(item)) {
            CommandSender.sendMessage(Config.Prefix + Message.NoItemOnHand);
            return;
        }

        BindHandler.setBindOnUse(player, item);

        (new UpdateInventoryTask(player)).runTaskLater(plugin, 2);
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
