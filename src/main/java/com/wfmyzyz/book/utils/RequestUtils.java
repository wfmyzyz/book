package com.wfmyzyz.book.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author admin
 */
public class RequestUtils {

    private static final String UNKNOWN = "unknown";
    private static final Integer MORE_IP_LENGTH = 15;
    private static final String SPLIT_CHAR = ",";

    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > MORE_IP_LENGTH) {
            // = 15
            if (ipAddress.indexOf(SPLIT_CHAR) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(SPLIT_CHAR));
            }
        }
        // 或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //return ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
        return ipAddress;
    }
}
