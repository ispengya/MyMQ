package com.ispengya.mq.config;

import com.ispengya.mq.util.AllUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);

    public boolean load() {
        String fileName = null;
        try {
            fileName = this.configFilePath();
            String jsonString = AllUtil.file2String(fileName);

            if (null == jsonString || jsonString.length() == 0) {
            } else {
                this.decode(jsonString);
                log.info("load " + fileName + " OK");
                return true;
            }
        } catch (Exception e) {
            log.error("load " + fileName + " failed, and try to load backup file", e);
        }
        return false;
    }

    public synchronized void persist() {
        String jsonString = this.encode(true);
        if (jsonString != null) {
            String fileName = this.configFilePath();
            try {
                AllUtil.string2File(jsonString, fileName);
            } catch (IOException e) {
                log.error("persist file " + fileName + " exception", e);
            }
        }
    }


    public abstract String encode();

    public abstract String configFilePath();

    public abstract void decode(final String jsonString);

    public abstract String encode(final boolean prettyFormat);
}
