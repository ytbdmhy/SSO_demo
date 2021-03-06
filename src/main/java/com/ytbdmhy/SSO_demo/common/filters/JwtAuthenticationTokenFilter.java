package com.ytbdmhy.SSO_demo.common.filters;

import com.alibaba.fastjson.JSON;
import com.ytbdmhy.SSO_demo.common.Enums.ResultEnum;
import com.ytbdmhy.SSO_demo.common.VO.ResultVO;
import com.ytbdmhy.SSO_demo.common.utils.*;
import com.ytbdmhy.SSO_demo.demo.service.SelfUserDetailsService;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt过滤器 继承 一次请求只过滤一次
 * @description: 确保在一次请求只通过一次filter，而不需要重复执行
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private SelfUserDetailsService userDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
//        String currentIp = AccessAddressUtil.getIpAddress(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7); // 7 = "Bearer ".length
            String username;
            try {
                username = JwtTokenUtil.parseToken(authToken, "_secret");
            } catch (SignatureException e) {
                // token无效 返回错误码给前端让前端跳转到登录页面
                log.info("token：{}无效，拒绝访问", authToken);
                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.TOKEN_NO_AVAIL, false)));
                return;
            }

            /**
             * 根据key(username)和filed(token)获取redis里的token，
             * 如果为 null或者token的值不相等 说明token已过期
             */
            String redisResult = String.valueOf(redisUtil.getTokenByUsername(username));
            if (redisResult == null || !redisResult.equals(authToken)) {
                log.info("用户：{}的token：{}已过期，拒绝访问", username, authToken);
                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                log.info("用户：{}可能已被注销", username);
                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_HAS_CANCELLED, false)));
                return;
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            redisUtil.expire(username, 2592000);

//            // 进入黑名单验证
//            if (redisUtil.isBlackList(authToken)) {
//                log.info("用户：{}的token：{}在黑名单之中，拒绝访问", username, authToken);
//                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.TOKEN_IS_BLACKLIST, false)));
//                return;
//            }
//
//            /**
//             * 判断token是否过期
//             * 过期的话，从redis中读取有效时间（比如七天登录有效），再refreshToken（根据以后业务加入，现在直接refresh）
//             * 同时，已过期的token加入黑名单
//            **/
//             // 判断redis是否有保存
//            if (redisUtil.hasKey(authToken)) {
//                String expirationTime = redisUtil.hget(authToken, "expirationTime").toString();
//                if (JwtTokenUtil.isExpiration(expirationTime)) {
//                    // 获得redis中用户的token刷新时效
//                    String tokenValidTime = (String) redisUtil.getTokenValidTimeByToken(authToken);
//                    String currentTime = DateUtil.getTime();
//                    // 这个token已作废，加入黑名单
//                    log.info("{}已作废，加入黑名单", authToken);
//                    redisUtil.hset("blacklist", authToken, DateUtil.getTime());
//
//                    if (DateUtil.compareDate(currentTime, tokenValidTime)) {
//                        // 超过有效期，不予刷新
//                        log.info("{}已超过有效期，不予刷新", authToken);
//                        response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
//                        return;
//                    } else {
//                        // 仍在刷新时间内，则刷新token，放入请求头中
//                        String usernameByToken = (String) redisUtil.getUsernameByToken(authToken);
//                        username = usernameByToken;
//                        ip = (String) redisUtil.getIPByToken(authToken);
//
//                        // 获取请求的IP地址
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("ip", ip);
//                        String jwtToken = JwtTokenUtil.generateToken(usernameByToken, expirationSeconds, map);
//
//                        // 更新redis
//                        // 刷新时间
//                        Integer expire = validTime * 24 * 60 * 60 * 1000;
//                        redisUtil.setTokenRefresh(jwtToken, usernameByToken, ip);
//                        // 删除旧的token保存的redis
//                        redisUtil.deleteKey(authToken);
//                        // 新的token保存到redis中
//                        redisUtil.setTokenRefresh(jwtToken, username, ip);
//
//                        log.info("redis已删除旧token：{}，新token：{}已更新redis", authToken, jwtToken);
//                        // 更新token，为了后面
//                        authToken = jwtToken;
//                        response.setHeader("Authorization", "Bearer " + jwtToken);
//                    }
//                }
//            }
//
//            /**
//             * token已被登出
//             */
//            if (redisUtil.getTokenByUsername(username) == null) {
//                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
//                return;
//            }
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                /**
//                 * 加入对ip的验证
//                 * 如果ip不正确，进入黑名单验证
//                 */
//                // 地址不正确
//                if (!ip.equals(currentIp)) {
//                    log.info("用户：{}的ip地址变动，进入黑名单校验", username);
//                    // 进入黑名单验证
//                    if (redisUtil.isBlackList(authToken)) {
//                        log.info("用户：{}的token：{}在黑名单之中，拒绝访问", username, authToken);
//                        response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.TOKEN_IS_BLACKLIST, false)));
//                        return;
//                    }
//                }
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                if (userDetails != null) {
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//            }
        }
        filterChain.doFilter(request, response);
    }
}
