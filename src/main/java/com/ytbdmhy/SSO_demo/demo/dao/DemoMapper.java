package com.ytbdmhy.SSO_demo.demo.dao;

import com.ytbdmhy.SSO_demo.demo.entity.DemoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DemoMapper {

    List<DemoEntity> getUser();
}
