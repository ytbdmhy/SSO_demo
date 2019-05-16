package com.ytbdmhy.SSO_demo.demo.service.impl;

import com.ytbdmhy.SSO_demo.demo.dao.DemoMapper;
import com.ytbdmhy.SSO_demo.demo.entity.DemoEntity;
import com.ytbdmhy.SSO_demo.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public List<DemoEntity> getUser() {
        return demoMapper.getUser();
    }
}
