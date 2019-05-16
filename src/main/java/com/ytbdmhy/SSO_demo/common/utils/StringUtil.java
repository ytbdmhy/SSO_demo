package com.ytbdmhy.SSO_demo.common.utils;

import java.util.UUID;

/**
 * 字符串工具类
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return (str == null || str.equals(""));
    }

    public static boolean equals(String str1, String str2) {
        if (StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2)) {
            return true;
        } else if (!StringUtil.isEmpty(str1) && !StringUtil.isEmpty(str2)) {
            return str1.equals(str2);
        }
        return false;
    }

    /**
     * 生成uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
