package com.ispengya.core;

import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 *
 */
public class MQCoreStartUp {

    private static final Logger log = LoggerFactory.getLogger(MQCoreStartUp.class);
    public static final String MQCORE_CONF_PROPERTIES_PATH = "MQCORE_CONF_PROPERTIES_PATH";
    private static Properties properties;

    public static void main(String[] args ) {
        MQCoreController mqCoreController = null;
        try {
            mqCoreController = createMQCoreController();
            mqCoreController.start();
        } catch (Exception e) {
            log.error("mq core start failed", e);
            System.exit(1);
        }
    }

    public static MQCoreController createMQCoreController() throws IOException {
        String config_path = System.getProperty(MQCORE_CONF_PROPERTIES_PATH);
//        if (StrUtil.isBlank(config_path)) {
//            log.error("Space config path is empty");
//            System.exit(1);
//        }

        //client config
        final ClientConfig clientConfig = new ClientConfig();
//        InputStream in = new BufferedInputStream(new FileInputStream(config_path));
//        properties = new Properties();
//        properties.load(in);
//        IOUtil.properties2Object(properties, clientConfig);

        final MQCoreController mqCoreController = new MQCoreController(clientConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            private AtomicInteger shutdownTimes = new AtomicInteger(0);

            @Override
            public void run() {
                synchronized (this) {
                    log.info("Shutdown hook was invoked, {}", this.shutdownTimes.incrementAndGet());
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                        long beginTime = System.currentTimeMillis();
                        try {
                            mqCoreController.shutdown();
                        } catch (SimpleServerException e) {
                            log.error("mq core shutdown failed", e);
                        }
                        long consumingTimeTotal = System.currentTimeMillis() - beginTime;
                        log.info("Shutdown hook over, consuming total time(ms): {}", consumingTimeTotal);
                    }
                }
            }
        }, "ShutdownHook"));

        //创建 MQCoreController
        return mqCoreController;
    }
}
