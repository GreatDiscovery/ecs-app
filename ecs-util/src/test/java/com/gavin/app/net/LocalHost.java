package com.gavin.app.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 获取本机IP
 *
 * @author: Gavin
 * @date: 2021/2/24 17:54
 * @description:
 */
public class LocalHost {
    private static final Log log = LogFactory.getLog(LocalHost.class);
    private static NetworkInterface localNetworkInterface;
    private static String hostName;

    public LocalHost() {
    }

    /**
     * 获取本机网络接口
     *
     * @return
     */
    public static NetworkInterface getLocalNetworkInterface() {
        return localNetworkInterface;
    }

    /**
     * 获取本机IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取hostname
     *
     * @return
     */
    public static String getMachineName() {
        return hostName;
    }

    static {
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if (!networkInterface.getName().equals("lo")) {
                    localNetworkInterface = networkInterface;
                    break;
                }
            }
        } catch (SocketException var3) {
            log.error("init LocalHost error!", var3);
        }

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var2) {
            log.error("init hostname error!", var2);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getMachineName());
        System.out.println(getLocalAddress());
    }
}
