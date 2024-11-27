package com.ispengya.server.netty;

import com.ispengya.server.procotol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-27 16:52
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        processMessageReceived(ctx, msg);
    }

    public void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        final RemotingCommand cmd = msg;
        if (cmd != null) {
            switch (cmd.getFlag()) {
                case 0:
                    processRequestCommand(ctx, cmd);
                    break;
                case 1:
                    processResponseCommand(ctx, cmd);
                    break;
                default:
                    break;
            }
        }
    }

    public void processRequestCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        System.out.println(cmd);
    }

    /**
     * Process response from remote peer to the previous issued requests.
     *
     * @param ctx channel handler context.
     * @param cmd response command instance.
     */
    public void processResponseCommand(ChannelHandlerContext ctx, RemotingCommand cmd) {
        System.out.println(cmd);
    }
}