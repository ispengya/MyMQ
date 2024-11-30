package com.ispengya.space;

import cn.hutool.core.util.StrUtil;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        if (StrUtil.isBlank(config_path)) {
            log.error("Space config path is empty");
//            System.exit(1);
        }

        //server config
        final ServerConfig serverConfig = new ServerConfig();
//        InputStream in = new BufferedInputStream(new FileInputStream(config_path));
//        properties = new Properties();
//        properties.load(in);
//        IOUtil.properties2Object(properties, serverConfig);

        //创建 spaceController
        return new SpaceController(serverConfig);
    }
}
