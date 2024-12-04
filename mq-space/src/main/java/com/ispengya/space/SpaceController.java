package com.ispengya.space;

import com.ispengya.mq.util.ThreadFactoryImpl;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.server.ServerConfig;
import com.ispengya.server.netty.server.SimpleServer;
import com.ispengya.space.processor.BrokerHousekeepingService;
import com.ispengya.space.processor.DefaultRequestProcessor;
import com.ispengya.space.processor.RouteInfoManager;
import com.ispengya.space.processor.SpaceConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 16:47
 **/
public class SpaceController {
    private SimpleServer server;
    private final ServerConfig serverConfig;
    private ExecutorService spaceProcessorExecutor;
    private final RouteInfoManager routeInfoManager;
    private BrokerHousekeepingService  brokerHousekeepingService;
    private final SpaceConfig spaceConfig;

    public SpaceController(ServerConfig serverConfig, SpaceConfig spaceConfig) {
        this.serverConfig = serverConfig;
        this.spaceConfig = spaceConfig;
        this.routeInfoManager = new RouteInfoManager();
    }

    public void init() {
        this.server = new SimpleServer(serverConfig);
        this.spaceProcessorExecutor = Executors.newFixedThreadPool(serverConfig.getServerWorkerThreads(), new ThreadFactoryImpl("SpaceProcessorExecutorThread_"));
        this.brokerHousekeepingService = new BrokerHousekeepingService(this);
        this.registerProcessor();
    }

    public void start() throws SimpleServerException {
        this.server.start();
    }

    public void shutdown() throws SimpleServerException {
        this.server.stop();
        this.spaceProcessorExecutor.shutdown();
    }

    private void registerProcessor() {
        this.server.registerDefaultProcessor(new DefaultRequestProcessor(this), this.spaceProcessorExecutor);
    }

    public RouteInfoManager getRouteInfoManager() {
        return routeInfoManager;
    }
}
