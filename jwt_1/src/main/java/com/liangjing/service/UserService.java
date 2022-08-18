package com.liangjing.service;

import com.liangjing.pojo.User;

import java.util.Map;

public interface UserService {

     String LoginJWT(User user);

     Map getInfo(String id);
}
