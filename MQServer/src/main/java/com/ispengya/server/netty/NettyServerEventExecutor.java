package com.ispengya.server.netty;

import com.ispengya.server.ChannelEventListener;
import com.ispengya.server.common.thread.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.ClassFileTransformer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.ispengya.server.common.constant.MQNettyAllCode.*;

public class NettyServerEventExecutor extends ServiceThread {
    private static final Logger log = LoggerFactory.getLogger(NettyServerEventExecutor.class);
    private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
    private final int maxSize = 10000;
    private ChannelEventListener channelEventListener;
    protected volatile boolean stopped = false;

    public void putNettyEvent(final NettyEvent event) {
        if (this.eventQueue.size() <= maxSize) {
            this.eventQueue.add(event);
        } else {
            log.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(), event.toString());
        }
    }

    @Override
    public void run() {
        log.info(NettyServerEventExecutor.class.getSimpleName() + " service started");

        final ChannelEventListener listener = this.channelEventListener;

        while (!this.stopped) {
            try {
                NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                if (event != null && listener != null) {
                    switch (event.getType()) {
                        case IDLE:
                            listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                            break;
                        case ClOSE:
                            listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CONNECT:
                            listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                            break;
                        case EXCEPTION:
                            listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                            break;
                        default:
                            break;

                    }
                }
            } catch (Exception e) {
                log.warn(NettyServerEventExecutor.class.getSimpleName() + " service has exception. ", e);
            }
        }

        log.info(NettyServerEventExecutor.class.getSimpleName() + " service end");
    }

    @Override
    public String getServiceName() {
        return NettyServerEventExecutor.class.getSimpleName();
    }

    public void setChannelEventListener(ChannelEventListener channelEventListener) {
        this.channelEventListener = channelEventListener;
    }
}