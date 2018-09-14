package com.johnli.callback.server.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 */
public class OSUtils {

    private static String serverIp = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(OSUtils.class);

    /**
     * 珍爱内网内网IP地址正则
     */
    private static final Pattern ZA_IP_PATTERN = Pattern.compile("10(\\.\\d{1,3}){3,5}$");

    /**
     * 判断是否有效的内网地址
     *
     * @param ip
     * @return
     */
    private static boolean isValidIP(String ip) {
        return ZA_IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 获取服务器IP地址
     *
     * @return
     */
    public static String getServerIp() {
        if (StringUtils.isNoneBlank(serverIp)) {
            return serverIp;
        }
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            if (isValidIP(hostAddress)) {
                serverIp = hostAddress;
                return serverIp;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to get ip address", e);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    if (addresses != null) {
                        while (addresses.hasMoreElements()) {
                            String hostAddress = addresses.nextElement().getHostAddress();
                            if (isValidIP(hostAddress)) {
                                serverIp = hostAddress;
                                return serverIp;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to get ip address", e);
        }

        serverIp = "127.0.0.1";
        LOGGER.error("Failed to get ip address, will use 127.0.0.1 instead.");
        return serverIp;
    }

}
