package com.ytbdmhy.SSO_demo.common.security;

import com.alibaba.fastjson.JSON;
import com.ytbdmhy.SSO_demo.common.Enums.ResultEnum;
import com.ytbdmhy.SSO_demo.common.VO.ResultVO;
import com.ytbdmhy.SSO_demo.common.utils.*;
import com.ytbdmhy.SSO_demo.demo.entity.SelfUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 登录成功
 */
@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String currentIp = AccessAddressUtil.getIpAddress(httpServletRequest);
        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("ip", currentIp);
        String jwtToken;
        Object token = redisUtil.getTokenByUsername(userDetails.getUsername());

        if (authHeader != null
                && authHeader.startsWith("Bearer ")
                && JwtTokenUtil.parseToken(authHeader.substring("Bearer ".length()), "_secret").equals(userDetails.getUsername())) {
            jwtToken = (String) token;
//            // token不为空，刷新token
////            String authToken = authHeader.substring("Bearer ".length());
////            String username = JwtTokenUtil.parseToken(authToken, "_secret");
//            if (!JwtTokenUtil.parseToken(authHeader.substring("Bearer ".length()), "_secret").equals(userDetails.getUsername()))
////            if (!username.equals(userDetails.getUsername())) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("ip", currentIp);
//                // 获取请求的IP地址
//                redisUtil.setTokenRefresh(jwtToken, userDetails.getUsername(), currentIp);
//                log.info("用户：{}登录成功,信息已保存至redis", userDetails.getUsername());
//                httpServletResponse.setHeader("Authorization", "Bearer " + jwtToken);
//            }
        } else {
            if (token != null) {
                jwtToken = (String) token;
            } else {
                jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), expirationSeconds, map);
                redisUtil.setTokenRefresh(userDetails.getUsername(), jwtToken, currentIp);
            }
        }

        log.info("用户：{}登录成功,信息已保存至redis", userDetails.getUsername());
        httpServletResponse.setHeader("Authorization", "Bearer " + jwtToken);
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGIN_SUCCESS, jwtToken, true)));
    }
}
