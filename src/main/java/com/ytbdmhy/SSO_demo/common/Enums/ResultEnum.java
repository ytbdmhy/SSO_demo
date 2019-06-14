package com.ytbdmhy.SSO_demo.common.Enums;

import lombok.Getter;

/**
 * @description: 返回的错误码枚举
 */
@Getter
public enum ResultEnum {

    SUCCESS(200, "成功"),
    FAILURE(300, "失败"),
    USER_NEED_AUTHORITIES(301, "用户未登录"),
    USER_LOGIN_FAILED(302, "用户账号或密码错误"),
    USER_LOGIN_SUCCESS(303, "用户登录成功"),
    USER_NO_ACCESS(304, "用户无权访问"),
    USER_LOGOUT_SUCCESS(305, "用户登出成功"),
    TOKEN_IS_BLACKLIST(306, "此token为黑名单"),
    LOGIN_IS_OVERDUE(307, "登录已失效"),
    USER_HAS_CANCELLED(308, "用户账号已被注销"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @description: 通过code返回枚举
     * @param code
     * @return
     */
    public static ResultEnum parse(int code) {
        ResultEnum[] values = values();
        for (ResultEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new RuntimeException("Unknown code for ResultEnum");
    }
}
