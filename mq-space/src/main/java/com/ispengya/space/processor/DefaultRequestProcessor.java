package com.ispengya.space.processor;

import com.ispengya.mq.constant.RequestCode;
import com.ispengya.mq.header.RegisterBrokerRequestHeader;
import com.ispengya.server.SimpleServerProcessor;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.common.util.SimpleServerUtil;
import com.ispengya.server.procotol.SimpleServerTransContext;
import com.ispengya.space.SpaceController;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 17:23
 **/
public class DefaultRequestProcessor implements SimpleServerProcessor {

    private static final Logger log = LoggerFactory.getLogger(DefaultRequestProcessor.class);
    protected final SpaceController spaceController;

    public DefaultRequestProcessor(SpaceController spaceController) {
        this.spaceController = spaceController;
    }


    @Override
    public SimpleServerTransContext processRequest(ChannelHandlerContext chc, SimpleServerTransContext request) throws Exception {
        if (chc != null) {
            log.debug("receive request, {} {} {}",
                    request.getProcessCode(),
                    SimpleServerUtil.parseChannelRemoteAddr(chc.channel()),
                    request);
        }
        switch (request.getProcessCode()) {
            case RequestCode.REGISTER_BROKER:
                return this.registerBroker(chc, request);
            default:
                break;
        }
        return null;
    }

    private SimpleServerTransContext registerBroker(ChannelHandlerContext chc, SimpleServerTransContext request) throws SimpleServerException {
        RegisterBrokerRequestHeader customHeader = (RegisterBrokerRequestHeader) request.decodeSSTCustomHeader(RegisterBrokerRequestHeader.class);
        this.spaceController.getRouteInfoManager().registerBroker(customHeader.getBrokerAddr(), customHeader.getBrokerName(),customHeader.getBrokerId(),chc.channel());
        return request;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
