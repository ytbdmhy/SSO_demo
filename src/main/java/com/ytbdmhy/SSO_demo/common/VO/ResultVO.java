package com.ytbdmhy.SSO_demo.common.VO;

import com.ytbdmhy.SSO_demo.common.Enums.ResultEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class ResultVO implements Serializable {

    private static final long serialVersionUID = 2325033580232310079L;

    /**
     * 返回msg, object, 以及token
     * 返回的code为默认
     * @param message
     * @param data
     * @param jwtToken
     * @param success
     * @return
     */
    public final static Map<String, Object> success(String message, Object data, String jwtToken, Boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken", jwtToken);
        map.put("code", ResultEnum.SUCCESS.getCode());
        map.put("message", message);
        map.put("success", success);
        map.put("data", data);
        return map;
    }

    /**
     * 返回object, 以及token
     * 返回msg, code为默认
     * @param data
     * @param jwtToken
     * @return
     */
    public final static Map<String, Object> success(Object data, String jwtToken) {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken", jwtToken);
        map.put("code", ResultEnum.SUCCESS.getCode());
        map.put("message", ResultEnum.SUCCESS.getMessage());
        map.put("success", true);
        map.put("data", data);
        return map;
    }

    /**
     * 返回默认的信息
     * @return
     */
    public final static Map<String, Object> success() {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken", null);
        map.put("code", ResultEnum.SUCCESS.getCode());
        map.put("message", ResultEnum.SUCCESS.getMessage());
        map.put("success", true);
        map.put("data", null);
        return map;
    }

    public final static Map<String, Object> failure(int code, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        map.put("success", false);
        map.put("data", data);
        return map;
    }

    public final static Map<String, Object> failure(int code, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        map.put("success", false);
        map.put("data", null);
        return map;
    }

    public final static Map<String, Object> failure(ResultEnum respCode, Object data, Boolean success) {
        return getStringObjectMap(respCode, data, success);
    }

    public final static Map<String, Object> failure(ResultEnum respCode, Boolean success) {
        return getStringObjectMap(respCode, success);
    }

    private static Map<String, Object> getStringObjectMap(ResultEnum respCode, Boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("success", success);
        map.put("data", null);
        return map;
    }

    private static Map<String, Object> getStringObjectMap(ResultEnum respCode, Object data, Boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("success", success);
        map.put("data", data);
        return map;
    }

    /**
     * 成功返回特定的状态码和信息
     * @param respCode
     * @param data
     * @param success
     * @return
     */
    public final static Map<String, Object> result(ResultEnum respCode, Object data, Boolean success) {
        return getStringObjectMap(respCode, data, success);
    }

    /**
     * 成功返回特定的状态码和信息
     * @param respCode
     * @param success
     * @return
     */
    public final static Map<String, Object> result(ResultEnum respCode, Boolean success) {
        return getStringObjectMap(respCode, success);
    }

    /**
     * 成功返回特定的状态码和信息
     * @param respCode
     * @param jwtToken
     * @param success
     * @return
     */
    public final static Map<String, Object> result(ResultEnum respCode, String jwtToken, Boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("jwtToken", jwtToken);
        map.put("code", respCode.getCode());
        map.put("message", respCode.getMessage());
        map.put("success", success);
        map.put("data", null);
        return map;
    }
}
