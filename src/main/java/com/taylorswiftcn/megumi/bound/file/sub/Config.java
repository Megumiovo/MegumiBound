package com.taylorswiftcn.megumi.bound.file.sub;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Config {
    private static YamlConfiguration config;
    public static String Prefix;
    public static Boolean Death_Inventory_Drop;
    public static Boolean Prevent_Item_Drop;
    public static Boolean Delete_On_Drop;
    public static Boolean Allow_Item_Storing;
    public static List<String> Block_Commands;
    public static List<String> White_Lore;

    public static class SQL {
        public static Boolean Enable;
        public static String Host;
        public static String Port;
        public static String Database;
        public static String Table;
        public static String Users;
        public static String Password;
    }

    public static class BOUND {
        public static String Format;
        public static String AfterBind_1;
        public static String AfterBind_2;
        public static String BindOnPickup;
        public static String BindOnUse;
        public static String BindOnEquip;
    }

    public static class GUI {
        public static String Title;
        public static ItemStack Button_Info;
    }

    public static void init() {
        config = MegumiBound.getInstance().getFileManager().getConfig();

        Prefix = getString("Prefix");
        Death_Inventory_Drop = config.getBoolean("Config.Death_Inventory_Drop");
        Prevent_Item_Drop = config.getBoolean("Config.Prevent_Item_Drop");
        Delete_On_Drop = config.getBoolean("Config.Delete_On_Drop");
        Allow_Item_Storing = config.getBoolean("Config.Allow_Item_Storing");
        Block_Commands = config.getStringList("Config.Block_Commands");
        White_Lore = getStringList("Config.White_Lore");

        SQL.Enable = config.getBoolean("MYSQL.Enable");
        SQL.Host = getString("MYSQL.Host");
        SQL.Port = getString("MYSQL.Port");
        SQL.Database = getString("MYSQL.Database");
        SQL.Table = getString("MYSQL.Table");
        SQL.Users = getString("MYSQL.Users");
        SQL.Password = getString("MYSQL.Password");

        BOUND.Format = getString("Bound.Format");
        BOUND.AfterBind_1 = getString("Bound.AfterBind_1");
        BOUND.AfterBind_2 = getString("Bound.AfterBind_2");
        BOUND.BindOnPickup = getString("Bound.BindOnPickup");
        BOUND.BindOnUse = getString("Bound.BindOnUse");
        BOUND.BindOnEquip = getString("Bound.BindOnEquip");

        GUI.Title = getString("Gui.Title");
        GUI.Button_Info = getButton("Info");
    }

    private static ItemStack getButton(String s) {
        String id = config.get(String.format("Gui.Button.%s.ID", s)).toString();
        int data = config.getInt(String.format("Gui.Button.%s.Data", s));
        String name = config.getString(String.format("Gui.Button.%s.Name", s));
        List<String> lore = config.getStringList(String.format("Gui.Button.%s.Lore", s));

        return WeiUtil.createItem(id, data, 1, name, lore, null);
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(config.getStringList(path));
    }
}
