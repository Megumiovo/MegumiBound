package com.taylorswiftcn.megumi.bound.database.mysql;

import com.taylorswiftcn.megumi.bound.file.sub.Config;

import java.util.LinkedList;

public class MysqlPool {
    private LinkedList<MysqlHandler> pools;
    private int min;
    private int max;

    public MysqlPool() {
        this.pools = new LinkedList<>();
        this.min = 1;
        this.max = 5;
    }

    public void init() {
        pools.clear();
        for (int i = min; i <= max; i++) {
            pools.add(new MysqlHandler(Config.SQL.Host, Config.SQL.Port, Config.SQL.Database, Config.SQL.Users, Config.SQL.Password));
        }
    }

    public MysqlHandler getHandler() {
        if (pools.size() == 0) return new MysqlHandler(Config.SQL.Host, Config.SQL.Port, Config.SQL.Database, Config.SQL.Users, Config.SQL.Password);
        return pools.remove(0);
    }

    public void recover(MysqlHandler handler) {
        if (pools.size() >= max) return;
        pools.add(handler);
    }
}
