package com.ispengya.server;

import com.ispengya.server.common.exception.RemotingCommandException;
import com.sun.istack.internal.Nullable;

class RequestHeader implements CommandCustomHeader {
        @Nullable
        private Integer count;

        @Nullable
        private String messageTitle;

        @Override
        public void checkFields() throws RemotingCommandException {
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }
    }