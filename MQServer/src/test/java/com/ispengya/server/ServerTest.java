package com.ispengya.server;

import com.ispengya.server.netty.NettyRemotingServer;
import com.ispengya.server.netty.NettyServerConfig;
import com.ispengya.server.procotol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-28 16:36
 **/
public class ServerTest {

    public static NettyRemotingServer createRemotingServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        NettyRemotingServer remotingServer = new NettyRemotingServer(config);
        remotingServer.registerProcessor(0, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
                request.setRemark("Hi " + ctx.channel().remoteAddress());
                return request;
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        }, Executors.newCachedThreadPool());

        remotingServer.start();

        return remotingServer;
    }

    public static void main(String[] args) throws InterruptedException {
        createRemotingServer();
    }
}
