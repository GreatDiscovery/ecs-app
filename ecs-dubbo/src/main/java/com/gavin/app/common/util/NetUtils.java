package com.gavin.app.common.util;

import com.gavin.app.common.CommonConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: Gavin
 * @date: 2021/7/6 10:24
 * @description:
 */
public class NetUtils {

    public static final int MIN_PORT = 0;
    public static final int MAX_PORT = 65535;

    // returned port range is [30000, 39999]
    private static final int RND_PORT_START = 30000;
    private static final int RND_PORT_RANGE = 10000;

    /**
     * 非本地IP
     *
     * @param host
     * @return
     */
    public static boolean isInvalidLocalHost(String host) {
        return host == null
                || host.length() == 0
                || host.equalsIgnoreCase(CommonConstants.LOCALHOST_KEY)
                || host.equals(CommonConstants.ANYHOST_VALUE)
                || host.startsWith("127.");
    }

    public static boolean isInvalidPort(int port) {
        return port <= MIN_PORT || port >= MAX_PORT;
    }

    public static int getAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(null);
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            return getRandomPort();
        }
    }

    public static int getAvailablePort(int port) {
        for (int i = port; i < MAX_PORT; i++) {
            try (ServerSocket ss = new ServerSocket(i)) {
                return i;
            } catch (Exception e) {

            }
        }
        return port;
    }

    public static int getRandomPort() {
        return RND_PORT_START + ThreadLocalRandom.current().nextInt(RND_PORT_RANGE);
    }

    public static void main(String[] args) {
        System.out.println(getAvailablePort(49242));
    }
}
