package com.ispengya.server;

import com.ispengya.server.common.exception.RemotingCommandException;
import com.ispengya.server.netty.NettyClientConfig;
import com.ispengya.server.netty.NettyRemotingClient;
import com.ispengya.server.procotol.RemotingCommand;
import com.sun.istack.internal.Nullable;

import static org.junit.Assert.assertTrue;


/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-28 16:13
 **/
public class ClientTest {


    public static NettyRemotingClient createRemotingClient() {
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig());
        client.start();
        return client;
    }



    public static void main(String[] args) throws Exception {
        NettyRemotingClient remotingClient = createRemotingClient();
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setCount(1);
        requestHeader.setMessageTitle("Welcome");
        RemotingCommand request = RemotingCommand.createRequestCommand(0, requestHeader);
        RemotingCommand response = remotingClient.invokeSync("localhost:8888", request, 1000 * 3);
        assertTrue(response != null);
    }

}
