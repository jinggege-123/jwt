package com.liangjing.controller;

import com.liangjing.pojo.User;
import com.liangjing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hewei
 * @date 2022/8/17 14:26
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(User user){
        String result = userService.LoginJWT(user);
        return result;
    }

    @GetMapping("/info")
    public Map getInfo(String id){
        Map info = userService.getInfo(id);
        return info;

    }
}
