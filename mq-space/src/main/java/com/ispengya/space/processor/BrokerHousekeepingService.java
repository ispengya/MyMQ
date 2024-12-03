package com.ispengya.space.processor;

import com.ispengya.server.ChannelEventListener;
import com.ispengya.space.SpaceController;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrokerHousekeepingService implements ChannelEventListener {

    private static final Logger log = LoggerFactory.getLogger(BrokerHousekeepingService.class);
    private final SpaceController spaceController;

    public BrokerHousekeepingService(SpaceController spaceController) {
        this.spaceController = spaceController;
    }

    @Override
    public void onChannelConnect(String remoteAddr, Channel channel) {
    }

    @Override
    public void onChannelClose(String remoteAddr, Channel channel) {
        this.spaceController.getRouteInfoManager().onChannelDestroy(remoteAddr, channel);
    }

    @Override
    public void onChannelException(String remoteAddr, Channel channel) {
        this.spaceController.getRouteInfoManager().onChannelDestroy(remoteAddr, channel);
    }

    @Override
    public void onChannelIdle(String remoteAddr, Channel channel) {
        this.spaceController.getRouteInfoManager().onChannelDestroy(remoteAddr, channel);
    }
}
