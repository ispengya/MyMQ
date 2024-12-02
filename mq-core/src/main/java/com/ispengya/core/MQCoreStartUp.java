package com.ispengya.core;

import cn.hutool.core.util.StrUtil;
import com.ispengya.core.config.BrokerConfig;
import com.ispengya.mq.util.AllUtil;
import com.ispengya.server.common.exception.SimpleServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 *
 */
public class MQCoreStartUp {

    private static final Logger log = LoggerFactory.getLogger(MQCoreStartUp.class);
    public static final String CONFIG_HOME = "CONFIG_HOME";
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
        //must be set
        String configHomePath = System.getenv(CONFIG_HOME);
        if (StrUtil.isBlank(configHomePath)) {
            log.error("config home path is empty");
            System.exit(1);
        }


        //broker config
        final BrokerConfig brokerConfig = new BrokerConfig();
        InputStream in = new BufferedInputStream(new FileInputStream(configHomePath+"/broker.conf"));
        properties = new Properties();
        properties.load(in);
        AllUtil.properties2Object(properties, brokerConfig);


        final MQCoreController mqCoreController = new MQCoreController(brokerConfig);

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
