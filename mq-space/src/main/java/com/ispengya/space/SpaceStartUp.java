package com.ispengya.space;

import cn.hutool.core.util.StrUtil;
import com.ispengya.mq.util.AllUtil;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class SpaceStartUp {
    public static final String SPACE_CONF_PROPERTIES_PATH = "SPACE_CONF_PROPERTIES_PATH";
    private static final Logger log = LoggerFactory.getLogger(SpaceStartUp.class);
    private static Properties properties = null;


    public static void main( String[] args ) throws SimpleServerException {
        SpaceController spaceController = null;
        try {
            //创建控制器
            spaceController = createSpaceController();
            //初始化 server、线程池、处理器
            spaceController.init();
            //启动
            spaceController.start();
        } catch (Exception e) {
            log.error("mq space start failed", e);
            spaceController.shutdown();
        }
    }

    public static SpaceController createSpaceController() throws IOException {
        String config_path = System.getProperty(SPACE_CONF_PROPERTIES_PATH);
        //server config
        final ServerConfig serverConfig = new ServerConfig();
        serverConfig.setListenPort(9876);
        if (StrUtil.isNotBlank(config_path)) {
            log.warn("Space config path is empty");
            InputStream in = new BufferedInputStream(new FileInputStream(config_path));
            properties = new Properties();
            properties.load(in);
            AllUtil.properties2Object(properties, serverConfig);
        }
        //创建 spaceController
        return new SpaceController(serverConfig);
    }
}
