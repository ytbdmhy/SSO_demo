package com.ytbdmhy.SSO_demo.demo.controller;

import com.ytbdmhy.SSO_demo.demo.entity.DemoEntity;
import com.ytbdmhy.SSO_demo.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/getUser")
    public List<DemoEntity> getUser() {
        List<DemoEntity> result = demoService.getUser();
        return result;
    }
}
