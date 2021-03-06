package com.ytbdmhy.SSO_demo.common.security;

import com.alibaba.fastjson.JSON;
import com.ytbdmhy.SSO_demo.common.Enums.ResultEnum;
import com.ytbdmhy.SSO_demo.common.VO.ResultVO;
import com.ytbdmhy.SSO_demo.common.utils.JwtTokenUtil;
import com.ytbdmhy.SSO_demo.common.utils.RedisUtil;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            final String authToken = authHeader.substring("Bearer ".length());
            String username = null;
            try {
                username = JwtTokenUtil.parseToken(authHeader.substring("Bearer ".length()), "_secret");
            } catch (SignatureException e) {

            }
            // 删除token
            redisUtil.deleteKey(username);
            log.info("用户登出成功！{}的token已删除", username);
        }
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGOUT_SUCCESS, true)));
    }
}
