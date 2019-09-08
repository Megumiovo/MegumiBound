package com.taylorswiftcn.megumi.bound.file;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {
    private MegumiBound plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;

    public FileManager(MegumiBound plugin) {
        this.plugin = plugin;
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");
    }

    private YamlConfiguration initFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            WeiUtil.copyFile(plugin.getResource(name), file);
            WeiUtil.log(String.format("File: 已生成 %s 文件", name));
        }
        else WeiUtil.log(String.format("File: 已加载 %s 文件", name));
        return YamlConfiguration.loadConfiguration(file);
    }
}
