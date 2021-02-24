package com.gavin.app.net;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static org.apache.dubbo.common.utils.NetUtils.isAnyHost;
import static org.apache.dubbo.common.utils.NetUtils.isLocalHost;

/**
 * 获取本机IP等网络操作
 * 可以看一下NetUtils里面的封装操作
 *
 * @author: Gavin
 * @date: 2021/2/24 17:54
 * @description:
 */
public class LocalHost {
    private static final Log log = LogFactory.getLog(LocalHost.class);

    public static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static NetworkInterface localNetworkInterface;
    private static String hostName;

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

    /**
     * 获取有效的IP地址
     *
     * @return
     */
    public static InetAddress getValidAddress() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidLocalAddress(localAddress)) {
                return localAddress;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = (NetworkInterface) interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = (InetAddress) addresses.nextElement();
                                    if (isValidLocalAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable var5) {
                                    log.warn("Error when retriving ip address: " + var5.getMessage(), var5);
                                }
                            }
                        }
                    } catch (Throwable var7) {
                        log.warn("Error when retriving ip address: " + var7.getMessage(), var7);
                    }
                }
            }
        } catch (Throwable var8) {
            log.warn("Error when retriving ip address: " + var8.getMessage(), var8);
        }

        log.error("Can't get valid host, will use 127.0.0.1 instead.");
        return localAddress;
    }

    public static boolean isValidLocalAddress(InetAddress inetAddress) {
        if (inetAddress != null && !inetAddress.isLoopbackAddress()) {
            String name = inetAddress.getHostAddress();
            return name != null && !isAnyHost(name) && !isLocalHost(name) && isIPv4Host(name);
        }
        return false;
    }

    public static boolean isIPv4Host(String host) {
        return StringUtils.isNotBlank(host) && IPV4_PATTERN.matcher(host).matches();
    }


    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getMachineName());
        System.out.println(getLocalAddress());
    }
}
