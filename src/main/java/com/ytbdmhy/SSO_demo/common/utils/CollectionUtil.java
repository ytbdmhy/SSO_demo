package com.ytbdmhy.SSO_demo.common.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * 集合工具类
 */
public class CollectionUtil {

    /**
     * @param map 取值的集合
     * @param key 所想取值的集合的key
     * @return 返回key对应的value
     */
    public static String getMapValue(Map<String, Object> map, String key) {
        String result = null;
        if (map != null) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if (key.equals(object))
                    if (map.get(object) != null)
                        result = map.get(object).toString();
            }
        }
        return result;
    }
}
