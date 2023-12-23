package com.zh.controlcenter.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zm
 */
@Slf4j
public class IpHelper {
    /**
     * 通过HttpServletRequest返回IP地址
     *
     * @return ip String
     * @throws Exception
     */
    public static final String LOCALHOST_IPV4 = "127.0.0.1";

    public static final String LOCALHOST = "localhost";

    public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private static final String UNKNOWN = "unknown";

    public static String getIpAddress(ServerHttpRequest req) {
        String ip = IpHelper.getFirst(req.getHeaders().get("x-forwarded-for"));
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            InetSocketAddress remoteAddress = req.getRemoteAddress();
            if (remoteAddress != null) {
                ip = remoteAddress.getHostString();
            }
        }
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        String comma = ",";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        return StringUtils.containsAnyIgnoreCase(ip, LOCALHOST_IPV4, LOCALHOST, LOCALHOST_IPV6, UNKNOWN) ? null : ip;
    }

    public static String getFirst(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}