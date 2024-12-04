package com.ispengya.space.processor;

import com.ispengya.mq.util.AllUtil;

import java.io.File;

public class SpaceConfig {
    private String mqHome = System.getProperty(AllUtil.MYMQ_HOME_PROPERTY, System.getenv(AllUtil.MYMQ_HOME_ENV));
    private String configStorePath = System.getProperty("user.home") + File.separator + "namesrv" + File.separator + "namesrv.properties";
    private boolean orderMessageEnable = false;

    public boolean isOrderMessageEnable() {
        return orderMessageEnable;
    }

    public void setOrderMessageEnable(boolean orderMessageEnable) {
        this.orderMessageEnable = orderMessageEnable;
    }

    public String getMqHome() {
        return mqHome;
    }

    public void setMqHome(String mqHome) {
        this.mqHome = mqHome;
    }

    public String getConfigStorePath() {
        return configStorePath;
    }

    public void setConfigStorePath(final String configStorePath) {
        this.configStorePath = configStorePath;
    }
}
