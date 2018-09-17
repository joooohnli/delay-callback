package com.johnli.callback.server.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author johnli  2018-08-10 10:09
 */
public class OSUtil {

    private static String serverIp = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(OSUtil.class);


    public static String getServerIp() {
        if (StringUtils.isNoneBlank(serverIp)) {
            return serverIp;
        }
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
            return serverIp;
        } catch (Exception e) {
            LOGGER.warn("Failed to get ip address", e);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                if (interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    serverIp = addresses.nextElement().getHostAddress();
                    return serverIp;
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
