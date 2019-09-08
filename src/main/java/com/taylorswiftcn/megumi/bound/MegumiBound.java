package com.taylorswiftcn.megumi.bound;

import com.taylorswiftcn.megumi.bound.commands.MainCommand;
import com.taylorswiftcn.megumi.bound.database.SQLManager;
import com.taylorswiftcn.megumi.bound.file.FileManager;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.gui.RecallBoxGui;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import com.taylorswiftcn.megumi.bound.listener.InventoryListener;
import com.taylorswiftcn.megumi.bound.listener.ItemListener;
import com.taylorswiftcn.megumi.bound.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MegumiBound extends JavaPlugin {
    @Getter private static MegumiBound instance;

    @Getter private FileManager fileManager;
    @Getter private SQLManager sqlManager;

    @Getter private RecallBoxGui gui;

    @Getter private HashMap<UUID, List<RecallItem>> boxMap;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        boxMap = new HashMap<>();

        fileManager = new FileManager(this);
        sqlManager = new SQLManager(this);

        gui = new RecallBoxGui(this);

        fileManager.init();

        Config.init();
        Message.init();

        if (Config.SQL.Enable) {
            sqlManager.init();
        }
        else {
            getLogger().info("[!] 未启用数据库连接!");
        }

        getCommand("mb").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new ItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        closeInventory();
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void closeInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() == null) continue;
            if (!player.getOpenInventory().getTitle().equals(Config.GUI.Title)) continue;
            player.closeInventory();
        }
    }
}
