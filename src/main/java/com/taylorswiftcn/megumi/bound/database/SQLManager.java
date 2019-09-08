package com.taylorswiftcn.megumi.bound.database;

import com.taylorswiftcn.megumi.bound.MegumiBound;
import com.taylorswiftcn.megumi.bound.database.mysql.MysqlHandler;
import com.taylorswiftcn.megumi.bound.database.mysql.MysqlPool;
import com.taylorswiftcn.megumi.bound.file.sub.Config;
import com.taylorswiftcn.megumi.bound.file.sub.Message;
import com.taylorswiftcn.megumi.bound.gui.RecallItem;
import com.taylorswiftcn.megumi.bound.util.ItemConvertUtil;
import com.taylorswiftcn.megumi.bound.util.ItemTagUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLManager {

    private MysqlPool pool;
    private ItemConvertUtil itemConvert;

    public SQLManager(MegumiBound plugin) {
        this.pool = new MysqlPool();
        this.itemConvert = new ItemConvertUtil(plugin.getVersion());
    }

    public void init() {
        pool.init();

        String table = String.format("CREATE TABLE IF NOT EXISTS %s (id int NOT NULL AUTO_INCREMENT PRIMARY KEY,uuid VARCHAR(40) NOT NULL, player VARCHAR(20) NOT NULL, time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, item text NOT NULL) ENGINE = InnoDB", Config.SQL.Table);

        MysqlHandler sql = pool.getHandler();
        sql.openConnection();
        sql.updateSQL(table);
        sql.closeConnection();
        pool.recover(sql);
    }

    public void addItem(ItemStack item) {
        String uuid = ItemTagUtil.getTag(item);
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

        String base64Item = itemConvert.convert(item);

        String insert = String.format("INSERT INTO %s (uuid, player, item) VALUES ('%s', '%s', '%s')", Config.SQL.Table, uuid, player.getName(), base64Item);

        MysqlHandler sql = pool.getHandler();
        sql.openConnection();
        sql.updateSQL(insert);
        sql.closeConnection();
        pool.recover(sql);

        if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            p.sendMessage(Config.Prefix + Message.ItemOnRecall);
        }
    }

    public void remove(int id) {
        String delete = String.format("DELETE FROM %s WHERE id = '%s'", Config.SQL.Table, id);

        MysqlHandler sql = pool.getHandler();
        sql.openConnection();
        sql.updateSQL(delete);
        sql.closeConnection();
        pool.recover(sql);
    }

    public List<RecallItem> getRecallItems(UUID uuid) {
        List<RecallItem> items = new ArrayList<>();

        String select = String.format("SELECT * FROM %s WHERE uuid = '%s'", Config.SQL.Table, uuid.toString());

        MysqlHandler sql = pool.getHandler();
        try {
            sql.openConnection();
            ResultSet set = sql.querySQL(select);
            while (set.next()) {
                int id = set.getInt("id");
                String base64Item = set.getString("item");

                items.add(new RecallItem(id, itemConvert.convert(base64Item)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sql.closeConnection();
            pool.recover(sql);
        }

        return items;
    }
}
