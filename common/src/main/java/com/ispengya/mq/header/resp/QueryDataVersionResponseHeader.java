package com.ispengya.mq.header.resp;

import com.ispengya.server.CustomHeader;

public class QueryDataVersionResponseHeader implements CustomHeader {
    private Boolean changed;

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryDataVersionResponseHeader{");
        sb.append("changed=").append(changed);
        sb.append('}');
        return sb.toString();
    }
}