package com.hckk.sgl.orderservice.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sun Guolei 2018/6/21 20:11
 */
public class StringUtils {
    //去掉字符串中的的空格、回车、换行符、制表符
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
