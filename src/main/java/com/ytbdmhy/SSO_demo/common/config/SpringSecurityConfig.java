package com.ytbdmhy.SSO_demo.common.config;

import com.ytbdmhy.SSO_demo.common.filters.JwtAuthenticationTokenFilter;
import com.ytbdmhy.SSO_demo.common.security.*;
import com.ytbdmhy.SSO_demo.demo.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // 未登录时返回JSON格式的数据给前端（否则为html）
    @Autowired
    private AjaxAuthenticationEntryPoint authenticationEntryPoint;

    // 登录成功时返回JSON格式的数据给前端（否则为html）
    @Autowired
    private AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

    // 登录失败时返回JSON格式的数据给前端（否则为html）
    @Autowired
    private AjaxAuthenticationFailureHandler authenticationFailureHandler;

    // 注销成功时返回JSON格式的数据给前端（否则为html）
    @Autowired
    private AjaxLogoutSuccessHandler logoutSuccessHandler;

    // 无权访问返回的JSON格式的数据给前端（否则为403html页面）
    @Autowired
    private AjaxAccessDeniedHandler accessDeniedHandler;

    // 自定义user
    @Autowired
    private SelfUserDetailsService userDetailsService;

    // JWT拦截器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
//        auth.authenticationProvider(provider);
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 去掉CSRF
        http.csrf().disable()

                // 使用JWT，关闭token

                // Session管理
                .sessionManagement()
                // STATELESS永远不会创建Session，不会使用Session来获取SecurityContext
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 未登录
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)

                .and()
                // 定义哪些URL需要被保护、哪些不需要被保护
                .authorizeRequests()
                // 任何请求，登录后可以访问
                .anyRequest()
                // RBAC动态url认证
                .access("@rbacauthorityservice.hasPermission(request, authentication)")

                .and()
                // 开启登录，定义当需要用户登录时转到的登录页面
                .formLogin()
//                .loginPage("/test/login.html")
//                .loginProcessingUrl("/login")
                // 登录成功
                .successHandler(authenticationSuccessHandler)
                // 登录失败
                .failureHandler(authenticationFailureHandler)
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        // 记住我
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(1000);

        // 无权访问JSON格式的数据
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // JWT Filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("123456"));
//    }
}
