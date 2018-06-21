package com.hckk.sgl.orderservice.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    /**
     * 判断时间是否在时间段内
     *
     * @param date         当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd   结束时间 00:05:00
     * @return 返回时间是否在规定时间呢
     */
    public static boolean betweenTime(LocalDateTime date, String strDateBegin, String strDateEnd) {
        String strDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 截取当前时间时分秒 转成整型
        int tempDate = Integer.parseInt(strDate.substring(11, 13) + strDate.substring(14, 16) + strDate.substring(17, 19));
        // 截取开始时间时分秒  转成整型
        int tempDateBegin = Integer.parseInt(strDateBegin.substring(0, 2) + strDateBegin.substring(3, 5) + strDateBegin.substring(6, 8));
        // 截取结束时间时分秒  转成整型
        int tempDateEnd = Integer.parseInt(strDateEnd.substring(0, 2) + strDateEnd.substring(3, 5) + strDateEnd.substring(6, 8));

        return (tempDate >= tempDateBegin && tempDate <= tempDateEnd);
    }
}
