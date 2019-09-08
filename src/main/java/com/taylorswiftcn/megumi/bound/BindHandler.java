package com.taylorswiftcn.megumi.bound;

import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.util.ItemTagUtil;
import com.taylorswiftcn.megumi.bound.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BindHandler {

    public static void bind(Player player, ItemStack item, boolean msg) {
        if (hasBind(item)) {
            if (msg) player.sendMessage(Config.Prefix + Message.AlreadyBound);
            return;
        }

        ItemTagUtil.setTag(item, player.getUniqueId().toString());

        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        clear(lore);

        lore.add(Config.BOUND.AfterBind_1);
        lore.add(Config.BOUND.AfterBind_2.replace("%player%", player.getName()));
        /*lore.add(Config.BOUND.Format.replace("%player%", player.getName()));*/

        meta.setLore(lore);
        item.setItemMeta(meta);

        if (msg) player.sendMessage(Config.Prefix + Message.BindSuccess);
    }

    public static void bind(OfflinePlayer player, ItemStack item) {
        if (hasBind(item)) {
            return;
        }

        ItemTagUtil.setTag(item, player.getUniqueId().toString());

        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        clear(lore);

        lore.add(Config.BOUND.AfterBind_1);
        lore.add(Config.BOUND.AfterBind_2.replace("%player%", player.getName()));
        /*lore.add(Config.BOUND.Format.replace("%player%", player.getName()));*/

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void unbind(Player player, ItemStack item, boolean msg) {
        if (!hasBind(item)) {
            if (msg) player.sendMessage(Config.Prefix + Message.NotYetBound);
            return;
        }

        if (player.hasPermission("megumibound.admin")) {
            removeBind(item);
            if (msg) player.sendMessage(Config.Prefix + Message.UnbindSuccess);
            return;
        }

        if (!isBindPlayer(player, item)) {
            if (msg) player.sendMessage(Config.Prefix + Message.BindNotYou);
            return;
        }

        removeBind(item);
        if (msg) player.sendMessage(Config.Prefix + Message.UnbindSuccess);
    }

    public static void setBindOnUse(Player player, ItemStack item) {
        setBindWay(player, item, Config.BOUND.BindOnUse);
    }

    public static void setBindOnPickup(Player player, ItemStack item) {
        setBindWay(player, item, Config.BOUND.BindOnPickup);
    }

    public static void setBindOnEquip(Player player, ItemStack item) {
        setBindWay(player, item, Config.BOUND.BindOnEquip);
    }

    public static boolean isBindOnPickup(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        List<String> lore = meta.getLore();
        return lore.contains(Config.BOUND.BindOnPickup);
    }

    public static boolean isBindOnUse(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        List<String> lore = meta.getLore();
        return lore.contains(Config.BOUND.BindOnUse);
    }

    public static boolean isBindOnEquip(ItemStack item) {
        if (WeiUtil.isEmpty(item)) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;

        List<String> lore = meta.getLore();
        return lore.contains(Config.BOUND.BindOnEquip);
    }

    private static void setBindWay(Player player, ItemStack item, String s) {
        if (hasBind(item)) {
            player.sendMessage(Config.Prefix + Message.AlreadyBound);
            return;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        if (lore.contains(s)) {
            return;
        }

        clear(lore);

        lore.add(s);
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendMessage(Config.Prefix + Message.SetBindWay.replace("%s%", s));
    }

    public static void removeBind(ItemStack item) {
        ItemTagUtil.delTag(item);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore.contains(Config.BOUND.AfterBind_1)) {
            int index = lore.indexOf(Config.BOUND.AfterBind_1);
            lore.remove(index);
            lore.remove(index);
        }
        else {
            for (String s : new ArrayList<>(lore)) {
                if (!isFormat(s)) continue;
                lore.remove(s);
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static boolean hasBind(ItemStack item) {
        if (ItemTagUtil.hasTag(item)) return true;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) {
            return false;
        }
        else {
            List<String> lore = meta.getLore();

            if (lore.contains(Config.BOUND.AfterBind_1)) return true;

            for (String s : lore) {
                if (!isFormat(s)) continue;
                return true;
            }

            return false;
        }
    }

    public static boolean isBindPlayer(Player player, ItemStack item) {
        if (ItemTagUtil.hasTag(item)) {
            String uuid = ItemTagUtil.getTag(item);
            return player.getUniqueId().toString().equals(uuid);
        }
        else {
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) return false;
            List<String> lore = item.getItemMeta().getLore();

            if (lore.contains(Config.BOUND.AfterBind_1)) {

                String name = getPlayer(lore);
                if (name != null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    ItemTagUtil.setTag(item, offlinePlayer.getUniqueId().toString());
                }
                else {
                    return false;
                }

                return name.equals(player.getName());
            }

            for (String s : lore) {
                if (isFormat(s)) {
                    String name = getFormatPlayer(s);
                    if (name != null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                        ItemTagUtil.setTag(item, offlinePlayer.getUniqueId().toString());
                    }
                    else {
                        return false;
                    }

                    return name.equals(player.getName());
                }
            }
        }
        return false;
    }

    private static boolean isFormat(String s) {
        String regular = filter(Config.BOUND.Format).replace("%player%", "(\\S+)");
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(filter(s));

        return matcher.matches();
    }

    private static String getFormatPlayer(String s) {
        String regular = filter(Config.BOUND.Format).replace("%player%", "(\\S+)");
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(filter(s));

        if (matcher.find()) return matcher.group();

        return null;
    }

    private static String getPlayer(List<String> lore) {
        int index = lore.indexOf(Config.BOUND.AfterBind_1);
        String name = lore.get(index + 1);

        String regular = filter(Config.BOUND.AfterBind_2).replace("%player%", "(\\S+)");
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(filter(name));

        if (matcher.find()) return matcher.group();

        return null;
    }

    private static String filter(String s) {
        s = ChatColor.stripColor(s.replace(" ", ""));
        String regEx = "[`~!@#$^&*()\\-+={}':;,\\[\\].<>/?￥…（）+|【】‘；：”“’。，、？\\s]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }

    private static void clear(List<String> lore) {
        lore.remove(Config.BOUND.BindOnUse);
        lore.remove(Config.BOUND.BindOnPickup);
        lore.remove(Config.BOUND.BindOnEquip);
    }
}
