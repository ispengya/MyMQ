package com.ispengya.server;

import com.ispengya.server.netty.ResponseFuture;

public interface InvokeCallback {
    void operationComplete(final ResponseFuture responseFuture);
}