package com.taylorswiftcn.megumi.bound.file.sub;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Message {
    private static YamlConfiguration message;

    public static List<String> Help;
    public static List<String> AdminHelp;

    public static String NoPermission;
    public static String NoItemOnHand;
    public static String AlreadyBound;
    public static String BindSuccess;
    public static String UnbindSuccess;
    public static String NotYetBound;
    public static String BindNotYou;
    public static String SetBindWay;
    public static String PlayerOffline;
    public static String UnableToExecute;
    public static String InventoryFull;
    public static String ItemOnRecall;
    public static String BoxHasItems;

    public static void init() {
        message = MegumiBound.getInstance().getFileManager().getMessage();

        Help = getStringList("Help");
        AdminHelp = getStringList("AdminHelp");

        NoPermission = getString("Message.NoPermission");
        NoItemOnHand = getString("Message.NoItemOnHand");
        AlreadyBound = getString("Message.AlreadyBound");
        BindSuccess = getString("Message.BindSuccess");
        UnbindSuccess = getString("Message.UnbindSuccess");
        NotYetBound = getString("Message.NotYetBound");
        BindNotYou = getString("Message.BindNotYou");
        SetBindWay = getString("Message.SetBindWay");
        PlayerOffline = getString("Message.PlayerOffline");
        UnableToExecute = getString("Message.UnableToExecute");
        InventoryFull = getString("Message.InventoryFull");
        ItemOnRecall = getString("Message.ItemOnRecall");
        BoxHasItems = getString("Message.BoxHasItems");
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(message.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(message.getStringList(path));
    }


}
