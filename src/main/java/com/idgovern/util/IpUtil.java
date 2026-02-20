package com.idgovern.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for IP address processing.
 *
 * <p>
 * Provides methods to retrieve and validate client IP addresses and server IP addresses.
 * Handles common scenarios including reverse proxy headers and localhost mappings.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Prefer the X-Real-IP or X-Forwarded-For headers for client IP resolution.</li>
 *     <li>Fall back to {@link HttpServletRequest#getRemoteAddr()} if headers are missing.</li>
 *     <li>Strip multiple IPs from comma-separated values (reverse proxy cases).</li>
 *     <li>Validate IP addresses against standard IPv4 format using regular expressions.</li>
 *     <li>Provide methods to extract the last segment of an IP address for business logic needs.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class IpUtil {

    public final static String ERROR_IP = "127.0.0.1";

    public final static Pattern pattern = Pattern.
            compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");


    /**
     * Retrieve the client's IP address, accounting for reverse proxy headers.
     *
     * @param request the HTTP request
     * @return the client IP as a string
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        // Filter reverse proxy IPs
        String[] stemps = ip.split(",");
        if (stemps != null && stemps.length >= 1) {
            //得到第一个IP，即客户端真实IP
            ip = stemps[0];
        }

        ip = ip.trim();
        if (ip.length() > 23) {
            ip = ip.substring(0, 23);
        }

        return ip;
    }

    /**
     * Retrieve the real IP address of the user, considering X-Real-IP and X-Forwarded-For headers.
     *
     * @param request the HTTP request
     * @return the user IP
     */
    public static String getUserIP(HttpServletRequest request) {

        // 优先取X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = ERROR_IP;
            }
        }

        if ("unknown".equalsIgnoreCase(ip)) {
            ip = ERROR_IP;
            return ip;
        }

        int pos = ip.indexOf(',');
        if (pos >= 0) {
            ip = ip.substring(0, pos);
        }

        return ip;
    }


    /**
     * Get the last segment of the user's IP (e.g., for sharding or business logic).
     *
     * @param request the HTTP request
     * @return last segment of IP as string
     */
    public static String getLastIpSegment(HttpServletRequest request) {
        String ip = getUserIP(request);
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }


    /**
     * Validate if the client IP is a standard IPv4 address.
     *
     * @param request the HTTP request
     * @return true if valid, false otherwise
     */
    public static boolean isValidIP(HttpServletRequest request) {
        String ip = getUserIP(request);
        return isValidIP(ip);
    }


    /**
     * Validate if the given IP string is a standard IPv4 address.
     *
     * @param ip the IP string
     * @return true if valid, false otherwise
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            log.debug("ip is null. valid result is false");
            return false;
        }

        Matcher matcher = pattern.matcher(ip);
        boolean isValid = matcher.matches();
        log.debug("valid ip:" + ip + " result is: " + isValid);
        return isValid;
    }


    /**
     * Get the last segment of the server IP.
     *
     * @return last segment of server IP
     */
    public static String getLastServerIpSegment() {
        String ip = getServerIP();
        if (ip != null) {
            ip = ip.substring(ip.lastIndexOf('.') + 1);
        } else {
            ip = "0";
        }
        return ip;
    }


    /**
     * Get the IP address of the local server.
     *
     * @return server IP, or 127.0.0.1 if unknown
     */
    public static String getServerIP() {
        InetAddress inet;
        try {
            inet = InetAddress.getLocalHost();
            String hostAddress = inet.getHostAddress();
            return hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}