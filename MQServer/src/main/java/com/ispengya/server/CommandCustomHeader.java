package com.ispengya.server;

import com.ispengya.server.common.exception.RemotingCommandException;

public interface CommandCustomHeader {
    void checkFields() throws RemotingCommandException;
}