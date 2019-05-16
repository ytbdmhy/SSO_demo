package com.ytbdmhy.SSO_demo.demo.dao;

import com.ytbdmhy.SSO_demo.demo.entity.SelfUserDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 用户dao层
 */
@Component
public interface UserMapper {

    // 通过username查询用户
    SelfUserDetails getUser(@Param("username") String username);
}
