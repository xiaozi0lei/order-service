package com.hckk.sgl.orderservice.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Sun Guolei 2018/6/21 20:02
 */
public class NetUtils {
    /**
     * 获取机器所有网卡的IP（ipv4）
     *
     * @return String 内网 ip 和主机名
     */
    // 获取 linux 机器的内网 IP 地址
    public static String getHostNameAndInternalIP() {
        InetAddress ip;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (null == ip || "".equals(ip.toString())) {
                        continue;
                    }
                    String sIP = ip.getHostAddress();
                    if (sIP == null || sIP.contains(":")) {
                        continue;
                    }
                    if (sIP.contains("127.0.0.1")) {
                        continue;
                    }
                    String nameAndIp = "主机名：" + ip.getHostName() + "\nip地址：" + sIP;
                    System.out.println(nameAndIp);
                    return nameAndIp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
