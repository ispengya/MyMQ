package com.ispengya.mq.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 17:02
 **/
public class AllUtil {

    public static final String MYMQ_HOME_PROPERTY = "mymq.home.dir";
    public static final String MYMQ_HOME_ENV = "MYMQ_HOME";

    public static void properties2Object(final Properties p, final Object object) {
        IOUtil.properties2Object(p, object);
    }

    public static String getLocalAddress() {
        return NetworkUtil.getLocalAddress();
    }

    public static String normalizeHostAddress(final InetAddress localHost) {
        return NetworkUtil.normalizeHostAddress(localHost);
    }

    public static String file2String(final String fileName) throws IOException {
        return IOUtil.file2String(fileName);
    }

    public static String file2String(final File file) throws IOException {
        return IOUtil.file2String(file);
    }

    public static void string2File(final String str, final String fileName) throws IOException {
        IOUtil.string2File(str, fileName);
    }

    public static void string2FileNotSafe(final String str, final String fileName) throws IOException {
        IOUtil.string2FileNotSafe(str, fileName);
    }

    public static Properties object2Properties(final Object object) {
        return IOUtil.object2Properties(object);
    }

    public static String properties2String(final Properties properties) {
        return IOUtil.properties2String(properties);
    }

    public static void main(String[] args) {
        System.out.println(getLocalAddress());
    }
}
