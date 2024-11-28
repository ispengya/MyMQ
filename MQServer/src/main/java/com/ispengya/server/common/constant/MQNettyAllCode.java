package com.ispengya.server.common.constant;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-27 20:49
 **/
public class MQNettyAllCode {
    //远程处理系统响应代码
    public static final int SUCCESS = 0;
    public static final int SYSTEM_ERROR = 1;
    public static final int SYSTEM_BUSY = 2;
    public static final int REQUEST_CODE_NOT_SUPPORTED = 3;
    public static final int TRANSACTION_FAILED = 4;


    //处理事件类型
    public static final int CONNECT = 1000;
    public static final int ClOSE = 1001;
    public static final int EXCEPTION = 1002;
    public static final int IDLE = 1003;
}
