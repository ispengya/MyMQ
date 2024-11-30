package com.ispengya.mq;

import java.util.HashMap;

public class KVTable {
    private HashMap<String, String> table = new HashMap<String, String>();

    public HashMap<String, String> getTable() {
        return table;
    }

    public void setTable(HashMap<String, String> table) {
        this.table = table;
    }
}