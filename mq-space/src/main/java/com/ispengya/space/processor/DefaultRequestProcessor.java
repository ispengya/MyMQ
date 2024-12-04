package com.ispengya.space.processor;

import com.ispengya.mq.body.TopicConfigBody;
import com.ispengya.mq.body.TopicRouteData;
import com.ispengya.mq.constant.RequestCode;
import com.ispengya.mq.constant.ResponseCode;
import com.ispengya.mq.core.DataVersion;
import com.ispengya.mq.header.req.GetRouteInfoRequestHeader;
import com.ispengya.mq.header.req.QueryDataVersionRequestHeader;
import com.ispengya.mq.header.req.RegisterBrokerRequestHeader;
import com.ispengya.mq.header.req.UnRegisterBrokerRequestHeader;
import com.ispengya.mq.header.resp.QueryDataVersionResponseHeader;
import com.ispengya.mq.util.MQSerializer;
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
                return registerBroker(chc, request);
            case RequestCode.QUERY_DATA_VERSION:
                return getDataVersion(request);
            case RequestCode.UNREGISTER_BROKER:
                return unregisterBroker(chc, request);
            case RequestCode.GET_ROUTEINTO_BY_TOPIC:
                return getRouteInfoByTopic(chc, request);
            default:
                break;
        }
        return null;
    }

    private SimpleServerTransContext getRouteInfoByTopic(ChannelHandlerContext chc, SimpleServerTransContext request) throws SimpleServerException {
        SimpleServerTransContext response = SimpleServerTransContext.createResponseSST(null);
        GetRouteInfoRequestHeader requestHeader = (GetRouteInfoRequestHeader) request.decodeCustomHeaderOfSST(GetRouteInfoRequestHeader.class);
        TopicRouteData topicRouteData = this.spaceController.getRouteInfoManager().pickupTopicRouteData(requestHeader.getTopic());

        if (topicRouteData != null) {
            byte[] content = MQSerializer.encode(topicRouteData);
            response.setBody(content);
            response.setStatusCode(ResponseCode.SUCCESS);
            response.setRemark(null);
            return response;
        }
        response.setStatusCode(ResponseCode.TOPIC_NOT_EXIST);
        response.setRemark("No topic route info in name server for the topic: " + requestHeader.getTopic());
        return response;
    }


    private SimpleServerTransContext unregisterBroker(ChannelHandlerContext ctx,
                                                      SimpleServerTransContext request) throws SimpleServerException {
        SimpleServerTransContext response = SimpleServerTransContext.createResponseSST(null);
        UnRegisterBrokerRequestHeader requestHeader =
                (UnRegisterBrokerRequestHeader) request.decodeCustomHeaderOfSST(UnRegisterBrokerRequestHeader.class);

        this.spaceController.getRouteInfoManager().unregisterBroker(
                requestHeader.getBrokerAddr(),
                requestHeader.getBrokerName(),
                requestHeader.getBrokerId());

        response.setStatusCode(ResponseCode.SUCCESS);
        response.setRemark(null);
        return response;
    }

    private SimpleServerTransContext getDataVersion(SimpleServerTransContext request) throws SimpleServerException {
        SimpleServerTransContext response = SimpleServerTransContext.createResponseSST(QueryDataVersionResponseHeader.class);
        QueryDataVersionResponseHeader responseHeader = (QueryDataVersionResponseHeader) response.readCustomHeader();
        QueryDataVersionRequestHeader requestHeader = ((QueryDataVersionRequestHeader) request.decodeCustomHeaderOfSST(QueryDataVersionRequestHeader.class));
        DataVersion dataVersion = MQSerializer.decode(request.getBody(), DataVersion.class);
        boolean changed = this.spaceController.getRouteInfoManager().isBrokerTopicConfigChanged(requestHeader.getBrokerAddr(), dataVersion);
        if (!changed) {
            this.spaceController.getRouteInfoManager().updateBrokerInfoUpdateTimestamp(requestHeader.getBrokerAddr());
        }

        DataVersion nameSeverDataVersion = this.spaceController.getRouteInfoManager().queryBrokerTopicConfig(requestHeader.getBrokerAddr());
        response.setStatusCode(ResponseCode.SUCCESS);
        if (nameSeverDataVersion != null) {
            response.setBody(MQSerializer.encode(nameSeverDataVersion));
        }
        responseHeader.setChanged(changed);
        return response;
    }

    private SimpleServerTransContext registerBroker(ChannelHandlerContext chc, SimpleServerTransContext request) throws SimpleServerException {
        //resolve header
        RegisterBrokerRequestHeader customHeader = (RegisterBrokerRequestHeader) request.decodeCustomHeaderOfSST(RegisterBrokerRequestHeader.class);
        TopicConfigBody topicConfigBody = new TopicConfigBody();
        //resolve body
        if (request.getBody() !=null) {
            topicConfigBody = MQSerializer.decode(request.getBody(), TopicConfigBody.class);
        }
        this.spaceController.getRouteInfoManager().registerBroker(customHeader.getBrokerAddr(),
                customHeader.getBrokerName(),
                customHeader.getBrokerId(),
                topicConfigBody,
                chc.channel());
        return request;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
